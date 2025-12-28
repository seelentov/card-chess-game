package ru.vladislavkomkov.model.player;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.SpellUtils;
import ru.vladislavkomkov.util.UnitUtils;

public class Tavern
{
  public static class Slot<T extends Entity>
  {
    public final static String F_CARD = "card";
    public final static String F_ENTITY = "entity";
    public final static String F_IS_FREEZED = "is_freezed";
    
    private final Card<T> card;
    private boolean isFreezed;
    
    public Slot(Card<T> card)
    {
      this(card, false);
    }
    
    public Slot(Card<T> card, boolean freezed)
    {
      this.card = card;
      this.isFreezed = freezed;
    }
    
    @JsonProperty(F_CARD)
    public Card<T> getCard()
    {
      return card;
    }
    
    @JsonProperty(F_ENTITY)
    public T getEntity()
    {
      return card.getEntity();
    }
    
    @JsonProperty(F_IS_FREEZED)
    public boolean isFreezed()
    {
      return isFreezed;
    }
    
    public void setFreezed(boolean freezed)
    {
      this.isFreezed = freezed;
    }
  }
  
  public Tavern()
  {
    this(SpellUtils.getTavern(), UnitUtils.getTavern());
  }
  
  public Tavern(List<Spell> spellsPool, List<Unit> unitsPool)
  {
    this.spellsPool.addAll(spellsPool);
    this.unitsPool.addAll(unitsPool);
  }
  
  final List<Spell> spellsPool = new ArrayList<>();
  final List<Unit> unitsPool = new ArrayList<>();
  
  List<Slot> cards = new ArrayList<>();
  boolean freeze = false;
  
  public static int getCountByLevel(int level)
  {
    return switch (level)
    {
      case 1 -> 3;
      case 2, 3 -> 4;
      case 4, 5 -> 5;
      case 6 -> 6;
      default -> throw new IllegalArgumentException("Wrong tavern level: " + level);
    };
  }
  
  public List<Slot> getCards()
  {
    return cards;
  }
  
  public List<Unit> getUnits()
  {
    return cards.stream().filter(card -> card.getEntity() instanceof Spell).map(card -> (Unit) card.getEntity()).toList();
  }
  
  public List<Spell> getSpells()
  {
    return cards.stream().filter(card -> card.getEntity() instanceof Spell).map(card -> (Spell) card.getEntity()).toList();
  }
  
  public boolean isFreeze()
  {
    return freeze;
  }
  
  public Card buy(int index)
  {
    return cards.remove(index).getCard();
  }
  
  public void add(Card card)
  {
    cards.add(new Slot(card));
  }
  
  public void setFreeze(boolean freeze)
  {
    cards.forEach(slot -> slot.setFreezed(freeze));
    this.freeze = freeze;
  }
  
  public void reset(int level)
  {
    reset(level, true);
  }
  
  public void reset(int level, boolean saveFreezed)
  {
    boolean isLastCardSpellAndFreezed = false;
    if (!cards.isEmpty() && saveFreezed)
    {
      Slot last = cards.get(cards.size() - 1);
      if (last.getEntity() instanceof Spell && last.isFreezed())
      {
        isLastCardSpellAndFreezed = true;
      }
    }
    
    if (saveFreezed)
    {
      cards.removeIf(card -> !card.isFreezed());
    }
    else
    {
      cards.clear();
    }
    
    int unitsCount = (int) cards.stream().filter(card -> card.getEntity() instanceof Unit).count();
    
    int count = getCountByLevel(level) - unitsCount;
    fillWithUnits(level, count);
    
    if (saveFreezed)
    {
      if (!isLastCardSpellAndFreezed)
      {
        addRandomSpell(level);
      }
    }
    else
    {
      addRandomSpell(level);
    }
    
    freeze = false;
    cards.forEach(slot -> slot.setFreezed(false));
  }
  
  void fillWithUnits(int level, int count)
  {
    for (int i = 0; i < count; i++)
    {
      Card unitCard = generateUnitCard(level);
      add(unitCard);
    }
  }
  
  Card generateUnitCard(int level)
  {
    int targetLevel = RandUtils.getRandLvl(level);
    List<Unit> units = getAvailableUnitsWithFallback(targetLevel, level);
    
    Unit unit = units.get(RandUtils.getRand(units.size() - 1));
    return new Card(unit);
  }
  
  void addRandomSpell(int level)
  {
    int targetLevel = RandUtils.getRandLvl(level);
    List<Spell> spells = getAvailableSpellsWithFallback(targetLevel, level);
    
    Spell spell = spells.get(RandUtils.getRand(spells.size() - 1));
    add(new Card(spell));
  }
  
  List<Unit> getAvailableUnitsWithFallback(int targetLevel, int maxLevel)
  {
    List<Unit> units = UnitUtils.getByTavern(targetLevel, unitsPool);
    
    while (units.isEmpty())
    {
      targetLevel = getNextFallbackLevel(targetLevel, maxLevel);
      units = UnitUtils.getByTavern(targetLevel, unitsPool);
    }
    
    return units;
  }
  
  List<Spell> getAvailableSpellsWithFallback(int targetLevel, int maxLevel)
  {
    List<Spell> spells = SpellUtils.getByTavern(targetLevel, spellsPool);
    
    while (spells.isEmpty())
    {
      targetLevel = getNextFallbackLevel(targetLevel, maxLevel);
      spells = SpellUtils.getByTavern(targetLevel, spellsPool);
    }
    
    return spells;
  }
  
  int getNextFallbackLevel(int currentLevel, int maxLevel)
  {
    if (currentLevel > 1)
    {
      return currentLevel - 1;
    }
    else
    {
      return maxLevel;
    }
  }
  
  public List<Unit> getUnitsPool()
  {
    return unitsPool;
  }
  
  public List<Spell> getSpellsPool()
  {
    return spellsPool;
  }
}