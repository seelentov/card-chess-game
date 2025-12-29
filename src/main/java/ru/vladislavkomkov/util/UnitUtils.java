package ru.vladislavkomkov.util;

import java.util.List;

import ru.vladislavkomkov.consts.Units;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

public class UnitUtils
{
  public static List<Unit> getAll(Player player)
  {
    return get(Units.units, player);
  };
  
  public static List<Unit> getTavern(Player player)
  {
    return get(Units.tavernUnits, player);
  };
  
  private static List<Unit> get(List<Class<? extends Unit>> list, Player player)
  {
    return (List<Unit>) list.stream().map(unit -> {
      try
      {
        return unit.getDeclaredConstructor(Player.class).newInstance(player);
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }).toList();
  }
  
  public static List<Class<? extends Unit>> getByTavern(int lvl, Player player)
  {
    return getByTavern(lvl, true, player);
  };
  
  public static List<Class<? extends Unit>> getByTavern(int lvl, boolean isTavern, Player player)
  {
    List<Class<? extends Unit>> units = isTavern ? Units.tavernUnits : Units.units;
    return getByTavern(lvl, units);
  }
  
  public static List<Class<? extends Unit>> getByTavern(int lvl, List<Class<? extends Unit>> pool)
  {
    return pool.stream().filter(unit -> {
      try
      {
        return unit.getDeclaredConstructor().newInstance().getLevel() == lvl;
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }).toList();
  };
}
