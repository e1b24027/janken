package oit.is.z3367.janken.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z3367.janken.model.Entry;
import oit.is.z3367.janken.model.Janken;

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
    return "janken.html";
  }
}
