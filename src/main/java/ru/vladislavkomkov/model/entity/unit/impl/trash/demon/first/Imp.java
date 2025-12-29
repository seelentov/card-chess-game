package ru.vladislavkomkov.model.entity.unit.impl.trash.demon.first;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.TrashUnit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.player.Player;

public class Imp extends TrashUnit
{
  public Imp()
  {
    this(DUMP_PLAYER);
  }
  
  public Imp(Player playerLink)
  {
    super(playerLink);

    unitType = List.of(UnitType.DEMON);
    
    actualHealth = getMaxHealth();
  }
}
