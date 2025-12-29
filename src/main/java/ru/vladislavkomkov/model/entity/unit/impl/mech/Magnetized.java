package ru.vladislavkomkov.model.entity.unit.impl.mech;

import java.util.List;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.player.Player;

public abstract class Magnetized extends Unit
{
  public Magnetized(Player playerLink)
  {
    super(playerLink);
  }
  
  @Override
  public void onPlayed(Game game, Fight fight, Player player, List<Integer> input, boolean auto)
  {
    super.onPlayed(game, fight, player, input, auto);
    if (fight != null)
    {
      return;
    }
    
    if (input.isEmpty())
    {
      return;
    }
    
    int rightIndex = input.get(0) + 1;
    
    if (player.getTable().size() > rightIndex)
    {
      Unit rightUnit = player.getTable().get(rightIndex);
      if (this.isType(rightUnit.getType()))
      {
        if (this.isTaunt)
        {
          rightUnit.setIsTaunt(true);
        }
        
        if (this.isBubbled)
        {
          rightUnit.setIsBubbled(true);
        }
        
        if (this.isRebirth)
        {
          rightUnit.setIsRebirth(true);
        }
        
        if (this.isMagnet)
        {
          rightUnit.setIsMagnet(true);
        }
        
        if (this.isDisguise)
        {
          rightUnit.setIsDisguise(true);
        }
        
        if (this.isAnswerOnPlayed)
        {
          rightUnit.setIsAnswerOnPlayed(true);
        }
        
        if (this.getAttacksCount().toInt() > rightUnit.getAttacksCount().toInt())
        {
          rightUnit.setAttacksCount(this.getAttacksCount());
        }
        
        if (this.isAnswerOnDead)
        {
          rightUnit.setIsAnswerOnDead(true);
        }
        
        rightUnit.incBaseAttack(this.newThis().getBaseAttack());
        rightUnit.incHealth(this.newThis().getBaseHealth());
        
        rightUnit.getListener().push(getListener(), true);
        
        rightUnit.addBuff(this.getBuffs());
        
        player.getTable().removeIf(unit -> unit == this);
      }
    }
  }
}
