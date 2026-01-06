package ru.vladislavkomkov.model.entity.spell.impl.first;

import static ru.vladislavkomkov.consts.Listeners.KEY_CORE;
import static ru.vladislavkomkov.consts.PlayerConst.DUMP_PLAYER;

import java.util.List;

import ru.vladislavkomkov.model.entity.PlayPair;
import ru.vladislavkomkov.model.entity.PlayType;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.player.Player;

public class AzeriteEmpowerment extends Spell
{
    public AzeriteEmpowerment()
    {
        this(DUMP_PLAYER, false);
    }

    public AzeriteEmpowerment(Player playerLink)
    {
        this(playerLink, false);
    }

    public AzeriteEmpowerment(Player playerLink, boolean isGold)
    {
        super(playerLink, isGold);
        isTavern = true;

        playType = List.of(new PlayPair(PlayType.TAVERN_FRIENDLY));
    }

    @Override
    public void build()
    {
        listener.onPlayedListeners.put(
                KEY_CORE,
                (game, fight, player, entity, input, auto) -> {

                });
    }

    @Override
    public String getDescription()
    {
        return "";
    }
}