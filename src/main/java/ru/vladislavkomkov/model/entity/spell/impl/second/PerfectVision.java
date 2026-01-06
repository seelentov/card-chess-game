package ru.vladislavkomkov.model.entity.spell.impl.second;

import ru.vladislavkomkov.model.entity.PlayPair;
import ru.vladislavkomkov.model.entity.PlayType;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

import java.util.List;
import java.util.Optional;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

public class PerfectVision extends Spell
{
  public static final int ATTACK = 20;
  public static final int HEALTH = 20;
  
  public PerfectVision()
  {
    this(DUMP_PLAYER, false);
  }
  
  public PerfectVision(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public PerfectVision(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    
    level = 2;
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


          unit.ifPresent(unit1 -> {
              int attackNeed = ATTACK - unit1.getBaseAttack();
              int healthNeed = HEALTH - unit1.getBaseHealth();


              unit1.addBuff(new Buff(
              unit2 -> {
                unit2.incBaseAttack(attackNeed);
                unit2.incHealth(healthNeed);
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
    return "Set a minion`s stats to " + ATTACK + "/" + HEALTH;
  }
}
