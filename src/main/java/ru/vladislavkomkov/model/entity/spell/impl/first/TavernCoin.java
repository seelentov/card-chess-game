package ru.vladislavkomkov.model.entity.spell.impl.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import ru.vladislavkomkov.model.entity.spell.Spell;

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
        (game, player, entity, index, isTavernIndex, index2, isTavernIndex2, auto) -> {
          player.addMoney();
        });
  }
}
