package ru.vladislavkomkov.models;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
            player.resetMoney();
            player.calcTavern();
        }
    }

    public void doFight(){
        inFight = true;

        List<CompletableFuture<?>> fightFutures = new ArrayList<>();

        for(Fight fight: fights){
            fightFutures.add(CompletableFuture.supplyAsync(()->{
                while(true){
                    if (fight.doTurn()){
                        break;
                    }
                }
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
