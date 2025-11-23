package ru.vladislavkomkov.model.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.util.Pair;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.Listener;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.util.ListenerUtils;
import ru.vladislavkomkov.util.SerializationUtils;

public class Player implements Cloneable, Serializable
{
  public static final int MAX_LEVEL = 6;
  public static final int TABLE_LIMIT = 7;
  public static final int HAND_LIMIT = 10;
  
  final List<Unit> table = new ArrayList<>(TABLE_LIMIT);
  
  final List<Card> hand = new ArrayList<>();
  
  final Tavern tavern = new Tavern();
  public Listener listener = new Listener();
  public Statistic statistic = new Statistic();
  public List<Unit> inFightTable = null;
  
  int health = 30;
  int maxHealth = 30;
  int armor = 0;
  int money = 0;
  int maxMoney = 3;
  int level = 1;
  int buyPrice = 3;
  int resetTavernPrice = 1;
  
  public Player()
  {
    super();
  }
  
  public void moveTable(int index, int index2)
  {
    if (table.size() <= index)
    {
      return;
    }
    
    Unit unit = table.get(index);
    table.remove(index);
    addToTable(null, unit, index2);
  }
  
  public void freezeTavern()
  {
    tavern.freeze = true;
  }
  
  public void playCard(Game game, int indexCard)
  {
    playCard(game, indexCard, 0);
  }
  
  public void playCard(Game game, int indexCard, int index)
  {
    playCard(game, indexCard, index, false);
  }
  
  public void playCard(Game game, int indexCard, int index, boolean isTavernIndex)
  {
    playCard(game, indexCard, index, isTavernIndex, 0);
  }
  
  public void playCard(Game game, int indexCard, int index, boolean isTavernIndex, int index2)
  {
    playCard(game, indexCard, index, isTavernIndex, index2, false);
  }
  
  public void playCard(Game game, int indexCard, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2)
  {
    if (indexCard < 0 || indexCard >= hand.size())
    {
      throw new IndexOutOfBoundsException("Index " + indexCard + " not existed in hand with length " + hand.size());
    }
    hand.get(indexCard).play(game, this, index, isTavernIndex, index2, isTavernIndex2);
    hand.remove(indexCard);
  }
  
  public void buyCard(Game game, int index)
  {
    if (money >= buyPrice && hand.size() < HAND_LIMIT)
    {
      money -= 3;
      addToHand(tavern.buy(index));
    }
  }
  
  public void resetTavernManual(Game game)
  {
    if (statistic.counters.freeTavernCounter() <= 0)
    {
      this.money -= resetTavernPrice;
    }
    
    tavern.freeze = false;
    
    resetTavern(game);
  }
  
  public void resetTavern(Game game)
  {
    tavern.reset(getLevel());
    listener.processOnResetTavernListeners(game,this);
  }
  
  public boolean addToTable(Game game, Unit unit, List<Unit> table, int index, boolean withoutOne)
  {
    int size = table.size();
    
    if (withoutOne)
    {
      size--;
    }
    
    if (size >= TABLE_LIMIT)
    {
      return false;
    }
    
    if (index < 0 || index >= size)
    {
      table.add(unit);
    }
    else
    {
      table.add(index, unit);
    }
    
    unit.onAppear(game, this);
    
    return true;
  }
  
  public boolean addToFightTable(Game game, Unit unit, int index, boolean withoutOne)
  {
    return addToTable(game, unit, inFightTable, index, withoutOne);
  }
  
  public boolean addToTable(Game game, Unit unit, int index)
  {
    return addToTable(game, unit, table, index, false);
  }
  
  public void addToFightTable(Game game, List<Unit> units, int index, boolean withoutOne)
  {
    units.forEach(unit -> addToFightTable(game, unit, index, withoutOne));
  }
  
  public void addToTable(Game game, List<Unit> units, int index)
  {
    units.forEach(unit -> addToTable(game, unit, index));
  }
  
  public boolean addToFightTable(Game game, Unit unit, int index)
  {
    return addToTable(game, unit, inFightTable, index, false);
  }
  
  public void addToFightTable(Game game, List<Unit> units, int index)
  {
    units.forEach(unit -> addToFightTable(game, unit, index, false));
  }
  
  public boolean addToTable(Unit unit)
  {
    return addToTable(null, unit, -1);
  }
  
  public boolean addToTable(Unit unit,int index)
  {
    return addToTable(null, unit, index);
  }
  
  public boolean addToTable(Game game, Unit unit)
  {
    return addToTable(game, unit, -1);
  }
  
  public boolean addToFightTable(Unit unit)
  {
    return addToFightTable(null, unit, -1, false);
  }
  
  public boolean addToFightTable(Game game, Unit unit)
  {
    return addToFightTable(game, unit, -1, false);
  }
  
  public void removeFromTable(Game game, Unit unit)
  {
    if (game != null)
    {
      unit.onDisappear(game, this);
    }
    table.removeIf(unit1 -> unit == unit1);
  }
  
  public void removeFromTable(Game game, int index)
  {
    table.get(index).onAppear(game, this);
    table.remove(index);
  }
  
  public void clearSpellCraft()
  {
    hand.removeIf(card -> card.get() instanceof SpellCraft);
  }
  
  public void addToHand(Card card)
  {
    addToHand(null, card);
  }
  
  public void addToHand(Card card, boolean force)
  {
    addToHand(null, card, force);
  }
  
  public void addToHand(Game game, Card card)
  {
    addToHand(game, card, false);
  }
  
  public void addToHand(Game game, Card card, boolean force)
  {
    if (force || hand.size() < HAND_LIMIT)
    {
      card.get().onHandled(game, this);
      hand.add(card);
    }
    
    calcTriplets();
  }
  
  public void calcTriplets()
  {
    Supplier<Map<String, List<Pair<Boolean, Integer>>>> calc = () -> {
      Map<String, List<Pair<Boolean, Integer>>> indexCountMap = new HashMap<>();
      
      for (int i = 0; i < hand.size(); i++)
      {
        Card card = hand.get(i);
        if (card.isSpell() || card.isGold())
        {
          continue;
        }
        
        String key = card.getName();
        Pair<Boolean, Integer> pair = new Pair<>(false, i);
        indexCountMap.computeIfAbsent(key, k -> new ArrayList<>()).add(pair);
      }
      
      for (int i = 0; i < table.size(); i++)
      {
        Unit unit = table.get(i);
        if (unit.isGold())
        {
          continue;
        }
        
        String key = unit.getName();
        Pair<Boolean, Integer> pair = new Pair<>(true, i);
        indexCountMap.computeIfAbsent(key, k -> new ArrayList<>()).add(pair);
      }
      
      return indexCountMap;
    };
    
    boolean foundTriplets;
    
    do
    {
      foundTriplets = false;
      Map<String, List<Pair<Boolean, Integer>>> indexCountMap = calc.get();
      
      for (List<Pair<Boolean, Integer>> cardList : indexCountMap.values())
      {
        if (cardList.size() >= 3)
        {
          foundTriplets = true;
          
          List<Pair<Boolean, Integer>> triplets = cardList.subList(0, 3);
          
          List<Card> removedCards = new ArrayList<>();
          
          List<Unit> unitsForGold = new ArrayList<>();
          for (Pair<Boolean, Integer> pair : triplets)
          {
            boolean isOnTable = pair.getKey();
            int index = pair.getValue();
            
            if (isOnTable)
            {
              unitsForGold.add(table.get(index));
            }
            else
            {
              unitsForGold.add((Unit) hand.get(index).get());
            }
          }
          
          triplets.sort((p1, p2) -> Boolean.compare(p2.getKey(), p1.getKey())); // сначала стол, потом рука
          triplets.sort((p1, p2) -> Integer.compare(p2.getValue(), p1.getValue())); // с конца
          
          for (Pair<Boolean, Integer> pair : triplets)
          {
            boolean isOnTable = pair.getKey();
            int index = pair.getValue();
            
            if (isOnTable)
            {
              table.remove(index);
            }
            else
            {
              hand.remove(index);
            }
          }
          
          Card goldCard = Card.of(unitsForGold.get(0).buildGold(unitsForGold));
          addToHand(goldCard);
          break;
        }
      }
      
    }
    while (foundTriplets);
  }
  
  public int getIndex(Unit unit)
  {
    return table.indexOf(unit);
  }
  
  public int getFightIndex(Unit unit)
  {
    return inFightTable.indexOf(unit);
  }
  
  public void doForAll(Consumer<Unit> consumer)
  {
    table.forEach(consumer);
  }
  
  public void doFor(Consumer<Unit> consumer, int index)
  {
    consumer.accept(table.get(index));
  }
  
  public void removeTempBuffs()
  {
    doForAll(Unit::removeTempBuffs);
    
    hand.stream()
        .map(Card::get)
        .forEach(entity -> {
          if (entity instanceof Unit)
          {
            ((Unit) entity).removeTempBuffs();
          }
        });
  }
  
  public List<Unit> cloneTable()
  {
    return table.stream().toList();
  }
  
  public List<Card> cloneHand()
  {
    return new ArrayList<>(hand);
  }
  
  public int getLevel()
  {
    return level;
  }
  
  public int getHealth()
  {
    return health;
  }
  
  public void incLevel(Game game)
  {
    if (level < MAX_LEVEL)
    {
      level += 1;
      listener.processOnIncTavernLevelListener(game,this);
    }
  }
  
  public void applyDamage(int damage)
  {
    int piercing = Math.max(damage - armor, 0);
    
    armor = Math.max(armor - damage, 0);
    health -= piercing;
  }
  
  public Tavern getTavern()
  {
    return tavern;
  }
  
  public boolean isAlive()
  {
    return health > 0;
  }
  
  public void resetMoney()
  {
    money = maxMoney;
  }
  
  public void incMaxMoney()
  {
    incMaxMoney(1);
  }
  
  public void incMaxMoney(int i)
  {
    maxMoney += i;
  }
  
  public int getMoney()
  {
    return money;
  }
  
  public void addMoney()
  {
    addMoney(1);
  }
  
  public void addMoney(int i)
  {
    money += i;
  }
  
  public void decMoney(int i)
  {
    money -= i;
  }
  
  public int getArmor()
  {
    return armor;
  }
  
  public void setArmor(int i)
  {
    armor = i;
  }
  
  public void addArmor(int i)
  {
    armor += i;
  }
  
  public int getMaxHealth()
  {
    return maxHealth;
  }
  
  public int getUnitsCount()
  {
    return table.size();
  }
  
  public int getFightUnitsCount()
  {
    return inFightTable.size();
  }
  
  @Override
  public Player clone()
  {
    try
    {
      Player player = (Player) super.clone();
      return SerializationUtils.deepCopy(player);
    }
    catch (CloneNotSupportedException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public List<Unit> getTable()
  {
    return table;
  }
  
  public boolean inFight()
  {
    return inFightTable != null;
  }
}
