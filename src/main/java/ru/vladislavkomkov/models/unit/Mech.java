package ru.vladislavkomkov.models.unit;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Mech extends Unit{
    public Mech(){
        type = Type.MECH;
    }

    public List<Mech> magnetized = new ArrayList<>();

    public List<Mech> copyMagnetized(){
        return magnetized.stream().toList();
    } 
    
    public void magnetize(Mech mech){
        this.maxHealth+=mech.maxHealth;
        this.actualHealth+=mech.actualHealth;
        this.attack+=mech.attack;

        magnetized.add(mech);
    }

    public void onStartTurn(Game game, Player player) {
        super.onStartTurn(game,player);
        magnetized.forEach(mech -> mech.onStartTurn(game,player));
    }

    public void onEndTurn(Game game, Player player) {
        super.onEndTurn(game,player);
        magnetized.forEach(mech -> mech.onEndTurn(game,player));
    }

    public void onStartFight(Game game, Player player, Player player2) {
        super.onStartFight(game,player,player2);
        magnetized.forEach(mech -> mech.onStartFight(game,player,player2));
    }

    public void onEndFight(Game game, Player player, Player player2) {
        super.onEndFight(game,player,player2);
        magnetized.forEach(mech -> mech.onEndFight(game,player,player2));
    }

    public void onAttacked(Game game, Player player, Player player2, Unit attacker) {
        super.onAttacked(game,player,player2,attacker);
        magnetized.forEach(mech -> mech.onAttacked(game,player,player2,attacker));
    }

    public void onAttack(Game game, Player player, Player player2, Unit attacked) {
        super.onAttacked(game,player,player2,attacked);
        magnetized.forEach(mech -> mech.onAttacked(game,player,player2,attacked));
    }

    public void onDead(Game game, Player player, Player player2, Unit attacker) {
        super.onAttacked(game,player,player2,attacker);
        magnetized.forEach(mech -> mech.onAttacked(game,player,player2,attacker));
    }
}
