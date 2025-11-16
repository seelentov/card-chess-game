package ru.vladislavkomkov.models;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.service.RandService;

public class Game implements AutoCloseable {
    final int PLAYERS_COUNT = 8;
    final int FIGHTS_COUNT = 4;

    boolean inFight = false;

    ExecutorService executor = Executors.newFixedThreadPool(FIGHTS_COUNT);

    Map<String, Player> players;
    final Fight[] fights = new Fight[FIGHTS_COUNT];

    public Game(Map<String, Player> players){
        this.players = players;
    }

    int turn = 1;

    public void doPreFight() {
        inFight = false;
        calcFights();

        for(Player player: players.values()){
            processStartTurn(player);
            player.resetMoney();
            player.calcTavern();
            processEndTurn(player);
        }
    }
    
    public void processStartTurn(Player player){
        player.doForAll(unit -> unit.onStartTurn(this, player));
    }
    
    public void processEndTurn(Player player) {
        player.doForAll(unit -> unit.onEndTurn(this, player));
    }
    
    public void processStartFight(Player player,Player player2){
        player.doForAll(unit -> unit.onStartFight(this, player,player2));
    }
    
    public void processEndFight(Player player,Player player2) {
        player.doForAll(unit -> unit.onEndFight(this, player,player2));
    }

    public void doFight(){
        inFight = true;

        List<CompletableFuture<?>> fightFutures = new ArrayList<>();

        for(Fight fight: fights){
            fightFutures.add(CompletableFuture.supplyAsync(()->{
                boolean isPlayerFirst = RandService.getRand(1) == 0;
                
                Player player = isPlayerFirst ? fight.getPlayer1() : fight.getPlayer2();
                Player player2 = isPlayerFirst ? fight.getPlayer2() : fight.getPlayer1();
                
                processStartFight(player, player2);
                
                while(true){
                    if (fight.doTurn()){
                        break;
                    }
                }
                
                processStartFight(player, player2);
                
                return null;
            }, executor));
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                fightFutures.toArray(new CompletableFuture[0])
        );

        allOf.join();
    }

    public int getTurn(){
        return turn;
    }

    public void incTurn() {
        turn++;
    }

    void calcFights(){
        Queue<Player> alive = players.values().stream().filter(Player::isAlive).collect(Collectors.toCollection(ArrayDeque::new));
        Queue<Player> dead = players.values().stream().filter(player -> !player.isAlive()).collect(Collectors.toCollection(ArrayDeque::new));

        if((alive.size() + dead.size()) != PLAYERS_COUNT){
            throw new IllegalArgumentException("Players count is " + (alive.size() + dead.size()) + " but expected " + PLAYERS_COUNT);
        }

        for (int i = 0; i < fights.length; i++) {
            if (!dead.isEmpty()){
                fights[i] = new Fight(this, alive.peek(), dead.peek());
                continue;
            }

            if (!alive.isEmpty()){
                fights[i] = new Fight(this, alive.peek(), alive.peek());
            }
        }
    }

    @Override
    public void close() throws Exception {
        executor.shutdown();
    }
}
