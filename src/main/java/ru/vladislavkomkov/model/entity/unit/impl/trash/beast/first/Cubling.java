package ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.TrashUnit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;

public class Cubling extends TrashUnit
{
  public Cubling()
  {
    this(DUMP_PLAYER);
  }
  
  public Cubling(Player playerLink)
  {
    super(playerLink);
    
    attack = 0;
    isTaunt = true;
    
    unitType = List.of(UnitType.BEAST);
    
    actualHealth = getMaxHealth();
  }
}
