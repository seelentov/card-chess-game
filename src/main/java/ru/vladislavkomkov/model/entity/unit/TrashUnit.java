package ru.vladislavkomkov.model.entity.unit;

import ru.vladislavkomkov.model.player.Player;

public abstract class TrashUnit extends Unit
{
  public TrashUnit(Player playerLink)
  {
    super(playerLink);
    
    level = 1;
    attack = 1;
    maxHealth = 1;
  }
}
