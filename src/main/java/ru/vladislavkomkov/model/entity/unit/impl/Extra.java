package ru.vladislavkomkov.model.entity.unit.impl;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

public class Extra extends Unit
{
  protected Action onPlayedExtra = new Action(0, 0);
  protected Action onDeadExtra = new Action(0, 0);
  protected Action onEndTurnExtra = new Action(0, 0);
  
  public Extra(Player player)
  {
    super(player);
  }
  
  public static class Action
  {
    private final int isExtra;
    private final int isTwice;
    
    public Action(int isExtra, int isTwice)
    {
      this.isExtra = isExtra;
      this.isTwice = isTwice;
    }
    
    public int getIsExtra()
    {
      return isExtra;
    }
    
    public int getIsTwice()
    {
      return isTwice;
    }
  }
  
  public Action getOnPlayedExtra()
  {
    return onPlayedExtra;
  }
  
  public Action getOnDeadExtra()
  {
    return onDeadExtra;
  }
  
  public Action getOnEndTurnExtra()
  {
    return onEndTurnExtra;
  }
}
