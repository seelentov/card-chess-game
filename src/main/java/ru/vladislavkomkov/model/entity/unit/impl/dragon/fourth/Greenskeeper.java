package ru.vladislavkomkov.model.entity.unit.impl.dragon.fourth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.RandUtils;

public class Greenskeeper extends Unit
{
  public Greenskeeper()
  {
    this(DUMP_PLAYER);
  }
  
  public Greenskeeper(Player playerLink)
  {
    super(playerLink);
    
    unitType = List.of(UnitType.DRAGON);
    
    attack = 4;
    
    maxHealth = 5;
    
    isTavern = true;
    
    level = 4;
    
    listener.onAttackListeners.put(
        KEY_CORE,
        (game, fight, player1, player2, unit1, attacked) -> {
          List<Unit> units = fight != null ? fight.getFightTable(player1) : player1.getTable();
          for (int i = units.size() - 1; i >= 0; i--)
          {
            Unit unit = units.get(i);
            if (unit.isAnswerOnPlayed())
            {
              unit.onPlayed(game, fight, player1, RandUtils.getRand(units.size()), true);
              if (isGold)
              {
                unit.onPlayed(game, fight, player1, RandUtils.getRand(units.size()), true);
              }
              break;
            }
          }
        });
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    return "Rally: Trigger your right-most Battlecry" + (isGold ? " twice" : "");
  }
}
