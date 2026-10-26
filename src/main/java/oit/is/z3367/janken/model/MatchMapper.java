package oit.is.z3367.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

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
  @Select("SELECT id, user1, user2, user1Hand, user2Hand FROM matches")
  ArrayList<Match> selectAllMatches();

  /**
   * 試合結果(自分のid，相手のid，自分の手，相手の手)をmatchesテーブルに追加する
   * idは自動で採番され，useGeneratedKeysで引数のmatchのidにセットされる
   *
   * @param match
   */
  @Insert("INSERT INTO matches (user1, user2, user1Hand, user2Hand) VALUES (#{user1}, #{user2}, #{user1Hand}, #{user2Hand})")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertMatch(Match match);
}
