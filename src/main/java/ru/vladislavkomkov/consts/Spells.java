package ru.vladislavkomkov.consts;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.spell.impl.first.*;
import ru.vladislavkomkov.model.entity.spell.impl.fourth.AzeriteEmpowerment;
import ru.vladislavkomkov.model.entity.spell.impl.fourth.EyesoftheEarthMother;
import ru.vladislavkomkov.model.entity.spell.impl.second.LostStaffofHamuul;
import ru.vladislavkomkov.model.entity.spell.impl.second.PerfectVision;
import ru.vladislavkomkov.model.entity.spell.impl.second.SaloonFinest;
import ru.vladislavkomkov.model.entity.spell.impl.second.StrikeOil;
import ru.vladislavkomkov.model.entity.spell.impl.third.ArmorStash;
import ru.vladislavkomkov.model.entity.spell.impl.third.ContractedCorpse;
import ru.vladislavkomkov.model.entity.spell.impl.third.HiredHeadhunter;
import ru.vladislavkomkov.model.entity.spell.impl.third.UpperHand;
import ru.vladislavkomkov.util.ReflectUtils;

public class Spells
{
  public static List<Class<? extends Spell>> spells = new ArrayList<>();
  public static List<Class<? extends Spell>> tavernSpells = new ArrayList<>();
  
  static
  {
    setup();
    setupTavern();
  }
  
  static void setup()
  {
    // 1
    spells.add(TavernCoin.class);
    spells.add(Fortify.class);
    spells.add(PointyArrow.class);
    spells.add(Sanctify.class);
    spells.add(TavernDishBanana.class);
    spells.add(ThemApples.class);

    // 2
    spells.add(StrikeOil.class);
    spells.add(PerfectVision.class);
    spells.add(SaloonFinest.class);
    spells.add(LostStaffofHamuul.class);

    // 3
    spells.add(ArmorStash.class);
    spells.add(ContractedCorpse.class);
    spells.add(HiredHeadhunter.class);
    spells.add(UpperHand.class);

    // 4
    spells.add(AzeriteEmpowerment.class);
    spells.add(EyesoftheEarthMother.class);
  }
  
  static void setupTavern()
  {
    spells.forEach(unit -> {
      if (ReflectUtils.getInstance(unit).isTavern())
      {
        tavernSpells.add(unit);
      }
    });
  }
}
