package ru.vladislavkomkov.model.entity.spell.impl;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;
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
    super(true);
    unitLvl = lvl;
    description = "Discover a minion from Tier " + (lvl);
  }
  
  @Override
  public void build()
  {
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          List<Unit> allUnits = UnitUtils.getByTavern(player.getLevel(), player.getTavern().getUnitsPool());
          
          if (allUnits.isEmpty())
          {
            throw new RuntimeException("Units list for reward is empty");
          }
          
          while (allUnits.size() < 3)
          {
            allUnits = new ArrayList<>(allUnits);
            allUnits.add(allUnits.get(0).newBase());
          }
          
          Set<Integer> setInts = new HashSet<>();
          while (setInts.size() < 3)
          {
            setInts.add(RandUtils.getRand(allUnits.size()));
          }
          
          List<Integer> ints = setInts.stream().toList();
          
          List<Unit> units = List.of(
              allUnits.get(ints.get(0)),
              allUnits.get(ints.get(1)),
              allUnits.get(ints.get(2)));
          
          player.putSenderWaiter((param) -> {
            if (param < 0 || param > 3)
            {
              param = RandUtils.getRand(1, 3);
            }
            
            player.addToHand(Card.of(units.get(param)));
          }, units);
        });
  }
  
  @Override
  public void buildFace(Player player)
  {
    
  }
}