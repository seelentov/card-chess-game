package ru.vladislavkomkov.model.entity.unit.impl.beast.fifth;

import ru.vladislavkomkov.model.Listener;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

import java.util.List;
import java.util.Optional;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

public class SilithidBurrower extends Unit{

  public static final int ATTACK_BOOST = 1;
  public static final int HEALTH_BOOST = 1;
  
  int stacks = 1;
  
  public SilithidBurrower()
  {
    this(DUMP_PLAYER);
  }
  
  public SilithidBurrower(Player playerLink)
  {
    super(playerLink);
    
    attack = 5;
    
    maxHealth = 4;
    
    isTavern = true;
    
    level = 5;
    
    unitType = List.of(UnitType.BEAST);
    
    actualHealth = getMaxHealth();
    
    String tempKey = UUIDUtils.generateKey();
    
    getListener().onAppearListeners.put(
        KEY_CORE,
        (g, f, p, e) -> {
          Listener listener = f != null ? f.getFightPlayer(p).getListener() : p.getListener();
          
          listener.onDeadListeners.put(
              tempKey,
              (game, fight, player1, player2, unit, attacker) -> {
                if (unit.getID().equals(getID()))
                {
                  return;
                }
                
                Optional<Unit> fromTable = player1.getTableUnit(getID());
                if (fromTable.isPresent())
                {
                  SilithidBurrower unit1 = ((SilithidBurrower) fromTable.get());
                  unit1.stacks += 1;
                }
                
                Optional<Unit> fromFightTable = fight != null ? fight.getFightUnit(player1, getID()) : Optional.empty();
                if (fromFightTable.isPresent())
                {
                  SilithidBurrower unit1 = ((SilithidBurrower) fromFightTable.get());
                  unit1.stacks += 1;
                }
              });
        });
    
    getListener().onDisappearListeners.put(
        KEY_CORE,
        (g, f, p, e) -> {
          Listener listener = f != null ? f.getFightPlayer(p).getListener() : p.getListener();
          listener.onDeadListeners.remove(tempKey);
        });
    
    getListener().onDeadListeners.put(
        KEY_CORE,
        (game, fight, player1, player2, unit, attacker) -> {
          List<Unit> units = fight != null ? fight.getFightTable(player1) : player1.getTable();
          units.stream()
              .filter(unit1 -> unit1.isType(UnitType.BEAST) && !unit1.getID().equals(getID()))
              .forEach(this::addBuff);
        });
  }
  
  @Override
  public String getDescription()
  {
    return "Deathrattle: Give your Beasts +" + getAttackBoost() + "/+" + getHealthBoost() + "." +
        " Avenge (1): Improve this by +" + (isGold() ? 2 : 1) + "/+" + (isGold() ? 2 : 1) + " permanently.";
  }
  
  private void addBuff(Unit unit)
  {
    unit.addBuff(new Buff(
        unit1 -> {
          unit1.incBaseAttack(getAttackBoost());
          unit1.incHealth(getHealthBoost());
        },
        unit1 -> {
          unit1.decBaseAttack(getAttackBoost());
          unit1.decHealth(getHealthBoost());
        },
        getDescription(),
        getID()));
  }
  
  private int getAttackBoost()
  {
    return getBoost(ATTACK_BOOST);
  }
  
  private int getHealthBoost()
  {
    return getBoost(HEALTH_BOOST);
  }
  
  private int getBoost(int i)
  {
    return i * stacks * (isGold() ? 2 : 1);
  }
}
