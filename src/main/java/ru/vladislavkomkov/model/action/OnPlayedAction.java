package ru.vladislavkomkov.model.action;

import java.util.List;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.player.Player;

public interface OnPlayedAction extends Action
{
  void process(Game game, Fight fight, Player player, Entity entity, List<Integer> input, boolean auto);
}
