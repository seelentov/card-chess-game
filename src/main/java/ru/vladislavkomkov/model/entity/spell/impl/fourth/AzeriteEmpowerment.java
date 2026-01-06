package ru.vladislavkomkov.model.entity.spell.impl.fourth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

public class AzeriteEmpowerment extends Spell
{
  public static final int ATTACK_BOOST = 2;
  public static final int HEALTH_BOOST = 2;
  
  public AzeriteEmpowerment()
  {
    this(DUMP_PLAYER, false);
  }
  
  public AzeriteEmpowerment(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public AzeriteEmpowerment(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    isTavern = true;
    
    playType = List.of();
  }
  
  @Override
  public void build()
  {
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          List<Unit> units = fight != null ? fight.getFightTable(player) : player.getTable();
          
          for (int i = 0; i < 2; i++)
          {
            units.forEach(unit -> {
              unit.addBuff(new Buff(
                  unit1 -> {
                    unit1.incBaseAttack(getAttackBoost());
                    unit1.incHealth(getHealthBoost());
                  },
                  null,
                  getDescription(),
                  getID()));
            });
          }
        });
  }
  
  @Override
  public String getDescription()
  {
    return "Give your minions +" + getAttackBoost() + "/+" + getHealthBoost() + " twice";
  }
  
  @Override
  protected int getAttackBoost()
  {
    return super.getAttackBoost() + ATTACK_BOOST;
  }
  
  @Override
  protected int getHealthBoost()
  {
    return super.getHealthBoost() + HEALTH_BOOST;
  }
}
