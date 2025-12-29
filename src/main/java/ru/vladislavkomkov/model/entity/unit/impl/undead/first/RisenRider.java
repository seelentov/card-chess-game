package ru.vladislavkomkov.model.entity.unit.impl.undead.first;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;

public class RisenRider extends Unit
{
  public RisenRider()
  {
    this(DUMP_PLAYER);
  }
  
  public RisenRider(Player playerLink)
  {
    super(playerLink);
    
    isTaunt = true;
    isRebirth = true;
    
    unitType = List.of(UnitType.UNDEAD);
    
    attack = 2;
    
    maxHealth = 1;
    
    isTavern = true;
    
    level = 1;
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    return "Taunt Reborn";
  }
}
