package oit.is.z3367.janken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
