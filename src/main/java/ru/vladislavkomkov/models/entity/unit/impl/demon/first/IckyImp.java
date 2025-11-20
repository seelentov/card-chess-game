package ru.vladislavkomkov.models.entity.unit.impl.demon.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.models.entity.unit.Type;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.impl.trash.demon.first.Imp;

public class IckyImp extends Unit
{
  public IckyImp()
  {
    description = "Deathrattle: Summon two 1/1 Imps";
    
    type = List.of(Type.DEMON);
    
    attack = 1;
    
    maxHealth = 1;
    actualHealth = 1;
    
    level = 1;
    
    listener.onDeadListeners.put(
        KEY_CORE,
        (game, player, player2, unit, attacker) -> {
          if (player.inFightTable != null)
          {
            int index = player.getFightIndex(this);
            for (int i = 0; i < 2; i++)
            {
              player.addToFightTable(new Imp(), index + 1, true);
            }
          }
          else
          {
            int index = player.getIndex(this);
            for (int i = 0; i < 2; i++)
            {
              player.addToTable(new Imp(), index + 1);
            }
          }
        });
  }
}
