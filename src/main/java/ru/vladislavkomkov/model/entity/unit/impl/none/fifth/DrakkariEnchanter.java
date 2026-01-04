package ru.vladislavkomkov.model.entity.unit.impl.none.fifth;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import ru.vladislavkomkov.model.entity.unit.impl.Extra;
import ru.vladislavkomkov.model.player.Player;

public class DrakkariEnchanter extends Extra
{
  public DrakkariEnchanter()
  {
    this(DUMP_PLAYER);
  }
  
  public DrakkariEnchanter(Player playerLink)
  {
    super(playerLink);
    
    attack = 1;
    
    level = 5;
    
    maxHealth = 5;
    
    isTavern = true;
    
    onEndTurnExtra = new Action(0, isGold() ? 2 : 1);
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    return "Your end of turn effects trigger " + (isGold() ? "three times" : "twice");
  }
  
}
