package ru.vladislavkomkov.model.entity.unit.impl.beast.fifth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

public class RaptorElder extends Unit
{
  public static final int ATTACK_BOOST = 3;
  public static final int HEALTH_BOOST = 2;
  
  private int stacks = 1;
  
  public RaptorElder()
  {
    this(DUMP_PLAYER);
  }
  
  public RaptorElder(Player playerLink)
  {
    super(playerLink);
    
    attack = 2;
    
    maxHealth = 7;
    
    isTavern = true;
    
    level = 5;
    
    isDisguise = true;
    
    unitType = List.of(UnitType.BEAST);
    
    actualHealth = getMaxHealth();
    
    String tempKey = UUIDUtils.generateKey();
    
    getListener().onAppearListeners.put(
        KEY_CORE,
        (game, fight, player, player2) -> {
          processCoreListener(fight, player, tempKey);
        });
    
    getListener().onStartFightListeners.put(
        KEY_CORE,
        (game, fight, player, player2) -> {
          processCoreListener(fight, player, tempKey);
        });
    
    getListener().onDisappearListeners.put(
        KEY_CORE,
        (game, fight, player, player2) -> {
          if (fight == null)
          {
            return;
          }
          
          removeBuffs(fight, player);
          
          fight.getFightPlayer(player).getListener().onSummonedListeners.remove(tempKey);
        });
  }
  
  @Override
  public String getDescription()
  {
    return "\n" +
        "Stealth. Your Beasts have +" + getAttackBoost() + "/+" + getHealthBoost() + ". (Improved by each Beast you've summoned this combat!)";
  }
  
  private void processCoreListener(Fight fight, Player player, String tempKey)
  {
    if (fight == null)
    {
      return;
    }
    
    stacks = 1;
    
    calcAndAddBuffs(fight, player);
    
    fight.getFightPlayer(player).getListener().onSummonedListeners.put(
        tempKey,
        (game1, fight1, player1, entity) -> {
          if (entity instanceof Unit unit && unit.isType(UnitType.BEAST))
          {
            stacks += 1;
            calcAndAddBuffs(fight, player);
          }
        });
  }
  
  private void calcAndAddBuffs(Fight fight, Player player)
  {
    fight.getFightTable(player).forEach(this::calcAndAddBuff);
  }
  
  private void removeBuffs(Fight fight, Player player)
  {
    fight.getFightTable(player).forEach(this::removeBuff);
  }
  
  private void removeBuff(Unit unit)
  {
    Optional<Buff> lastBuff = unit.getBuffs().stream()
        .filter(buff -> buff.getCreator().equals(getID()))
        .findFirst();
    
    if (lastBuff.isPresent())
    {
      Buff buff = lastBuff.get();
      buff.getRollback().accept(unit);
      unit.getBuffs().remove(buff);
    }
  }
  
  private void calcAndAddBuff(Unit unit)
  {
    removeBuff(unit);
    
    int attackBoost = getAttackBoost();
    int healthBoost = getHealthBoost();
    
    unit.addBuff(new Buff(
        unit1 -> {
          unit1.incHealth(healthBoost);
          unit1.incBaseAttack(attackBoost);
        },
        unit1 -> {
          unit1.decHealth(healthBoost);
          unit1.decBaseAttack(attackBoost);
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
