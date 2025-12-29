package ru.vladislavkomkov.model.entity.spell.impl.second;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.player.Player;

public class StrikeOil extends Spell
{
  public StrikeOil()
  {
    this(DUMP_PLAYER, false);
  }
  
  public StrikeOil(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public StrikeOil(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    isTavern = true;
  }
  
  @Override
  public void build()
  {
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          player.incMaxMoney();
        });
  }
  
  @Override
  public String getDescription()
  {
    return "Increase your maximum Gold by 1";
  }
}
