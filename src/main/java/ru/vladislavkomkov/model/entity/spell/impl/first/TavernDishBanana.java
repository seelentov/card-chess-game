package ru.vladislavkomkov.model.entity.spell.impl.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.model.entity.PlayPair;
import ru.vladislavkomkov.model.entity.PlayType;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

public class TavernDishBanana extends Spell
{
  public static final int ATTACK_BOOST = 2;
  public static final int HEALTH_BOOST = 2;
  
  public TavernDishBanana()
  {
    this(DUMP_PLAYER, false);
  }
  
  public TavernDishBanana(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public TavernDishBanana(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    isTavern = true;
    
    playType = List.of(new PlayPair(PlayType.TAVERN_FRIENDLY));
  }
  
  @Override
  public void build()
  {
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          Optional<Unit> unit = getUnitFromTavernFriendlyInput(fight, player, input);
          unit.ifPresent(unit1 -> unit1.addBuff(new Buff(
              unit2 -> {
                unit2.incBaseAttack(getAttackBoost());
                unit2.incHealth(getHealthBoost());
              },
              null,
              getDescription(),
              getID())));
              
        });
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
  
  @Override
  public String getDescription()
  {
    return "Give a minion +" + getAttackBoost() + "/+" + getHealthBoost();
  }
}
