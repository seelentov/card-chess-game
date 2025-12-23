package ru.vladislavkomkov.model.entity.unit;

import ru.vladislavkomkov.model.player.Player;

public abstract class TrashUnit extends Unit
{
  public TrashUnit()
  {
    level = 1;
    attack = 1;
    maxHealth = 1;
    actualHealth = 1;
  }
  
  @Override
  public void buildFace(Player player)
  {
    
  }
}
