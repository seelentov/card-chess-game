package ru.vladislavkomkov.model.entity.spell.impl.fourth;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;
import java.util.Optional;

import ru.vladislavkomkov.model.entity.PlayPair;
import ru.vladislavkomkov.model.entity.PlayType;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;

public class EyesoftheEarthMother extends Spell
{
  public static final int MAX_LEVEL = 4;
  
  public EyesoftheEarthMother()
  {
    this(DUMP_PLAYER, false);
  }
  
  public EyesoftheEarthMother(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public EyesoftheEarthMother(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    
    level = 2;
    isTavern = true;
    
    playType = List.of(new PlayPair(PlayType.FRIENDLY));
  }
  
  @Override
  public void build()
  {
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          Optional<Unit> unit = getUnitFromTavernFriendlyInput(fight, player, input, true);
          if (unit.isEmpty())
          {
            return;
          }
          
          if (unit.get().getLevel() > MAX_LEVEL)
          {
            return;
          }
          
          unit.get().setIsGold(true);
        });
  }
  
  @Override
  public String getDescription()
  {
    return "Choose a friendly minion from Tier " + MAX_LEVEL + " or below. Make it Golden.";
  }
}
