package ru.vladislavkomkov.models.unit;

import ru.vladislavkomkov.models.Entity;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.Player;
import ru.vladislavkomkov.models.card.UnitCard;
import ru.vladislavkomkov.models.unit.actions.*;

public abstract class Unit extends Entity {
    protected int attack = 0;
    protected int maxHealth = 1;
    protected Type type = Type.NONE;

    protected boolean isBubbled = false;
    protected boolean isTaunt = false;
    protected boolean isRebirth = false;
    protected boolean isDoubleAttack = false;

    public OnAnotherPlayedAction onAnotherPlayed = null;

    public OnSellAction onSell = null;

    public OnStartTurnAction onStartTurn = null;
    public OnEndTurnAction onEndTurn = null;

    public OnPlayedAction onPlayed = (Game game, Player player, int index) -> {
        player.addToTable(this, index);
    };

    public OnHandledAction onHandled = (Game game, Player player) -> player.addToTable(this, -1);

    public OnAttackedAction onAttacked = (Game game, Player player1, Player player2, Unit attacker) -> {
        if(isBubbled){
            isBubbled = false;
        } else {
            this.actualHealth-=attacker.getAttack();
        }
    };

    public OnAttackAction onAttack = (Game game, Player player1, Player player2, Unit attacked) -> {
        if(isBubbled){
            isBubbled = false;
        } else {
            this.actualHealth-=attacked.getAttack();
        }
    };;
    public OnDeadAction onDead = (Game game, Player player1, Player player2, Unit attacker) -> {
        if(isRebirth){
            isRebirth = false;
            actualHealth = 1;
        }
    };

    protected int actualHealth = 1;

    public int getAttack(){
        return attack;
    }

    public boolean isDead(){
        return actualHealth < 1;
    }
}
