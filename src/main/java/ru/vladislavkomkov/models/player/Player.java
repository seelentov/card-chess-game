package ru.vladislavkomkov.models.player;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.card.Card;
import ru.vladislavkomkov.models.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.util.ListenerUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Player {
    public static final int MAX_LEVEL = 6;
    static final int TABLE_LIMIT = 7;
    static final int HAND_LIMIT = 10;

    final Unit[] table = new Unit[TABLE_LIMIT];
    
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
    
    public void playCard(Game game, int indexCard, int indexPos){
        if (indexCard < 0 || indexCard >= hand.size()){
            throw new IndexOutOfBoundsException("Index " + indexCard + " not existed in hand with length " + hand.size());
        }
        hand.get(indexCard).play(game, this,indexPos);
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
    
    int getFreeTableIndexFromRight(){
        return getFreeIndexFromRight(table);
    }
    
    <T> int getFreeIndexFromRight(T[] array){
        for (int i = 0; i < array.length; i++) {
            if(array[i] == null){
                return i;
            }
        }

        return -1;
    }
    
    public void addToTable(Unit unit, int index){
        if(index < table.length && index >= 0 && table[index] == null){
            table[index] = unit;
            return;
        }
        
        int placeIndex = getFreeTableIndexFromRight();
        if (placeIndex != -1){
            table[placeIndex] = unit;
        }
    }
    
    public void removeFromTable(Unit unit){
        int index = findTableIndex(unit);
        
        if(index != -1){
            removeFromTable(index);
        }
    }
    
    public void removeFromTable(int index){
        if(index < table.length && index >= 0 && table[index] != null){
            table[index] = null;
        }
    }
    
    public int findTableIndex(Unit unit){
        for (int i = 0; i < table.length; i++) {
            if(table[i] != null && table[i].getID() == unit.getID()){
               return i;
            }
        }
        
        return -1;
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
    }

    public void doForAll(Consumer<Unit> consumer){
        for (int i = 0; i < table.length; i++) {
            doFor(consumer,i);
        }
    }
    
    public void doFor(Consumer<Unit> consumer, int index){
        Unit unit = table[index];
        if(unit != null){
            consumer.accept(unit);
        }
    }

    public Unit[] cloneTable(){
        return Arrays.copyOf(this.table, this.table.length);
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
        return Math.toIntExact(Arrays.stream(table).filter(Objects::nonNull).count());
    }
}
