package ru.vladislavkomkov.model.entity.unit.impl.beast.second;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;

import java.util.List;

import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Type;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.util.UUIDUtils;

public class HungrySnapjaw extends Unit
{
  public static final int ATTACK_BOOST = 1;
  
  public HungrySnapjaw()
  {
    super();
    
    description = "After a friendly Beast dies, gain +1 Health permanently.";
    attack = 4;
    
    level = 2;
    
    maxHealth = 2;
    actualHealth = 2;
    
    type = List.of(Type.BEAST);
    
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, player, entity, index, isTavernIndex, index2, isTavernIndex2) -> {
          player.listener.onDeadListeners.put(
              UUIDUtils.generateKeyTemp(),
              (game1, player1, player2, unit, attacker) -> this.addBuff(new Buff(
                  unit1 -> unit1.incHealth(ATTACK_BOOST),
                  unit1 -> unit1.decHealth(ATTACK_BOOST),
                  "After a friendly Beast dies, gain +1 Health permanently.")));
        });
  }
  
  @Override
  public Unit buildGold(Unit unit1, Unit unit2, Unit unit3)
  {
    Unit gold = super.buildGold(unit1, unit2, unit3);
    
    gold.setDescription("After a friendly Beast dies, gain +2 Health permanently.");
    gold.getListener().onPlayedListeners.put(
        KEY_CORE,
        (game, player, entity, index, isTavernIndex, index2, isTavernIndex2) -> {
          player.listener.onDeadListeners.put(
              UUIDUtils.generateKeyTemp(),
              (game1, player1, player2, unit, attacker) -> this.addBuff(new Buff(
                  unit4 -> unit.incHealth(ATTACK_BOOST * 2),
                  unit4 -> unit.decHealth(ATTACK_BOOST * 2),
                  "After a friendly Beast dies, gain +2 Health permanently.")));
        });
    
    return gold;
  }
}
