package ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.TrashUnit;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;

public class Mechorse extends TrashUnit
{
  public Mechorse()
  {
    this(DUMP_PLAYER);
  }
  
  public Mechorse(Player playerLink)
  {
    super(playerLink);
    
    attack = 2;
    maxHealth = 2;
    
    level = 1;
    
    unitType = List.of(UnitType.MECH, UnitType.BEAST);
    
    actualHealth = getMaxHealth();
    
    getListener().onDeadListeners.put(
        KEY_CORE,
        (game, fight, player1, player2, unit, attacker) -> {
          if (fight != null)
          {
            fight.addToFightTable(player1, isGold ? new Mechapony(player1).newGold() : new Mechapony(player1), unit, true);
          }
          else
          {
            int index = player1.getIndex(unit);
            player1.addToTable(isGold ? new Mechapony(player1).newGold() : new Mechapony(player1), index + 1, true);
          }
        });
  }
  
  @Override
  public String getDescription()
  {
    Unit sub = isGold() ? new Mechapony(playerLink).buildGold() : new Mechapony(playerLink);
    return "Deathrattle: Summon a " + sub.getBaseAttack() + "/" + sub.getBaseHealth() + " " + sub.getName();
  }
}
