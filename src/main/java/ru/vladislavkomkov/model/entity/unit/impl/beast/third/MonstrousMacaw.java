package ru.vladislavkomkov.model.entity.unit.impl.beast.third;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;

public class MonstrousMacaw extends Unit
{
  public MonstrousMacaw()
  {
    this(DUMP_PLAYER);
  }
  
  public MonstrousMacaw(Player player)
  {
    super(player);
    
    attack = 4;
    
    maxHealth = 3;
    
    unitType = List.of(UnitType.BEAST);
    
    isTavern = true;
    
    level = 3;
    
    listener.onAttackListeners.put(
        KEY_CORE,
        (game, fight, player1, player2, unit1, attacked) -> {
          List<Unit> units = fight != null ? fight.getFightTable(player1) : player1.getTable();
          for (Unit unit : units)
          {
            if (unit.isAnswerOnDead())
            {
              for (int i = 0; i < (isGold() ? 2 : 1); i++)
              {
                unit.onDead(game, fight, player1, player2, null, false);
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
    return "Rally: Trigger your left-most Deathrattle" + (isGold() ? " twice" : "") + "\n" +
        "(except this minion's).";
  }
}
