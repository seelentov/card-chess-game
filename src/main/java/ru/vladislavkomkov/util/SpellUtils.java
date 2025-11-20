package ru.vladislavkomkov.util;

import java.util.List;

import ru.vladislavkomkov.consts.Spells;
import ru.vladislavkomkov.models.entity.spell.Spell;

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
  
  public static List<Spell> getByTavern(int lvl, boolean tavern)
  {
    List<Spell> units = tavern ? getTavern() : getAll();
    return units.stream().filter(unit -> unit.getLevel() == lvl).toList();
  };
}
