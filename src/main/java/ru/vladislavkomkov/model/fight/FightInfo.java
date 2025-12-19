package ru.vladislavkomkov.model.fight;

import ru.vladislavkomkov.model.player.Player;

public class FightInfo
{
    public final Player player1;
    public final Player player2;
    public final Result result;
    public final int damage;
    
    public FightInfo(Player player1, Player player2, Result result, int damage)
    {
        this.player1 = player1;
        this.player2 = player2;
        this.result = result;
        this.damage = damage;
    }
    
    public enum Result
    {
        PLAYER1_WIN,
        PLAYER2_WIN,
        DRAW
    }
}
