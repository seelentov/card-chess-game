package ru.vladislavkomkov.model.entity.unit.impl.none.fifth;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.entity.unit.impl.Extra;
import ru.vladislavkomkov.model.player.Player;

public class BrannBronzebeard extends Extra
{
  public BrannBronzebeard()
  {
    this(DUMP_PLAYER);
  }
  
  public BrannBronzebeard(Player playerLink)
  {
    super(playerLink);
    
    attack = 2;
    
    level = 5;
    
    maxHealth = 4;
    
    isTavern = true;
    
    onPlayed = new Action(0, isGold() ? 2 : 1);
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    return "Your Battlecries trigger " + (isGold() ? "three times" : "twice");
  }
}
