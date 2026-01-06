package ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.TrashUnit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;

public class Mechapony extends TrashUnit
{
  public Mechapony()
  {
    this(DUMP_PLAYER);
  }
  
  public Mechapony(Player playerLink)
  {
    super(playerLink);
    
    attack = 1;
    maxHealth = 1;
    
    level = 1;
    
    unitType = List.of(UnitType.MECH, UnitType.BEAST);
    
    actualHealth = getMaxHealth();
  }
}
