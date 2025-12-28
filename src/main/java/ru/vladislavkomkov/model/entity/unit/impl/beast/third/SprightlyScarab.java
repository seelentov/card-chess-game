package ru.vladislavkomkov.model.entity.unit.impl.beast.third;

import ru.vladislavkomkov.model.entity.Choice;
import ru.vladislavkomkov.model.entity.PlayPair;
import ru.vladislavkomkov.model.entity.PlayType;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

import java.util.List;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

public class SprightlyScarab extends Unit
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
    
    level = 3;
    
    playType = List.of(
        new PlayPair(PlayType.TABLE),
        new PlayPair(PlayType.CHOICE, List.of(
            new SprightlySprucing(isGold()),
            new SprightlySupport(isGold()))),
        new PlayPair(PlayType.TAVERN_FRENDLY, List.of(UnitType.BEAST)));
    
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          if (fight != null)
          {
            fight.addToFightTable(player, new Cat(), (Unit) entity);
          }
          else
          {
            int indexParent = player.getIndex((Unit) entity);
            player.addToTable(new Cat(), indexParent + 1);
          }
        });
  }
  
  public static class SprightlySprucing extends Choice
  {
    public SprightlySprucing(boolean isGold)
    {
      description = "Give a Beast " + (isGold ? "+2/+2" : "+1/+1" + " and Reborn.");
    }
  }
  
  public static class SprightlySupport extends Choice
  {
    public SprightlySupport(boolean isGold)
    {
      description = "Give a Beast " + (isGold ? "+8" : "+4") + " Attack and Windfury.";
    }
  }
}
