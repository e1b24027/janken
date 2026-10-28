package oit.is.z3367.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * matchinfoテーブルを操作するMapper
 */
@Mapper
public interface MatchInfoMapper {

  /**
   * isActiveがtrueの(相手の手を待っている)試合をすべて取得する
   *
   * @return
   */
  @Select("SELECT id, user1, user2, user1Hand, isActive FROM matchinfo WHERE isActive = TRUE")
  ArrayList<MatchInfo> selectActiveMatchInfo();

  /**
   * 先に手を選んだユーザの情報(自分のid，相手のid，自分の手，isActive)をmatchinfoテーブルに追加する
   *
   * @param matchInfo
   */
  @Insert("INSERT INTO matchinfo (user1, user2, user1Hand, isActive) VALUES (#{user1}, #{user2}, #{user1Hand}, #{isActive})")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertMatchInfo(MatchInfo matchInfo);
}
