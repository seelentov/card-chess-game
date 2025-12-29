package ru.vladislavkomkov.model.entity.unit.impl.elemental.second;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.AttacksCount;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;

public class CracklingCyclone extends Unit
{
  public CracklingCyclone()
  {
    this(DUMP_PLAYER);
  }
  
  public CracklingCyclone(Player playerLink)
  {
    super(playerLink);
    
    unitType = List.of(UnitType.ELEMENTAL);
    
    attack = 3;
    
    maxHealth = 1;
    
    isTavern = true;
    
    level = 2;
    
    isBubbled = true;
    attacksCount = AttacksCount.DOUBLE;
    
    actualHealth = getMaxHealth();
  }
  
  @Override
  public String getDescription()
  {
    return "Divine Shield\n" +
        "Windfury";
  }
}
