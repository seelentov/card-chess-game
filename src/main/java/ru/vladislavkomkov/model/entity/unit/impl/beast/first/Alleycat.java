package ru.vladislavkomkov.model.entity.unit.impl.beast.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.player.Player;

public class Alleycat extends Unit
{
  public Alleycat()
  {
    super();
    
    description = "Summon a 1/1 Cat";
    level = 1;
    isTavern = true;
    
    attack = 1;
    
    maxHealth = 1;
    actualHealth = 1;
    
    unitType = List.of(UnitType.BEAST);
    
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
  
  @Override
  public Unit buildGold(Unit unit, Unit unit2, Unit unit3)
  {
    Unit gold = super.buildGold(unit, unit2, unit3);
    
    gold.setDescription("Summon a 2/2 Cat");
    gold.getListener().onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          if (fight != null)
          {
            fight.addToFightTable(player, new Cat().newGold(), (Unit) entity);
          }
          else
          {
            int indexParent = player.getIndex((Unit) entity);
            player.addToTable(new Cat().newGold(), indexParent + 1);
          }
        });
    
    return gold;
  }
  
  @Override
  public void buildFace(Player player)
  {
    
  }
}
