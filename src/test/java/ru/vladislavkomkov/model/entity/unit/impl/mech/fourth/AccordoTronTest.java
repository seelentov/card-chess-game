package ru.vladislavkomkov.model.entity.unit.impl.mech.fourth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.impl.mech.MechTestCase;

public class AccordoTronTest extends MechTestCase
{
  @Test
  void testDefault()
  {
    testDefault(new AccordoTron());
  }
  
  @Test
  protected void testOnStartTurn()
  {
    int initMoney = player.getMoney();
    player.addToTable(new AccordoTron(), 0);
    game.processStartTurn(player);
    assertEquals(initMoney + 1, player.getMoney());
  }
}
