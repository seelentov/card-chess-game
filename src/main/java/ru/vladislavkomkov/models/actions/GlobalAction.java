package ru.vladislavkomkov.models.actions;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;

public interface GlobalAction extends Action
{
  void process(Game game, Player player);
}
