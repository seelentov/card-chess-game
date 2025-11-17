package ru.vladislavkomkov.models.entity;

import java.io.Serializable;
import java.util.function.Supplier;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.card.Card;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.util.SerializationUtils;

public abstract class Entity implements Serializable, Cloneable {
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
    
    public Entity newThis() {
        Supplier<? extends Entity> supplier = () -> {
            try {
                return this.getClass().getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create Mech instance", e);
            }
        };
        
        return supplier.get();
    }
    
    @Override
    public Unit clone() {
        try {
            Unit unit = (Unit) super.clone();
            return SerializationUtils.deepCopy(unit);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
