package ru.vladislavkomkov.models.player;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.actions.OnAttackAction;
import ru.vladislavkomkov.models.actions.OnAttackedAction;
import ru.vladislavkomkov.models.actions.OnDeadAction;
import ru.vladislavkomkov.models.actions.OnHandledAction;
import ru.vladislavkomkov.models.actions.OnPlayedAction;
import ru.vladislavkomkov.models.actions.OnSellAction;
import ru.vladislavkomkov.models.card.Card;
import ru.vladislavkomkov.models.card.SpellCard;
import ru.vladislavkomkov.models.card.UnitCard;
import ru.vladislavkomkov.models.spell.Spell;
import ru.vladislavkomkov.models.unit.Unit;
import ru.vladislavkomkov.service.RandService;
import ru.vladislavkomkov.service.SpellService;
import ru.vladislavkomkov.service.UnitService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Player {
    static final int TABLE_LIMIT = 7;
    static final int HAND_LIMIT = 10;

    final Unit[] table = new Unit[TABLE_LIMIT];
    final Card[] hand = new Card[HAND_LIMIT];
    final List<Card> tavern = new ArrayList<>();
    int health = 30;
    int armor = 0;

    int money = 0;
    int maxMoney = 3;

    int level;
    
    public Listener listener = new Listener();
    public Statistic statistic = new Statistic();
    
    public void playCard(Game game, int indexCard, int indexPos){
        if (indexCard < 0 || indexCard >= hand.length){
            throw new IndexOutOfBoundsException("Index " + indexCard + " not existed in hand with length " + hand.length);
        }
        hand[indexCard].play(game, this,indexPos);
        hand[indexCard] = null;
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

    int getFreeHandIndexFromRight(){
        return getFreeIndexFromRight(hand);
    }

    <T> int getFreeIndexFromRight(T[] table){
        for (int i = 0; i < table.length; i++) {
            if(table[i] == null){
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

    public void addToHand(Card card){
        int placeIndex = getFreeHandIndexFromRight();
        if (placeIndex != -1){
            hand[placeIndex] = card;
        }
    }

    public void doForAll(Consumer<Unit> consumer){
        for (Unit unit: table){
            if(unit != null){
                consumer.accept(unit);
            }
        }
    }

    public Unit[] cloneTable(){
        return Arrays.copyOf(this.table, this.table.length);
    }

    public Card[] cloneHand(){
        return Arrays.copyOf(this.hand, this.hand.length);
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
            int lvl = RandService.getRandLvl(getLevel());
            List<Unit> units = UnitService.getUnitsByTavern(lvl);
            Unit unit = units.get(RandService.getRand(units.size() - 1));
            tavern.add(new UnitCard(unit));
        }

        int lvl = RandService.getRandLvl(getLevel());
        List<Spell> spells = SpellService.getByTavern(lvl);
        Spell spell = spells.get(RandService.getRand(spells.size() - 1));

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
}
