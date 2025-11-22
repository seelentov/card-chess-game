package ru.vladislavkomkov.model.entity.spell.impl.spellcraft;

import ru.vladislavkomkov.model.entity.spell.Spell;

public abstract class SpellCraft extends Spell
{
  public SpellCraft(boolean isGold)
  {
    super(isGold);
    level = 0;
  }
}
