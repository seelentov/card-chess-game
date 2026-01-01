package ru.vladislavkomkov.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.consts.Spells;
import ru.vladislavkomkov.consts.Units;
import ru.vladislavkomkov.controller.sender.Sender;
import ru.vladislavkomkov.enviroment.Config;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.Listener;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.event.Event;
import ru.vladislavkomkov.model.event.data.SenderWaiterDataReq;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.UUIDUtils;
import ru.vladislavkomkov.util.models.Pair;

public class Player
{
  
  public static final int MAX_LEVEL = 6;
  public static final int MAX_MONEY = 10;
  public static final int START_MONEY = Config.getInstance().isDebug() ? 100000 : 3;
  
  public static final int TABLE_LIMIT = 7;
  public static final int HAND_LIMIT = 10;
  
  public static final int START_HEALTH = 30;
  public static final int START_ARMOR = 0;
  
  public final static String F_UUID = "uuid";
  
  public static final Map<Integer, Integer> INC_LEVEL_PRICE_MAP = Map.of(
      1, 5,
      2, 7);
  public static final int INC_LEVEL_PRICE_MAX = 10;
  
  final String uuid;
  final Tavern tavern;
  final Listener listener = new Listener();
  final Statistic statistic = new Statistic();
  
  final List<Unit> table = new ArrayList<>(TABLE_LIMIT);
  final List<Card> hand = new ArrayList<>();
  
  int health = START_HEALTH;
  int maxHealth = START_HEALTH;
  int armor = START_ARMOR;
  int money = 0;
  int maxMoney = START_MONEY;
  int level = 1;
  int buyPrice = 3;
  int resetTavernPrice = 1;
  
  Game game;
  final Map<String, Consumer<Integer>> senderWaiters = new HashMap<>();
  Sender sender;
  
  public Player()
  {
    uuid = "";
    tavern = null;
  }
  
  public Player(Game game)
  {
    this(UUIDUtils.generateKey(), game);
  }
  
  public Player(String uuid, Game game)
  {
    this(uuid, game, Units.tavernUnits, Spells.tavernSpells);
  }
  
  public Player(Game game, List<Class<? extends Unit>> unitsPool, List<Class<? extends Spell>> spellsPool)
  {
    this(UUIDUtils.generateKey(), game, unitsPool, spellsPool);
  }
  
  public Player(String uuid, Game game, List<Class<? extends Unit>> unitsPool, List<Class<? extends Spell>> spellsPool)
  {
    this.uuid = uuid;
    this.game = game;
    this.tavern = new Tavern(unitsPool, spellsPool, this);
  }
  
  public void sendFullStat()
  {
    sendArmorHealth();
    sendMaxMoney();
    sendMoney();
    sendFreeze();
    sendLevel();
    sendHand();
    sendTavern();
    sendTable();
    sendLvlIncPrice();
  }
  
  public void sendArmorHealth()
  {
    sendHealth();
    sendArmor();
  }
  
  public void sendMaxMoney()
  {
    sendMessage(Event.Type.MAX_MONEY, getMaxMoney());
  }
  
  public void sendLvlIncPrice()
  {
    sendMessage(Event.Type.LVL_PRICE, getIncLevelPrice());
  }
  
  public void sendMoney()
  {
    sendMessage(Event.Type.MONEY, getMoney());
  }
  
  public void sendFreeze()
  {
    sendMessage(Event.Type.FREEZE, getTavern().isFreeze());
  }
  
  public void sendLevel()
  {
    sendMessage(Event.Type.LVL, getLevel());
  }
  
  public void sendHand()
  {
    sendMessage(Event.Type.HAND, getHand());
  }
  
  public void sendTavern()
  {
    sendMessage(Event.Type.TAVERN, getTavern().getCards());
  }
  
  public void sendTable()
  {
    sendMessage(Event.Type.TABLE, getTable());
  }
  
  public void sendHealth()
  {
    sendMessage(Event.Type.HEALTH, getHealth());
  }
  
  public void sendArmor()
  {
    sendMessage(Event.Type.ARMOR, getArmor());
  }
  
  public void sendMessage(Event.Type type)
  {
    if (sender != null)
    {
      sender.send(new Event(game.getUUID(), getUUID(), type).getBytes());
    }
  }
  
  public void sendMessage(Event.Type type, int data)
  {
    if (sender != null)
    {
      sender.send(new Event(game.getUUID(), getUUID(), type, data).getBytes());
    }
  }
  
  public void sendMessage(Event.Type type, boolean data)
  {
    if (sender != null)
    {
      sender.send(new Event(game.getUUID(), getUUID(), type, data).getBytes());
    }
  }
  
  public void sendMessage(Event.Type type, Object data)
  {
    if (sender != null)
    {
      sender.send(new Event(game.getUUID(), getUUID(), type, data).getBytes());
    }
  }
  
  public void sendMessage(Event.Type type, byte[] data)
  {
    if (sender != null)
    {
      sender.send(new Event(game.getUUID(), getUUID(), type, data).getBytes());
    }
  }
  
  public void putSenderWaiter(Consumer<Integer> consumer, List<Card> data)
  {
    String key = UUIDUtils.generateKey();
    senderWaiters.put(key, consumer);
    sendMessage(Event.Type.WAIT_REQ, new SenderWaiterDataReq(key, data));
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
  
  @JsonProperty(F_UUID)
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
    sendTable();
  }
  
  public boolean addToTable(Unit unit, int index)
  {
    return addToTable(unit, index, false);
  }
  
  public boolean addToTable(Unit unit, int index, boolean withoutOne)
  {
    int tableSize = table.size() - (withoutOne ? 1 : 0);
    
    if (tableSize >= TABLE_LIMIT)
    {
      return false;
    }
    
    if (index < 0 || index >= tableSize)
    {
      table.add(unit);
    }
    else
    {
      table.add(index, unit);
    }
    
    unit.onSummoned(game, null, this);
    unit.onAppear(game, null, this);
    
    sendTable();
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
      table.removeIf(unit1 -> unit1 == unit);
      unit.onDisappear(game, null, this);
      sendTable();
    }
  }
  
  public void removeFromTable(int index)
  {
    if (index >= 0 && index < table.size())
    {
      table.remove(index);
      table.get(index).onDisappear(game, null, this);
      sendTable();
    }
  }
  
  public int getIndex(Unit unit)
  {
    return table.indexOf(unit);
  }
  
  public void addToTavern(Entity entity)
  {
    tavern.add(Card.of(entity));
    sendTavern();
  }
  
  public void addToHand(Card card)
  {
    addToHand(card, false);
  }
  
  public void addToHand(Card card, boolean force)
  {
    if (force || hand.size() < HAND_LIMIT)
    {
      card.getEntity().onHandled(game, null, this);
      hand.add(card);
    }
    calcTriplets();
    sendHand();
  }
  
  public void clearSpellCraft()
  {
    hand.removeIf(card -> card.getEntity() instanceof SpellCraft);
    sendHand();
  }
  
  public void playCard(int indexCard)
  {
    playCard(indexCard, 0);
  }
  
  public void playCard(int indexCard, int index)
  {
    playCard(indexCard, index, 0);
  }
  
  public void playCard(int indexCard, int index, int index2)
  {
    playCard(indexCard, List.of(index, 0, index2, 0));
  }
  
  public void playCard(int indexCard, int index, boolean isTavernIndex)
  {
    playCard(indexCard, List.of(index, isTavernIndex ? 1 : 0));
  }
  
  public void playCard(int indexCard, List<Integer> input)
  {
    if (indexCard < 0 || indexCard >= hand.size())
    {
      throw new IndexOutOfBoundsException("Index " + indexCard + " not existed in hand with length " + hand.size());
    }
    
    Card cardTemp = hand.get(indexCard);
    hand.remove(indexCard);
    
    boolean played = cardTemp.play(game, this, input);
    if (!played)
    {
      hand.add(indexCard, cardTemp);
      sendHand();
    }
    else
    {
      sendFullStat();
    }
  }
  
  public void buyCard(int index)
  {
    boolean success = false;
    
    if (tavern.getCards().size() <= index)
    {
      return;
    }
    
    boolean isSpell = tavern.getCards().get(index).getEntity() instanceof Spell;
    if (isSpell)
    {
      int price = tavern.getCards().get(index).getEntity().getLevel();
      if (money >= price && hand.size() < HAND_LIMIT)
      {
        decMoney(price);
        success = true;
      }
    }
    else
    {
      if (money >= buyPrice && hand.size() < HAND_LIMIT)
      {
        decMoney(buyPrice);
        success = true;
      }
    }
    
    if (success)
    {
      addToHand(tavern.buy(index));
      sendTavern();
    }
  }
  
  public void sellCard(int index)
  {
    if (index < 0 || index >= table.size())
    {
      return;
    }
    
    Unit unit = table.get(index);
    unit.onSell(game, null, this);
    sendTavern();
    sendMoney();
  }
  
  public void freezeTavern()
  {
    tavern.setFreeze(!tavern.isFreeze());
    sendTavern();
    sendFreeze();
  }
  
  public void resetTavernManual()
  {
    if (money < resetTavernPrice)
    {
      return;
    }
    
    money -= resetTavernPrice;
    sendMoney();
    
    tavern.setFreeze(false);
    resetTavern();
    
    sendTavern();
    sendFreeze();
  }
  
  public void resetTavern()
  {
    resetTavern(false);
  }
  
  public void resetTavern(boolean saveFreezed)
  {
    tavern.reset(level, saveFreezed);
    listener.processOnResetTavernListeners(game, null, this);
    sendTavern();
    sendFreeze();
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
    
    sendHand();
    sendTable();
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
  
  public void setLevel(int level)
  {
    this.level = level;
    sendLevel();
    sendLvlIncPrice();
  }
  
  public void incLevel()
  {
    incLevel(false);
  }
  
  public void incLevel(boolean free)
  {
    if (level < MAX_LEVEL)
    {
      int price = free ? 0 : getIncLevelPrice();
      
      if (price <= money)
      {
        level++;
        listener.processOnIncTavernLevelListener(game, null, this);
        decMoney(price);
        sendLevel();
        statistic.counters.resetIncLevelDecreaser();
        sendLvlIncPrice();
      }
    }
  }
  
  public int getIncLevelPrice()
  {
    int basePrice = INC_LEVEL_PRICE_MAP.getOrDefault(level, INC_LEVEL_PRICE_MAX);
    int calcedPrice = basePrice - statistic.counters.getIncLevelDecreaser();
    return Math.max(calcedPrice, 0);
  }
  
  public int getFullHealth()
  {
    return getHealth() + getArmor();
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
    sendArmor();
  }
  
  public void addArmor(int amount)
  {
    this.armor += amount;
    sendArmor();
  }
  
  public void applyDamage(int damage)
  {
    int piercingDamage = Math.max(damage - armor, 0);
    armor = Math.max(armor - damage, 0);
    health -= piercingDamage;
    
    if (!isAlive())
    {
      sendMessage(Event.Type.LOSE);
      sendMessage(Event.Type.DISCONNECTED);
      return;
    }
    
    sendArmorHealth();
  }
  
  public int getMoney()
  {
    return money;
  }
  
  public void setMoney(int money)
  {
    this.money = money;
    sendMoney();
  }
  
  public int getMaxMoney()
  {
    return maxMoney;
  }
  
  public int getMaxMoneyBase()
  {
    return maxMoney;
  }
  
  public void setMaxMoney(int maxMoney)
  {
    this.maxMoney = maxMoney;
    sendMaxMoney();
  }
  
  public void incMaxMoney()
  {
    incMaxMoney(1);
  }
  
  public void incMaxMoney(int amount)
  {
    maxMoney += amount;
    sendMaxMoney();
  }
  
  public void resetMoney()
  {
    money = getMaxMoney();
    sendMoney();
  }
  
  public void addMoney()
  {
    addMoney(1);
  }
  
  public void addMoney(int amount)
  {
    money += amount;
    sendMoney();
  }
  
  public void decMoney(int amount)
  {
    money = Math.max(money - amount, 0);
    sendMoney();
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
    sendHand();
    sendTable();
  }
  
  public boolean isAlive()
  {
    return health > 0;
  }
  
  public List<Unit> cloneTable()
  {
    return table.stream()
        .filter(Objects::nonNull)
        .map(Unit::clone)
        .collect(Collectors.toCollection(ArrayList::new));
  }
  
  public List<Card> cloneHand()
  {
    return new ArrayList<>(hand);
  }

  public List<Unit> getTable()
  {
    return table;
  }

  public Optional<Unit> getTableUnit(String ID)
  {
    return table.stream().filter(unit -> unit.getID().equals(ID)).findFirst();
  }
  
  public List<Card> getHand()
  {
    return hand;
  }
  
  public int getUnitsCount()
  {
    return table.size();
  }
  
  public Tavern getTavern()
  {
    return tavern;
  }
  
  public int getBuyPrice()
  {
    return buyPrice;
  }
  
  public Listener getListener()
  {
    return listener;
  }
  
  public Statistic getStatistic()
  {
    return statistic;
  }
}