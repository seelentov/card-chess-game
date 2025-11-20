package ru.vladislavkomkov.models.entity.spell.impl.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import ru.vladislavkomkov.models.entity.spell.Spell;

public class TavernCoin extends Spell
{
  public TavernCoin()
  {
    isTavern = true;
    description = "Gain 1 Gold";
    
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, player, entity, index, isTavernIndex, index2, isTavernIndex2) -> {
          player.addMoney();
        });
  }
}
