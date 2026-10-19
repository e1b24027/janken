package oit.is.z3367.janken.model;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

/**
 * じゃんけんにエントリー(ログイン)しているユーザを保持するクラス
 * @Componentをつけることで，Spring Bootが1つだけインスタンスを生成し(DI)，
 * すべてのユーザ(リクエスト)で同じオブジェクトが共有される
 */
@Component
public class Entry {
  ArrayList<String> users = new ArrayList<>();

  /**
   * ユーザをエントリーに追加する．同名のユーザが既にいる場合は追加しない
   *
   * @param name
   */
  public void addUser(String name) {
    for (String s : this.users) {
      if (s.equals(name)) {
        return;
      }
    }
    this.users.add(name);
  }

  // Thymeleafから値を取得するためのgetter/setter
  public ArrayList<String> getUsers() {
    return users;
  }

  public void setUsers(ArrayList<String> users) {
    this.users = users;
  }

  public int getUserCount() {
    return users.size();
  }
}
