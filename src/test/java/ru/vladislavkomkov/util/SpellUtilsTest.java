package ru.vladislavkomkov.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SpellUtilsTest
{
  @Test
  void testGetAll()
  {
    var units = SpellUtils.getAll();
    
    assertNotNull(units);
    assertFalse(units.isEmpty());
  }
  
  @Test
  void testGetTavern()
  {
    var units = SpellUtils.getTavern();
    
    assertNotNull(units);
    units.forEach(unit -> assertTrue(unit.isTavern()));
  }
  
  @Test
  void testGetTavernByLvl()
  {
    for (int level = 1; level <= 6; level++)
    {
      int fLevel = level;
      
      var units = SpellUtils.getByTavern(fLevel);
      
      assertNotNull(units);
      
      units.forEach(unit -> assertEquals(fLevel, ReflectUtils.getInstance(unit).getLevel()));
    }
  }
}
