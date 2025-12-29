package ru.vladislavkomkov.model.entity.unit.impl.dump.fourth;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import ru.vladislavkomkov.model.entity.unit.impl.dump.Dump;
import ru.vladislavkomkov.model.player.Player;

public class DumpFourth extends Dump
{
  public DumpFourth()
  {
    this(DUMP_PLAYER);
  }
  
  public DumpFourth(Player playerLink)
  {
    super(playerLink, 4);
  }
}
