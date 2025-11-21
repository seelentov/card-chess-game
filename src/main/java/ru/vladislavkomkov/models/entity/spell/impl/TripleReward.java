package ru.vladislavkomkov.models.entity.spell.impl;

import java.util.List;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.card.Card;
import ru.vladislavkomkov.models.entity.spell.Spell;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.UnitUtils;

public class TripleReward extends Spell
{
  int unitLvl;
  
  public TripleReward()
  {
    this(0);
  }
  
  public TripleReward(int lvl)
  {
    unitLvl = lvl;
    description = "Discover a minion from Tier " + (lvl);
  }
  
  @Override
  public void onPlayed(Game game, Player player, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2)
  {
      throw new RuntimeException("Not implemented");
//    super.onPlayed(game, player, index, isTavernIndex, index2, isTavernIndex2);
//
//    if (unitLvl <= 0)
//    {
//      return;
//    }
//
//    List<Unit> units = UnitUtils.getUnitsByTavern(unitLvl);
//
//    if (units.isEmpty())
//    {
//      return;
//    }
//    Unit unit = units.get(RandUtils.getRand(units.size() - 1));
//
//    player.addToHand(Card.of(unit));
  }
}