package ru.vladislavkomkov.model.entity.spell.impl.spellcraft;

import java.util.List;

import ru.vladislavkomkov.model.entity.PlayPair;
import ru.vladislavkomkov.model.entity.PlayType;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.player.Player;

public abstract class SpellCraft extends Spell
{
  public SpellCraft(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    level = 0;
    
    playType = List.of(new PlayPair(PlayType.TAVERN_FRENDLY));
  }
}
