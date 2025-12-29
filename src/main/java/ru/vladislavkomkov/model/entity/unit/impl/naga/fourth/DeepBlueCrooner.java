package ru.vladislavkomkov.model.entity.unit.impl.naga.fourth;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import ru.vladislavkomkov.model.entity.spell.impl.spellcraft.impl.DeepBlues;
import ru.vladislavkomkov.model.entity.unit.impl.naga.SpellCrafter;
import ru.vladislavkomkov.model.player.Player;

public class DeepBlueCrooner extends SpellCrafter
{
  public DeepBlueCrooner()
  {
    this(DUMP_PLAYER);
  }
  
  public DeepBlueCrooner(Player playerLink)
  {
    super(playerLink, new DeepBlues(playerLink));
    
    level = 4;
    isTavern = true;
    
    attack = 2;
    
    maxHealth = 3;
    
    actualHealth = getMaxHealth();
  }
}
