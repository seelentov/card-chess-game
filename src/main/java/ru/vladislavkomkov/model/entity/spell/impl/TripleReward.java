package ru.vladislavkomkov.model.entity.spell.impl;

import ru.vladislavkomkov.model.entity.spell.Spell;

public class TripleReward extends Spell
{
  int unitLvl;
  
  public TripleReward()
  {
    this(0);
  }
  
  public TripleReward(int lvl)
  {
    super(true);
    unitLvl = lvl;
    description = "Discover a minion from Tier " + (lvl);
  }
  
  @Override
  public void build()
  {
    
  }
}