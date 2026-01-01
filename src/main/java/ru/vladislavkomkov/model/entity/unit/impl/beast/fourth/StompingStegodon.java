package ru.vladislavkomkov.model.entity.unit.impl.beast.fourth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.vladislavkomkov.model.action.OnAttackAction;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

public class StompingStegodon extends Unit
{
  public static final int ATTACK_BOOST = 3;
  public static final String ON_ATTACK_LISTENER_KEY = "STEGODON-BOOST";
  public static final String ON_ATTACK_LISTENER_PREFIX = ON_ATTACK_LISTENER_KEY + "_";
  
  public StompingStegodon()
  {
    this(DUMP_PLAYER);
  }
  
  public StompingStegodon(Player player)
  {
    super(player);
    
    level = 4;
    isTavern = true;
    
    attack = 3;
    
    maxHealth = 3;
    
    unitType = List.of(UnitType.BEAST);
    
    isTaunt = true;
    
    actualHealth = getMaxHealth();
    
    boolean isGold = isGold();
    
    OnAttackAction onAttackActionIncAttack = (game, fight, player1, player2, unit, attacked) -> {
      List<Unit> units = fight != null ? fight.getFightTable(player1) : player1.getTable();
      List<Unit> forBoost = units.stream().filter(unit1 -> unit1.isType(UnitType.BEAST)).toList();
      
      for (Unit un : forBoost)
      {
        un.addBuff(new Buff(unit1 -> unit1.incBaseAttack(ATTACK_BOOST * (isGold ? 2 : 1)), null, getDescription()));
      }
    };
    
    OnAttackAction onAttackActionGetAnother = (game, fight, player1, player2, unit, attacked) -> {
      List<Unit> units = fight != null ? fight.getFightTable(player1) : player1.getTable();
      List<Unit> forBoost = units.stream().filter(unit1 -> unit1.isType(UnitType.BEAST) && !unit1.getID().equals(unit.getID())).toList();
      
      for (Unit un : forBoost)
      {
        unit.getListener().onAttackListeners.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(ON_ATTACK_LISTENER_KEY))
            .forEach(entry -> {
              un.addBuff(new Buff(
                  unit1 -> unit1.getListener().onAttackListeners.put(UUIDUtils.generateKey(ON_ATTACK_LISTENER_PREFIX), entry.getValue()),
                  null,
                  getDescription()));
            });
      }
    };
    
    getListener().onAttackListeners.put(UUIDUtils.generateKey(ON_ATTACK_LISTENER_PREFIX), onAttackActionIncAttack);
    getListener().onAttackListeners.put(ON_ATTACK_LISTENER_KEY, onAttackActionGetAnother);
  }
  
  @Override
  public String getDescription()
  {
    return "Rally: Give your other Beasts +" + (ATTACK_BOOST * (isGold() ? 2 : 1)) + " Attack and this Rally.";
  }
}
