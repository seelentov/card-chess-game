package ru.vladislavkomkov.model.entity.spell.impl.spellcraft.impl;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.model.entity.PlayType;
import ru.vladislavkomkov.model.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.PlayPair;
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
    
    playType = List.of(new PlayPair(PlayType.TAVERN_FRENDLY));
  }
  
  @Override
  public void build()
  {
    description = "Give a minion +%d/+%d until next turn. Improve your future Deep Blues.";
    
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          
          int multi = calcMulti(player);
          int attackBonus = getAttackBoost(multi);
          int hpBonus = getHealthBoost(multi);
          
          if(input.size() < 2){
              return;
          }
          
          boolean isTavernIndex = input.get(1) == 1;
          
          Unit unit;
          if (!isTavernIndex)
          {
            unit = player.getTable().get(input.get(0));
          }
          else
          {
            unit = (Unit) player.getTavern().getCards().get(input.get(0)).getCard().getEntity();
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
                  getDescription(player)));
                  
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
    String formatDescription = "Give a minion +%d/+%d until next turn. Improve your future Deep Blues.";
    return String.format(formatDescription, getAttackBoost(multi), getHealthBoost(multi));
  }
  
  int getAttackBoost(int multi)
  {
    return (isGold ? ATTACK_BOOST_GOLD : ATTACK_BOOST) * multi;
  }
  
  int getHealthBoost(int multi)
  {
    return (isGold ? HEALTH_BOOST_GOLD : HEALTH_BOOST) * multi;
  }
  
  @Override
  public void buildFace(Player player)
  {
    description = getDescription(player);
  }
}
