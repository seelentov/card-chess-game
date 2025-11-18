package ru.vladislavkomkov.models;

import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.util.RandUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Fight {
    final Game game;
    final Player player1;
    final Player player2;
    
    static final int TURN_LIMIT = 10000;

    List<Unit> player1Units;
    List<Unit> player2Units;

    int player1Turn = 0;
    int player2Turn = 0;

    int turn = -1;

    public Fight(Game game, Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;

        this.game = game;
    }

    public boolean doTurn(){
        if (turn == -1) {
            setup();
        } else if(turn >= TURN_LIMIT){
            return true;
        }

        if (player1Units.isEmpty() || player2Units.isEmpty()) {
            if(player1Units.isEmpty() && player2Units.isEmpty()){
                return true;
            }
            
            boolean isPlayer1Win = player2Units.isEmpty();
            Player loser = isPlayer1Win ? player2 : player1;

            int dmg;
            
            if (isPlayer1Win)
                dmg = calcPlayerDamage(player1);
            else
                dmg = calcPlayerDamage(player2);

            loser.applyDamage(dmg);

            return true;
        }

        boolean isPlayer1Turn = turn % 2 == 0;

        Unit attacker = isPlayer1Turn ? player1Units.get(player1Turn) : player2Units.get(player2Turn);
        Unit attacked = isPlayer1Turn ? getRandUnit(player2Units) : getRandUnit(player1Units);

        Player turnPlayer1 = isPlayer1Turn ? player1 : player2;
        Player turnPlayer2 = isPlayer1Turn ? player2 : player1;

        attacker.onAttack(game, turnPlayer1, turnPlayer2, attacked);

        attacked.onAttacked(game, turnPlayer2, turnPlayer1, attacker);

        if (attacker.isDead()){
            attacker.onDead(game,turnPlayer2,turnPlayer1,attacked);
            if (attacker.isDead()){
                if (isPlayer1Turn)
                    player1Units.removeIf(o -> o == attacker);
                else
                    player2Units.removeIf(o -> o == attacker);
            }
        }

        if (attacked.isDead()){
            attacked.onDead(game,turnPlayer2,turnPlayer1,attacker);
            if (attacked.isDead()){
                if (isPlayer1Turn)
                    player2Units.removeIf(o -> o == attacked);
                else
                    player1Units.removeIf(o -> o == attacked);
            }
        }

        if(isPlayer1Turn)
            player1Turn++;
            if(player1Turn >= player1Units.size())
                player1Turn = 0;
            
        else
            player2Turn++;
            if(player2Turn >= player2Units.size())
                player2Turn = 0;
            
        turn++;

        return false;
    }
    
    void setup(){
        this.player1Units = new ArrayList<>();
        this.player2Units = new ArrayList<>();
        
        for (Unit item : player1.cloneTable()) {
            if (item != null) {
                this.player1Units.add(item);
            }
        }
        
        for (Unit item : player2.cloneTable()) {
            if (item != null) {
                this.player2Units.add(item);
            }
        }
        
        int player1Attack = calcAttack(this.player1Units);
        int player2Attack = calcAttack(this.player2Units);
        
        turn = player1Attack > player2Attack ? 0 : player1Attack < player2Attack ? 1 : RandUtils.getRand(1);
    }

    int calcAttack(List<Unit> units){
        return units.stream().reduce(0, (total, unit) -> total + unit.getAttack(), Integer::sum);
    }

    Unit getRandUnit(List<Unit> units){
        int i = RandUtils.getRand(units.size() - 1);
        return units.get(i);
    }

    int calcPlayerDamage(Player player){
        int unitsDmg = Arrays.stream(player.cloneTable()).filter(Objects::nonNull).reduce(0, (total, unit) -> total + unit.getLevel(), Integer::sum);
        int playerDmg = player.getLevel();
        return playerDmg + unitsDmg;
    }
    
    public Player getPlayer1(){
        return player1;
    }
    
    public Player getPlayer2(){
        return player2;
    }
}
