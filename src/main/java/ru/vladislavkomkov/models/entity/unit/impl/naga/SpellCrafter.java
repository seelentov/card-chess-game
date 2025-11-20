package ru.vladislavkomkov.models.entity.unit.impl.naga;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.models.card.Card;
import ru.vladislavkomkov.models.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.models.entity.unit.Type;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.player.Player;

public class SpellCrafter extends Unit
{
  protected SpellCraft spellcraft;
  
  public SpellCrafter(SpellCraft spellcraft)
  {
    super();
    description = "Spellcraft: " + spellcraft.getDescription();
    this.spellcraft = spellcraft;
    
    type = List.of(Type.NAGA);
    
    listener.onStartTurnListeners.put(
        KEY_CORE,
        (game, player) -> addSpellCraft(player));
    
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, player, entity, index, isTavernIndex, index2, isTavernIndex2) -> addSpellCraft(player));
  }
  
  public SpellCraft getSpellcraft()
  {
    return spellcraft;
  }
  
  private void addSpellCraft(Player player)
  {
    player.addToHand(Card.of(spellcraft));
  }
}
