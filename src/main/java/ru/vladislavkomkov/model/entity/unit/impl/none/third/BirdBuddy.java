package ru.vladislavkomkov.model.entity.unit.impl.none.third;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import ru.vladislavkomkov.model.Listener;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

import java.util.List;

public class BirdBuddy extends Unit
{
  public static int ATTACK_BOOST = 1;
  public static int HEALTH_BOOST = 1;
  
  public BirdBuddy()
  {
    this(DUMP_PLAYER);
  }
  
  public BirdBuddy(Player player)
  {
    super(player);
    
    level = 3;
    isTavern = true;
    
    attack = 2;
    
    maxHealth = 4;
    
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

                List<Unit> units = fight != null ? fight.getFightTable(player1) : player1.getTable();
                units.stream().filter(unit1 -> unit1.isType(UnitType.BEAST)).forEach(this::addBuff);
              });
        });
    
    getListener().onDisappearListeners.put(
        KEY_CORE,
        (g, f, p, e) -> {
          Listener listener = f != null ? f.getFightPlayer(p).getListener() : p.getListener();
          listener.onDeadListeners.remove(tempKey);
        });
  }
  
  @Override
  public String getDescription()
  {
    int attack = ATTACK_BOOST * (isGold() ? 2 : 1);
    int health = HEALTH_BOOST * (isGold() ? 2 : 1);
    
    return "Avenge (1): Give your Beasts +" + attack + "/+" + health + ".";
  }
  
  private void addBuff(Unit unit)
  {
    unit.addBuff(new Buff(
        unit1 -> {
          unit1.incBaseAttack(ATTACK_BOOST * (isGold() ? 2 : 1));
          unit1.incHealth(HEALTH_BOOST * (isGold() ? 2 : 1));
        },
        unit1 -> {
          unit1.decBaseAttack(ATTACK_BOOST * (isGold() ? 2 : 1));
          unit1.decHealth(HEALTH_BOOST * (isGold() ? 2 : 1));
        },
        getDescription(),
        getID()));
  }
}
