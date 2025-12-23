package ru.vladislavkomkov.model.entity.spell;

import java.util.List;

import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.player.Player;

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
    
    playType = List.of();
  }
  
  public abstract void build();
  
  @Override
  public boolean isSpell()
  {
    return true;
  }
  
  @Override
  public Spell clone()
  {
    return (Spell) super.clone();
  }
  
  @Override
  public void buildFace(Player player)
  {
    
  }
}
