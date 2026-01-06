package ru.vladislavkomkov.model.entity.spell.impl.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.player.Player;

public class ThemApples extends Spell
{
  public static final int ATTACK_BOOST = 1;
  public static final int HEALTH_BOOST = 2;
  
  public ThemApples()
  {
    this(DUMP_PLAYER, false);
  }
  
  public ThemApples(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public ThemApples(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    isTavern = true;
    
    playType = List.of();
  }
  
  @Override
  public void build()
  {
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          player.getTavern().getUnits().forEach(unit1 -> unit1.addBuff(new Buff(
              unit2 -> {
                unit2.incBaseAttack(getAttackBoost());
                unit2.incHealth(getHealthBoost());
              },
              null,
              getDescription(),
              getID())));
              
        });
  }
  
  @Override
  protected int getAttackBoost()
  {
    return super.getAttackBoost() + ATTACK_BOOST;
  }
  
  @Override
  protected int getHealthBoost()
  {
    return super.getHealthBoost() + HEALTH_BOOST;
  }
  
  @Override
  public String getDescription()
  {
    return "Give minions to the tavern +" + getAttackBoost() + "/+" + getHealthBoost();
  }
}
