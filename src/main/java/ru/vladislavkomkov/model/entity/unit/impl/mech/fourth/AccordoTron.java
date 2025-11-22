package ru.vladislavkomkov.model.entity.unit.impl.mech.fourth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.mech.Mech;

public class AccordoTron extends Mech
{
  public AccordoTron()
  {
    super();
    description = "At the start of your turn, gain 1 Gold";
    level = 4;
    isTavern = true;
    
    attack = 5;
    
    maxHealth = 5;
    actualHealth = 5;
    
    listener.onStartTurnListeners.put(
        KEY_CORE,
        (game, player) -> player.addMoney());
  }
  
  @Override
  public Unit buildGold(Unit unit, Unit unit2, Unit unit3)
  {
    Unit gold = super.buildGold(unit, unit2, unit3);
    gold.setDescription("At the start of your turn, gain 2 Gold");
    gold.getListener().onStartTurnListeners.put(
        KEY_CORE,
        (game, player) -> player.addMoney(2));
    
    return gold;
  }
}
