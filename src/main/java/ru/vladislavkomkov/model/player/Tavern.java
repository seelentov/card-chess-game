package ru.vladislavkomkov.model.player;

import java.util.ArrayList;
import java.util.List;

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
    private final Card<T> card;
    private boolean freezed;
    
    public Slot(Card<T> card)
    {
      this(card, false);
    }
    
    public Slot(Card<T> card, boolean freezed)
    {
      this.card = card;
      this.freezed = freezed;
    }
    
    public Card<T> getCard()
    {
      return card;
    }
    
    public T getEntity()
    {
      return card.getEntity();
    }
    
    public boolean isFreezed()
    {
      return freezed;
    }
    
    public void setFreezed(boolean freezed)
    {
      this.freezed = freezed;
    }
  }
  
  final List<Slot> cards = new ArrayList<>();
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
    this.freeze = freeze;
  }
  
  public void reset(int level, boolean saveFreezed)
  {
    if(!saveFreezed){
    
    }
    
    throw new RuntimeException("Not implemented");
  }
  
  void fillWithUnits(int level, int count) {
    for (int i = 0; i < count; i++) {
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
  
  List<Unit> getAvailableUnitsWithFallback(int targetLevel, int maxLevel)
  {
    List<Unit> units = UnitUtils.getUnitsByTavern(targetLevel);
    
    while (units.isEmpty())
    {
      targetLevel = getNextFallbackLevel(targetLevel, maxLevel);
      units = UnitUtils.getUnitsByTavern(targetLevel);
    }
    
    return units;
  }
  
  List<Spell> getAvailableSpellsWithFallback(int targetLevel, int maxLevel)
  {
    List<Spell> spells = SpellUtils.getByTavern(targetLevel);
    
    while (spells.isEmpty())
    {
      targetLevel = getNextFallbackLevel(targetLevel, maxLevel);
      spells = SpellUtils.getByTavern(targetLevel);
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
}