package ru.vladislavkomkov.model.action;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.player.Player;

public interface PrepareAction extends Action
{
  void process(Game game, Fight fight, Player player, Entity entity);
}
