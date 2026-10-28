package oit.is.z3367.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
   * 相手(user1)が自分(user2)を選んで手を待っている(isActiveがtrueの)試合を取得する
   * 該当するレコードがなければnullを返す
   *
   * @param user1 先に手を選んだユーザ(相手)のid
   * @param user2 後から手を選んだユーザ(自分)のid
   * @return
   */
  @Select("SELECT id, user1, user2, user1Hand, isActive FROM matchinfo WHERE isActive = TRUE AND user1 = #{user1} AND user2 = #{user2} LIMIT 1")
  MatchInfo selectWaitingMatchInfo(@Param("user1") int user1, @Param("user2") int user2);

  /**
   * 試合が成立したmatchinfoのレコードのisActiveをfalseにする
   *
   * @param id
   */
  @Update("UPDATE matchinfo SET isActive = FALSE WHERE id = #{id}")
  void updateInactive(int id);

  /**
   * 先に手を選んだユーザの情報(自分のid，相手のid，自分の手，isActive)をmatchinfoテーブルに追加する
   *
   * @param matchInfo
   */
  @Insert("INSERT INTO matchinfo (user1, user2, user1Hand, isActive) VALUES (#{user1}, #{user2}, #{user1Hand}, #{isActive})")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  void insertMatchInfo(MatchInfo matchInfo);
}
