package ru.vladislavkomkov.model.player;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.event.Event;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.SpellUtils;
import ru.vladislavkomkov.util.UnitUtils;

public class Tavern
{
  final List<Card> cards = new ArrayList<>();
  boolean freeze = false;
  
  static int getCountByLvl(int level)
  {
    return switch (level)
    {
      case 1 -> 3;
      case 2, 3 -> 4;
      case 4, 5 -> 5;
      case 6 -> 6;
      default -> throw new RuntimeException("Wrong level");
    };
  }
  
  public Card buy(int index)
  {
    return cards.remove(index);
  }
  
  public void add(Card card)
  {
    cards.add(card);
  }
  
  public void reset(int level)
  {
    if (freeze)
    {
      freeze = false;
      return;
    }
    
    cards.clear();
    
    int count = getCountByLvl(level);
    
    for (int i = 0; i < count; i++)
    {
      int lvl = RandUtils.getRandLvl(level);
      List<Unit> units = UnitUtils.getUnitsByTavern(lvl);
      
      while (units.isEmpty())
      {
        if (lvl > 1)
        {
          lvl--;
        }
        else if (lvl == 1)
        {
          lvl = level;
        }
        
        units = UnitUtils.getUnitsByTavern(lvl);
      }
      
      Unit unit = units.get(RandUtils.getRand(units.size() - 1));
      add(new Card(unit));
    }
    
    int lvl = RandUtils.getRandLvl(level);
    List<Spell> spells = SpellUtils.getByTavern(lvl);
    while (spells.isEmpty())
    {
      if (lvl > 1)
      {
        lvl--;
      }
      else if (lvl == 1)
      {
        lvl = level;
      }
      
      spells = SpellUtils.getByTavern(lvl);
    }
    
    Spell spell = spells.get(RandUtils.getRand(spells.size() - 1));
    
    add(new Card(spell));
  }
  
  public List<Card> getCards()
  {
    return cards;
  }
}
