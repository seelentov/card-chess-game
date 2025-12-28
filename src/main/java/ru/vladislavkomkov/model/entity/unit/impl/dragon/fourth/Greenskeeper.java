package ru.vladislavkomkov.model.entity.unit.impl.dragon.fourth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.RandUtils;

public class Greenskeeper extends Unit
{
  public Greenskeeper()
  {
    super();
    
    description = "Rally: Trigger your right-most Battlecry";
    
    unitType = List.of(UnitType.DRAGON);
    
    attack = 4;
    
    maxHealth = 5;
    actualHealth = 5;
    
    isTavern = true;
    
    level = 4;
    
    listener.onAttackListeners.put(
        KEY_CORE,
        (game, fight, player, player2, unit1, attacked) -> {
          List<Unit> units = fight != null ? fight.getFightTable(player) : player.getTable();
          for (int i = units.size() - 1; i >= 0; i--)
          {
            Unit unit = units.get(i);
            if (unit.isAnswerOnPlayed())
            {
              unit.onPlayed(game, fight, player, RandUtils.getRand(units.size()), true);
              break;
            }
          }
        });
  }
  
  @Override
  public Unit buildGold(Unit unit, Unit unit2, Unit unit3)
  {
    Unit gold = super.buildGold(unit, unit2, unit3);
    gold.setDescription("Rally: Trigger your right-most Battlecry twice");
    gold.getListener().onAttackListeners.put(
        KEY_CORE,
        (game, fight, player, player2, unit1, attacked) -> {
          List<Unit> units = fight != null ? fight.getFightTable(player) : player.cloneTable();
          for (int i = units.size() - 1; i >= 0; i--)
          {
            Unit u = units.get(i);
            if (u.isAnswerOnPlayed())
            {
              u.onPlayed(game, fight, player, RandUtils.getRand(units.size()), RandUtils.getRand(units.size()));
              u.onPlayed(game, fight, player, RandUtils.getRand(units.size()), RandUtils.getRand(units.size()));
              break;
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
