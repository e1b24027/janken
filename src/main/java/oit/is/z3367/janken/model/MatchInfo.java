package oit.is.z3367.janken.model;

/**
 * matchinfoテーブルの1レコード(相手の手を待っている試合)を表すクラス
 * フィールド名をカラム名と同じにすることで，MyBatisが自動で値をセットする
 */
public class MatchInfo {
  int id;
  int user1;
  int user2;
  String user1Hand;
  boolean isActive; // 相手の手を待っている(試合中の)ときtrue

  // Thymeleafから値を取得するためのgetter/setter
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUser1() {
    return user1;
  }

  public void setUser1(int user1) {
    this.user1 = user1;
  }

  public int getUser2() {
    return user2;
  }

  public void setUser2(int user2) {
    this.user2 = user2;
  }

  public String getUser1Hand() {
    return user1Hand;
  }

  public void setUser1Hand(String user1Hand) {
    this.user1Hand = user1Hand;
  }

  public boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }
}
