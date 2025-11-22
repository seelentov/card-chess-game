package ru.vladislavkomkov.model.entity.unit.impl.demon.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Type;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.trash.demon.first.Imp;

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
          if (player.inFight())
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
  
  @Override
  public Unit buildGold(Unit unit, Unit unit2, Unit unit3)
  {
    Unit gold = super.buildGold(unit, unit2, unit3);
    gold.setDescription("Deathrattle: Summon two 2/2 Imps");
    gold.getListener().onDeadListeners.put(
        KEY_CORE,
        (game, player, player2, uni1, attacker) -> {
          if (player.inFight())
          {
            int index = player.getFightIndex(gold);
            for (int i = 0; i < 2; i++)
            {
              player.addToFightTable(new Imp().newGold(), index + 1, true);
            }
          }
          else
          {
            int index = player.getIndex(gold);
            for (int i = 0; i < 2; i++)
            {
              player.addToTable(new Imp().newGold(), index + 1);
            }
          }
        });
    
    return gold;
  }
}
