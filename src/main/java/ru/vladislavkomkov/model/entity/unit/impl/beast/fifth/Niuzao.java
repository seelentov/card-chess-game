package ru.vladislavkomkov.model.entity.unit.impl.beast.fifth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Mechorse;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.RandUtils;

public class Niuzao extends Unit
{
  public Niuzao()
  {
    this(DUMP_PLAYER);
  }
  
  public Niuzao(Player playerLink)
  {
    super(playerLink);
    
    attack = 7;
    
    maxHealth = 6;
    
    isTavern = true;
    
    level = 5;
    
    unitType = List.of(UnitType.BEAST);
    
    actualHealth = getMaxHealth();
    
    getListener().onAttackListeners.put(
        KEY_CORE,
        (game, fight, player1, player2, unit, attacked) -> {
          if (fight == null)
          {
            return;
          }
          
          for (int i = 0; i < (isGold() ? 2 : 1); i++)
          {
            List<Unit> enemies = fight.getFightTable(player2);
            if (enemies.size() < 2)
            {
              return;
            }
            
            Unit randEnemy;
            do
            {
              randEnemy = enemies.get(RandUtils.getRand(enemies.size() - 1));
            }
            while (randEnemy == attacked);
            randEnemy.onAttacked(game, fight, player2, player1, unit, true);
          }
        });
  }
  
  @Override
  public String getDescription()
  {
    return "Rally: Deal damage equal to this minion's Attack to " + (isGold() ? "2" : "a") + " random enemy minion" + (isGold() ? "s" : "") + " other than the target.";
  }
}
