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

public class Fortify extends Spell
{
  public static final int HEALTH_BOOST = 3;
  
  public Fortify()
  {
    this(DUMP_PLAYER, false);
  }
  
  public Fortify(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public Fortify(Player playerLink, boolean isGold)
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
                unit2.incHealth(getHealthBoost());
                unit2.setIsTaunt(true);
              },
              null,
              getDescription(),
              getID())));
              
        });
  }
  
  @Override
  protected int getHealthBoost()
  {
    return super.getHealthBoost() + HEALTH_BOOST;
  }
  
  @Override
  public String getDescription()
  {
    return "Give a minion +" + getHealthBoost() + " Health and Taunt";
  }
}
