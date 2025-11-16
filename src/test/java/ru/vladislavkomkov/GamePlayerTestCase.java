package ru.vladislavkomkov;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;

public abstract class GamePlayerTestCase {
    protected Player player;
    protected Player player2;
    protected Game game;
    
    @BeforeEach
    protected void setUp(){
        player = new Player();
        player2 = new Player();
        Map<String,Player> players = Map.of(
                "1", player,
                "2", player2
        );
        game = new Game(players);
        player.resetMoney();
    }
    
    @AfterEach
    protected void tearDown() throws Exception {
        if(game != null){
            game.close();
        }
    }
}
