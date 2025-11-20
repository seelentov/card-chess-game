package ru.vladislavkomkov.models.entity.unit.impl.trash.beast.first;

import java.util.List;

import ru.vladislavkomkov.models.entity.unit.TrashUnit;
import ru.vladislavkomkov.models.entity.unit.Type;

public class Cat extends TrashUnit
{
  public Cat()
  {
    super();
    type = List.of(Type.BEAST);
  }
}
