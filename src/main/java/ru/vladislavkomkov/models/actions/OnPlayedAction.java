package ru.vladislavkomkov.models.actions;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.Entity;
import ru.vladislavkomkov.models.player.Player;

public interface OnPlayedAction extends Action
{
  void process(Game game, Player player, Entity entity, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2);
}
