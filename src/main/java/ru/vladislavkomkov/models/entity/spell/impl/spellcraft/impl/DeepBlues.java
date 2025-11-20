package ru.vladislavkomkov.models.entity.spell.impl.spellcraft.impl;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.models.entity.unit.Buff;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.player.Player;

public class DeepBlues extends SpellCraft
{
  public static final int ATTACK_BOOST = 2;
  public static final int HEALTH_BOOST = 3;
  
  public DeepBlues()
  {
    super();
    description = "Give a minion +%d/+%d until next turn. Improve your future Deep Blues.";
    
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, player, entity, index, isTavernIndex, index2, isTavernIndex2) -> {
          int multi = calcMulti(player);
          int attackBonus = ATTACK_BOOST * multi;
          int hpBonus = HEALTH_BOOST * multi;
          
          Unit unit;
          if (!isTavernIndex)
          {
            unit = player.getTable().get(index);
          }
          else
          {
            unit = (Unit) player.getTavern().getCards().get(index).get();
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
                  
          player.statistic.onlyBluesPlayed++;
        });
  }
  
  int calcMulti(Player player)
  {
    int casted = player.statistic.onlyBluesPlayed;
    return casted + 1;
  }
  
  @Override
  public void onPlayed(Game game, Player player, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2)
  {
    super.onPlayed(game, player, index, isTavernIndex, index2, isTavernIndex2);
  }
  
  @Override
  public String getDescription()
  {
    return String.format(description, ATTACK_BOOST, HEALTH_BOOST);
  }
  
  @Override
  public String getDescription(Player player)
  {
    return String.format(description, ATTACK_BOOST * calcMulti(player), HEALTH_BOOST * calcMulti(player));
  }
}
