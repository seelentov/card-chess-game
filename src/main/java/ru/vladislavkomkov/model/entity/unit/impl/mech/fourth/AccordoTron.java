package ru.vladislavkomkov.model.entity.unit.impl.mech.fourth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.entity.unit.impl.mech.Magnetized;
import ru.vladislavkomkov.model.player.Player;

public class AccordoTron extends Magnetized
{
  public static int GOLD = 1;
  
  public AccordoTron()
  {
    this(DUMP_PLAYER);
  }
  
  public AccordoTron(Player playerLink)
  {
    super(playerLink);
    
    level = 4;
    isTavern = true;
    
    attack = 5;
    
    maxHealth = 5;
    
    unitType = List.of(UnitType.MECH);
    
    isMagnet = true;
    
    listener.onStartTurnListeners.put(
        KEY_CORE,
        (game, fight, player1) -> player1.addMoney(GOLD));
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    return "At the start of your turn, gain " + (GOLD * (isGold ? 2 : 1)) + " Gold";
  }
}
