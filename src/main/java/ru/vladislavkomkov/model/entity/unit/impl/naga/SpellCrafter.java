package ru.vladislavkomkov.model.entity.unit.impl.naga;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.model.entity.unit.Type;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

public abstract class SpellCrafter extends Unit
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
        (game, player, entity, index, isTavernIndex, index2, isTavernIndex2, auto) -> addSpellCraft(player));
  }
  
  public SpellCraft getSpellcraft()
  {
    return spellcraft;
  }
  
  void addSpellCraft(Player player)
  {
    if (this.isGold())
    {
      spellcraft.setIsGold(true);
      spellcraft.build();
    }
    
    player.addToHand(Card.of(spellcraft));
  }
}
