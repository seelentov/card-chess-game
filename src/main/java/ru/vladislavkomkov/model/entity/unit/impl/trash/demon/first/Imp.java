package ru.vladislavkomkov.model.entity.unit.impl.trash.demon.first;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.TrashUnit;
import ru.vladislavkomkov.model.entity.unit.Type;

public class Imp extends TrashUnit
{
  public Imp()
  {
    super();
    type = List.of(Type.DEMON);
  }
}
