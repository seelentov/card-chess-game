package ru.vladislavkomkov.model.entity.unit.impl.beast.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cubling;
import ru.vladislavkomkov.model.player.Player;

public class Manasaber extends Unit
{
  public Manasaber()
  {
    description = "Deathrattle: Summon two 0/1 Cublings with Taunt";
    level = 1;
    isTavern = true;
    
    attack = 4;
    
    maxHealth = 1;
    actualHealth = 1;
    
    unitType = List.of(UnitType.BEAST);
    
    listener.onDeadListeners.put(
        KEY_CORE,
        (game, fight, player, player2, unit, attacker) -> {
          if (fight != null)
          {
            for (int i = 0; i < 2; i++)
            {
              fight.addToFightTable(player, new Cubling(), unit, true);
            }
          }
          else
          {
            int index = player.getIndex(unit);
            for (int i = 0; i < 2; i++)
            {
              player.addToTable(new Cubling(), index + 1, true);
            }
          }
        });
  }
  
  @Override
  public Unit buildGold(Unit unit, Unit unit2, Unit unit3)
  {
    Unit gold = super.buildGold(unit, unit2, unit3);
    gold.setDescription("Deathrattle: Summon two 0/2 Cublings with Taunt");
    gold.getListener().onDeadListeners.put(
        KEY_CORE,
        (game, fight, player, player2, unit1, attacker) -> {
          if (fight != null)
          {
            for (int i = 0; i < 2; i++)
            {
              fight.addToFightTable(player, new Cubling().newGold(), unit1, true);
            }
          }
          else
          {
            int index = player.getIndex(unit1);
            for (int i = 0; i < 2; i++)
            {
              player.addToTable(new Cubling().newGold(), index + 1, true);
            }
          }
        });
    
    return gold;
  }
  
  @Override
  public void buildFace(Player player)
  {
    
  }
}
