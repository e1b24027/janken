package oit.is.z3367.janken.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z3367.janken.model.Entry;
import oit.is.z3367.janken.model.Janken;
import oit.is.z3367.janken.model.Match;
import oit.is.z3367.janken.model.MatchInfo;
import oit.is.z3367.janken.model.MatchInfoMapper;
import oit.is.z3367.janken.model.MatchMapper;
import oit.is.z3367.janken.model.User;
import oit.is.z3367.janken.model.UserMapper;
import oit.is.z3367.janken.service.AsyncKekka;

/**
 * じゃんけんアプリのController
 */
@Controller
public class JankenController {

  /**
   * DIによりSpring Bootが生成したEntryオブジェクトが代入される
   * (全ユーザで同じオブジェクトを共有する)
   */
  @Autowired
  private Entry entry;

  /**
   * DIによりMyBatisが生成したUserMapperオブジェクトが代入される
   */
  @Autowired
  private UserMapper userMapper;

  /**
   * DIによりMyBatisが生成したMatchMapperオブジェクトが代入される
   */
  @Autowired
  private MatchMapper matchMapper;

  /**
   * DIによりMyBatisが生成したMatchInfoMapperオブジェクトが代入される
   */
  @Autowired
  private MatchInfoMapper matchInfoMapper;

  /**
   * 試合結果を非同期(SSE)で送るサービス
   */
  @Autowired
  private AsyncKekka asyncKekka;

  /**
   * /jankenへのGETリクエスト
   * ログインユーザ名を取得してエントリーに追加し，janken.htmlに渡す
   *
   * @param prin  ログインユーザ情報
   * @param model
   * @return
   */
  @GetMapping("/janken")
  public String janken(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    this.entry.addUser(loginUser);
    model.addAttribute("username", loginUser);
    model.addAttribute("entry", this.entry);
    ArrayList<User> users = userMapper.selectAllUsers(); // usersテーブルの全ユーザを取得する
    model.addAttribute("users", users);
    ArrayList<Match> matches = matchMapper.selectAllMatches(); // matchesテーブルの全試合結果を取得する
    model.addAttribute("matches", matches);
    ArrayList<MatchInfo> activeMatches = matchInfoMapper.selectActiveMatchInfo(); // 相手の手を待っている試合を取得する
    model.addAttribute("activeMatches", activeMatches);
    return "janken.html";
  }

  /**
   * グー・チョキ・パーのリンクから/jankengame?hand=...へのGETリクエストを受け取り，
   * CPUとの対戦結果をjanken.htmlに渡す
   *
   * @param hand
   * @param prin
   * @param model
   * @return
   */
  @GetMapping("/jankengame")
  public String jankenGame(@RequestParam String hand, Principal prin, ModelMap model) {
    Janken janken = new Janken(hand);
    model.addAttribute("janken", janken);
    model.addAttribute("username", prin.getName());
    model.addAttribute("entry", this.entry);
    model.addAttribute("users", userMapper.selectAllUsers());
    model.addAttribute("matches", matchMapper.selectAllMatches());
    model.addAttribute("activeMatches", matchInfoMapper.selectActiveMatchInfo());
    return "janken.html";
  }

  /**
   * janken.htmlのユーザ名のリンクから/match?id=...へのGETリクエストを受け取り，
   * ログインユーザと選んだユーザ(対戦相手)をmatch.htmlに渡す
   *
   * @param id    対戦相手のユーザのid
   * @param prin
   * @param model
   * @return
   */
  @GetMapping("/match")
  public String match(@RequestParam int id, Principal prin, ModelMap model) {
    User opponent = userMapper.selectById(id); // idで対戦相手のユーザを取得する
    model.addAttribute("username", prin.getName());
    model.addAttribute("opponent", opponent);
    return "match.html";
  }

  /**
   * match.htmlの手のリンクから/fight?hand=...&id=...へのGETリクエストを受け取る．
   * 相手がCPUのときは試合結果をmatchesテーブルに追加して，両者の手と勝敗をmatch.htmlに渡す．
   * 相手がCPU以外のユーザのときは，相手が自分を選んで待っていれば試合を成立させて結果をmatchesテーブルに登録し，
   * 待っていなければ自分の手をmatchinfoテーブルに登録してwait.htmlで相手の手を待つ
   *
   * @param hand  自分の手(Gu, Choki, Pa)
   * @param id    対戦相手のユーザのid
   * @param prin
   * @param model
   * @return
   */
  @GetMapping("/fight")
  public String fight(@RequestParam String hand, @RequestParam int id, Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    User me = userMapper.selectByName(loginUser); // ログインユーザ名で自分のユーザを取得する
    User opponent = userMapper.selectById(id);

    if (!opponent.getName().equals("CPU")) {
      // 相手が自分を選んで手を待っている(isActiveがtrueの)matchinfoがあるか調べる
      MatchInfo waiting = matchInfoMapper.selectWaitingMatchInfo(opponent.getId(), me.getId());
      if (waiting != null) {
        // 試合成立: 相手(先手)と自分(後手)の手をmatchesテーブルに登録する(isActive=true: まだ相手に結果を表示していない)
        Match match = new Match();
        match.setUser1(waiting.getUser1());
        match.setUser2(me.getId());
        match.setUser1Hand(waiting.getUser1Hand());
        match.setUser2Hand(hand);
        match.setIsActive(true);
        matchMapper.insertMatch(match);
        // 試合が終わったので待機中のmatchinfoのisActiveをfalseにする
        matchInfoMapper.updateInactive(waiting.getId());

        model.addAttribute("username", loginUser);
        model.addAttribute("opponent", opponent);
        model.addAttribute("janken", new Janken(hand, waiting.getUser1Hand()));
        return "match.html";
      }

      // 相手が待っていないときは，自分の手をmatchinfoテーブルに登録して(isActive=true)相手の手を待つ
      MatchInfo matchInfo = new MatchInfo();
      matchInfo.setUser1(me.getId());
      matchInfo.setUser2(opponent.getId());
      matchInfo.setUser1Hand(hand);
      matchInfo.setIsActive(true);
      matchInfoMapper.insertMatchInfo(matchInfo);

      model.addAttribute("username", loginUser);
      return "wait.html";
    }

    String cpuHand = "Gu"; // CPUの手は固定でグー

    // 試合結果をmatchesテーブルに追加する
    Match match = new Match();
    match.setUser1(me.getId());
    match.setUser2(opponent.getId());
    match.setUser1Hand(hand);
    match.setUser2Hand(cpuHand);
    matchMapper.insertMatch(match);

    model.addAttribute("username", loginUser);
    model.addAttribute("opponent", opponent);
    model.addAttribute("janken", new Janken(hand, cpuHand));
    return "match.html";
  }

  /**
   * wait.htmlのJavaScriptからEventSourceとして呼び出されるGETリクエスト
   * SseEmitterを返すことで，相手が手を選んで試合が成立したときに結果をPUSHで送る
   *
   * @param prin
   * @return
   */
  @GetMapping("/janken/result")
  public SseEmitter result(Principal prin) {
    User me = userMapper.selectByName(prin.getName());
    final SseEmitter sseEmitter = new SseEmitter();
    this.asyncKekka.asyncShowKekka(sseEmitter, me.getId());
    return sseEmitter;
  }
}
