package ru.vladislavkomkov.util;

import java.util.List;

import ru.vladislavkomkov.consts.Units;
import ru.vladislavkomkov.models.entity.unit.Unit;

public class UnitUtils
{
  public static List<Unit> getUnits()
  {
    return Units.units;
  };
  
  public static List<Unit> getTavernUnits()
  {
    return Units.tavernUnits;
  };
  
  public static List<Unit> getUnitsByTavern(int lvl)
  {
    return getUnitsByTavern(lvl, true);
  };
  
  public static List<Unit> getUnitsByTavern(int lvl, boolean tavern)
  {
    List<Unit> units = tavern ? getTavernUnits() : getUnits();
    return units.stream().filter(unit -> unit.getLevel() == lvl).toList();
  }
  
  public static <T extends Unit> T buildGold(List<T> units)
  {
    return buildGold(units.get(0), units.get(1), units.get(2));
  };
  
  public static <T extends Unit> T buildGold(T unit, T unit2, T unit3)
  {
    T entity = (T) unit.newThis();
    entity.setAttack(entity.getAttack() * 2);
    entity.setHealth(entity.getHealth() * 2);
    
    List<T> units = List.of(unit, unit2, unit3);
    units.forEach(Unit::removeCoreListeners);
    units.stream()
        .map(Unit::getBuffs)
        .flatMap(List::stream)
        .forEach(entity::addBuff);
    
    entity.setIsGold(true);
    
    return entity;
  };
}
