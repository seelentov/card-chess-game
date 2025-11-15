package ru.vladislavkomkov.models.spell;

import ru.vladislavkomkov.models.Entity;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.Player;

public abstract class Spell extends Entity {
    public abstract void cast(Game game, Player player1, int index);
}
