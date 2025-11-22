package ru.vladislavkomkov.model.entity.unit.impl.mech;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.vladislavkomkov.model.entity.unit.UnitTestCase;

public class MechTestCase extends UnitTestCase
{
  protected void testDefault(Mech mech)
  {
    super.testDefault(mech);
    
    testMagnetize(mech);
  }
  
  void testMagnetize(Mech mech)
  {
    setUp();
    
    Mech mech2 = (Mech) mech.newThis();
    Mech mech3 = (Mech) mech.newThis();
    
    mech.magnetize(mech2);
    mech.magnetize(mech3);
    
    assertEquals(mech2.getName(), mech.cloneMagnetized().get(0).getName());
    assertEquals(mech3.getName(), mech.cloneMagnetized().get(1).getName());
    
    tearDown();
  }
}
