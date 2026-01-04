package ru.vladislavkomkov.model.entity.unit.impl.beast.fourth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ru.vladislavkomkov.model.action.OnAttackAction;
import ru.vladislavkomkov.model.entity.Entity;
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
    
    actualHealth = getMaxHealth();
    
    getListener().onAttackListeners.put(UUIDUtils.generateKey(ON_ATTACK_LISTENER_PREFIX), newOnAttackActionIncAttack(isGold()));
    getListener().onAttackListeners.put(ON_ATTACK_LISTENER_KEY, newOnAttackActionGetAnother());
  }
  
  @Override
  public String getDescription()
  {
    return "Rally: Give your other Beasts +" + (ATTACK_BOOST * (isGold() ? 2 : 1)) + " Attack and this Rally.";
  }
  
  @Override
  public Unit buildGold(Unit unit, Unit unit2, Unit unit3)
  {
    Unit gold = super.buildGold(unit, unit2, unit3);
    
    gold.getListener().onAttackListeners.remove(ON_ATTACK_LISTENER_KEY);
    gold.getListener().onAttackListeners.entrySet().removeIf(entry -> entry.getKey().startsWith(ON_ATTACK_LISTENER_PREFIX));
    
    List<Map.Entry<String, OnAttackAction>> onAttackListeners = gold.getListener().onAttackListeners.entrySet().stream().toList();
    for (int i = 0; i < onAttackListeners.size(); i++)
    {
      String key = onAttackListeners.get(i).getKey();
      if (key.startsWith(ON_ATTACK_LISTENER_KEY))
      {
        gold.getListener().onAttackListeners.remove(key);
        break;
      }
    }
    
    gold.getListener().onAttackListeners.put(UUIDUtils.generateKey(ON_ATTACK_LISTENER_PREFIX), newOnAttackActionIncAttack(true));
    gold.getListener().onAttackListeners.put(ON_ATTACK_LISTENER_KEY, newOnAttackActionGetAnother());
    
    return gold;
  }
  
  public OnAttackAction newOnAttackActionIncAttack(boolean isGold)
  {
    return (game, fight, player1, player2, unit, attacked) -> {
      List<Unit> units = fight != null ? fight.getFightTable(player1) : player1.getTable();
      List<Unit> forBoost = units.stream().filter(unit1 -> unit1.isType(UnitType.BEAST) && !unit1.getID().equals(unit.getID())).toList();
      
      for (Unit un : forBoost)
      {
        un.addBuff(new Buff(unit1 -> unit1.incBaseAttack(ATTACK_BOOST * (isGold ? 2 : 1)), null, getDescription()));
      }
    };
  }
  
  public OnAttackAction newOnAttackActionGetAnother()
  {
    return (game, fight, player1, player2, unit, attacked) -> {
      List<Unit> units = fight != null ? fight.getFightTable(player1) : player1.getTable();
      List<Unit> forBoost = units.stream().filter(unit1 -> unit1.isType(UnitType.BEAST) && !unit1.getID().equals(unit.getID())).toList();
      
      for (Unit un : forBoost)
      {
        unit.getListener().onAttackListeners.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(ON_ATTACK_LISTENER_KEY))
            .forEach(entry -> {
              String key = entry.getKey().equals(ON_ATTACK_LISTENER_KEY) ? ON_ATTACK_LISTENER_KEY : UUIDUtils.generateKey(ON_ATTACK_LISTENER_PREFIX);
              un.addBuff(new Buff(
                  unit1 -> unit1.getListener().onAttackListeners.put(key, entry.getValue()),
                  null,
                  getDescription()));
            });
      }
    };
  }
}
