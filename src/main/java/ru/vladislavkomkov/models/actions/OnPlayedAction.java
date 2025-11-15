package ru.vladislavkomkov.models.actions;

import ru.vladislavkomkov.models.Entity;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.Player;
import ru.vladislavkomkov.models.unit.Unit;

public interface OnPlayedAction{
    void process(Game game, Player player, Entity entity, int index);
}
