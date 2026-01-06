package ru.vladislavkomkov.model.fight;

import java.util.List;

import ru.vladislavkomkov.model.Listener;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

public class FightPlayer
{
  final Player player;
  final List<Unit> units;
  final Listener listener;
  int turn;
  
  public FightPlayer(Player player, List<Unit> units)
  {
    this.player = player;
    this.units = units;
    this.listener = player.getListener().clone();
  }
  
  public Player getPlayer()
  {
    return player;
  }
  
  public List<Unit> getUnits()
  {
    return units;
  }
  
  public int getTurn()
  {
    return turn;
  }
  
  public Listener getListener()
  {
    return listener;
  }
}