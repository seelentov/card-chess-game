package ru.vladislavkomkov.consts;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Manasaber;
import ru.vladislavkomkov.model.entity.unit.impl.beast.second.HummingBird;
import ru.vladislavkomkov.model.entity.unit.impl.demon.first.IckyImp;
import ru.vladislavkomkov.model.entity.unit.impl.dragon.fourth.Greenskeeper;
import ru.vladislavkomkov.model.entity.unit.impl.dump.fifth.DumpFifth;
import ru.vladislavkomkov.model.entity.unit.impl.dump.first.DumpFirst;
import ru.vladislavkomkov.model.entity.unit.impl.dump.fourth.DumpFourth;
import ru.vladislavkomkov.model.entity.unit.impl.dump.second.DumpSecond;
import ru.vladislavkomkov.model.entity.unit.impl.dump.third.DumpThird;
import ru.vladislavkomkov.model.entity.unit.impl.mech.fourth.AccordoTron;
import ru.vladislavkomkov.model.entity.unit.impl.naga.fourth.DeepBlueCrooner;
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
    // BEASTS
    // 1
    units.add(new Alleycat());
    units.add(new Cat());
    units.add(new Manasaber());
    units.add(new Cubling());
    
    units.add(new HummingBird());
    
    // UNDEAD
    // 1
    units.add(new RisenRider());
    
    // DEMONS
    // 1
    units.add(new IckyImp());
    units.add(new Imp());

    // DRAGONS
    // 4
    units.add(new Greenskeeper());

    // MECH
    // 4
    units.add(new AccordoTron());

    // NAGA
    // 4
    units.add(new DeepBlueCrooner());

    // DUMPS
    units.add(new DumpFirst());
    units.add(new DumpSecond());
    units.add(new DumpThird());
    units.add(new DumpFourth());
    units.add(new DumpFifth());
    units.add(new DumpSecond());
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
