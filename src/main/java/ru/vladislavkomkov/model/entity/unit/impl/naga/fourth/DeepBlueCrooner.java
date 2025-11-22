package ru.vladislavkomkov.model.entity.unit.impl.naga.fourth;

import ru.vladislavkomkov.model.entity.spell.impl.spellcraft.impl.DeepBlues;
import ru.vladislavkomkov.model.entity.unit.impl.naga.SpellCrafter;
import ru.vladislavkomkov.model.player.Player;

public class DeepBlueCrooner extends SpellCrafter
{
  public DeepBlueCrooner()
  {
    this(null);
  }
  
  public DeepBlueCrooner(Player player)
  {
    super(new DeepBlues());
    
    level = 4;
    isTavern = true;
    
    attack = 2;
    
    maxHealth = 3;
    actualHealth = 3;
  }
}
