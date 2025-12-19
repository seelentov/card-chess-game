package ru.vladislavkomkov.model.action;

import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

public interface OnAttackedAction extends Action
{
  void process(Game game, Fight fight, Player player1, Player player2, Unit unit, Unit attacker);
}
