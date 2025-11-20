package ru.vladislavkomkov.models.entity.unit.impl.mech.fourth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import ru.vladislavkomkov.models.entity.unit.impl.mech.Mech;

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
}
