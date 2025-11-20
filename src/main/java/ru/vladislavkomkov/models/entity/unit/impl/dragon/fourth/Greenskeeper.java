package ru.vladislavkomkov.models.entity.unit.impl.dragon.fourth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.models.entity.unit.Type;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.util.RandUtils;

public class Greenskeeper extends Unit
{
  public Greenskeeper()
  {
    description = "Rally: Trigger your right-most Battlecry";
    
    type = List.of(Type.DRAGON);
    
    attack = 4;
    
    maxHealth = 5;
    actualHealth = 5;
    
    level = 4;
    
    listener.onAttackListeners.put(
        KEY_CORE,
        (game, player, player2, unit1, attacked) -> {
          List<Unit> units = player.inFightTable != null ? player.inFightTable : player.cloneTable();
          for (int i = units.size() - 1; i >= 0; i--)
          {
            Unit unit = units.get(i);
            if (unit.isAnswerOnPlayed())
            {
              unit.onPlayed(game, player, RandUtils.getRand(units.size()), RandUtils.getRand(units.size()));
              break;
            }
          }
        });
  }
}
