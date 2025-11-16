package ru.vladislavkomkov.models.entity.spell.impl.first;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.models.entity.spell.Spell;

public class TavernCoin extends Spell {
    protected String description = "Gain 1 Gold";

    @Override
    public void cast(Game game, Player player, int index) {
        player.addMoney();
    }
}
