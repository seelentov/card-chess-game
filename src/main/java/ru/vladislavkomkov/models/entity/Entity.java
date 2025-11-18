package ru.vladislavkomkov.models.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import ru.vladislavkomkov.consts.Listeners;
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
        processListeners(player.listener.onHandledListeners, (action)->action.process(game,player,this), player);
    };
    
    public void onPlayed(Game game, Player player, int index){
        processListeners(player.listener.onPlayedListeners, (action)->action.process(game,player,this,index), player);
    };

    protected <T> void processListeners(Map<String, T> listeners, Consumer<T> actionMove,Player player){
        new HashMap<>(listeners).forEach((key, action)->{
            actionMove.accept(action);
            if (key.startsWith(Listeners.KEY_ONCE_PREFIX)) {
                player.listener.removeListener(key);
            }
        });
    }
    
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
                throw new RuntimeException(e);
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
