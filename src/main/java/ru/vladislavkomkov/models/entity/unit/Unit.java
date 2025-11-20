package ru.vladislavkomkov.models.entity.unit;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.models.entity.Entity;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;

public abstract class Unit extends Entity {
    protected int attack = 0;
    protected int maxHealth = 1;
    protected List<Type> type = new ArrayList<>();
    
    protected boolean isBubbled = false;
    protected boolean isTaunt = false;
    protected boolean isRebirth = false;
    protected boolean isDoubleAttack = false;
    protected boolean isDisguise = false;
    
    protected boolean answerOnPlayed = false;
    protected boolean answerOnDead = false;
    
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

        processListeners(player.listener.onSellListeners, (action)->action.process(game,player,this), player);
    }
    
    public void onStartTurn(Game game, Player player) {
    }
    
    public void onEndTurn(Game game, Player player) {
    
    }
    
    public void onStartFight(Game game, Player player, Player player2) {
    
    }
    
    public void onEndFight(Game game, Player player, Player player2) {
    
    }
    
    public void onPlayed(Game game, Player player, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2) {
        super.onPlayed(game,player,index,isTavernIndex,index2,isTavernIndex2);
    }
    
    public void onAttacked(Game game, Player player, Player player2, Unit attacker) {
        processListeners(player.listener.onAttackedListeners, (action)->action.process(game,player,player2,this,attacker), player);
        if (isBubbled) {
            isBubbled = false;
        } else {
            this.actualHealth -= attacker.getAttack();
        }
    }
    
    public void onAttack(Game game, Player player, Player player2, Unit attacked) {
        processListeners(player.listener.onAttackListeners, (action)->action.process(game,player,player2,this,attacked), player);
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
        }
        processListeners(player.listener.onDeadListeners, (action)->action.process(game,player,player2,this,attacker), player);
    }
    
    public int getHealth(){
        return actualHealth;
    }

    public int getMaxHealth(){
        return maxHealth;
    }

    public void setHealth(int i){
        maxHealth=i;
        actualHealth=i;
    }

    public void incHealth(int i){
        maxHealth+=i;
        actualHealth+=i;
    }
    
    public void decHealth(int i){
        maxHealth-=i;
        actualHealth-=i;
    }
    
    public void applyDamage(int i){
        actualHealth-=i;
    }
    
    public void incAttack(int i){
        attack+=i;
    }
    
    public void decAttack(int i){
        attack-=i;
    }

    public void setAttack(int i){
        attack = i;
    }

    public void setIsBubbled(boolean bubbled){
        isBubbled = bubbled;
    }

    public boolean getIsTaunt(){
        return isTaunt;
    }

    public void setIsTaunt(boolean isTaunt){
        this.isTaunt = isTaunt;
    }

    public boolean getIsDisguise(){
        return isDisguise;
    }

    public void setIsDisguise(boolean disguise){
        isDisguise = disguise;
    }

    public void kill(){
        actualHealth = 0;
    }

    public boolean getIsRebirth(){
        return isRebirth;
    }
    
    public boolean isAnswerOnPlayed(){
        return answerOnPlayed;
    }
    
    public boolean isAnswerOnDead(){
        return answerOnDead;
    }
    
//    public abstract Unit getGold();
}