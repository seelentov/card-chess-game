package ru.vladislavkomkov.model.entity.unit.impl.dump;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

public abstract class Dump extends Unit
{
  public Dump(Player playerLink, int lvl)
  {
    super(playerLink);

    level = lvl;
    isTavern = true;
    
    attack = lvl;
    
    maxHealth = lvl;
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    return "";
  }
}
