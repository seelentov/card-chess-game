package ru.vladislavkomkov.models.unit.impl.trash.beast.first;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.models.unit.Unit;
import ru.vladislavkomkov.models.unit.UnitTestCase;

public class CatTest extends UnitTestCase {
    Unit unit = new Cat();
    
    @Test
    void testDefault(){
        super.testDefault(unit);
    }
}
