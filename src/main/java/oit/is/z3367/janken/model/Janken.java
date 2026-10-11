package oit.is.z3367.janken.model;

import java.util.Random;

/**
 * じゃんけんの手と結果を保持するクラス
 * 手は"Gu","Choki","Pa"の文字列で受け取り，表示用に日本語に変換して保持する
 * Thymeleafから値を取り出せるようにgetterを用意する
 */
public class Janken {
  String playerHand;
  String cpuHand;
  String result;

  // コンストラクタ ユーザの手を受け取り，CPUの手と勝敗を決める
  public Janken(String hand) {
    String[] hands = { "Gu", "Choki", "Pa" };
    String cpu = hands[new Random().nextInt(hands.length)]; // CPUはランダムに手を出す
    this.playerHand = toJapanese(hand);
    this.cpuHand = toJapanese(cpu);
    this.result = judge(hand, cpu);
  }

  /**
   * 2つの手から勝ち・負け・あいこを判定する
   */
  private String judge(String player, String cpu) {
    if (player.equals(cpu)) {
      return "あいこ";
    }
    if ((player.equals("Gu") && cpu.equals("Choki"))
        || (player.equals("Choki") && cpu.equals("Pa"))
        || (player.equals("Pa") && cpu.equals("Gu"))) {
      return "勝ち";
    }
    return "負け";
  }

  /**
   * 手の文字列を表示用の日本語に変換する
   */
  private String toJapanese(String hand) {
    switch (hand) {
      case "Gu":
        return "グー";
      case "Choki":
        return "チョキ";
      case "Pa":
        return "パー";
      default:
        return hand;
    }
  }

  public String getPlayerHand() {
    return playerHand;
  }

  public String getCpuHand() {
    return cpuHand;
  }

  public String getResult() {
    return result;
  }
}
