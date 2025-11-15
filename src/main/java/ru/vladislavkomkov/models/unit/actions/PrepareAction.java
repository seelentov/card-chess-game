package ru.vladislavkomkov.models.unit.actions;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.Player;

public interface PrepareAction {
    void process(Game game, Player player);
}
