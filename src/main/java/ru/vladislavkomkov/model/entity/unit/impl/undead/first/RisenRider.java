package ru.vladislavkomkov.model.entity.unit.impl.undead.first;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;

public class RisenRider extends Unit
{
  public RisenRider()
  {
    super();
    
    description = "Taunt Reborn";
    
    isTaunt = true;
    isRebirth = true;
    
    unitType = List.of(UnitType.UNDEAD);
    
    attack = 2;
    
    maxHealth = 1;
    actualHealth = 1;
    
    isTavern = true;
    
    level = 1;
  }
  
  @Override
  public void buildFace(Player player)
  {
    
  }
}
