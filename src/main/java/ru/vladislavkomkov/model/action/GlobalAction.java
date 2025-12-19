package ru.vladislavkomkov.model.action;

import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.player.Player;

public interface GlobalAction extends Action
{
  void process(Game game, Fight fight, Player player);
}
