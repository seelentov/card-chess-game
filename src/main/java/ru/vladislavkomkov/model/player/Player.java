package ru.vladislavkomkov.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javafx.util.Pair;
import ru.vladislavkomkov.controller.sender.Sender;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.Listener;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.event.Event;
import ru.vladislavkomkov.model.event.data.SenderWaiterDataReq;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.UUIDUtils;

public class Player
{
  public static final int MAX_LEVEL = 6;
  public static final int MAX_MONEY = 6;
  public static final int TABLE_LIMIT = 7;
  public static final int HAND_LIMIT = 10;
  
  final String uuid;
  
  final Tavern tavern = new Tavern();
  public Listener listener = new Listener();
  public Statistic statistic = new Statistic();
  
  final List<Unit> table = new ArrayList<>(TABLE_LIMIT);
  final List<Card> hand = new ArrayList<>();
  public List<Unit> inFightTable = null;
  
  int health = 30;
  int maxHealth = 30;
  int armor = 0;
  int money = 0;
  int maxMoney = 3;
  int level = 1;
  int buyPrice = 3;
  int resetTavernPrice = 1;
  
  Game game;
  final Map<String, Consumer<Integer>> senderWaiters = new HashMap<>();
  Sender sender;
  
  public Player(Game game)
  {
    this(UUIDUtils.generateKey(), game);
  }
  
  public Player(String uuid, Game game)
  {
    this.uuid = uuid;
    this.game = game;
  }
  
  public void sendMessage(String gameUUID, String playerUUID, Event.Type type)
  {
    if (sender != null)
    {
      sender.send(new Event(gameUUID, playerUUID, type).getBytes());
    }
  }
  
  public void sendMessage(String gameUUID, String playerUUID, Event.Type type, int data)
  {
    if (sender != null)
    {
      sender.send(new Event(gameUUID, playerUUID, type, data).getBytes());
    }
  }
  
  public void sendMessage(String gameUUID, String playerUUID, Event.Type type, Object data)
  {
    if (sender != null)
    {
      sender.send(new Event(gameUUID, playerUUID, type, data).getBytes());
    }
  }
  
  public void sendMessage(String gameUUID, String playerUUID, Event.Type type, byte[] data)
  {
    if (sender != null)
    {
      sender.send(new Event(gameUUID, playerUUID, type, data).getBytes());
    }
  }
  
  public void putSenderWaiter(Consumer<Integer> consumer, Object data)
  {
    String key = UUIDUtils.generateKey();
    senderWaiters.put(key, consumer);
    
    sendMessage(game.getUUID(), getUUID(), Event.Type.WAIT_REQ,
        new SenderWaiterDataReq(key, data));
  }
  
  public void doSenderWaiter(String key, Integer param)
  {
    Consumer<Integer> waiter = senderWaiters.get(key);
    if (waiter != null)
    {
      waiter.accept(param);
      senderWaiters.remove(key);
    }
  }
  
  public void clearSenderWaiters()
  {
    senderWaiters.forEach((key, action) -> action.accept(RandUtils.getRand()));
    senderWaiters.clear();
  }
  
  public String getUUID()
  {
    return uuid;
  }
  
  public Sender getSender()
  {
    return sender;
  }
  
  public void setSender(Sender sender)
  {
    this.sender = sender;
  }
  
  public Game getGame()
  {
    return game;
  }
  
  public void setGame(Game game)
  {
    this.game = game;
  }
  
  public void moveTable(int fromIndex, int toIndex)
  {
    if (fromIndex < 0 || fromIndex >= table.size() || toIndex < 0 || toIndex >= table.size())
    {
      return;
    }
    
    Unit unit = table.remove(fromIndex);
    table.add(toIndex, unit);
    
    sendMessage(game.getUUID(), getUUID(), Event.Type.MOVE, table);
  }
  
  public boolean addToTable(Unit unit, int index)
  {
    if (table.size() >= TABLE_LIMIT)
    {
      return false;
    }
    
    if (index < 0 || index >= table.size())
    {
      table.add(unit);
    }
    else
    {
      table.add(index, unit);
    }
    
    unit.onAppear(game, this);
    sendMessage(game.getUUID(), getUUID(), Event.Type.ADD_TO_TABLE, table);
    
    return true;
  }
  
  public boolean addToTable(Unit unit)
  {
    return addToTable(unit, -1);
  }
  
  public void addToTable(List<Unit> units, int index)
  {
    units.forEach(unit -> addToTable(unit, index));
  }
  
  public void removeFromTable(Unit unit)
  {
    if (game != null)
    {
      unit.onDisappear(game, this);
      table.removeIf(unit1 -> unit1 == unit);
      sendMessage(game.getUUID(), getUUID(), Event.Type.REMOVE_FROM_TABLE, table);
    }
  }
  
  public void removeFromTable(int index)
  {
    if (index >= 0 && index < table.size())
    {
      table.get(index).onDisappear(game, this);
      table.remove(index);
    }
  }
  
  public int getIndex(Unit unit)
  {
    return table.indexOf(unit);
  }
  
  public int getFightIndex(Unit unit)
  {
    return inFightTable != null ? inFightTable.indexOf(unit) : -1;
  }
  
  public boolean addToFightTable(Unit unit, int index, boolean withoutOne)
  {
    if (inFightTable == null)
    {
      return false;
    }
    
    int size = withoutOne ? inFightTable.size() - 1 : inFightTable.size();
    if (size >= TABLE_LIMIT)
    {
      return false;
    }
    
    if (index < 0 || index >= inFightTable.size())
    {
      inFightTable.add(unit);
    }
    else
    {
      inFightTable.add(index, unit);
    }
    
    unit.onAppear(game, this);
    
    if (game != null)
    {
      sendMessage(game.getUUID(), getUUID(), Event.Type.ADD_TO_FIGHT_TABLE, table);
    }
    
    return true;
  }
  
  public boolean addToFightTable(Unit unit, int index)
  {
    return addToFightTable(unit, index, false);
  }
  
  public boolean addToFightTable(Unit unit)
  {
    return addToFightTable(unit, -1, false);
  }
  
  public void addToFightTable(List<Unit> units, int index, boolean withoutOne)
  {
    units.forEach(unit -> addToFightTable(unit, index, withoutOne));
  }
  
  public void addToFightTable(List<Unit> units, int index)
  {
    units.forEach(unit -> addToFightTable(unit, index, false));
  }
  
  public boolean inFight()
  {
    return inFightTable != null;
  }
  
  public void addToHand(Card card)
  {
    addToHand(card, false);
  }
  
  public void addToHand(Card card, boolean force)
  {
    if (force || hand.size() < HAND_LIMIT)
    {
      card.getEntity().onHandled(game, this);
      hand.add(card);
    }
    
    calcTriplets();
    sendMessage(game.getUUID(), getUUID(), Event.Type.ADD_TO_HAND, hand);
  }
  
  public void clearSpellCraft()
  {
    hand.removeIf(card -> card.getEntity() instanceof SpellCraft);
    sendMessage(game.getUUID(), getUUID(), Event.Type.REMOVE_FROM_HAND, hand);
  }
  
  public void playCard(int indexCard)
  {
    playCard(indexCard, 0, false, 0, false);
  }
  
  public void playCard(int indexCard, int index)
  {
    playCard(indexCard, index, false, 0, false);
  }
  
  public void playCard(int indexCard, int index, boolean isTavernIndex)
  {
    playCard(indexCard, index, isTavernIndex, 0, false);
  }
  
  public void playCard(int indexCard, int index, boolean isTavernIndex, int index2)
  {
    playCard(indexCard, index, isTavernIndex, index2, false);
  }
  
  public void playCard(int indexCard, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2)
  {
    if (indexCard < 0 || indexCard >= hand.size())
    {
      throw new IndexOutOfBoundsException(
          "Index " + indexCard + " not existed in hand with length " + hand.size());
    }
    
    hand.get(indexCard).play(game, this, index, isTavernIndex, index2, isTavernIndex2);
    hand.remove(indexCard);
    
    sendMessage(game.getUUID(), getUUID(), Event.Type.PLAY, hand);
  }
  
  public void buyCard(int index)
  {
    if (money >= buyPrice && hand.size() < HAND_LIMIT)
    {
      money -= buyPrice;
      addToHand(tavern.buy(index));
      
      sendMessage(game.getUUID(), getUUID(), Event.Type.BUY, hand);
    }
  }
  
  public void sellCard(int index)
  {
    if (index < 0 || index >= hand.size())
    {
      return;
    }
    
    Entity entity = hand.get(index).getEntity();
    if (entity instanceof Unit unit)
    {
      unit.onSell(game, this);
      sendMessage(game.getUUID(), getUUID(), Event.Type.SELL, hand);
    }
  }
  
  public void freezeTavern()
  {
    tavern.setFreeze(!tavern.isFreeze());
    sendMessage(game.getUUID(), getUUID(), Event.Type.FREEZE, tavern.isFreeze());
  }
  
  public void resetTavernManual()
  {
    if (statistic.counters.getFreeTavernCount() <= 0)
    {
      money -= resetTavernPrice;
    }
    
    tavern.setFreeze(false);
    resetTavern();
    
    sendMessage(game.getUUID(), getUUID(), Event.Type.RESET_TAVERN, tavern.getCards());
    sendMessage(game.getUUID(), getUUID(), Event.Type.FREEZE, tavern.isFreeze());
  }
  
  public void resetTavern()
  {
    tavern.reset(level);
    listener.processOnResetTavernListeners(game, this);
  }
  
  public void calcTriplets()
  {
    boolean foundTriplets;
    
    do
    {
      foundTriplets = false;
      Map<String, List<Pair<Boolean, Integer>>> indexCountMap = calculateCardIndexes();
      
      for (List<Pair<Boolean, Integer>> cardList : indexCountMap.values())
      {
        if (cardList.size() >= 3)
        {
          foundTriplets = true;
          processTriplets(cardList);
          break;
        }
      }
    }
    while (foundTriplets);
  }
  
  Map<String, List<Pair<Boolean, Integer>>> calculateCardIndexes()
  {
    Map<String, List<Pair<Boolean, Integer>>> indexCountMap = new HashMap<>();
    
    for (int i = 0; i < hand.size(); i++)
    {
      Card card = hand.get(i);
      if (card.isSpell() || card.getEntity().isGold())
      {
        continue;
      }
      
      String key = card.getEntity().getName();
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
  }
  
  void processTriplets(List<Pair<Boolean, Integer>> triplets)
  {
    List<Unit> unitsForGold = new ArrayList<>();
    List<Pair<Boolean, Integer>> tripletsToRemove = triplets.subList(0, 3);
    
    for (Pair<Boolean, Integer> pair : tripletsToRemove)
    {
      boolean isOnTable = pair.getKey();
      int index = pair.getValue();
      
      if (isOnTable)
      {
        unitsForGold.add(table.get(index));
      }
      else
      {
        unitsForGold.add((Unit) hand.get(index).getEntity());
      }
    }
    
    tripletsToRemove.sort((p1, p2) -> Boolean.compare(p2.getKey(), p1.getKey()));
    tripletsToRemove.sort((p1, p2) -> Integer.compare(p2.getValue(), p1.getValue()));
    
    for (Pair<Boolean, Integer> pair : tripletsToRemove)
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
  }
  
  public int getLevel()
  {
    return level;
  }
  
  public void incLevel()
  {
    if (level < MAX_LEVEL)
    {
      level++;
      listener.processOnIncTavernLevelListener(game, this);
      sendMessage(game.getUUID(), getUUID(), Event.Type.LVL_UP, level);
    }
  }
  
  public int getHealth()
  {
    return health;
  }
  
  public int getMaxHealth()
  {
    return maxHealth;
  }
  
  public int getArmor()
  {
    return armor;
  }
  
  public void setArmor(int armor)
  {
    this.armor = Math.max(armor, 0);
    sendMessage(game.getUUID(), getUUID(), Event.Type.ARMOR, this.armor);
  }
  
  public void addArmor(int amount)
  {
    this.armor += amount;
    sendMessage(game.getUUID(), getUUID(), Event.Type.ARMOR, this.armor);
  }
  
  public void applyDamage(int damage)
  {
    int piercingDamage = Math.max(damage - armor, 0);
    
    armor = Math.max(armor - damage, 0);
    health -= piercingDamage;
    
    sendMessage(game.getUUID(), getUUID(), Event.Type.APPLY_DAMAGE, health);
  }
  
  public int getMoney()
  {
    return money;
  }
  
  public int getMaxMoney()
  {
    return maxMoney;
  }
  
  public void resetMoney()
  {
    money = maxMoney;
    sendMessage(game.getUUID(), getUUID(), Event.Type.MONEY, money);
  }
  
  public void addMoney()
  {
    addMoney(1);
  }
  
  public void addMoney(int amount)
  {
    money += amount;
    sendMessage(game.getUUID(), getUUID(), Event.Type.MONEY, money);
  }
  
  public void decMoney(int amount)
  {
    money = Math.max(money - amount, 0);
    sendMessage(game.getUUID(), getUUID(), Event.Type.MONEY, money);
  }
  
  public void incMaxMoney()
  {
    incMaxMoney(1);
  }
  
  public void incMaxMoney(int amount)
  {
    maxMoney += amount;
    sendMessage(game.getUUID(), getUUID(), Event.Type.MAX_MONEY, maxMoney);
  }
  
  public void doForAll(Consumer<Unit> consumer)
  {
    table.forEach(consumer);
  }
  
  public void doFor(Consumer<Unit> consumer, int index)
  {
    if (index >= 0 && index < table.size())
    {
      consumer.accept(table.get(index));
    }
  }
  
  public void removeTempBuffs()
  {
    doForAll(Unit::removeTempBuffs);
    
    hand.stream()
        .map(Card::getEntity)
        .filter(entity -> entity instanceof Unit)
        .map(entity -> (Unit) entity)
        .forEach(Unit::removeTempBuffs);
  }
  
  public boolean isAlive()
  {
    return health > 0;
  }
  
  public List<Unit> cloneTable()
  {
    return new ArrayList<>(table);
  }
  
  public List<Card> cloneHand()
  {
    return new ArrayList<>(hand);
  }
  
  public List<Unit> getTable()
  {
    return table;
  }
  
  public int getUnitsCount()
  {
    return table.size();
  }
  
  public int getFightUnitsCount()
  {
    return inFightTable != null ? inFightTable.size() : 0;
  }
  
  public Tavern getTavern()
  {
    return tavern;
  }
}