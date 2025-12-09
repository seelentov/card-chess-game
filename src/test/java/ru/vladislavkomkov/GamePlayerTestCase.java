package ru.vladislavkomkov;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import ru.vladislavkomkov.model.Game;
import ru.vladislavkomkov.model.player.Player;
import ru.vladislavkomkov.util.UUIDUtils;

public abstract class GamePlayerTestCase
{
  protected static final int STEP_MONEY = 10;
  protected Player player;
  protected Player player2;
  protected Map<String, Player> players;
  protected Game game;
  
  @BeforeEach
  protected void setUp()
  {
    player = new Player();
    player2 = new Player();
    players = Map.of(
        "1", player,
        "2", player2,
        "3", new Player(),
        "4", new Player(),
        "5", new Player(),
        "6", new Player(),
        "7", new Player(),
        "8", new Player());
    game = new Game(players);
    player.resetMoney(game);
  }
  
  @AfterEach
  protected void tearDown()
  {
    try
    {
      if (game != null)
      {
        game.close();
      }
    }
    catch (Exception ex)
    {
      //
    }
  }
  
  protected <T> void testListener(Map<String, T> listeners, Runnable processListener, T action, boolean once)
  {
    int initMoney = player.getMoney();
    
    listeners.put(once ? UUIDUtils.generateKeyOnce() : UUIDUtils.generateKey(), action);
    
    processListener.run();
    
    assertEquals(initMoney + STEP_MONEY, player.getMoney());
    
    processListener.run();
    
    assertEquals(initMoney + (STEP_MONEY * (once ? 1 : 2)), player.getMoney());
  }
}
