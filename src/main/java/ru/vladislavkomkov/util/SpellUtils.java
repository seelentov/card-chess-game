package ru.vladislavkomkov.util;

import java.util.List;

import ru.vladislavkomkov.consts.Spells;
import ru.vladislavkomkov.model.entity.spell.Spell;

public class SpellUtils
{
  public static List<Spell> getAll()
  {
    return get(Spells.spells);
  };
  
  public static List<Spell> getTavern()
  {
    return get(Spells.tavernSpells);
  };
  
  private static List<Spell> get(List<Class<? extends Spell>> list)
  {
    return (List<Spell>) list.stream().map(ReflectUtils::getInstance).toList();
  }
  
  public static List<Class<? extends Spell>> getByTavern(int lvl)
  {
    return getByTavern(lvl, true);
  };
  
  public static List<Class<? extends Spell>> getByTavern(int lvl, boolean isTavern)
  {
    List<Class<? extends Spell>> units = isTavern ? Spells.tavernSpells : Spells.spells;
    return getByTavern(lvl, units);
  }
  
  public static List<Class<? extends Spell>> getByTavern(int lvl, List<Class<? extends Spell>> pool)
  {
    return pool.stream().filter(unit -> ReflectUtils.getInstance(unit).getLevel() == lvl).toList();
  };
}
