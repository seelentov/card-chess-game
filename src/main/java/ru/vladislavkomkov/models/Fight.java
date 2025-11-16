package ru.vladislavkomkov.models;

import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.service.RandService;

import java.util.List;

public class Fight {
    final Game game;
    final Player player1;
    final Player player2;

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
        }

        if (player1Units.isEmpty() || player2Units.isEmpty()) {
            boolean isPlayer1Win = player1Units.isEmpty();

            Player loser = isPlayer1Win ? player2 : player1;

            int dmg;

            if (isPlayer1Win)
                dmg = calcPlayerDamage(player1, List.of(player1.cloneTable()));
            else
                dmg = calcPlayerDamage(player2, List.of(player2.cloneTable()));

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
        else
            player2Turn++;

        return false;
    }

    void setup(){
        player1Units = List.of(player1.cloneTable());
        player2Units = List.of(player2.cloneTable());

        turn = calcAttack(player1Units) > calcAttack(player2Units) ? 0 : 1;
    }

    int calcAttack(List<Unit> units){
        return units.stream().reduce(0, (total, unit) -> total + unit.getAttack(), Integer::sum);
    }


    Unit getRandUnit(List<Unit> units){
        int i = RandService.getRand(units.size() - 1);
        return units.get(i);
    }

    int calcPlayerDamage(Player player, List<Unit> units){
        int unitsDmg = units.stream().reduce(0, (total, unit) -> total + unit.getLevel(), Integer::sum);
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
