package ru.vladislavkomkov.models.actions;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.player.Player;

public interface OnAttackedAction extends Action
{
  void process(Game game, Player player1, Player player2, Unit unit, Unit attacker);
}
