package ru.vladislavkomkov.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.impl.trash.beast.first.Cat;

public class UnitUtilsTest
{
  @Test
  void testGetUnits()
  {
    var units = UnitUtils.getUnits();
    
    assertNotNull(units);
    assertFalse(units.isEmpty());
  }
  
  @Test
  void testGetTavernUnits()
  {
    var units = UnitUtils.getTavernUnits();
    
    assertNotNull(units);
    units.forEach(unit -> assertTrue(unit.isTavern()));
  }
  
  @Test
  void testGetTavernUnitsByLvl()
  {
    for (int level = 1; level <= 6; level++)
    {
      int fLevel = level;
      
      var units = UnitUtils.getUnitsByTavern(fLevel);
      
      assertNotNull(units);
      units.forEach(unit -> assertEquals(fLevel, unit.getLevel()));
    }
  }
  
  @Test
  void testBuildGold()
  {
    Unit unit = UnitUtils.buildGold(new Cat(), new Cat(), new Cat());
    assertTrue(unit.isGold());
    assertEquals(new Cat().getName(), unit.getName());
    assertEquals(new Cat().getAttack() * 2, unit.getAttack());
    assertEquals(new Cat().getHealth() * 2, unit.getHealth());
  }
  
  @Test
  void testBuildGoldBuffedTemp()
  {
  }
  
  @Test
  void testBuildGoldBuffedStatic()
  {
  }
}
