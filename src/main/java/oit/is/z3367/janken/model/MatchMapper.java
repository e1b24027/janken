package oit.is.z3367.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * matchesテーブルを操作するMapper
 */
@Mapper
public interface MatchMapper {

  /**
   * matchesテーブルの全試合結果を取得する
   *
   * @return
   */
  @Select("SELECT id, user1, user2, user1Hand, user2Hand, isActive FROM matches")
  ArrayList<Match> selectAllMatches();

  /**
   * 試合結果(自分のid，相手のid，自分の手，相手の手，isActive)をmatchesテーブルに追加する
   * idは自動で採番され，useGeneratedKeysで引数のmatchのidにセットされる
   *
   * @param match
   */
  @Insert("INSERT INTO matches (user1, user2, user1Hand, user2Hand, isActive) VALUES (#{user1}, #{user2}, #{user1Hand}, #{user2Hand}, #{isActive})")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertMatch(Match match);

  /**
   * 先に手を選んだユーザ(user1)の，まだ結果を表示していない(isActiveがtrueの)試合を取得する
   * 該当するレコードがなければnullを返す
   *
   * @param user1 先に手を選んで結果を待っているユーザのid
   * @return
   */
  @Select("SELECT id, user1, user2, user1Hand, user2Hand, isActive FROM matches WHERE isActive = TRUE AND user1 = #{user1} LIMIT 1")
  Match selectActiveMatchByUser1(int user1);

  /**
   * 結果を表示した試合のisActiveをfalseにする
   *
   * @param id
   */
  @Update("UPDATE matches SET isActive = FALSE WHERE id = #{id}")
  void updateInactive(int id);
}
