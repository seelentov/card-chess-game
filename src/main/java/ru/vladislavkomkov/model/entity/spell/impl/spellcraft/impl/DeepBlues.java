package ru.vladislavkomkov.model.entity.spell.impl.spellcraft.impl;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

public class DeepBlues extends SpellCraft
{
  public static final int ATTACK_BOOST = 2;
  public static final int HEALTH_BOOST = 3;
  
  public static final int ATTACK_BOOST_GOLD = 4;
  public static final int HEALTH_BOOST_GOLD = 6;
  
  public DeepBlues()
  {
    this(false);
  }
  
  public DeepBlues(boolean isGold)
  {
    super(isGold);
    build();
  }
  
  @Override
  public void build()
  {
    description = "Give a minion +%d/+%d until next turn. Improve your future Deep Blues.";
    
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, index, isTavernIndex, index2, isTavernIndex2, auto) -> {
          
          int multi = calcMulti(player);
          int attackBonus = getAttackBoost(multi);
          int hpBonus = getHealthBoost(multi);
          
          Unit unit;
          if (!isTavernIndex)
          {
            unit = player.getTable().get(index);
          }
          else
          {
            unit = (Unit) player.getTavern().getCards().get(index).getCard().getEntity();
          }
          
          unit.addBuff(
              new Buff(
                  unit1 -> {
                    unit1.incAttack(attackBonus);
                    unit1.incHealth(hpBonus);
                  },
                  unit1 -> {
                    unit1.decAttack(attackBonus);
                    unit1.decHealth(hpBonus);
                  },
                  description));
                  
          player.statistic.played.onlyBluesPlayed++;
        });
  }
  
  int calcMulti(Player player)
  {
    int casted = player.statistic.played.onlyBluesPlayed;
    return casted + 1;
  }
  
  @Override
  public String getDescription()
  {
    return String.format(description, ATTACK_BOOST, HEALTH_BOOST);
  }
  
  @Override
  public String getDescription(Player player)
  {
    int multi = calcMulti(player);
    return String.format(description, getAttackBoost(multi), getHealthBoost(multi));
  }
  
  int getAttackBoost(int multi)
  {
    return (isGold ? ATTACK_BOOST_GOLD : ATTACK_BOOST) * multi;
  }
  
  int getHealthBoost(int multi)
  {
    return (isGold ? HEALTH_BOOST_GOLD : HEALTH_BOOST) * multi;
  }
}
