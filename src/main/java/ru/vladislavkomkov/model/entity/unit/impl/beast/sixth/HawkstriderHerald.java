package ru.vladislavkomkov.model.entity.unit.impl.beast.sixth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;

public class HawkstriderHerald extends Unit
{
  public HawkstriderHerald()
  {
    this(DUMP_PLAYER);
  }
  
  public HawkstriderHerald(Player playerLink)
  {
    super(playerLink);
    
    attack = 6;
    
    maxHealth = 2;
    
    isTavern = true;
    
    level = 6;
    
    unitType = List.of(UnitType.BEAST);
    
    getListener().onStartFightListeners.put(
        KEY_CORE,
        (game, fight, player, player2) -> {
          if (fight == null)
          {
            return;
          }
          
          List<Unit> units = fight.getFightTable(player);
          List<Unit> unitsForUse = new ArrayList<>();
          int unitToUseCount = 2;
          for (Unit unit : units)
          {
            if (unit.isAnswerOnDead())
            {
              unitsForUse.add(unit);
              
              unitToUseCount -= 1;
              if (unitToUseCount <= 0)
              {
                break;
              }
            }
          }
          
          unitsForUse.forEach(unit -> {
            for (int i = 0; i < (isGold() ? 2 : 1); i++)
            {
              unit.onDead(game, fight, player, player2, null, false);
            }
          });
        });
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    return "Start of Combat: Trigger your two left-most Deathrattles" + (isGold() ? " twice" : "");
  }
}
