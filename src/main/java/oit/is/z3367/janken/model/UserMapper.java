package oit.is.z3367.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * usersテーブルを操作するMapper
 * @Mapperをつけることで，MyBatisがSQLを実行するクラスを自動で生成する
 */
@Mapper
public interface UserMapper {

  /**
   * usersテーブルの全ユーザを取得する
   *
   * @return
   */
  @Select("SELECT id, name FROM users")
  ArrayList<User> selectAllUsers();

  /**
   * idを指定してユーザを1人取得する
   *
   * @param id
   * @return
   */
  @Select("SELECT id, name FROM users WHERE id = #{id}")
  User selectById(int id);
}
