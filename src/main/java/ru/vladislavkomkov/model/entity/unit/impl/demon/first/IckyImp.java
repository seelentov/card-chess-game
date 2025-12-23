package ru.vladislavkomkov.model.entity.unit.impl.demon.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.entity.unit.impl.trash.demon.first.Imp;
import ru.vladislavkomkov.model.player.Player;

public class IckyImp extends Unit
{
  public IckyImp()
  {
    description = "Deathrattle: Summon two 1/1 Imps";
    
    unitType = List.of(UnitType.DEMON);
    
    attack = 1;
    
    maxHealth = 1;
    actualHealth = 1;
    
    isTavern = true;
    
    level = 1;
    
    listener.onDeadListeners.put(
        KEY_CORE,
        (game, fight, player, player2, unit, attacker) -> {
          if (fight != null)
          {
            for (int i = 0; i < 2; i++)
            {
              fight.addToFightTable(player, new Imp(), unit, true);
            }
          }
          else
          {
            int index = player.getIndex(unit);
            for (int i = 0; i < 2; i++)
            {
              player.addToTable(new Imp(), index + 1, true);
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
        (game, fight, player, player2, unit1, attacker) -> {
          if (fight != null)
          {
            for (int i = 0; i < 2; i++)
            {
              fight.addToFightTable(player, new Imp().newGold(), unit1);
            }
          }
          else
          {
            int index = player.getIndex(unit1);
            for (int i = 0; i < 2; i++)
            {
              player.addToTable(new Imp().newGold(), index + 1);
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
