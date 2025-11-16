package ru.vladislavkomkov.models.actions;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.models.unit.Unit;

public interface OnAttackedAction {
    void process(Game game, Player player1, Player player2, Unit unit, Unit attacker);
}
