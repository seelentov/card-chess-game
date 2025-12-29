package ru.vladislavkomkov.model.entity.unit.impl.naga;

import java.util.List;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitType;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.player.Player;

public abstract class SpellCrafter extends Unit
{
  protected SpellCraft spellcraft;
  
  public SpellCrafter(Player playerLink, SpellCraft spellcraft)
  {
    super(playerLink);

    this.spellcraft = spellcraft;
    
    unitType = List.of(UnitType.NAGA);
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
    
    player.addToHand(Card.of(spellcraft), true);
  }
  
  @Override
  public void onStartTurn(Game game, Fight fight, Player player)
  {
    super.onStartTurn(game, fight, player);
    addSpellCraft(player);
  }
  
  @Override
  public void onPlayed(Game game, Fight fight, Player player, List<Integer> input, boolean auto)
  {
    super.onPlayed(game, fight, player, input, auto);
    addSpellCraft(player);
  }
  
  @Override
  public String getDescription()
  {
    return "Spellcraft: " + spellcraft.getDescription();
  }
}
