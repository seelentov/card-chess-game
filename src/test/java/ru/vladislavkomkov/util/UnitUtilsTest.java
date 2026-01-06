package ru.vladislavkomkov.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class UnitUtilsTest
{
  @Test
  void testGetUnits()
  {
    var units = UnitUtils.getAll();
    
    assertNotNull(units);
    assertFalse(units.isEmpty());
  }
  
  @Test
  void testGetTavernUnits()
  {
    var units = UnitUtils.getTavern();
    
    assertNotNull(units);
    units.forEach(unit -> assertTrue(unit.isTavern()));
  }
  
  @Test
  void testGetTavernUnitsByLvl()
  {
    for (int level = 1; level <= 6; level++)
    {
      int fLevel = level;
      
      var units = UnitUtils.getByTavern(fLevel);
      
      assertNotNull(units);
      
      units.forEach(unit -> assertEquals(fLevel, ReflectUtils.getInstance(unit).getLevel()));
    }
  }
  
  @Test
  void testBuildGold()
  {
    Unit unit = new Cat().buildGold(new Cat(), new Cat(), new Cat());
    assertTrue(unit.isGold());
    assertEquals(new Cat().getName(), unit.getName());
    assertEquals(new Cat().getAttack() * 2, unit.getAttack());
    assertEquals(new Cat().getHealth() * 2, unit.getHealth());
  }
  
  @Test
  void testBuildGoldBuffed()
  {
    int boost = 10;
    String description = "Add +10/+10";
    
    Unit unit = new Cat();
    unit.addBuff(new Buff(
        unit1 -> {
          unit1.incHealth(boost);
          unit1.incBaseAttack(boost);
        },
        unit1 -> {
          unit1.decBaseAttack(boost);
          unit1.decHealth(boost);
        },
        description,
        null));
    
    Unit gold = new Cat().buildGold(new Cat(), unit, new Cat());
    assertTrue(gold.isGold());
    assertEquals(new Cat().getName(), gold.getName());
    assertEquals(new Cat().getAttack() * 2 + boost, gold.getAttack());
    assertEquals(new Cat().getHealth() * 2 + boost, gold.getHealth());
    assertEquals(description, gold.getBuffs().get(0).getDescription());
  }
}
