package ru.vladislavkomkov.model.entity.unit;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.PlayPair;
import ru.vladislavkomkov.model.entity.PlayType;
import ru.vladislavkomkov.model.entity.spell.impl.TripleReward;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.fight.FightEvent;
import ru.vladislavkomkov.model.player.Player;

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
  public final static String F_TYPE = "type";
  
  List<Buff> buffs = new ArrayList<>();
  protected List<UnitType> unitType = new ArrayList<>();
  
  protected int attack = 0;
  protected int maxHealth = 1;
  protected int actualHealth = 1;
  
  protected boolean isBubbled = false;
  protected boolean isTaunt = false;
  protected boolean isRebirth = false;
  protected boolean isMagnet = false;
  protected boolean isDisguise = false;
  
  protected boolean isAnswerOnPlayed = true;
  protected boolean isAnswerOnStartTurn = true;
  protected boolean isAnswerOnDead = true;
  
  public Unit()
  {
    super();
    
    playType = List.of(new PlayPair(PlayType.TABLE));
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
  
  public void setMaxHealth(int maxHealth)
  {
    this.maxHealth = maxHealth;
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
  
  public void setIsMagnet(boolean isMagnet)
  {
    this.isMagnet = isMagnet;
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
  
  public void setIsRebirth(boolean isRebirth)
  {
    this.isRebirth = isRebirth;
  }
  
  public boolean isAnswerOnPlayed()
  {
    if (!isAnswerOnPlayed)
    {
      return false;
    }
    
    return !listener.onPlayedListeners.isEmpty();
  }
  
  public void setIsAnswerOnPlayed(boolean isAnswerOnPlayed)
  {
    this.isAnswerOnPlayed = isAnswerOnPlayed;
  }
  
  public boolean isAnswerOnStartTurn()
  {
    if (!isAnswerOnStartTurn)
    {
      return false;
    }
    
    return !listener.onStartTurnListeners.isEmpty();
  }
  
  public void setIsAnswerOnStartTurn(boolean isAnswerOnStartTurn)
  {
    this.isAnswerOnStartTurn = isAnswerOnStartTurn;
  }
  
  public boolean isAnswerOnDead()
  {
    if (!isAnswerOnDead)
    {
      return false;
    }
    
    return !listener.onDeadListeners.isEmpty();
  }
  
  public void setIsAnswerOnDead(boolean isAnswerOnDead)
  {
    this.isAnswerOnDead = isAnswerOnDead;
  }
  
  @JsonProperty(F_TYPE)
  public List<UnitType> getType()
  {
    return unitType;
  }
  
  @JsonProperty(F_BUFFS)
  public List<Buff> getBuffs()
  {
    return buffs;
  }
  
  public void addBuff(List<Buff> buff)
  {
    buff.forEach(this::addBuff);
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
  
  public void onSell(Game game, Fight fight, Player player)
  {
    player.listener.removeListener((Unit) this);
    player.addMoney(1);
    player.removeFromTable((Unit) this);
    processListeners(player.listener.onSellListeners, (action) -> action.process(game, null, player, this), player);
    
    listener.processOnSellListeners(game, fight, player, this);
    if (fight != null)
    {
      fight.addToHistory(FightEvent.Type.ON_SELL, player, List.of(this));
    }
  }
  
  public void onStartTurn(Game game, Fight fight, Player player)
  {
    listener.processOnStartTurnListeners(game, fight, player);
    if (fight != null)
    {
      fight.addToHistory(FightEvent.Type.ON_START_TURN, player, List.of(this));
    }
  }
  
  public void onEndTurn(Game game, Fight fight, Player player)
  {
    listener.processOnEndTurnListeners(game, fight, player);
    if (fight != null)
    {
      fight.addToHistory(FightEvent.Type.ON_END_TURN, player, List.of(this));
    }
  }
  
  public void onStartFight(Game game, Fight fight, Player player, Player player2)
  {
    listener.processOnStartFightListeners(game, fight, player, player2);
    if (fight != null)
    {
      fight.addToHistory(FightEvent.Type.ON_START_FIGHT, player, List.of(this));
    }
  }
  
  public void onEndFight(Game game, Fight fight, Player player, Player player2)
  {
    listener.processOnEndFightListeners(game, fight, player, player2);
    if (fight != null)
    {
      fight.addToHistory(FightEvent.Type.ON_END_FIGHT, player, List.of(this));
    }
  }
  
  public void onAttacked(Game game, Fight fight, Player player, Player player2, Unit attacker)
  {
    processListeners(player.listener.onAttackedListeners, (action) -> action.process(game, null, player, player2, this, attacker), player);
    if (this.isBubbled)
    {
      this.isBubbled = false;
    }
    else
    {
      this.actualHealth -= attacker.getAttack();
    }
    
    listener.processOnAttackedListeners(game, fight, player, player2, this, attacker);
    if (fight != null)
    {
      fight.addToHistory(FightEvent.Type.ON_ATTACKED, player, List.of(this, attacker));
    }
  }
  
  public void onAttack(Game game, Fight fight, Player player, Player player2, Unit attacked)
  {
    processListeners(player.listener.onAttackListeners, (action) -> action.process(game, null, player, player2, this, attacked), player);
    if (this.isBubbled)
    {
      this.isBubbled = false;
    }
    else
    {
      this.actualHealth -= attacked.getAttack();
    }
    
    listener.processOnAttackListeners(game, fight, player, player2, this, attacked);
    if (fight != null)
    {
      fight.addToHistory(FightEvent.Type.ON_ATTACK, player, List.of(this, attacked));
    }
  }
  
  public void onDead(Game game, Fight fight, Player player, Player player2, Unit attacker)
  {
    processListeners(player.listener.onDeadListeners, (action) -> action.process(game, null, player, player2, this, attacker), player);
    List table = fight != null ? fight.getFightTable(player) : player.getTable();
    if (this.isRebirth)
    {
      this.isRebirth = false;
      if (table.size() < Player.TABLE_LIMIT)
      {
        this.actualHealth = 1;
      }
    }
    
    listener.processOnDeadListeners(game, fight, player, player2, this, attacker);
    if (fight != null)
    {
      fight.addToHistory(FightEvent.Type.ON_DEAD, player, List.of(this, attacker));
    }
  }
  
  public void onAppear(Game game, Fight fight, Player player)
  {
    processListeners(player.listener.onAppearListeners, (action) -> action.process(game, null, player, this), player);
    
    listener.processOnAppearListeners(game, fight, player, this);
    if (fight != null)
    {
      fight.addToHistory(FightEvent.Type.ON_APPEAR, player, List.of(this));
    }
  }
  
  public void onDisappear(Game game, Fight fight, Player player)
  {
    processListeners(player.listener.onDisappearListeners, (action) -> action.process(game, null, player, this), player);
    
    listener.processOnDisappearListeners(game, fight, player, this);
    if (fight != null)
    {
      fight.addToHistory(FightEvent.Type.ON_DISAPPEAR, player, List.of(this));
    }
  }
  
  @Override
  public void onPlayed(Game game, Fight fight, Player player, List<Integer> input, boolean auto)
  {
    super.onPlayed(game, fight, player, input, auto);
    if (this.isGold())
    {
      player.addToHand(Card.of(new TripleReward(player.getLevel() + 1)));
    }
  }
  
  public Unit buildGold()
  {
    return buildGold(this, this.newBase(), this.newBase());
  }
  
  public Unit buildGold(Unit unit)
  {
    return buildGold(unit, unit.newBase(), unit.newBase());
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
    Unit entity = this.newBase();
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
  
  public Unit newBase()
  {
    return (Unit) super.newBase();
  }
  
  public Unit newGold()
  {
    Unit u = (Unit) super.newGold();
    
    u.setHealth(u.getHealth() * 2);
    u.setAttack(u.getAttack() * 2);
    
    return u;
  }
  
  public Unit newThis()
  {
    return (Unit) super.newThis();
  }
  
  public boolean isType(UnitType unitType)
  {
    return this.unitType.contains(unitType);
  }
  
  public boolean isType(List<UnitType> unitType)
  {
    for (UnitType unitType1 : unitType)
    {
      if (isType(unitType1))
      {
        return true;
      }
    }
    return false;
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
    
    clonedUnit.unitType = new ArrayList<>(this.unitType);
    
    return clonedUnit;
  }
  
  @Override
  public boolean isSpell() {
    return false;
  }
  
  @Override
  public void buildFace(Player player)
  {
    
  }
}