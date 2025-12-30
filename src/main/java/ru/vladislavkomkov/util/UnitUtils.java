package ru.vladislavkomkov.util;

import java.util.List;

import ru.vladislavkomkov.consts.Units;
import ru.vladislavkomkov.model.entity.unit.Unit;

public class UnitUtils
{
  public static List<Unit> getAll()
  {
    return get(Units.units);
  };
  
  public static List<Unit> getTavern()
  {
    return get(Units.tavernUnits);
  };
  
  private static List<Unit> get(List<Class<? extends Unit>> list)
  {
    return (List<Unit>) list.stream().map(ReflectUtils::getInstance).toList();
  }
  
  public static List<Class<? extends Unit>> getByTavern(int lvl)
  {
    return getByTavern(lvl, true);
  };
  
  public static List<Class<? extends Unit>> getByTavern(int lvl, boolean isTavern)
  {
    List<Class<? extends Unit>> units = isTavern ? Units.tavernUnits : Units.units;
    return getByTavern(lvl, units);
  }
  
  public static List<Class<? extends Unit>> getByTavern(int lvl, List<Class<? extends Unit>> pool)
  {
    return pool.stream().filter(unit -> ReflectUtils.getInstance(unit).getLevel() == lvl).toList();
  };
}
