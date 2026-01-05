package ru.vladislavkomkov.model.fight;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.model.ActionEvent;
import ru.vladislavkomkov.model.player.Player;

public class FightInfo
{
  public final static String F_RESULT = "result";
  public final static String F_DAMAGE = "damage";
  public final static String F_HISTORY = "history";
  
  public final static String F_PLAYER_1 = "player1";
  public final static String F_PLAYER_2 = "player2";
  
  public final Player player1;
  public final Player player2;
  public final Result result;
  public final int damage;
  public final List<ActionEvent> history;
  
  public FightInfo()
  {
    this.player1 = null;
    this.player2 = null;
    this.result = null;
    this.damage = 0;
    this.history = null;
  }
  
  public FightInfo(Player player1, Player player2, Result result, int damage, List<ActionEvent> history)
  {
    this.player1 = player1;
    this.player2 = player2;
    this.result = result;
    this.damage = damage;
    this.history = history;
  }
  
  @JsonProperty(F_RESULT)
  public Result getResult()
  {
    return result;
  }
  
  @JsonProperty(F_DAMAGE)
  public int getDamage()
  {
    return damage;
  }
  
  @JsonProperty(F_HISTORY)
  public List<ActionEvent> getHistory()
  {
    return history;
  }
  
  @JsonProperty(F_PLAYER_1)
  public Player getPlayer1()
  {
    return player1;
  }
  
  @JsonProperty(F_PLAYER_2)
  public Player getPlayer2()
  {
    return player2;
  }
  
  public enum Result
  {
    PLAYER1_WIN,
    PLAYER2_WIN,
    DRAW
  }
}
