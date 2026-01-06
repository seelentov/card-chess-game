package ru.vladislavkomkov.model.entity.unit.impl.dump.second;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import ru.vladislavkomkov.model.entity.unit.impl.dump.Dump;
import ru.vladislavkomkov.model.player.Player;

public class DumpSecond extends Dump
{
  public DumpSecond()
  {
    this(DUMP_PLAYER);
  }
  
  public DumpSecond(Player playerLink)
  {
    super(playerLink, 2);
  }
}
