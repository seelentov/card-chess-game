package ru.vladislavkomkov.model.entity.spell.impl;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.ReflectUtils;
import ru.vladislavkomkov.util.UnitUtils;

public class TripleReward extends Spell
{
  int unitLvl;
  
  public TripleReward()
  {
    this(DUMP_PLAYER);
  }
  
  public TripleReward(Player playerLink)
  {
    this(playerLink, 0);
  }
  
  public TripleReward(Player playerLink, int lvl)
  {
    super(playerLink, true);
    unitLvl = lvl;
  }
  
  @Override
  public void build()
  {
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          List<Class<? extends Unit>> allUnits = UnitUtils.getByTavern(player.getLevel(), player.getTavern().getUnitsPool());
          
          if (allUnits.isEmpty())
          {
            return;
          }
          
          while (allUnits.size() < 3)
          {
            allUnits = new ArrayList<>(allUnits);
            allUnits.add(allUnits.get(0));
          }
          
          Set<Integer> setInts = new HashSet<>();
          while (setInts.size() < 3)
          {
            setInts.add(RandUtils.getRand(allUnits.size()));
          }
          
          List<Integer> ints = setInts.stream().toList();
          
          List<Class<? extends Unit>> unitClasses = List.of(
              allUnits.get(ints.get(0)),
              allUnits.get(ints.get(1)),
              allUnits.get(ints.get(2)));
          
          List<Card> units = unitClasses.stream()
              .map(unitClass -> ReflectUtils.getInstance(unitClass, playerLink))
              .map(unit -> (Card) Card.of(unit))
              .toList();
          
          player.putSenderWaiter((param) -> {
            if (param < 0 || param > 3)
            {
              param = RandUtils.getRand(1, 3);
            }
            
            try
            {
              player.addToHand(units.get(param));
            }
            catch (Exception e)
            {
              throw new RuntimeException(e);
            }
          }, units);
        });
  }
  
  @Override
  public String getDescription()
  {
    return "Discover a minion from Tier " + unitLvl;
  }
}