package ru.vladislavkomkov.model.entity.unit.impl.beast.second;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cubling;
import ru.vladislavkomkov.model.player.Player;

import java.util.List;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

public class SewerRat extends Unit
{
  public SewerRat()
  {
    this(DUMP_PLAYER);
  }
  
  public SewerRat(Player playerLink)
  {
    super(playerLink);
    
    attack = 3;
    
    level = 2;
    
    maxHealth = 2;
    
    isTavern = true;
    
    unitType = List.of(UnitType.BEAST);
    
    listener.onDeadListeners.put(
        KEY_CORE,
        (game, fight, player1, player2, unit, attacker) -> {
          if (fight != null)
          {
            fight.addToFightTable(player1, isGold ? new HalfShell(player1).newGold() : new HalfShell(player1), unit, true);
          }
          else
          {
            int index = player1.getIndex(unit);
            player1.addToTable(isGold ? new HalfShell(player1).newGold() : new HalfShell(player1), index + 1, true);
          }
        });
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    Unit sub = isGold ? new HalfShell(playerLink).buildGold() : new HalfShell(playerLink);
    return "Deathrattle: Summon a " + sub.getAttack() + "/" + sub.getHealth() + " Turtle with Taunt.";
  }
  
  public static class HalfShell extends Unit
  {
    public HalfShell()
    {
      this(DUMP_PLAYER);
    }
    
    public HalfShell(Player playerLink)
    {
      super(playerLink);
      
      attack = 2;
      maxHealth = 3;
      
      isTaunt = true;
      
      level = 1;
      
      unitType = List.of(UnitType.BEAST);
      
      actualHealth = getMaxHealth();
    }
  }
  
}
