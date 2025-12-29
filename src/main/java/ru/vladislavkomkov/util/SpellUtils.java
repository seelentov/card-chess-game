package ru.vladislavkomkov.util;

import java.util.List;

import ru.vladislavkomkov.consts.Spells;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.player.Player;

public class SpellUtils
{
  public static List<Spell> getAll(Player player)
  {
    return get(Spells.spells, player);
  };
  
  public static List<Spell> getTavern(Player player)
  {
    return get(Spells.tavernSpells, player);
  };
  
  private static List<Spell> get(List<Class<? extends Spell>> list, Player player)
  {
    return (List<Spell>) list.stream().map(unit -> {
      try
      {
        return unit.getDeclaredConstructor(Player.class).newInstance(player);
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }).toList();
  }
  
  public static List<Class<? extends Spell>> getByTavern(int lvl, Player player)
  {
    return getByTavern(lvl, true, player);
  };
  
  public static List<Class<? extends Spell>> getByTavern(int lvl, boolean isTavern, Player player)
  {
    List<Class<? extends Spell>> units = isTavern ? Spells.tavernSpells : Spells.spells;
    return getByTavern(lvl, units);
  }
  
  public static List<Class<? extends Spell>> getByTavern(int lvl, List<Class<? extends Spell>> pool)
  {
    return pool.stream().filter(unit -> {
      try
      {
        return unit.getDeclaredConstructor().newInstance().getLevel() == lvl;
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }).toList();
  };
}
