package ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.TrashUnit;
import ru.vladislavkomkov.model.entity.unit.UnitType;

public class Cat extends TrashUnit
{
  public Cat()
  {
    super();

    unitType = List.of(UnitType.BEAST);
  }
}
