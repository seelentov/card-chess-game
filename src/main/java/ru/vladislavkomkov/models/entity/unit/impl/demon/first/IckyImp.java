package ru.vladislavkomkov.models.entity.unit.impl.demon.first;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.entity.unit.Type;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.models.entity.unit.impl.trash.demon.first.Imp;
import ru.vladislavkomkov.models.player.Player;

import java.util.Arrays;
import java.util.List;

public class IckyImp extends Unit {
    public IckyImp(){
        description = "Deathrattle: Summon two 1/1 Imps";

        type = List.of(Type.DEMON);

        attack = 1;

        maxHealth = 1;
        actualHealth = 1;

        level = 1;
    }

    @Override
    public void onDead(Game game, Player player, Player player2, Unit attacker) {
        super.onDead(game, player, player2, attacker);
        if(player.inFightTable != null && player.getFightUnitsCount() - 1 < 7){
            int index = player.getFightIndex(this);
            player.inFightTable.add(index + 1, new Imp());
            player.inFightTable.add(index + 1, new Imp());
        } else {
            int index = player.getIndex(this);
            player.addToTable(new Imp(),index + 1);
            player.addToTable(new Imp(),index + 1);
        }
    }
}
