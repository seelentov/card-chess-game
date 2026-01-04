package ru.vladislavkomkov.model.entity.unit.impl.murloc.secord;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.RandUtils;

public class GrimscaleElegist extends Unit
{
  public static final int ATTACK_BOOST = 1;
  public static final int HEALTH_BOOST = 1;
  
  public GrimscaleElegist()
  {
    this(DUMP_PLAYER);
  }
  
  public GrimscaleElegist(Player playerLink)
  {
    super(playerLink);
    
    unitType = List.of(UnitType.MURLOC);
    
    attack = 3;
    
    maxHealth = 2;
    
    level = 2;
    
    isTavern = true;
    
    actualHealth = getMaxHealth();
    
    getListener().onEndTurnListeners.put(
        KEY_CORE,
        (game, fight, player) -> {
          List<Unit> unitsTable = player.getTable();
          
          if (!unitsTable.isEmpty())
          {
            int tableRand = RandUtils.getRand(unitsTable.size() - 1);
            addBuff(unitsTable.get(tableRand));
          }
          
          List<Unit> unitsHand = player.getHand().stream()
              .filter(card -> !card.isSpell())
              .map(card -> (Unit) card.getEntity())
              .toList();
          
          if (!unitsHand.isEmpty())
          {
            int tableRand = RandUtils.getRand(unitsHand.size() - 1);
            addBuff(unitsHand.get(tableRand));
          }
        });
  }
  
  private void addBuff(Unit unit)
  {
    int attack = ATTACK_BOOST * (isGold() ? 2 : 1);
    int health = HEALTH_BOOST * (isGold() ? 2 : 1);
    
    unit.addBuff(new Buff(
        unit1 -> {
          unit.incHealth(health);
          unit.incBaseAttack(attack);
        },
        null,
        getDescription()));
  }
  
  @Override
  public String getDescription()
  {
    int attack = ATTACK_BOOST * (isGold() ? 2 : 1);
    int health = HEALTH_BOOST * (isGold() ? 2 : 1);
    return "At the end of your turn, give a friendly minion and a minion in your hand +" + attack + "/+" + health + ".";
  }
}
