package ru.vladislavkomkov.model.entity.unit.impl.beast.third;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.entity.Choice;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.PlayPair;
import ru.vladislavkomkov.model.entity.PlayType;
import ru.vladislavkomkov.model.entity.unit.AttacksCount;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Choicer;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.RandUtils;

public class SprightlyScarab extends Choicer
{
  public SprightlyScarab()
  {
    this(DUMP_PLAYER);
  }
  
  public SprightlyScarab(Player playerLink)
  {
    super(playerLink);
    
    attack = 3;
    
    maxHealth = 3;
    
    unitType = List.of(UnitType.BEAST);
    
    isTavern = true;
    
    level = 3;
    
    playType = List.of(
        new PlayPair(PlayType.TABLE),
        new PlayPair(PlayType.CHOICE, List.of(
            new SprightlySprucing(playerLink, isGold()),
            new SprightlySupport(playerLink, isGold()))),
        new PlayPair(PlayType.TAVERN_FRENDLY, List.of(UnitType.BEAST)));
    
    isAnswerOnPlayed = false;
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player1, entity, input, auto) -> {
          Choice choice;
          if (input.size() > 1)
          {
            choice = input.get(1) == 0 ? new SprightlySprucing(player1, entity.isGold()) : new SprightlySupport(player1, entity.isGold());
          }
          else
          {
            choice = RandUtils.getRand(1) == 0 ? new SprightlySprucing(player1, entity.isGold()) : new SprightlySupport(player1, entity.isGold());
          }
          
          choice.process(game, fight, player1, entity, input, auto);
        });
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    return "Choose One: "
        + new SprightlySprucing(playerLink, isGold).getDescription()
        + "; or "
        + new SprightlySupport(playerLink, isGold).getDescription();
  }
  
  private static Unit findUnit(Fight fight, Player player, List<Integer> input)
  {
    boolean isTavernIndex = input.get(3) == 1;
    
    List<Unit> units = isTavernIndex ? player.getTavern().getUnits() : fight != null ? fight.getFightTable(player) : player.getTable();
    int index = SprightlyScarab.calcIndex(input, units);
    
    Unit unit = units.get(index);
    
    if (!unit.isType(UnitType.BEAST))
    {
      Optional<Unit> unitOptional = units.stream().filter(u -> u.isType(UnitType.BEAST)).findFirst();
      if (unitOptional.isPresent())
      {
        unit = unitOptional.get();
      }
    }
    
    return unit;
  }
  
  private static int calcIndex(List<Integer> input, List<Unit> units)
  {
    int index;
    if (input.size() > 2)
    {
      index = input.get(2);
    }
    else
    {
      index = RandUtils.getRand(units.size() - 1);
    }
    
    if (input.get(0) <= index)
    {
      index += 1;
    }
    
    if (index >= units.size())
    {
      index = 0;
    }
    
    return index;
  }
  
  public static class SprightlySprucing extends Choice
  {
    public static int ATTACK_BOOST = 1;
    public static int HEALTH_BOOST = 1;
    
    public SprightlySprucing(Player player, boolean isGold)
    {
      super(player, isGold);
    }
    
    public void process(Game game, Fight fight, Player player, Entity entity, List<Integer> input, boolean auto)
    {
      Unit unit = SprightlyScarab.findUnit(fight, player, input);
      unit.addBuff(new Buff(
          u -> {
            u.incBaseAttack(ATTACK_BOOST * (this.isGold ? 2 : 1));
            u.incHealth(HEALTH_BOOST * (this.isGold ? 2 : 1));
            u.setIsRebirth(true);
          },
          null,
          getDescription()));
    }
    
    @Override
    public String getDescription()
    {
      int attackBoost = ATTACK_BOOST * (isGold ? 2 : 1);
      int healthBoost = HEALTH_BOOST * (isGold ? 2 : 1);
      
      return "Give a Beast +" + attackBoost + "/+" + healthBoost + " and Reborn.";
    }
  }
  
  public static class SprightlySupport extends Choice
  {
    public static int ATTACK_BOOST = 4;
    
    public SprightlySupport(Player player, boolean isGold)
    {
      super(player, isGold);
      
      this.isGold = isGold;
    }
    
    public void process(Game game, Fight fight, Player player, Entity entity, List<Integer> input, boolean auto)
    {
      Unit unit = SprightlyScarab.findUnit(fight, player, input);
      unit.addBuff(new Buff(
          u -> {
            u.incBaseAttack(ATTACK_BOOST * (this.isGold ? 2 : 1));
            u.setAttacksCount(AttacksCount.DOUBLE);
          },
          null,
          getDescription()));
    }
    
    @Override
    public String getDescription()
    {
      return "Give a Beast +" + ATTACK_BOOST * (this.isGold ? 2 : 1) + " Attack and Windfury.";
    }
  }
}
