package ru.vladislavkomkov.model.entity.spell.impl.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.player.Player;

public class TavernCoin extends Spell
{
  public TavernCoin()
  {
    this(false);
  }
  
  public TavernCoin(boolean isGold)
  {
    super(isGold);
    isTavern = true;
    description = "Gain 1 Gold";
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
  public void buildFace(Player player)
  {
    
  }
}
