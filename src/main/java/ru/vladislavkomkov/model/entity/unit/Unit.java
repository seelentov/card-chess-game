package ru.vladislavkomkov.model.entity.unit;

import java.util.ArrayList;
import java.util.List;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.spell.impl.TripleReward;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

public abstract class Unit extends Entity
{
  protected final List<Buff> buffs = new ArrayList<>();
  protected int attack = 0;
  protected int maxHealth = 1;
  protected List<Type> type = new ArrayList<>();
  protected boolean isBubbled = false;
  protected boolean isTaunt = false;
  protected boolean isRebirth = false;
  protected boolean isMagnet = false;
  protected boolean isDisguise = false;
  protected boolean answerOnPlayed = false;
  protected boolean answerOnDead = false;
  protected int actualHealth = 1;
  
  public Unit()
  {
    super();
    
    listener.onSellListeners.put(
        UUIDUtils.generateKey(),
        (game, player, entity) -> {
          player.listener.removeListener(this);
          
          player.addMoney(1);
          player.removeFromTable(null, this);
          
          processListeners(player.listener.onSellListeners, (action) -> action.process(game, player, this), player);
        });
    
    listener.onAppearListeners.put(
        UUIDUtils.generateKey(),
        (game, player, entity) -> {
          processListeners(player.listener.onAppearListeners, (action) -> action.process(game, player, this), player);
        });
    
    listener.onDisappearListeners.put(
        UUIDUtils.generateKey(),
        (game, player, entity) -> {
          processListeners(player.listener.onDisappearListeners, (action) -> action.process(game, player, this), player);
        });
    
    listener.onAttackedListeners.put(
        UUIDUtils.generateKey(),
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
        UUIDUtils.generateKey(),
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
        UUIDUtils.generateKey(),
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
  
  public void magnetize(Unit unit)
  {
    if (!unit.isType(Type.MECH) || type.stream().noneMatch(unit::isType))
    {
      return;
    }
    
    unit.getBuffs().forEach(this::addBuff);
    Unit u = unit.isGold() ? unit.newGold() : unit.newThis();
    this.incAttack(u.getAttack());
    this.incHealth(u.getHealth());
    this.addBuff(new Buff(
        unit1 -> {
          listener.push(unit1.listener.newCoreListener(false));
        },
        null,
        unit.getDescription()));
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
  
  @Override
  public void onPlayed(Game game, Player player, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2)
  {
    super.onPlayed(game, player, index, isTavernIndex, index2, isTavernIndex2);
  }
  
  public void onAppear(Game game, Player player)
  {
    listener.processOnAppearListeners(game, player, this);
  }
  
  public void onDisappear(Game game, Player player)
  {
    listener.processOnDisappearListeners(game, player, this);
  }
  
  public void onAppearEnemy(Game game, Player player, Player player2)
  {
    listener.processOnAppearListeners(game, player, this);
  }
  
  public void onDisappearEnemy(Game game, Player player, Player player2)
  {
    listener.processOnDisappearListeners(game, player, this);
  }
  
  @Override
  public void onHandled(Game game, Player player)
  {
    super.onHandled(game, player);
  }
  
  @Override
  public void onPlayed(Game game, Player player, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2, boolean auto)
  {
    super.onPlayed(game, player, index, isTavernIndex, index2, isTavernIndex2, auto);
    if (this.isGold())
    {
      player.addToHand(Card.of(new TripleReward(player.getLevel() + 1)));
    }
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
  
  public Unit buildGold()
  {
    return buildGold(this.newThis(), this.newThis(), this.newThis());
  }
  
  public Unit buildGold(Unit unit)
  {
    return buildGold(unit.newThis(), unit.newThis(), unit.newThis());
  }
  
  public Unit buildGold(List<Unit> units)
  {
    if (units.size() != 3)
    {
      throw new RuntimeException("Need 3 copy of card");
    }
    
    return buildGold(units.get(0), units.get(1), units.get(2));
  }
  
  public Unit buildGold(Unit unit, Unit unit2, Unit unit3)
  {
    Unit entity = this.newThis();
    entity.setAttack(entity.getAttack() * 2);
    entity.setHealth(entity.getHealth() * 2);
    
    List<Unit> units = List.of(unit, unit2, unit3);
    units.stream()
        .map(Unit::getBuffs)
        .flatMap(List::stream)
        .forEach(entity::addBuff);
    
    entity.setIsGold(true);
    return entity;
  };
  
  public Unit newThis()
  {
    return (Unit) super.newThis();
  }
  
  public Unit newGold()
  {
    Unit u = (Unit) this.newThis();
    
    u.setHealth(u.getHealth() * 2);
    u.setAttack(u.getAttack() * 2);
    u.setIsGold(true);
    
    return u;
  }
  
  public boolean isType(Type type)
  {
    return this.type.contains(type);
  }
}