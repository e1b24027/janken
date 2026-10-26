package oit.is.z3367.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
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
}
