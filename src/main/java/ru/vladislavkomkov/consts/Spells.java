package ru.vladislavkomkov.consts;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.spell.impl.first.TavernCoin;
import ru.vladislavkomkov.model.entity.spell.impl.second.StrikeOil;

public class Spells
{
  public static List<Class<? extends Spell>> spells = new ArrayList<>();
  public static List<Class<? extends Spell>> tavernSpells = new ArrayList<>();
  
  static
  {
    setup();
    setupTavern();
  }
  
  static void setup()
  {
    spells.add(TavernCoin.class);
    spells.add(StrikeOil.class);
  }
  
  static void setupTavern()
  {
    spells.forEach(unit -> {
      try
      {
        if (unit.getDeclaredConstructor().newInstance().isTavern())
        {
          tavernSpells.add(unit);
        }
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    });
  }
}
