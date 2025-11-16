package ru.vladislavkomkov.models.entity.unit;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.models.entity.Entity;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;

public abstract class Unit extends Entity implements Cloneable {
    protected int attack = 0;
    protected int maxHealth = 1;
    protected List<Type> type = new ArrayList<>();
    
    protected boolean isBubbled = false;
    protected boolean isTaunt = false;
    protected boolean isRebirth = false;
    protected boolean isDoubleAttack = false;
    
    protected int actualHealth = 1;
    
    public int getAttack() {
        return attack;
    }
    
    public boolean isDead() {
        return actualHealth < 1;
    }
    
    public void onSell(Game game, Player player) {
        player.listener.removeListener(this);
        player.addMoney(1);
        player.removeFromTable(this);
        player.listener.onSellListeners.values().forEach(action -> action.process(game,player,this));
    }
    
    public void onStartTurn(Game game, Player player) {
    }
    
    public void onEndTurn(Game game, Player player) {
    
    }
    
    public void onStartFight(Game game, Player player, Player player2) {
    
    }
    
    public void onEndFight(Game game, Player player, Player player2) {
    
    }
    
    public void onPlayed(Game game, Player player, int index) {
        player.addToTable(this, index);
        super.onPlayed(game,player,index);
    }
    
    public void onAttacked(Game game, Player player, Player player2, Unit attacker) {
        player.listener.onAttackedListeners.values().forEach(action -> action.process(game,player,player2,this,attacker));
        if (isBubbled) {
            isBubbled = false;
        } else {
            this.actualHealth -= attacker.getAttack();
        }
    }
    
    public void onAttack(Game game, Player player, Player player2, Unit attacked) {
        player.listener.onAttackListeners.values().forEach(action -> action.process(game,player,player2,this,attacked));
        if (isBubbled) {
            isBubbled = false;
        } else {
            this.actualHealth -= attacked.getAttack();
        }
    }
    
    public void onDead(Game game, Player player, Player player2, Unit attacker) {
        if (isRebirth) {
            isRebirth = false;
            actualHealth = 1;
        } else {
            player.listener.onDeadListeners.values().forEach(action -> action.process(game,player,player2,this,attacker));
        }
    }
    
    public int getHealth(){
        return actualHealth;
    }
    
    public void incHealth(){
        incHealth(1);
    }
    
    public void incHealth(int i){
        maxHealth+=i;
        actualHealth+=i;
    }
    
    public void incAttack(){
        incAttack(1);
    }
    
    public void incAttack(int i){
        attack+=i;
    }

    @Override
    public Unit clone() {
        try {
            return (Unit) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}