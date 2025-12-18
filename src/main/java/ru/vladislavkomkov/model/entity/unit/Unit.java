package ru.vladislavkomkov.model.entity.unit;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.model.Fight;
import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.spell.impl.TripleReward;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

public abstract class Unit extends Entity
{
  public final static String F_ATTACK = "attack";
  
  public final static String F_HEALTH = "health";
  public final static String F_MAX_HEALTH = "max_health";
  
  public final static String F_IS_BUBBLED = "is_bubbled";
  public final static String F_IS_TAUNT = "is_taunt";
  public final static String F_IS_REBIRTH = "is_rebirth";
  public final static String F_IS_MAGNET = "is_magnet";
  public final static String F_IS_DISGUISE = "is_disguise";
  
  public final static String F_BUFFS = "buffs";
  
  List<Buff> buffs = new ArrayList<>();
  protected List<Type> type = new ArrayList<>();
  
  protected int attack = 0;
  protected int maxHealth = 1;
  protected int actualHealth = 1;
  
  protected boolean isBubbled = false;
  protected boolean isTaunt = false;
  protected boolean isRebirth = false;
  protected boolean isMagnet = false;
  protected boolean isDisguise = false;
  
  protected boolean isAnswerOnPlayed = false;
  protected boolean isAnswerOnDead = false;
  
  public Unit()
  {
    super();
    
    listener.onSellListeners.put(
        UUIDUtils.generateKey(),
        (game, fight, player, entity) -> {
          player.listener.removeListener((Unit) entity);
          player.addMoney(1);
          player.removeFromTable((Unit) entity);
          processListeners(player.listener.onSellListeners, (action) -> action.process(game, null, player, entity), player);
        });
    
    listener.onAppearListeners.put(
        UUIDUtils.generateKey(),
        (game, fight, player, entity) -> {
          processListeners(player.listener.onAppearListeners, (action) -> action.process(game, null, player, entity), player);
        });
    
    listener.onDisappearListeners.put(
        UUIDUtils.generateKey(),
        (game, fight, player, entity) -> {
          processListeners(player.listener.onDisappearListeners, (action) -> action.process(game, null, player, entity), player);
        });
    
    listener.onAttackedListeners.put(
        UUIDUtils.generateKey(),
        (game, fight, player, player2, unit, attacker) -> {
          processListeners(player.listener.onAttackedListeners, (action) -> action.process(game, null, player, player2, unit, attacker), player);
          if (unit.isBubbled)
          {
            unit.isBubbled = false;
          }
          else
          {
            unit.actualHealth -= attacker.getAttack();
          }
        });
    
    listener.onAttackListeners.put(
        UUIDUtils.generateKey(),
        (game, fight, player, player2, unit, attacked) -> {
          processListeners(player.listener.onAttackListeners, (action) -> action.process(game, null, player, player2, unit, attacked), player);
          if (unit.isBubbled)
          {
            unit.isBubbled = false;
          }
          else
          {
            unit.actualHealth -= attacked.getAttack();
          }
        });
    
    listener.onDeadListeners.put(
        UUIDUtils.generateKey(),
        (game, fight, player, player2, unit, attacker) -> {
          processListeners(player.listener.onDeadListeners, (action) -> action.process(game, null, player, player2, unit, attacker), player);
          List table = fight != null ? fight.getFightTable(player) : player.getTable();
          if (unit.isRebirth)
          {
            unit.isRebirth = false;
            if (table.size() < Player.TABLE_LIMIT)
            {
              unit.actualHealth = 1;
            }
          }
        });
  }
  
  @JsonProperty(F_ATTACK)
  public int getAttack()
  {
    return attack;
  }
  
  public void setAttack(int i)
  {
    attack = i;
  }
  
  public void incAttack(int i)
  {
    attack += i;
  }
  
  public void decAttack(int i)
  {
    attack -= i;
  }
  
  @JsonProperty(F_HEALTH)
  public int getHealth()
  {
    return actualHealth;
  }
  
  public void setHealth(int i)
  {
    maxHealth = i;
    actualHealth = i;
  }
  
  @JsonProperty(F_MAX_HEALTH)
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
  
  public boolean isDead()
  {
    return actualHealth < 1;
  }
  
  public void kill()
  {
    actualHealth = 0;
  }
  
  @JsonProperty(F_IS_TAUNT)
  public boolean isTaunt()
  {
    return isTaunt;
  }
  
  public void setIsTaunt(boolean isTaunt)
  {
    this.isTaunt = isTaunt;
  }
  
  @JsonProperty(F_IS_DISGUISE)
  public boolean isDisguise()
  {
    return isDisguise;
  }
  
  public void setIsDisguise(boolean disguise)
  {
    isDisguise = disguise;
  }
  
  @JsonProperty(F_IS_MAGNET)
  public boolean isMagnet()
  {
    return isMagnet;
  }
  
  @JsonProperty(F_IS_BUBBLED)
  public boolean isBubbled()
  {
    return isBubbled;
  }
  
  public void setIsBubbled(boolean isBubbled)
  {
    this.isBubbled = isBubbled;
  }
  
  @JsonProperty(F_IS_REBIRTH)
  public boolean isRebirth()
  {
    return isRebirth;
  }
  
  public boolean isAnswerOnPlayed()
  {
    return isAnswerOnPlayed;
  }
  
  public boolean isAnswerOnDead()
  {
    return isAnswerOnDead;
  }
  
  @JsonProperty(F_BUFFS)
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
        unit1 -> listener.push(unit1.listener.newCoreListener(false)),
        null,
        unit.getDescription()));
  }
  
  public void removeCoreListeners()
  {
    listener.removeCoreListener();
  }
  
  public void onSell(Game game, Fight fight, Player player)
  {
    listener.processOnSellListeners(game, fight, player, this);
  }
  
  public void onStartTurn(Game game, Fight fight, Player player)
  {
    listener.processOnStartTurnListeners(game, fight, player);
  }
  
  public void onEndTurn(Game game, Fight fight, Player player)
  {
    listener.processOnEndTurnListeners(game, fight, player);
  }
  
  public void onStartFight(Game game, Fight fight, Player player, Player player2)
  {
    listener.processOnStartFightListeners(game, fight, player, player2);
  }
  
  public void onEndFight(Game game, Fight fight, Player player, Player player2)
  {
    listener.processOnEndFightListeners(game, fight, player, player2);
  }
  
  public void onAttacked(Game game, Fight fight, Player player, Player player2, Unit attacker)
  {
    listener.processOnAttackedListeners(game, fight, player, player2, this, attacker);
  }
  
  public void onAttack(Game game, Fight fight, Player player, Player player2, Unit attacked)
  {
    listener.processOnAttackListeners(game, fight, player, player2, this, attacked);
  }
  
  public void onDead(Game game, Fight fight, Player player, Player player2, Unit attacker)
  {
    listener.processOnDeadListeners(game, fight, player, player2, this, attacker);
  }
  
  public void onAppear(Game game, Fight fight, Player player)
  {
    listener.processOnAppearListeners(game, fight, player, this);
  }
  
  public void onDisappear(Game game, Fight fight, Player player)
  {
    listener.processOnDisappearListeners(game, fight, player, this);
  }
  
  public void onAppearEnemy(Game game, Fight fight, Player player, Player player2)
  {
    listener.processOnAppearListeners(game, fight, player, this);
  }
  
  public void onDisappearEnemy(Game game, Fight fight, Player player, Player player2)
  {
    listener.processOnDisappearListeners(game, fight, player, this);
  }
  
  @Override
  public void onPlayed(Game game, Fight fight, Player player, int index, boolean isTavernIndex, int index2, boolean isTavernIndex2, boolean auto)
  {
    super.onPlayed(game, fight, player, index, isTavernIndex, index2, isTavernIndex2, auto);
    if (this.isGold())
    {
      player.addToHand(Card.of(new TripleReward(player.getLevel() + 1)));
    }
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
  }
  
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
  
  @Override
  public Unit clone()
  {
    Unit clonedUnit = (Unit) super.clone();
    
    clonedUnit.buffs = new ArrayList<>();
    for (Buff buff : this.buffs)
    {
      clonedUnit.buffs.add(buff.clone());
    }
    
    clonedUnit.type = new ArrayList<>(this.type);
    
    return clonedUnit;
  }
}