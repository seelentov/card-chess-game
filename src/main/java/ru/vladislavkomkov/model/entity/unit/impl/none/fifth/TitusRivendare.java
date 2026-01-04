package ru.vladislavkomkov.model.entity.unit.impl.none.fifth;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import ru.vladislavkomkov.model.entity.unit.impl.Extra;
import ru.vladislavkomkov.model.player.Player;

public class TitusRivendare extends Extra
{
  public TitusRivendare()
  {
    this(DUMP_PLAYER);
  }
  
  public TitusRivendare(Player playerLink)
  {
    super(playerLink);
    
    attack = 1;
    
    level = 5;
    
    maxHealth = 7;
    
    isTavern = true;
    
    onDead = new Action(isGold() ? 2 : 1, 0);
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    return "Your Deathrattles trigger " + (isGold() ? "2" : "an") + " extra time" + (isGold() ? "s" : "");
  }
}
