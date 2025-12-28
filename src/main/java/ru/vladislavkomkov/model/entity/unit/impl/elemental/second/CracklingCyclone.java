package ru.vladislavkomkov.model.entity.unit.impl.elemental.second;

import ru.vladislavkomkov.model.entity.unit.AttacksCount;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;

import java.util.List;

public class CracklingCyclone extends Unit
{
  public CracklingCyclone()
  {
    super();

    description = "Divine Shield\n" +
        "Windfury";
    
    unitType = List.of(UnitType.ELEMENTAL);
    
    attack = 3;
    
    maxHealth = 1;
    actualHealth = 1;
    
    isTavern = true;
    
    level = 2;
    
    isBubbled = true;
    attacksCount = AttacksCount.DOUBLE;
  }
}
