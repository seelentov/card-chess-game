package ru.vladislavkomkov.models.player;

import ru.vladislavkomkov.models.actions.*;
import ru.vladislavkomkov.models.entity.unit.Unit;

import java.util.HashMap;
import java.util.Map;

public class Listener{
    public Listener(){}
    
    public Map<Integer, OnPlayedAction> onPlayedListeners = new HashMap<>();
    public Map<Integer, OnHandledAction> onHandledListeners = new HashMap<>();
    public Map<Integer, OnAttackAction> onAttackListeners = new HashMap<>();
    public Map<Integer, OnAttackedAction> onAttackedListeners = new HashMap<>();
    public Map<Integer, OnDeadAction> onDeadListeners = new HashMap<>();
    public Map<Integer, OnSellAction> onSellListeners = new HashMap<>();

    public void removeListener(int i){
        onPlayedListeners.remove(i);
        onHandledListeners.remove(i);
        onAttackListeners.remove(i);
        onAttackedListeners.remove(i);
        onDeadListeners.remove(i);
        onSellListeners.remove(i);
    }

    public void removeListener(Unit unit){
        removeListener(unit.getID());
    }
}
