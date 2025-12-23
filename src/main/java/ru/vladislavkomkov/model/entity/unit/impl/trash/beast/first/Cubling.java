package ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.TrashUnit;
import ru.vladislavkomkov.model.entity.unit.UnitType;

public class Cubling extends TrashUnit
{
  public Cubling()
  {
    super();
    
    attack = 0;
    isTaunt = true;
    
    unitType = List.of(UnitType.BEAST);
  }
}
