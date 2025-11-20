package ru.vladislavkomkov.models.entity.spell;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.Entity;
import ru.vladislavkomkov.models.player.Player;

public abstract class Spell extends Entity
{
  @Override
  public void onPlayed(Game game, Player player, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2)
  {
    super.onPlayed(game, player, index, isTavernIndex, index2, isTavernIndex2);
  }
}
