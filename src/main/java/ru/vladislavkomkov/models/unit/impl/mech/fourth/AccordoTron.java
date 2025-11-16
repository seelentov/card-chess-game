package ru.vladislavkomkov.models.unit.impl.mech.fourth;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.models.unit.impl.mech.Mech;

public class AccordoTron extends Mech {
    public AccordoTron(){
        super();
        description = "At the start of your turn, gain 1 Gold";
        level = 4;
        isTavern = true;

        attack = 5;

        maxHealth = 5;
        actualHealth = 5;
    }

    public void onStartTurn(Game game, Player player) {
        super.onStartTurn(game,player);
        player.addMoney();
    }
}
