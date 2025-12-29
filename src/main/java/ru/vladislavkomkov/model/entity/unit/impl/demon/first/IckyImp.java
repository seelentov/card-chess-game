package ru.vladislavkomkov.model.entity.unit.impl.demon.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.entity.unit.impl.trash.demon.first.Imp;
import ru.vladislavkomkov.model.player.Player;

public class IckyImp extends Unit
{
  public IckyImp()
  {
    this(DUMP_PLAYER);
  }
  
  public IckyImp(Player playerLink)
  {
    super(playerLink);
    
    unitType = List.of(UnitType.DEMON);
    
    attack = 1;
    
    maxHealth = 1;
    
    isTavern = true;
    
    level = 1;
    
    listener.onDeadListeners.put(
        KEY_CORE,
        (game, fight, player1, player2, unit, attacker) -> {
          if (fight != null)
          {
            for (int i = 0; i < 2; i++)
            {
              fight.addToFightTable(player1, isGold ? new Imp(player1).buildGold() : new Imp(player1), unit, true);
            }
          }
          else
          {
            int index = player1.getIndex(unit);
            for (int i = 0; i < 2; i++)
            {
              player1.addToTable(isGold ? new Imp(player1).buildGold() : new Imp(player1), index + 1, true);
            }
          }
        });
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    Unit unit = isGold ? new Imp(playerLink).buildGold() : new Imp(playerLink);
    return "Deathrattle: Summon two " + unit.getAttack() + "/" + unit.getHealth() + " Imps";
  }
}
