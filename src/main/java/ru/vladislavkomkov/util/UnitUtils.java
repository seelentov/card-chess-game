package ru.vladislavkomkov.util;

import java.util.List;

import ru.vladislavkomkov.consts.Units;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Unit;

public class UnitUtils
{
  public static List<Unit> getAll()
  {
    return Units.units;
  };
  
  public static List<Unit> getTavern()
  {
    return Units.tavernUnits;
  };
  
  public static List<Unit> getByTavern(int lvl)
  {
    return getByTavern(lvl, true);
  };
  
  public static List<Unit> getByTavern(int lvl, boolean isTavern)
  {
    List<Unit> units = isTavern ? getTavern() : getAll();
    return getByTavern(lvl, units);
  }
  
  
  public static List<Unit> getByTavern(int lvl, List<Unit> pool)
  {
    return pool.stream().filter(unit -> unit.getLevel() == lvl).toList();
  };
}
