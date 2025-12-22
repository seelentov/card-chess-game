package ru.vladislavkomkov.consts;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Manasaber;
import ru.vladislavkomkov.model.entity.unit.impl.beast.second.HummingBird;
import ru.vladislavkomkov.model.entity.unit.impl.beast.second.HungrySnapjaw;
import ru.vladislavkomkov.model.entity.unit.impl.demon.first.IckyImp;
import ru.vladislavkomkov.model.entity.unit.impl.dragon.fourth.Greenskeeper;
import ru.vladislavkomkov.model.entity.unit.impl.mech.fourth.AccordoTron;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cubling;
import ru.vladislavkomkov.model.entity.unit.impl.trash.demon.first.Imp;
import ru.vladislavkomkov.model.entity.unit.impl.undead.first.RisenRider;

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
    units.add(new Manasaber());
    units.add(new Cubling());
    units.add(new HummingBird());
    units.add(new HungrySnapjaw());
    
    units.add(new RisenRider());
    
    units.add(new IckyImp());
    units.add(new Imp());
    
    units.add(new Greenskeeper());

//    units.add(new AccordoTron());

//    units.add(new DeepBlueCrooner());
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
