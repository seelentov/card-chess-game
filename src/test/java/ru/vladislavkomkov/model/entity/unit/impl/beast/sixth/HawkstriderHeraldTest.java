package ru.vladislavkomkov.model.entity.unit.impl.beast.sixth;

import org.junit.jupiter.api.Test;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.UnitTestCase;
import ru.vladislavkomkov.model.entity.unit.impl.beast.second.SewerRat;
import ru.vladislavkomkov.model.fight.Fight;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.HalfShell;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HawkstriderHeraldTest extends UnitTestCase
{
  @Test
  void testDefault()
  {
    super.testDefault(new HawkstriderHerald());
  }

  @Test
  void testOnStartFight()
  {
    player.addToTable(new SewerRat(player));
    player.addToTable(new SewerRat(player));
    player.addToTable(new HawkstriderHerald(player));

    Fight fight = new Fight(game, player, player2);

    game.processStartFight(fight, player, player2);

    List<Unit> units = fight.getFightTable(player);
    assertEquals(new SewerRat().getName(), units.get(0).getName());
    assertEquals(new HalfShell().getName(), units.get(1).getName());
    assertEquals(new SewerRat().getName(), units.get(2).getName());
    assertEquals(new HalfShell().getName(), units.get(3).getName());
  }

  @Test
  void testOnStartFightGold()
  {
    player.addToTable(new SewerRat(player));
    player.addToTable(new SewerRat(player));
    player.addToTable(new HawkstriderHerald(player).buildGold());

    Fight fight = new Fight(game, player, player2);

    game.processStartFight(fight, player, player2);

    List<Unit> units = fight.getFightTable(player);
    assertEquals(new SewerRat().getName(), units.get(0).getName());
    assertEquals(new HalfShell().getName(), units.get(1).getName());
    assertEquals(new HalfShell().getName(), units.get(2).getName());
    assertEquals(new SewerRat().getName(), units.get(3).getName());
    assertEquals(new HalfShell().getName(), units.get(4).getName());
    assertEquals(new HalfShell().getName(), units.get(5).getName());
  }
}
