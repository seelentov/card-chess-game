package ru.vladislavkomkov.model.entity.unit.impl.beast.fourth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.Listener;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

public class MamaBear extends Unit
{
  public static int ATTACK_BOOST = 6;
  public static int HEALTH_BOOST = 6;
  
  public MamaBear()
  {
    this(DUMP_PLAYER);
  }
  
  public MamaBear(Player player)
  {
    super(player);
    
    level = 4;
    isTavern = true;
    
    attack = 6;
    
    maxHealth = 6;
    
    unitType = List.of(UnitType.BEAST);
    
    actualHealth = getMaxHealth();
    
    String tempKey = UUIDUtils.generateKey();
    
    getListener().onAppearListeners.put(
        KEY_CORE,
        (g, f, p, e) -> {
          Listener listener = f != null
              ? f.getFightPlayer(p).getListener()
              : p.getListener();
          
          listener.onSummonedListeners.put(
              tempKey,
              (game, fight, player1, unit) -> {
                if (((Unit) unit).isType(UnitType.BEAST))
                {
                  addBuff((Unit) unit);
                }
              });
        });
    
    getListener().onDisappearListeners.put(
        KEY_CORE,
        (g, f, p, e) -> {
          Listener listener = f != null
              ? f.getFightPlayer(p).getListener()
              : p.getListener();
          
          listener.onSummonedListeners.remove(tempKey);
        });
  }
  
  @Override
  public String getDescription()
  {
    int attack = ATTACK_BOOST * (isGold() ? 2 : 1);
    int health = HEALTH_BOOST * (isGold() ? 2 : 1);
    return "Whenever you summon a Beast, give it +" + attack + "/+" + health + ".";
  }
  
  private void addBuff(Unit unit)
  {
    unit.addBuff(new Buff(
        unit1 -> {
          unit1.incBaseAttack(ATTACK_BOOST * (isGold() ? 2 : 1));
          unit1.incHealth(HEALTH_BOOST * (isGold() ? 2 : 1));
        },
        null,
        getDescription()));
  }
}
