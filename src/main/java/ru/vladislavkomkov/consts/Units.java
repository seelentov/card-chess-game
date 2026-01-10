package ru.vladislavkomkov.consts;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.enviroment.Config;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.beast.fifth.*;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Manasaber;
import ru.vladislavkomkov.model.entity.unit.impl.beast.fourth.MamaBear;
import ru.vladislavkomkov.model.entity.unit.impl.beast.fourth.RylakMetalhead;
import ru.vladislavkomkov.model.entity.unit.impl.beast.fourth.StompingStegodon;
import ru.vladislavkomkov.model.entity.unit.impl.beast.fourth.ValiantTiger;
import ru.vladislavkomkov.model.entity.unit.impl.beast.second.HummingBird;
import ru.vladislavkomkov.model.entity.unit.impl.beast.second.HungrySnapjaw;
import ru.vladislavkomkov.model.entity.unit.impl.beast.second.SewerRat;
import ru.vladislavkomkov.model.entity.unit.impl.beast.sixth.GoldrinnTheGreatWolf;
import ru.vladislavkomkov.model.entity.unit.impl.beast.sixth.HawkstriderHerald;
import ru.vladislavkomkov.model.entity.unit.impl.beast.sixth.P0ULTR0N;
import ru.vladislavkomkov.model.entity.unit.impl.beast.third.MonstrousMacaw;
import ru.vladislavkomkov.model.entity.unit.impl.beast.third.SlyRaptor;
import ru.vladislavkomkov.model.entity.unit.impl.beast.third.SprightlyScarab;
import ru.vladislavkomkov.model.entity.unit.impl.demon.first.IckyImp;
import ru.vladislavkomkov.model.entity.unit.impl.dragon.fourth.Greenskeeper;
import ru.vladislavkomkov.model.entity.unit.impl.elemental.second.CracklingCyclone;
import ru.vladislavkomkov.model.entity.unit.impl.mech.fourth.AccordoTron;
import ru.vladislavkomkov.model.entity.unit.impl.naga.fourth.DeepBlueCrooner;
import ru.vladislavkomkov.model.entity.unit.impl.none.fifth.BrannBronzebeard;
import ru.vladislavkomkov.model.entity.unit.impl.none.fifth.DrakkariEnchanter;
import ru.vladislavkomkov.model.entity.unit.impl.none.fifth.TitusRivendare;
import ru.vladislavkomkov.model.entity.unit.impl.none.third.BirdBuddy;
import ru.vladislavkomkov.model.entity.unit.impl.undead.first.RisenRider;
import ru.vladislavkomkov.util.ReflectUtils;

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
    units.add(Manasaber.class);
    
    // 2
    units.add(HummingBird.class);
    units.add(HungrySnapjaw.class);
    units.add(SewerRat.class);
    
    // 3
    units.add(MonstrousMacaw.class);
    units.add(SlyRaptor.class);
    units.add(SprightlyScarab.class);
    
    // 4
    units.add(MamaBear.class);
    units.add(RylakMetalhead.class);
    units.add(StompingStegodon.class);
    units.add(ValiantTiger.class);

    // 5
    units.add(LightfeatherScreecher.class);
    units.add(MechanizedGiftHorse.class);
    units.add(Niuzao.class);
    units.add(RaptorElder.class);
    units.add(SilithidBurrower.class);

    //6
    units.add(GoldrinnTheGreatWolf.class);
    units.add(HawkstriderHerald.class);
    units.add(P0ULTR0N.class);

    // UNDEAD
    // 1
    units.add(RisenRider.class);
    
    // DEMONS
    // 1
    units.add(IckyImp.class);
    
    // DRAGONS
    // 4
    units.add(Greenskeeper.class);
    
    // MECH
    // 4
    units.add(AccordoTron.class);
    
    // NAGA
    // 4
    units.add(DeepBlueCrooner.class);

    // ELEMENTAL
    // 2
    units.add(CracklingCyclone.class);

    // NONE
    // 3
    units.add(BirdBuddy.class);

    // 5
    units.add(BrannBronzebeard.class);
    units.add(TitusRivendare.class);
    units.add(DrakkariEnchanter.class);

    // DUMPS
//    units.add(DumpFirst.class);
//    units.add(DumpSecond.class);
//    units.add(DumpThird.class);
//    units.add(DumpFourth.class);
//    units.add(DumpFifth.class);
//    units.add(DumpSixth.class);
  }
  
  static void setupTavernUnits()
  {
    units.forEach(unit -> {
      if (ReflectUtils.getInstance(unit).isTavern())
      {
        tavernUnits.add(unit);
      }
    });
  }
}
