package ru.vladislavkomkov.model.entity.unit.impl.beast.fourth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;

public class ValiantTiger extends Unit
{
  public static final int ATTACK_BOOST = 5;
  public static final int HEALTH_BOOST = 5;
  
  public ValiantTiger()
  {
    this(DUMP_PLAYER);
  }
  
  public ValiantTiger(Player player)
  {
    super(player);
    
    level = 4;
    isTavern = true;
    
    attack = 6;
    
    maxHealth = 3;
    
    unitType = List.of(UnitType.BEAST);
    
    isTaunt = true;
    
    actualHealth = getMaxHealth();
    
    getListener().onDeadListeners.put(
        KEY_CORE,
        (game, fight, player1, player2, unit, attacker) -> {
          if (fight != null)
          {
            List<Unit> units = fight.getFightTable(player1);
            
            for (int i = units.size() - 1; i >= 0; i--)
            {
              Unit forBoost = units.get(i);
              if (forBoost.isType(UnitType.BEAST) && !forBoost.getID().equals(unit.getID()))
              {
                addBuff(forBoost);
                
                Optional<Unit> fromTable = player.getTableUnit(forBoost.getID());
                fromTable.ifPresent(this::addBuff);
                
                break;
              }
            }
          }
          else
          {
            List<Unit> units = player1.getTable();
            for (int i = units.size() - 1; i >= 0; i--)
            {
              Unit forBoost = units.get(i);
              if (forBoost.isType(UnitType.BEAST) && !forBoost.getID().equals(unit.getID()))
              {
                addBuff(unit);
                break;
              }
            }
          }
        });
  }
  
  private void addBuff(Unit unit)
  {
    unit.addBuff(new Buff(
        unit1 -> {
          unit1.incHealth(HEALTH_BOOST * (isGold() ? 2 : 1));
          unit1.incBaseAttack(ATTACK_BOOST * (isGold() ? 2 : 1));
        },
        null,
        getDescription(),
        getID()));
  }
  
  @Override
  public String getDescription()
  {
    int attack = ATTACK_BOOST * (isGold() ? 2 : 1);
    int health = HEALTH_BOOST * (isGold() ? 2 : 1);
    
    return "Taunt. Deathrattle: Give your right-most Beast +" + attack + "/+" + health + " permanently.";
  }
}
