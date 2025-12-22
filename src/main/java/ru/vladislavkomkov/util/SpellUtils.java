package ru.vladislavkomkov.util;

import java.util.List;

import ru.vladislavkomkov.consts.Spells;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Unit;

public class SpellUtils
{
  public static List<Spell> getAll()
  {
    return Spells.spells.stream().map(Spell::clone).toList();
  };
  
  public static List<Spell> getTavern()
  {
    return Spells.tavernSpells.stream().map(Spell::clone).toList();
  };
  
  public static List<Spell> getByTavern(int lvl)
  {
    return getByTavern(lvl, true);
  };
  
  public static List<Spell> getByTavern(int lvl, boolean isTavern)
  {
    List<Spell> units = isTavern ? getTavern() : getAll();
    return getByTavern(lvl, units);
  }
  
  
  public static List<Spell> getByTavern(int lvl, List<Spell> pool)
  {
    return pool.stream().filter(unit -> unit.getLevel() == lvl).map(Spell::clone).toList();
  };
}
