package ru.vladislavkomkov.model.entity.unit.impl.mech;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;

public class MechTestCase extends UnitTestCase
{
  protected void testDefault(Unit mech)
  {
    super.testDefault(mech);
    
    testMagnetize(mech);
  }
  
  void testMagnetize(Unit mech)
  {
    setUp();
    
    Unit mech2 = (Unit) mech.newThis();
    Unit mech3 = (Unit) mech.newThis();
    
    mech.magnetize(mech2);
    mech.magnetize(mech3);

    tearDown();
  }
}
