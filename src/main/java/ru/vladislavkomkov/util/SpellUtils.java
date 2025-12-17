package ru.vladislavkomkov.util;

import java.util.List;

import ru.vladislavkomkov.consts.Spells;
import ru.vladislavkomkov.model.entity.spell.Spell;

public class SpellUtils
{
  public static List<Spell> getAll()
  {
    return Spells.spells;
  };
  
  public static List<Spell> getTavern()
  {
    return Spells.tavernSpells;
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
    return pool.stream().filter(unit -> unit.getLevel() == lvl).toList();
  };
}
