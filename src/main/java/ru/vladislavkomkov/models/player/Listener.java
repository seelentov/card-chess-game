package ru.vladislavkomkov.models.player;

import ru.vladislavkomkov.models.actions.*;
import ru.vladislavkomkov.models.entity.unit.Unit;

import java.util.HashMap;
import java.util.Map;

public class Listener{
    public Listener(){}
    
    public Map<String, OnPlayedAction> onPlayedListeners = new HashMap<>();
    public Map<String, OnHandledAction> onHandledListeners = new HashMap<>();
    public Map<String, OnAttackAction> onAttackListeners = new HashMap<>();
    public Map<String, OnAttackedAction> onAttackedListeners = new HashMap<>();
    public Map<String, OnDeadAction> onDeadListeners = new HashMap<>();
    public Map<String, OnSellAction> onSellListeners = new HashMap<>();
    public Map<String, OnStartTurnAction> onStartTurnListeners = new HashMap<>();
    public Map<String, OnEndTurnAction> onEndTurnListeners = new HashMap<>();
    public Map<String, OnStartFightAction> onStartFightListeners = new HashMap<>();
    public Map<String, OnEndFightAction> onEndFightListeners = new HashMap<>();
    public Map<String, OnResetTavernAction> onResetTavernListeners = new HashMap<>();

    public void removeListener(Unit unit){
        removeListener(unit.getID());
    }
    
    public void removeListener(Integer i){
        removeListener(i.toString());
    }
    
    public void removeListener(String i){
        onPlayedListeners.remove(i);
        onHandledListeners.remove(i);
        onAttackListeners.remove(i);
        onAttackedListeners.remove(i);
        onDeadListeners.remove(i);
        onSellListeners.remove(i);
        onStartTurnListeners.remove(i);
        onEndTurnListeners.remove(i);
        onStartFightListeners.remove(i);
        onEndFightListeners.remove(i);
        onResetTavernListeners.remove(i);
    }
}
