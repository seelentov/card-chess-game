package ru.vladislavkomkov.model.entity.unit.impl.dump.third;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import ru.vladislavkomkov.model.entity.unit.impl.dump.Dump;
import ru.vladislavkomkov.model.player.Player;

public class DumpThird extends Dump
{
    public DumpThird()
    {
      this(DUMP_PLAYER);
    }
    
    public DumpThird(Player playerLink)
    {
      super(playerLink, 3);
    }
}
