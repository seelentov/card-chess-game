package ru.vladislavkomkov.models.actions;

import ru.vladislavkomkov.models.Entity;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;

public interface PrepareAction {
    void process(Game game, Player player, Entity entity);
}
