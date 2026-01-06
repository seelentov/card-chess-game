package ru.vladislavkomkov.model.entity.spell;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.player.Player;

public abstract class Spell extends Entity
{
  public Spell()
  {
    this(DUMP_PLAYER);
  }
  
  public Spell(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public Spell(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
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
  public String getDescription()
  {
    return "";
  }
  
  protected int getAttackBoost()
  {
    return playerLink.getStatistic().getBoosts().getAttackSpell() + (isTavern() ? playerLink.getStatistic().getBoosts().getTavernAttackSpell() : 0);
  }
  
  protected int getHealthBoost()
  {
    return playerLink.getStatistic().getBoosts().getHealthSpell() + (isTavern() ? playerLink.getStatistic().getBoosts().getTavernHealthSpell() : 0);
  }
}
