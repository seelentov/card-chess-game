package ru.vladislavkomkov.model.entity.unit.impl.beast.third;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.ReflectUtils;

public class SlyRaptor extends Unit
{
  public static int ATTACK = 8;
  public static int HEALTH = 8;
  
  public SlyRaptor()
  {
    this(DUMP_PLAYER);
  }
  
  public SlyRaptor(Player playerLink)
  {
    super(playerLink);
    
    level = 3;
    isTavern = true;
    
    attack = 1;
    
    maxHealth = 3;
    
    unitType = List.of(UnitType.BEAST);
    
    listener.onDeadListeners.put(
        KEY_CORE,
        (game, fight, player1, player2, unit, attacker) -> {
          List<Class<? extends Unit>> pool = player1.getTavern().getUnitsPool().stream().filter(u -> ReflectUtils.getInstance(u).isType(UnitType.BEAST)).toList();
          
          if (pool.isEmpty())
          {
            return;
          }
          
          Unit summon = ReflectUtils.getInstance(pool.get(RandUtils.getRand(pool.size() - 1)));
          
          summon.setBaseAttack(ATTACK * (isGold() ? 2 : 1));
          summon.setHealth(HEALTH * (isGold() ? 2 : 1));
          
          if (fight != null)
          {
            for (int i = 0; i < 2; i++)
            {
              fight.addToFightTable(player1, summon, unit, true);
            }
          }
          else
          {
            int index = player1.getIndex(unit);
            for (int i = 0; i < 2; i++)
            {
              player1.addToTable(summon, index + 1, true);
            }
          }
        });
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    int attack = ATTACK * (isGold() ? 2 : 1);
    int health = HEALTH * (isGold() ? 2 : 1);
    
    return "Deathrattle: Summon a random Beast. Set its stats to " + attack + "/" + health + ".";
  }
}
