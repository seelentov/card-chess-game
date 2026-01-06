package ru.vladislavkomkov.model.entity.spell.impl.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.player.Player;

public class TavernCoin extends Spell
{
  public TavernCoin()
  {
    this(DUMP_PLAYER, false);
  }
  
  public TavernCoin(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public TavernCoin(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    isTavern = true;

    playType = List.of();
  }
  
  @Override
  public void build()
  {
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          player.addMoney();
        });
  }
  
  @Override
  public String getDescription()
  {
    return "Gain 1 Gold";
  }
}
