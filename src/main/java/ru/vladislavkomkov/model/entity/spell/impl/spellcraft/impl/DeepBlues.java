package ru.vladislavkomkov.model.entity.spell.impl.spellcraft.impl;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.model.entity.PlayPair;
import ru.vladislavkomkov.model.entity.PlayType;
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
    this(DUMP_PLAYER, false);
  }
  
  public DeepBlues(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public DeepBlues(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    build();
    
    playType = List.of(new PlayPair(PlayType.TAVERN_FRIENDLY));
  }
  
  @Override
  public void build()
  {
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          
          int multi = calcMulti();
          int attackBonus = getAttackBoost(multi);
          int hpBonus = getHealthBoost(multi);
          
          Optional<Unit> unit = getUnitFromTavernFriendlyInput(fight, player, input);
          
          unit.ifPresent(value -> value.addBuff(
              new Buff(
                  unit1 -> {
                    unit1.incBaseAttack(attackBonus);
                    unit1.incHealth(hpBonus);
                  },
                  unit1 -> {
                    unit1.decBaseAttack(attackBonus);
                    unit1.decHealth(hpBonus);
                  },
                  getDescription(),
                  getID(),
                  true)));
                  
          player.getStatistic().getPlayed().incOnlyBluesPlayed();
        });
  }
  
  int calcMulti()
  {
    int casted = playerLink.getStatistic().getPlayed().getOnlyBluesPlayed();
    return casted + 1;
  }
  
  @Override
  public String getDescription()
  {
    int multi = calcMulti();
    String formatDescription = "Give a minion +%d/+%d until next turn. Improve your future Deep Blues.";
    return String.format(formatDescription, getAttackBoost(multi), getHealthBoost(multi));
  }
  
  int getAttackBoost(int multi)
  {
    return ((isGold ? ATTACK_BOOST_GOLD : ATTACK_BOOST) + playerLink.getStatistic().getBoosts().getAttackSpell()) * multi;
  }
  
  int getHealthBoost(int multi)
  {
    return ((isGold ? HEALTH_BOOST_GOLD : HEALTH_BOOST) + playerLink.getStatistic().getBoosts().getHealthSpell()) * multi;
  }
}
