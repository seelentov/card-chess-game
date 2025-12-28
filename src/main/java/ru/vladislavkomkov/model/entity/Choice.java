package ru.vladislavkomkov.model.entity;

import java.util.List;

import ru.vladislavkomkov.model.GObject;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.player.Player;

public abstract class Choice extends GObject
{
  public Choice(boolean isGold)
  {
    super(isGold);
  }
  
  public abstract void process(Game game, Fight fight, Player player, Entity entity, List<Integer> input, boolean auto);
}
