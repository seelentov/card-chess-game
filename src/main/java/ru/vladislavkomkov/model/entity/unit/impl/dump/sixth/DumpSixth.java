package ru.vladislavkomkov.model.entity.unit.impl.dump.sixth;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import ru.vladislavkomkov.model.entity.unit.impl.dump.Dump;
import ru.vladislavkomkov.model.player.Player;

public class DumpSixth extends Dump
{
  public DumpSixth()
  {
    this(DUMP_PLAYER);
  }
  
  public DumpSixth(Player playerLink)
  {
    super(playerLink, 6);
  }
}
