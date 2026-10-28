package oit.is.z3367.janken.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z3367.janken.model.Janken;
import oit.is.z3367.janken.model.Match;
import oit.is.z3367.janken.model.MatchMapper;

/**
 * wait.htmlで相手の手を待っているユーザに，試合結果を非同期(SSE)で送るサービス
 */
@Service
public class AsyncKekka {
  private final Logger logger = LoggerFactory.getLogger(AsyncKekka.class);

  @Autowired
  MatchMapper matchMapper;

  /**
   * 待っているユーザ(user1)の試合がmatchesテーブルに登録される(isActiveがtrueになる)のを監視し，
   * 登録されたら試合結果をブラウザに送って，そのレコードのisActiveをfalseにする
   *
   * @param emitter
   * @param userId  wait.htmlで相手の手を待っているユーザのid
   */
  @Async
  public void asyncShowKekka(SseEmitter emitter, int userId) {
    try {
      while (true) {// 結果を送るまでループ
        // 自分が先に手を選んだ試合で，まだ結果を表示していないものをDBから探す
        Match match = matchMapper.selectActiveMatchByUser1(userId);
        if (match == null) {
          // まだ相手が手を選んでいなければ0.5s休む
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }
        // 自分(user1)から見た両者の手と勝敗をsendし，1s休む
        Janken kekka = new Janken(match.getUser1Hand(), match.getUser2Hand());
        emitter.send(kekka);
        TimeUnit.MILLISECONDS.sleep(1000);
        // 同じ結果が何度も表示されないように，表示した試合のisActiveをfalseにする
        matchMapper.updateInactive(match.getId());
        break;
      }
    } catch (Exception e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowKekka complete");
  }
}
