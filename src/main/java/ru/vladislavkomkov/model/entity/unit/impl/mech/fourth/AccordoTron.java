package ru.vladislavkomkov.model.entity.unit.impl.mech.fourth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.entity.unit.impl.mech.Magnetized;
import ru.vladislavkomkov.model.player.Player;

public class AccordoTron extends Magnetized
{
  public static int GOLD = 1;
  
  public AccordoTron()
  {
    super();

    description = "At the start of your turn, gain " + GOLD + " Gold";
    level = 4;
    isTavern = true;
    
    attack = 5;
    
    maxHealth = 5;
    actualHealth = 5;
    
    unitType = List.of(UnitType.MECH);
    
    isMagnet = true;
    
    listener.onStartTurnListeners.put(
        KEY_CORE,
        (game, fight, player) -> player.addMoney(GOLD));
  }
  
  @Override
  public Unit buildGold(Unit unit, Unit unit2, Unit unit3)
  {
    Unit gold = super.buildGold(unit, unit2, unit3);
    gold.setDescription("At the start of your turn, gain 2 Gold");
    gold.getListener().onStartTurnListeners.put(
        KEY_CORE,
        (game, fight, player) -> player.addMoney(GOLD * 2));
    
    return gold;
  }
  
  @Override
  public void buildFace(Player player)
  {
    
  }
}
