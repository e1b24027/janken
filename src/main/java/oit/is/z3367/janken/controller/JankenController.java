package oit.is.z3367.janken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z3367.janken.model.Janken;

/**
 * じゃんけんアプリのController
 */
@Controller
public class JankenController {

  /**
   * /jankenへのGETリクエストではユーザ名を渡さずにjanken.htmlを返す
   */
  @GetMapping("/janken")
  public String janken() {
    return "janken.html";
  }

  /**
   * index.htmlのフォームから/jankenへPOSTされたユーザ名を受け取り，janken.htmlに渡す
   *
   * @param username
   * @param model
   * @return
   */
  @PostMapping("/janken")
  public String jankenEntry(@RequestParam String username, ModelMap model) {
    model.addAttribute("username", username);
    return "janken.html";
  }

  /**
   * グー・チョキ・パーのリンクから/jankengame?hand=...へのGETリクエストを受け取り，
   * CPUとの対戦結果をjanken.htmlに渡す
   *
   * @param hand
   * @param model
   * @return
   */
  @GetMapping("/jankengame")
  public String jankenGame(@RequestParam String hand, ModelMap model) {
    Janken janken = new Janken(hand);
    model.addAttribute("janken", janken);
    return "janken.html";
  }
}
