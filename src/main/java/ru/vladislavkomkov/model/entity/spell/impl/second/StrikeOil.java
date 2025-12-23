package ru.vladislavkomkov.model.entity.spell.impl.second;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.player.Player;

public class StrikeOil extends Spell
{
  public StrikeOil()
  {
    this(false);
  }
  
  public StrikeOil(boolean isGold)
  {
    super(isGold);
    isTavern = true;
    description = "Increase your maximum Gold by 1";
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
  public void buildFace(Player player)
  {
    
  }
}
