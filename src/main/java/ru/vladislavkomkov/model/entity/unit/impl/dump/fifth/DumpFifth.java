package ru.vladislavkomkov.model.entity.unit.impl.dump.fifth;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import ru.vladislavkomkov.model.entity.unit.impl.dump.Dump;
import ru.vladislavkomkov.model.player.Player;

public class DumpFifth extends Dump
{
  public DumpFifth()
  {
    this(DUMP_PLAYER);
  }
  
  public DumpFifth(Player playerLink)
  {
    super(playerLink, 5);
  }
}
