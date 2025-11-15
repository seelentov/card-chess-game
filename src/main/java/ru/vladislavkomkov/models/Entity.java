package ru.vladislavkomkov.models;

import ru.vladislavkomkov.models.card.Card;

public abstract class Entity {
    protected int ID = System.identityHashCode(this);
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
    
    public void onHandled(Game game, Player player){
        player.addToHand(Card.of(this));
        player.listener.onHandledListeners.values().forEach(action -> action.process(game,player,this));
    };
    
    public void onPlayed(Game game, Player player, int index){
        player.listener.onPlayedListeners.values().forEach(onPlayedAction -> onPlayedAction.process(game,player,this,index));
    };
    
    public String getName(){
        return name;
    }
    
    public int getID(){
        return ID;
    }
}
