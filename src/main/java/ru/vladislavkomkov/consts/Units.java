package ru.vladislavkomkov.consts;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.enviroment.Config;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Manasaber;
import ru.vladislavkomkov.model.entity.unit.impl.beast.second.HummingBird;
import ru.vladislavkomkov.model.entity.unit.impl.beast.third.SprightlyScarab;
import ru.vladislavkomkov.model.entity.unit.impl.demon.first.IckyImp;
import ru.vladislavkomkov.model.entity.unit.impl.dragon.fourth.Greenskeeper;
import ru.vladislavkomkov.model.entity.unit.impl.dump.fifth.DumpFifth;
import ru.vladislavkomkov.model.entity.unit.impl.dump.first.DumpFirst;
import ru.vladislavkomkov.model.entity.unit.impl.dump.fourth.DumpFourth;
import ru.vladislavkomkov.model.entity.unit.impl.dump.second.DumpSecond;
import ru.vladislavkomkov.model.entity.unit.impl.dump.sixth.DumpSixth;
import ru.vladislavkomkov.model.entity.unit.impl.dump.third.DumpThird;
import ru.vladislavkomkov.model.entity.unit.impl.mech.fourth.AccordoTron;
import ru.vladislavkomkov.model.entity.unit.impl.naga.fourth.DeepBlueCrooner;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cubling;
import ru.vladislavkomkov.model.entity.unit.impl.trash.demon.first.Imp;
import ru.vladislavkomkov.model.entity.unit.impl.undead.first.RisenRider;

public class Units
{
  public static List<Class<? extends Unit>> units = new ArrayList<>();
  public static List<Class<? extends Unit>> tavernUnits = new ArrayList<>();
  
  static
  {
    if (Config.getInstance().isDebug())
    {
      setupUnitsDebug();
    }
    else
    {
      setupUnits();
    }
    
    setupTavernUnits();
  }
  
  static void setupUnitsDebug()
  {
    setupUnits();
  }
  
  static void setupUnits()
  {
    // BEASTS
    // 1
    units.add(Alleycat.class);
    units.add(Cat.class);
    units.add(Manasaber.class);
    units.add(Cubling.class);
    
    // 2
    units.add(HummingBird.class);
    
    // 3
    units.add(SprightlyScarab.class);
    
    // UNDEAD
    // 1
    units.add(RisenRider.class);
    
    // DEMONS
    // 1
    units.add(IckyImp.class);
    units.add(Imp.class);
    
    // DRAGONS
    // 4
    units.add(Greenskeeper.class);
    
    // MECH
    // 4
    units.add(AccordoTron.class);
    
    // NAGA
    // 4
    units.add(DeepBlueCrooner.class);
    
    // DUMPS
    units.add(DumpFirst.class);
    units.add(DumpSecond.class);
    units.add(DumpThird.class);
    units.add(DumpFourth.class);
    units.add(DumpFifth.class);
    units.add(DumpSixth.class);
  }
  
  static void setupTavernUnits()
  {
    units.forEach(unit -> {
      try
      {
        if (unit.getDeclaredConstructor().newInstance().isTavern())
        {
          tavernUnits.add(unit);
        }
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    });
  }
}
