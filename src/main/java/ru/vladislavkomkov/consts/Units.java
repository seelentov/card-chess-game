package ru.vladislavkomkov.consts;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.model.entity.unit.impl.mech.fourth.AccordoTron;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class Units
{
  public static List<Unit> units = new ArrayList<>();
  public static List<Unit> tavernUnits = new ArrayList<>();
  
  static
  {
    setupUnits();
    setupTavernUnits();
  }
  
  static void setupUnits()
  {
    units.add(new Alleycat());
    units.add(new Cat());
    units.add(new AccordoTron());
  }
  
  static void setupTavernUnits()
  {
    units.forEach(unit -> {
      if (unit.isTavern())
      {
        tavernUnits.add(unit);
      }
    });
  }
}
