package ru.vladislavkomkov.model.entity.unit.impl.undead.first;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Type;
import ru.vladislavkomkov.model.entity.unit.Unit;

public class RisenRider extends Unit
{
  public RisenRider()
  {
    super();
    
    description = "Taunt Reborn";
    
    isTaunt = true;
    isRebirth = true;
    
    type = List.of(Type.UNDEAD);
    
    attack = 2;
    
    maxHealth = 1;
    actualHealth = 1;
    
    level = 1;
  }
}
