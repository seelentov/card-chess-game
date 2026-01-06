package ru.vladislavkomkov.model.action;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.player.Player;

public interface FightAction
{
  void process(Game game, Fight fight, Player player, Player player2);
}
