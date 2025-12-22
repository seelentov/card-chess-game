package ru.vladislavkomkov.model.entity.spell;

import ru.vladislavkomkov.model.entity.Entity;

public abstract class Spell extends Entity
{
  public Spell()
  {
    this(false);
  }
  
  public Spell(boolean isGold)
  {
    super(isGold);
    build();
  }
  
  public abstract void build();
  
  @Override
  public Spell clone() {
    return (Spell) super.clone();
  }
}
