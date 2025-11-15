package ru.vladislavkomkov.models.unit.actions;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.Player;
import ru.vladislavkomkov.models.unit.Unit;

public interface OnAttackAction{
    void process(Game game, Player player1, Player player2, Unit attacked);
}
