package ru.vladislavkomkov.model.entity.spell.impl.second;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.player.Player;

import static java.util.List.of;
import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

public class SaloonFinest extends Spell
{
  public SaloonFinest()
  {
    this(DUMP_PLAYER, false);
  }
  
  public SaloonFinest(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public SaloonFinest(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    isTavern = true;
    
    playType = of();
  }
  
  @Override
  public void build()
  {
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          player.getTavern().getCards().clear();
          for (int i = 0; i < player.getTavern().getCountByLevel(player.getLevel()); i++)
          {
            player.getTavern().addRandomSpell(player.getLevel());
          }
        });
  }
  
  @Override
  public String getDescription()
  {
    return "Refresh the Tavern with Tavern spells";
  }
}