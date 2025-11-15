package ru.vladislavkomkov.models;

import ru.vladislavkomkov.models.card.Card;
import ru.vladislavkomkov.models.card.UnitCard;
import ru.vladislavkomkov.models.unit.actions.OnHandledAction;
import ru.vladislavkomkov.models.unit.actions.OnPlayedAction;

public abstract class Entity {
    protected String name = this.getClass().getSimpleName();
    protected String description = "";
    protected int level = 1;
    protected boolean isTavern = false;

    public int getLevel(){
        return level;
    }

    public boolean isTavern(){
        return isTavern;
    }

    public OnHandledAction onHandled = (Game game, Player player) -> player.addToHand(Card.of(this));
    public OnPlayedAction onPlayed = (Game game, Player player, int index) -> {};

    public String getName(){
        return name;
    }
}
