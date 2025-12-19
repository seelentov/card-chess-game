package ru.vladislavkomkov.model.fight;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;

public class FightEvent
{
  public final Type type;
  public final List<Unit> player1Units;
  public final List<Unit> player2Units;
  public final List<Object> data;
  
  public FightEvent(Type type, List<Unit> player1Units, List<Unit> player2Units, List<Object> data)
    {
        this.type = type;
        
        this.player1Units = player1Units;
        this.player2Units = player2Units;
        
        this.data = data;
    }
  
  public enum Type
  {
    ATTACK,
  
  }
}