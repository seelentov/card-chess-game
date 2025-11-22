package ru.vladislavkomkov.consts;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.spell.impl.first.TavernCoin;

public class Spells
{
  public static List<Spell> spells = new ArrayList<>();
  public static List<Spell> tavernSpells = new ArrayList<>();
  
  static
  {
    setup();
    setupTavern();
  }
  
  static void setup()
  {
    spells.add(new TavernCoin());
  }
  
  static void setupTavern()
  {
    spells.forEach(unit -> {
      if (unit.isTavern())
      {
        tavernSpells.add(unit);
      }
    });
  }
}
