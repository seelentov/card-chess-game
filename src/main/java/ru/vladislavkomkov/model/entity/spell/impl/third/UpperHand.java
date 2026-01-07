package ru.vladislavkomkov.model.entity.spell.impl.third;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.View;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.unit.Buff;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.RandUtils;
import ru.vladislavkomkov.util.UUIDUtils;

public class UpperHand extends Spell
{
  public static final int HEALTH = 1;
  
  public UpperHand()
  {
    this(DUMP_PLAYER, false);
  }
  
  public UpperHand(Player playerLink)
  {
    this(playerLink, false);
  }
  
  public UpperHand(Player playerLink, boolean isGold)
  {
    super(playerLink, isGold);
    isTavern = true;
    
    level = 3;
    
    playType = List.of();
  }
  
  @Override
  public void build()
  {
    listener.onPlayedListeners.put(
        KEY_CORE,
        (game, fight, player, entity, input, auto) -> {
          player.addOnStartFightActionsView(new UpperHandView());
          
          player.getListener().onStartFightListeners.put(
              UUIDUtils.generateKeyOnce(),
              (game1, fight1, player1, player2) -> {
                if (fight1 == null)
                {
                  return;
                }
                
                List<Unit> units = fight1.getFightTable(player2);
                units.get(RandUtils.getRand(units.size() - 1)).addBuff(new Buff(
                    unit -> unit.setHealth(HEALTH),
                    null,
                    getDescription()));
              });
        });
  }
  
  @Override
  public String getDescription()
  {
    return "Start of Combat: Set a random enemy minions Health to " + HEALTH;
  }
  
  public static class UpperHandView extends View
  {
    public UpperHandView()
    {
      
    }
    
    @Override
    public String getDescription()
    {
      return new UpperHand().getDescription();
    }
  }
}
