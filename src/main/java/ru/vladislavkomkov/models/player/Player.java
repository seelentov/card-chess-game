package ru.vladislavkomkov.models.player;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.card.Card;
import ru.vladislavkomkov.models.card.SpellCard;
import ru.vladislavkomkov.models.card.UnitCard;
import ru.vladislavkomkov.models.entity.spell.Spell;
import ru.vladislavkomkov.models.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.SpellUtils;
import ru.vladislavkomkov.util.UnitUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Player {
    static final int TABLE_LIMIT = 7;
    static final int HAND_LIMIT = 10;

    final Unit[] table = new Unit[TABLE_LIMIT];
    
    final List<Card> hand = new ArrayList<>();
    
    final List<Card> tavern = new ArrayList<>();
    int health = 30;
    int armor = 0;

    int money = 0;
    int maxMoney = 3;

    int level;
    
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
        if(tavern.size() > index){
            Card card = tavern.get(index);
            money-=3;
            card.get().onHandled(game, this);
        }
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

    public void incLevel() {
        if(level < 6){
            level+=1;
        }
    }

    public void applyDamage(int damage){
        int piercing = damage - armor;

        armor -= damage;
        if(armor < 0){
            armor = 0;
        }

        health -= piercing;
    }

    public List<Card> getTavern() {
        return tavern;
    }

    public boolean isAlive(){
        return health > 0;
    }

    public void calcTavern(){
        tavern.clear();

        int count = switch (getLevel()){
            case 1 -> 3;
            case 2,3 -> 4;
            case 4,5 -> 5;
            case 6 -> 6;
            default -> throw new RuntimeException("Wrong level");
        };

        for (int i = 0; i < count; i++) {
            int lvl = RandUtils.getRandLvl(getLevel());
            List<Unit> units = UnitUtils.getUnitsByTavern(lvl);
            Unit unit = units.get(RandUtils.getRand(units.size() - 1));
            tavern.add(new UnitCard(unit));
        }

        int lvl = RandUtils.getRandLvl(getLevel());
        List<Spell> spells = SpellUtils.getByTavern(lvl);
        Spell spell = spells.get(RandUtils.getRand(spells.size() - 1));

        tavern.add(new SpellCard(spell));
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
}
