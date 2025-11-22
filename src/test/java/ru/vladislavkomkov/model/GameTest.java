package ru.vladislavkomkov.model;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.GamePlayerTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class GameTest extends GamePlayerTestCase
{
  @Test
  void testSimpleGame()
  {
    game.doPreFight();
    players.values().forEach(player1 -> player1.addToTable(new Cat()));
    game.doFight();
  }
}
