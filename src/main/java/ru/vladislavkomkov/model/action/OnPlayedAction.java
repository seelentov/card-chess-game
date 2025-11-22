package ru.vladislavkomkov.model.action;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.player.Player;

public interface OnPlayedAction extends Action
{
  void process(Game game, Player player, Entity entity, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2);
}
