package oit.is.z3367.janken.model;

/**
 * usersテーブルの1レコードを表すクラス
 * フィールド名をカラム名と同じにすることで，MyBatisが自動で値をセットする
 */
public class User {
  int id;
  String name;

  // Thymeleafから値を取得するためのgetter/setter
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
