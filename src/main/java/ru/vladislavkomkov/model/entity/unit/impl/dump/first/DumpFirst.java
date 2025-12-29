package ru.vladislavkomkov.model.entity.unit.impl.dump.first;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import ru.vladislavkomkov.model.entity.unit.impl.dump.Dump;
import ru.vladislavkomkov.model.player.Player;

public class DumpFirst extends Dump
{
  public DumpFirst()
  {
    this(DUMP_PLAYER);
  }
  
  public DumpFirst(Player playerLink)
  {
    super(playerLink, 1);
  }
}
