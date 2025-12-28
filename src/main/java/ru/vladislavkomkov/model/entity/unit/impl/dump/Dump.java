package ru.vladislavkomkov.model.entity.unit.impl.dump;

import ru.vladislavkomkov.model.entity.unit.Unit;

public abstract class Dump extends Unit
{
  public Dump(int lvl)
  {
    super();

    level = lvl;
    isTavern = true;
    
    attack = lvl;
    
    maxHealth = lvl;
    actualHealth = lvl;
  }
  
}
