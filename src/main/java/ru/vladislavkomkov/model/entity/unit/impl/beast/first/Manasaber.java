package ru.vladislavkomkov.model.entity.unit.impl.beast.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cubling;
import ru.vladislavkomkov.model.player.Player;

public class Manasaber extends Unit
{
  public Manasaber()
  {
    this(DUMP_PLAYER);
  }
  
  public Manasaber(Player playerLink)
  {
    super(playerLink);
    
    level = 1;
    isTavern = true;
    
    attack = 4;
    
    maxHealth = 1;
    
    unitType = List.of(UnitType.BEAST);
    
    listener.onDeadListeners.put(
        KEY_CORE,
        (game, fight, player1, player2, unit, attacker) -> {
          if (fight != null)
          {
            for (int i = 0; i < 2; i++)
            {
              fight.addToFightTable(player1, isGold ? new Cubling(player1).newGold() : new Cubling(player1), unit, true);
            }
          }
          else
          {
            int index = player1.getIndex(unit);
            for (int i = 0; i < 2; i++)
            {
              player1.addToTable(isGold ? new Cubling(player1).newGold() : new Cubling(player1), index + 1, true);
            }
          }
        });
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    Unit sub = isGold ? new Cubling(playerLink).buildGold() : new Cubling(playerLink);
    return "Summon a " + sub.getAttack() + "/" + sub.getHealth() + " Cat";
  }
}
