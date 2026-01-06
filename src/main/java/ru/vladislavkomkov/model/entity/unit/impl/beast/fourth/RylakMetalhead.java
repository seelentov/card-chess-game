package ru.vladislavkomkov.model.entity.unit.impl.beast.fourth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.RandUtils;

public class RylakMetalhead extends Unit
{
  public RylakMetalhead()
  {
    this(DUMP_PLAYER);
  }
  
  public RylakMetalhead(Player player)
  {
    super(player);
    
    level = 4;
    isTavern = true;
    
    attack = 5;
    
    maxHealth = 3;
    
    unitType = List.of(UnitType.BEAST);
    
    isTaunt = true;
    
    actualHealth = getMaxHealth();
    
    getListener().onDeadListeners.put(
        KEY_CORE,
        (game, fight, player1, player2, unit, attacker) -> {
          List<Unit> units = fight != null ? fight.getFightTable(player1) : player1.getTable();
          int index = units.indexOf(unit);
          
          Unit left = index > 0 && units.size() > index ? units.get(index - 1) : null;
          Unit right = units.size() - 1 > index ? units.get(index + 1) : null;
          
          List<Unit> unitsForActivated = new ArrayList<>();
          
          if (left != null && left.isAnswerOnDead())
          {
            unitsForActivated.add(left);
          }
          
          if (right != null && right.isAnswerOnDead())
          {
            unitsForActivated.add(right);
          }
          
          if (!isGold && unitsForActivated.size() > 1)
          {
            unitsForActivated.remove(RandUtils.getRand(1));
          }
          
          unitsForActivated.forEach(unit1 -> unit1.onDead(game, fight, player1, player2, null, false));
        });
  }
  
  @Override
  public String getDescription()
  {
    return "Deathrattle: Trigger the Battlecry of an adjacent minion" + (isGold() ? "s" : "");
  }
}
