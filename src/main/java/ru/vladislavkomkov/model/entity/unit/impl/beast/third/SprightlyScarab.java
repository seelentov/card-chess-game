package ru.vladislavkomkov.model.entity.unit.impl.beast.third;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.action.OnPlayedAction;
import ru.vladislavkomkov.model.card.Card;
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

import java.util.List;
import java.util.Optional;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

public class SprightlyScarab extends Choicer
{
  public SprightlyScarab()
  {
    super();
    
    description = "Choose One - Give a Beast +1/+1 and Reborn; or +4 Attack and Windfury.";
    
    attack = 3;
    
    actualHealth = 3;
    maxHealth = 3;
    
    unitType = List.of(UnitType.BEAST);
    
    isTavern = true;
    
    level = 1;
    
    playType = List.of(
        new PlayPair(PlayType.TABLE),
        new PlayPair(PlayType.CHOICE, List.of(
            new SprightlySprucing(isGold()),
            new SprightlySupport(isGold()))),
        new PlayPair(PlayType.TAVERN_FRENDLY, List.of(UnitType.BEAST)));
    
    isAnswerOnPlayed = false;
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          Choice choice;
          if (input.size() > 1)
          {
            choice = input.get(1) == 0 ? new SprightlySprucing(false) : new SprightlySupport(entity.isGold());
          }
          else
          {
            choice = RandUtils.getRand(1) == 0 ? new SprightlySprucing(false) : new SprightlySupport(entity.isGold());
          }
          
          choice.process(game, fight, player, entity, input, auto);
        });
  }
  
  @Override
  public Unit buildGold(Unit unit, Unit unit2, Unit unit3)
  {
    Unit gold = super.buildGold(unit, unit2, unit3);
    gold.setDescription("Choose One - Give a Beast +2/+2 and Reborn; or +8 Attack and Windfury.");
    gold.getListener().onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          Choice choice;
          if (input.size() > 1)
          {
            choice = input.get(1) == 0 ? new SprightlySprucing(true) : new SprightlySupport(true);
          }
          else
          {
            choice = RandUtils.getRand(1) == 0 ? new SprightlySprucing(true) : new SprightlySupport(true);
          }
          
          choice.process(game, fight, player, entity, input, auto);
        });
    
    return gold;
  }
  
  private static Unit findUnit(Fight fight, Player player, List<Integer> input)
  {
    boolean isTavernIndex = input.get(2) == 1;
    
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
    if (input.size() > 3)
    {
      index = input.get(3);
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
    
    public SprightlySprucing(boolean isGold)
    {
      super(isGold);
      
      description = "Give a Beast " + (this.isGold ? "+2/+2" : "+1/+1" + " and Reborn.");
    }
    
    public void process(Game game, Fight fight, Player player, Entity entity, List<Integer> input, boolean auto)
    {
      Unit unit = SprightlyScarab.findUnit(fight, player, input);
      unit.addBuff(new Buff(
          u -> {
            u.incAttack(ATTACK_BOOST * (this.isGold ? 2 : 1));
            u.incHealth(HEALTH_BOOST * (this.isGold ? 2 : 1));
            u.setIsRebirth(true);
          },
          null,
          getDescription()));
    }
    
    @Override
    public void buildFace(Player player)
    {
      description = "Give a Beast " + (this.isGold ? "+2/+2" : "+1/+1") + " and Reborn.";
    }
  }
  
  public static class SprightlySupport extends Choice
  {
    public static int ATTACK_BOOST = 4;
    
    public SprightlySupport(boolean isGold)
    {
      super(isGold);
      
      this.isGold = isGold;
      description = "Give a Beast " + (this.isGold ? "+8" : "+4") + " Attack and Windfury.";
    }
    
    public void process(Game game, Fight fight, Player player, Entity entity, List<Integer> input, boolean auto)
    {
      Unit unit = SprightlyScarab.findUnit(fight, player, input);
      unit.addBuff(new Buff(
          u -> {
            u.incAttack(ATTACK_BOOST * (this.isGold ? 2 : 1));
            u.setAttacksCount(AttacksCount.DOUBLE);
          },
          null,
          getDescription()));
    }
    
    @Override
    public void buildFace(Player player)
    {
      description = "Give a Beast " + (this.isGold ? "+8" : "+4") + " Attack and Windfury.";
    }
  }
}
