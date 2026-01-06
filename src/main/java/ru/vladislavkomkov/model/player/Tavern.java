package ru.vladislavkomkov.model.player;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.consts.Spells;
import ru.vladislavkomkov.consts.Units;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.ReflectUtils;
import ru.vladislavkomkov.util.SpellUtils;
import ru.vladislavkomkov.util.UnitUtils;

public class Tavern
{
  final List<Class<? extends Spell>> spellsPool = new ArrayList<>();
  final List<Class<? extends Unit>> unitsPool = new ArrayList<>();
  List<Slot> cards = new ArrayList<>();
  boolean freeze = false;
  private Player player;
  public Tavern()
  {
    this(DUMP_PLAYER);
  }
  
  public Tavern(Player player)
  {
    this(Units.tavernUnits, Spells.tavernSpells, player);
  }
  
  public Tavern(List<Class<? extends Unit>> unitsPool, List<Class<? extends Spell>> spellsPool, Player player)
  {
    this.spellsPool.addAll(spellsPool);
    this.unitsPool.addAll(unitsPool);
    this.player = player;
  }
  
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
  
  public void setFreeze(boolean freeze)
  {
    cards.forEach(slot -> slot.setFreezed(freeze));
    this.freeze = freeze;
  }
  
  public Card buy(int index)
  {
    return cards.remove(index).getCard();
  }
  
  public void add(Card card)
  {
    cards.add(new Slot(card));
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
    List<Class<? extends Unit>> units = getAvailableUnitsWithFallback(targetLevel, level);

    Class<? extends Unit> unit = units.get(RandUtils.getRand(units.size() - 1));

    return new Card(ReflectUtils.getInstance(unit, player));
  }
  
  void addRandomSpell(int level)
  {
    int targetLevel = RandUtils.getRandLvl(level);
    List<Class<? extends Spell>> spells = getAvailableSpellsWithFallback(targetLevel, level);

    Class<? extends Spell> spell = spells.get(RandUtils.getRand(spells.size() - 1));

    add(new Card(ReflectUtils.getInstance(spell, player)));
  }
  
  List<Class<? extends Unit>> getAvailableUnitsWithFallback(int targetLevel, int maxLevel)
  {
    List<Class<? extends Unit>> units = UnitUtils.getByTavern(targetLevel, unitsPool);

    while (units.isEmpty())
    {
      targetLevel = getNextFallbackLevel(targetLevel, maxLevel);
      units = UnitUtils.getByTavern(targetLevel, unitsPool);
    }

    return units;
  }
  
  List<Class<? extends Spell>> getAvailableSpellsWithFallback(int targetLevel, int maxLevel)
  {
    List<Class<? extends Spell>> spells = SpellUtils.getByTavern(targetLevel, spellsPool);

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
  
  public List<Class<? extends Unit>> getUnitsPool()
  {
    return unitsPool;
  }
  
  public List<Class<? extends Spell>> getSpellsPool()
  {
    return spellsPool;
  }
  
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
}