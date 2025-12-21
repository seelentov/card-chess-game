package ru.vladislavkomkov.model.fight;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

public class FightEvent
{
  public final static String F_TYPE = "type";
  public final static String F_PLAYER = "player";
  
  public final static String F_PLAYER_UNITS = "playerUnits";
  public final static String F_ENEMY_UNITS = "enemyUnits";
  public final static String F_DATA = "data";
  
  public final Type type;
  public final Player player;
  
  public final List<Unit> playerUnits;
  public final List<Unit> enemyUnits;
  public final List<Object> data;
  
  public FightEvent()
  {
    type = null;
    player = null;
    
    playerUnits = null;
    enemyUnits = null;
    data = null;
    
  }
  
  public FightEvent(Type type, List<Unit> playerUnits, List<Unit> enemyUnits, List<Object> data, Player player)
  {
    this.type = type;
    
    this.playerUnits = playerUnits;
    this.enemyUnits = enemyUnits;
    
    this.data = data;
    this.player = player;
  }
  
  @JsonProperty(F_TYPE)
  public Type getType()
  {
    return type;
  }
  
  @JsonProperty(F_PLAYER)
  public Player getPlayer()
  {
    return player;
  }
  
  @JsonProperty(F_PLAYER_UNITS)
  public List<Unit> getPlayerUnits()
  {
    return playerUnits;
  }
  
  @JsonProperty(F_ENEMY_UNITS)
  public List<Unit> getEnemyUnits()
  {
    return enemyUnits;
  }
  
  @JsonProperty(F_DATA)
  public List<Object> getData()
  {
    return data;
  }
  
  public enum Type
  {
    START,
    END,
    ON_PLAYED,
    ON_HANDLED,
    ON_ATTACK,
    ON_ATTACKED,
    ON_DEAD,
    ON_SELL,
    ON_START_TURN,
    ON_END_TURN,
    ON_START_FIGHT,
    ON_END_FIGHT,
    ON_RESET_TAVERN,
    ON_INC_TAVERN_LEVEL,
    ON_APPEAR,
    ON_DISAPPEAR
  }
}