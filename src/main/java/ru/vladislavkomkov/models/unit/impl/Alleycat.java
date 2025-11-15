package ru.vladislavkomkov.models.unit.impl;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.Player;
import ru.vladislavkomkov.models.unit.Type;
import ru.vladislavkomkov.models.unit.Unit;
import ru.vladislavkomkov.models.unit.actions.OnPlayedAction;

public class Alleycat extends Unit {
    protected String description = "Summon a 1/1 Cat";
    protected Integer level = 1;
    protected boolean isTavern = true;

    protected int attack = 1;
    protected int maxHealth = 1;
    protected int actualHealth = 1;

    protected Type type = Type.BEAST;

    protected OnPlayedAction onPlayed = (Game game, Player player, int index) -> {
        super.onPlayed.process(game,player,index);
        player.addToTable(new Cat(), -1);
    };
}
