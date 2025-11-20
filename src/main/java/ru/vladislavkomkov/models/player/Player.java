package ru.vladislavkomkov.models.player;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.card.Card;
import ru.vladislavkomkov.models.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.util.ListenerUtils;
import ru.vladislavkomkov.util.SerializationUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Player implements Cloneable, Serializable {
    public static final int MAX_LEVEL = 6;
    public static final int TABLE_LIMIT = 7;
    public static final int HAND_LIMIT = 10;

    final List<Unit> table = new ArrayList<>(TABLE_LIMIT);
    
    final List<Card> hand = new ArrayList<>();
    
    final Tavern tavern = new Tavern();
    int health = 30;
    int maxHealth = 30;
    int armor = 0;

    int money = 0;
    int maxMoney = 3;

    int level = 1;

    int buyPrice = 3;
    
    public Listener listener = new Listener();
    public Statistic statistic = new Statistic();

    public List<Unit> inFightTable = null;
    
    public void playCard(Game game, int indexCard){
        playCard(game,indexCard,0);
    }
    
    public void playCard(Game game, int indexCard, int index){
        playCard(game,indexCard,index,false);
    }
    
    public void playCard(Game game, int indexCard, int index, boolean isTavernIndex){
        playCard(game,indexCard,index,isTavernIndex,0);
    }
    
    public void playCard(Game game, int indexCard, int index, boolean isTavernIndex, int index2){
        playCard(game,indexCard,index,isTavernIndex,index2,false);
    }
    
    public void playCard(Game game, int indexCard, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2){
        if (indexCard < 0 || indexCard >= hand.size()){
            throw new IndexOutOfBoundsException("Index " + indexCard + " not existed in hand with length " + hand.size());
        }
        hand.get(indexCard).play(game, this,index, isTavernIndex, index2, isTavernIndex2);
        hand.remove(indexCard);
    }
    
    public void buyCard(Game game, int index){
        if (money>=buyPrice && hand.size() < HAND_LIMIT){
            money-=3;
            addToHand(tavern.buy(index));
        }
    }

    public void resetTavern(Game game){
        tavern.reset(getLevel());

        ListenerUtils.processGlobalActionListeners(listener.onResetTavernListeners, game, this);
    }
    
    public boolean addToTable(Unit unit, List<Unit> table, int index, boolean withoutOne) {
        int size = table.size();
        
        if(withoutOne){
            size--;
        }
        
        if (size >= TABLE_LIMIT){
            return false;
        }
        
        if (index < 0 || index >= size){
            table.add(unit);
        } else {
            table.add(index, unit);
        }
        
        return true;
    }
    
    public boolean addToFightTable(Unit unit, int index, boolean withoutOne) {
        return addToTable(unit, inFightTable, index, withoutOne);
    }

    public boolean addToTable(Unit unit, int index) {
        return addToTable(unit, table, index, false);
    }
    
    public void addToFightTable(List<Unit> units, int index, boolean withoutOne) {
        units.forEach(unit -> addToFightTable(unit, index, withoutOne));
    }
    
    public void addToTable(List<Unit> units, int index) {
        units.forEach(unit -> addToTable(unit, index));
    }
    
    public boolean addToFightTable(Unit unit, int index) {
        return addToTable(unit, inFightTable, index, false);
    }
    
    public void addToFightTable(List<Unit> units, int index) {
        units.forEach(unit -> addToFightTable(unit, index, false));
    }

    public boolean addToTable(Unit unit) {
        return addToTable(unit, -1);
    }
    public boolean addToFightTable(Unit unit) {
        return addToFightTable(unit, -1, false);
    }
    
    public void removeFromTable(Unit unit){
        table.removeIf(unit1 -> unit == unit1);
    }
    
    public void removeFromTable(int index){
        table.remove(index);
    }
    
    public void clearSpellCraft(){
        hand.removeIf(card -> card.get() instanceof SpellCraft);
    }
    
    public void addToHand(Card card){
        addToHand(card, false);
    }
    
    public void addToHand(Card card, boolean force){
        if(force || hand.size() < HAND_LIMIT){
            hand.add(card);
        }
        
        calcTriplets();
    }
    
    public void calcTriplets(){
    
    }

    public int getIndex(Unit unit){
        return table.indexOf(unit);
    }

    public int getFightIndex(Unit unit){
        return inFightTable.indexOf(unit);
    }

    public void doForAll(Consumer<Unit> consumer){
        table.forEach(consumer);
    }
    
    public void doFor(Consumer<Unit> consumer, int index){
        consumer.accept(table.get(index));
    }

    public List<Unit> cloneTable(){
        return table.stream().toList();
    }

    public List<Card> cloneHand(){
        return new ArrayList<>(hand);
    }

    public int getLevel() {
        return level;
    }

    public int getHealth() {
        return health;
    }

    public void incLevel(Game game) {
        if(level < MAX_LEVEL){
            level+=1;
            
            ListenerUtils.processGlobalActionListeners(listener.onIncLevelListeners, game, this);
        }
    }

    public void applyDamage(int damage){
        int piercing = Math.max(damage - armor, 0);
        
        armor = Math.max(armor - damage, 0);
        health -= piercing;
    }

    public Tavern getTavern() {
        return tavern;
    }

    public boolean isAlive(){
        return health > 0;
    }

    public void resetMoney(){
        money = maxMoney;
    }

    public void incMaxMoney(){
        incMaxMoney(1);
    }

    public void incMaxMoney(int i){
        maxMoney+=i;
    }

    public int getMoney(){
        return money;
    }

    public void addMoney(){
        addMoney(1);
    }

    public void addMoney(int i){
        money+=i;
    }

    public void decMoney(int i) {money-=i;}
    
    public int getArmor(){
        return armor;
    }
    
    public void addArmor(int i){
        armor += i;
    }
    
    public void setArmor(int i){
        armor = i;
    }
    
    public int getMaxHealth(){
        return maxHealth;
    }

    public int getUnitsCount(){
        return table.size();
    }

    public int getFightUnitsCount(){
        return inFightTable.size();
    }

    @Override
    public Player clone() {
        try {
            Player player = (Player) super.clone();
            return SerializationUtils.deepCopy(player);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
