package ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.TrashUnit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;

public class Cat extends TrashUnit
{
  public Cat()
  {
    this(DUMP_PLAYER);
  }
  
  public Cat(Player playerLink)
  {
    super(playerLink);
    
    unitType = List.of(UnitType.BEAST);
    
    actualHealth = getMaxHealth();
  }
}
