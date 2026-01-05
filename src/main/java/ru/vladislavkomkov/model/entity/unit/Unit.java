package ru.vladislavkomkov.model.entity.unit;

import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonProperty;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.Entity;
import ru.vladislavkomkov.model.entity.PlayPair;
import ru.vladislavkomkov.model.entity.PlayType;
import ru.vladislavkomkov.model.entity.spell.impl.TripleReward;
import ru.vladislavkomkov.model.entity.unit.impl.Extra;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.ActionEvent;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.ListenerUtils;

public abstract class Unit extends Entity
{
  public final static String F_ATTACK = "attack";
  public final static String F_ATTACKS_COUNT = "attacks_count";
  
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
  protected AttacksCount attacksCount = AttacksCount.DEFAULT;
  protected int maxHealth = 1;
  protected int actualHealth;
  
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
    this(DUMP_PLAYER);
  }
  
  public Unit(Player playerLink)
  {
    super(playerLink);
    
    playType = List.of(new PlayPair(PlayType.TABLE));
  }
  
  @JsonProperty(F_ATTACKS_COUNT)
  public AttacksCount getAttacksCount()
  {
    return attacksCount;
  }
  
  public void setAttacksCount(AttacksCount attacksCount)
  {
    this.attacksCount = attacksCount;
  }
  
  public int getBaseAttack()
  {
    return attack;
  }
  
  @JsonProperty(F_ATTACK)
  public int getAttack()
  {
    return attack
        + playerLink.getStatistic().getBoosts().getAttackByUnitType(unitType)
        + playerLink.getStatistic().getBoosts().getAttackUnit();
  }
  
  public void setBaseAttack(int i)
  {
    attack = i;
  }
  
  public void incBaseAttack(int i)
  {
    attack += i;
  }
  
  public void decBaseAttack(int i)
  {
    attack -= i;
  }
  
  public int getBaseHealth()
  {
    return maxHealth;
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
    return maxHealth
        + playerLink.getStatistic().getBoosts().getHealthByUnitType(unitType)
        + playerLink.getStatistic().getBoosts().getHealthUnit();
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
        .filter(Buff::isTemp)
        .forEach(buff -> buff.getRollback().accept(this));
    
    buffs.removeIf(buff -> buff.getRollback() != null);
  }
  
  public void removeCoreListeners()
  {
    listener.removeCoreListener();
  }
  
  public void onSell(Game game, Fight fight, Player player)
  {
    player.getListener().removeListener((Unit) this);
    player.addMoney(1);
    player.removeFromTable((Unit) this);
    
    processListeners(
        ListenerUtils.getPlayerListener(fight, player).onSellListeners,
        (action) -> action.process(game, fight, player, this), player);
    
    listener.processOnSellListeners(game, fight, player, this);
    if (fight != null)
    {
      fight.addToHistory(ActionEvent.Type.ON_SELL, player, List.of(this));
    }
  }
  
  public void onStartTurn(Game game, Fight fight, Player player)
  {
    processListeners(
        ListenerUtils.getPlayerListener(fight, player).onStartTurnListeners,
        (action) -> action.process(game, fight, player), player);
    
    listener.processOnStartTurnListeners(game, fight, player);
    if (fight != null)
    {
      fight.addToHistory(ActionEvent.Type.ON_START_TURN, player, List.of(this));
    }
  }
  
  public void onEndTurn(Game game, Fight fight, Player player)
  {
    processListeners(
        ListenerUtils.getPlayerListener(fight, player).onEndTurnListeners,
        (action) -> action.process(game, fight, player), player);
    
    int extra = calcExtraAction(unit -> ((Extra) unit).getOnEndTurnExtra(), player);
    for (int i = 0; i < extra + 1; i++)
    {
      listener.processOnEndTurnListeners(game, fight, player);
      if (fight != null)
      {
        fight.addToHistory(ActionEvent.Type.ON_END_TURN, player, List.of(this));
      }
    }
  }
  
  public void onStartFight(Game game, Fight fight, Player player, Player player2)
  {
    processListeners(
        ListenerUtils.getPlayerListener(fight, player).onStartFightListeners,
        (action) -> action.process(game, fight, player, player2), player);
    
    listener.processOnStartFightListeners(game, fight, player, player2);
    if (fight != null)
    {
      fight.addToHistory(ActionEvent.Type.ON_START_FIGHT, player, List.of(this));
    }
  }
  
  public void onEndFight(Game game, Fight fight, Player player, Player player2)
  {
    processListeners(
        ListenerUtils.getPlayerListener(fight, player).onEndFightListeners,
        (action) -> action.process(game, fight, player, player2), player);
    
    listener.processOnEndFightListeners(game, fight, player, player2);
    if (fight != null)
    {
      fight.addToHistory(ActionEvent.Type.ON_END_FIGHT, player, List.of(this));
    }
  }
  
  public void onAttacked(Game game, Fight fight, Player player, Player player2, Unit attacker)
  {
    onAttacked(game, fight, player, player2, attacker, true);
  }
  
  public void onAttacked(Game game, Fight fight, Player player, Player player2, Unit attacker, boolean processDamage)
  {
    processListeners(
        ListenerUtils.getPlayerListener(fight, player).onAttackedListeners,
        (action) -> action.process(game, fight, player, player2, this, attacker), player);
    
    listener.processOnAttackedListeners(game, fight, player, player2, this, attacker);
    
    processDamage(attacker);
    
    processOnDead(game, fight, player, player2, attacker);
    
    if (fight != null)
    {
      fight.addToHistory(ActionEvent.Type.ON_ATTACKED, player, List.of(this, attacker));
    }
  }
  
  public void onAttack(Game game, Fight fight, Player player, Player player2, Unit attacked)
  {
    this.isDisguise = false;
    
    processListeners(
        ListenerUtils.getPlayerListener(fight, player).onAttackListeners,
        (action) -> action.process(game, fight, player, player2, this, attacked), player);
    
    listener.processOnAttackListeners(game, fight, player, player2, this, attacked);
    
    processDamage(attacked);
    processOnDead(game, fight, player, player2, attacked);
    
    if (fight != null)
    {
      fight.addToHistory(ActionEvent.Type.ON_ATTACK, player, List.of(this, attacked));
    }
  }
  
  public void processDamage(Unit attackedOrAttacker)
  {
    if (this.isBubbled)
    {
      this.isBubbled = false;
    }
    else
    {
      this.actualHealth -= attackedOrAttacker.getAttack();
    }
  }
  
  private void processOnDead(Game game, Fight fight, Player player, Player player2, Unit attackedOrAttacker)
  {
    List<Unit> table = fight != null ? fight.getFightTable(player) : player.getTable();
    int indexOfThis = table.indexOf(this);
    
    if (isDead())
    {
      onDead(game, fight, player, player2, attackedOrAttacker);
      
      if (isRebirth())
      {
        Unit reCreated;
        if (fight != null)
        {
          Optional<Unit> reCreatedOpt = player.getTableUnit(this.ID);
          
          if (reCreatedOpt.isEmpty())
          {
            throw new RuntimeException("Can`t find unit for rebirth");
          }
          
          reCreated = reCreatedOpt.get();
        }
        else
        {
          reCreated = this;
        }
        
        reCreated.actualHealth = 1;
        reCreated.isRebirth = false;
        
        if (fight != null)
        {
          fight.addToFightTable(player, reCreated, indexOfThis, false);
        }
        else
        {
          player.addToTable(this, indexOfThis, false);
        }
      }
    }
  }
  
  public void onDead(Game game, Fight fight, Player player, Player player2, Unit attacker)
  {
    onDead(game, fight, player, player2, attacker, true);
  }
  
  public void onDead(Game game, Fight fight, Player player, Player player2, Unit attacker, boolean processDead)
  {
    processListeners(
        ListenerUtils.getPlayerListener(fight, player).onDeadListeners,
        (action) -> action.process(game, fight, player, player2, this, attacker), player);
    
    int extra = calcExtraAction(unit -> ((Extra) unit).getOnDeadExtra(), player);
    for (int i = 0; i < extra + 1; i++)
    {
      listener.processOnDeadListeners(game, fight, player, player2, this, attacker);
      if (fight != null)
      {
        fight.addToHistory(ActionEvent.Type.ON_DEAD, player, attacker != null ? List.of(this, attacker) : List.of(this));
      }
    }
    
    if (processDead)
    {
      if (fight != null)
      {
        fight.removeFromFightTable(player, this);
      }
      else
      {
        player.removeFromTable(this);
      }
    }
  }
  
  public void onAppear(Game game, Fight fight, Player player)
  {
    processListeners(
        ListenerUtils.getPlayerListener(fight, player).onAppearListeners,
        (action) -> action.process(game, fight, player, this), player);
    
    listener.processOnAppearListeners(game, fight, player, this);
    
    if (fight != null)
    {
      fight.addToHistory(ActionEvent.Type.ON_APPEAR, player, List.of(this));
    }
  }
  
  public void onSummoned(Game game, Fight fight, Player player)
  {
    processListeners(
        ListenerUtils.getPlayerListener(fight, player).onSummonedListeners,
        (action) -> action.process(game, fight, player, this), player);
    
    listener.processOnSummonedListeners(game, fight, player, this);
    
    if (fight != null)
    {
      fight.addToHistory(ActionEvent.Type.ON_SUMMONED, player, List.of(this));
    }
  }
  
  public void onDisappear(Game game, Fight fight, Player player)
  {
    processListeners(
        ListenerUtils.getPlayerListener(fight, player).onDisappearListeners,
        (action) -> action.process(game, fight, player, this), player);
    
    listener.processOnDisappearListeners(game, fight, player, this);
    
    if (fight != null)
    {
      fight.addToHistory(ActionEvent.Type.ON_DISAPPEAR, player, List.of(this));
    }
  }
  
  @Override
  public void onPlayed(Game game, Fight fight, Player player, List<Integer> input, boolean auto)
  {
    int extra = calcExtraAction(unit -> ((Extra) unit).getOnPlayedExtra(), player);
    for (int i = 0; i < extra + 1; i++)
    {
      super.onPlayed(game, fight, player, input, auto);
    }
    
    if (this.isGold())
    {
      player.addToHand(Card.of(new TripleReward(player, player.getLevel() + 1)), true);
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
    entity.setBaseAttack(entity.getBaseAttack() * 2);
    entity.setHealth(entity.getBaseHealth() * 2);
    
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
    u.setBaseAttack(u.getAttack() * 2);
    
    return u;
  }
  
  public Unit newThis()
  {
    return (Unit) super.newThis();
  }
  
  public boolean isType(UnitType unitType)
  {
    return this.unitType.contains(UnitType.ALL) || this.unitType.contains(unitType);
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
  public boolean isSpell()
  {
    return false;
  }
  
  @Override
  public String getDescription()
  {
    return "";
  }
  
  private int calcExtraAction(Function<Unit, Extra.Action> function, Player player)
  {
    Optional<Integer> twiceTriple = player.getTable().stream()
        .filter(unit -> unit instanceof Extra)
        .map(unit -> function.apply(unit).getIsTwice())
        .max(Comparator.naturalOrder());
    
    Optional<Integer> extra = player.getTable().stream()
        .filter(unit -> unit instanceof Extra)
        .map(unit -> function.apply(unit).getIsExtra())
        .reduce(Integer::sum);
    
    return twiceTriple.orElse(0) + extra.orElse(0);
  }
}