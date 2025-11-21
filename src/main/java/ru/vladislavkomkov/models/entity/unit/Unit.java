package ru.vladislavkomkov.models.entity.unit;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.Entity;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.util.ListenerUtils;

public abstract class Unit extends Entity
{
  protected final List<Buff> buffs = new ArrayList<>();
  protected int attack = 0;
  protected int maxHealth = 1;
  protected List<Type> type = new ArrayList<>();
  protected boolean isBubbled = false;
  protected boolean isTaunt = false;
  protected boolean isRebirth = false;
  protected boolean isDoubleAttack = false;
  protected boolean isDisguise = false;
  protected boolean answerOnPlayed = false;
  protected boolean answerOnDead = false;
  protected int actualHealth = 1;
  
  public Unit()
  {
    super();
    
    listener.onSellListeners.put(
        ListenerUtils.generateKey(),
        (game, player, entity) -> {
          player.listener.removeListener(this);
          
          player.addMoney(1);
          player.removeFromTable(this);
          
          processListeners(player.listener.onSellListeners, (action) -> action.process(game, player, this), player);
        });
    
    listener.onAttackedListeners.put(
        ListenerUtils.generateKey(),
        (game, player, player2, unit, attacker) -> {
          processListeners(player.listener.onAttackedListeners, (action) -> action.process(game, player, player2, this, attacker), player);
          if (isBubbled)
          {
            isBubbled = false;
          }
          else
          {
            this.actualHealth -= attacker.getAttack();
          }
        });
    
    listener.onAttackListeners.put(
        ListenerUtils.generateKey(),
        (game, player, player2, unit, attacked) -> {
          processListeners(player.listener.onAttackListeners, (action) -> action.process(game, player, player2, this, attacked), player);
          if (isBubbled)
          {
            isBubbled = false;
          }
          else
          {
            this.actualHealth -= attacked.getAttack();
          }
        });
    
    listener.onDeadListeners.put(
        ListenerUtils.generateKey(),
        (game, player, player2, unit, attacker) -> {
          if (isRebirth)
          {
            isRebirth = false;
            actualHealth = 1;
          }
          processListeners(player.listener.onDeadListeners, (action) -> action.process(game, player, player2, this, attacker), player);
        });
  }
  
  public int getAttack()
  {
    return attack;
  }
  
  public void setAttack(int i)
  {
    attack = i;
  }
  
  public boolean isDead()
  {
    return actualHealth < 1;
  }
  
  public void onSell(Game game, Player player)
  {
    listener.processOnSellListeners(game, player, this);
  }
  
  public void onStartTurn(Game game, Player player)
  {
    listener.processOnStartTurnListeners(game, player);
    
  }
  
  public void onEndTurn(Game game, Player player)
  {
    listener.processOnEndTurnListeners(game, player);
    
  }
  
  public void onStartFight(Game game, Player player, Player player2)
  {
    listener.processOnStartFightListeners(game, player);
  }
  
  public void onEndFight(Game game, Player player, Player player2)
  {
    listener.processOnEndFightListeners(game, player);
  }
  
  public void onAttacked(Game game, Player player, Player player2, Unit attacker)
  {
    listener.processOnAttackedListeners(game, player, player2, this, attacker);
  }
  
  public void onAttack(Game game, Player player, Player player2, Unit attacked)
  {
    listener.processOnAttackListeners(game, player, player2, this, attacked);
  }
  
  public void onDead(Game game, Player player, Player player2, Unit attacker)
  {
    listener.processOnDeadListeners(game, player, player2, this, attacker);
  }
  
  public int getHealth()
  {
    return actualHealth;
  }
  
  public void setHealth(int i)
  {
    maxHealth = i;
    actualHealth = i;
  }
  
  public int getMaxHealth()
  {
    return maxHealth;
  }
  
  public void incHealth(int i)
  {
    maxHealth += i;
    actualHealth += i;
  }
  
  public void decHealth(int i)
  {
    maxHealth -= i;
    actualHealth -= i;
  }
  
  public void applyDamage(int i)
  {
    actualHealth -= i;
  }
  
  public void incAttack(int i)
  {
    attack += i;
  }
  
  public void decAttack(int i)
  {
    attack -= i;
  }
  
  public void setIsBubbled(boolean bubbled)
  {
    isBubbled = bubbled;
  }
  
  public boolean getIsTaunt()
  {
    return isTaunt;
  }
  
  public void setIsTaunt(boolean isTaunt)
  {
    this.isTaunt = isTaunt;
  }
  
  public boolean getIsDisguise()
  {
    return isDisguise;
  }
  
  public void setIsDisguise(boolean disguise)
  {
    isDisguise = disguise;
  }
  
  public void kill()
  {
    actualHealth = 0;
  }
  
  public boolean getIsRebirth()
  {
    return isRebirth;
  }
  
  public boolean isAnswerOnPlayed()
  {
    return answerOnPlayed;
  }
  
  public boolean isAnswerOnDead()
  {
    return answerOnDead;
  }
  
  public List<Buff> getBuffs()
  {
    return buffs;
  }
  
  public void addBuff(Buff buff)
  {
    buff.getUpgrade().accept(this);
    buffs.add(buff);
  }
  
  public void removeTempBuffs()
  {
    buffs.stream()
        .filter(buff -> buff.getRollback() != null)
        .forEach(buff -> buff.getRollback().accept(this));
    
    buffs.removeIf(buff -> buff.getRollback() != null);
  }
  
  public void removeCoreListeners()
  {
    listener.removeCoreListener();
  }
  
}