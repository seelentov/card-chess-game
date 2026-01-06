package ru.vladislavkomkov.model.entity.spell.impl.first;

import static java.util.List.of;
import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

public class Sanctify extends Spell
{
  public static final int ATTACK_BOOST = 6;
  
  public Sanctify()
  {
    this(DUMP_PLAYER, false);
  }
  
  public Sanctify(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public Sanctify(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    isTavern = true;
    
    playType = of();
  }
  
  @Override
  public void build()
  {
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          List<Unit> units = fight != null ? fight.getFightTable(player) : player.getTable();
          
          units.stream().filter(Unit::isBubbled).forEach(unit -> {
            unit.addBuff(new Buff(
                unit1 -> {
                  unit1.incBaseAttack(getAttackBoost());
                },
                null,
                getDescription(),
                getID()));
          });
        });
  }
  
  @Override
  public String getDescription()
  {
    return "Give your minions with Divine Shield +" + getAttackBoost() + " Attack";
  }
  
  @Override
  public int getAttackBoost()
  {
    return super.getAttackBoost() + ATTACK_BOOST;
  }
}
