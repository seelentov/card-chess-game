package ru.vladislavkomkov.model.entity.spell.impl.spellcraft;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.player.Player;

public abstract class SpellCraft extends Spell
{
  public SpellCraft(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    level = 0;
  }
}
