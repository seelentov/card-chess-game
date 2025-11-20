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
        if(player.inFightTable != null){
            int index = player.getFightIndex(this);
            for (int i = 0; i < 2; i++) {
                player.addToFightTable(new Imp(), index + 1, true);
            }
        } else {
            int index = player.getIndex(this);
            for (int i = 0; i < 2; i++) {
                player.addToTable(new Imp(),index + 1);
            }
        }
    }
}
