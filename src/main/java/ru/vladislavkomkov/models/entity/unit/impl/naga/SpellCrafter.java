package ru.vladislavkomkov.models.entity.unit.impl.naga;

import java.util.List;

import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.card.Card;
import ru.vladislavkomkov.models.entity.spell.impl.spellcraft.SpellCraft;
import ru.vladislavkomkov.models.entity.unit.Type;
import ru.vladislavkomkov.models.entity.unit.Unit;
import ru.vladislavkomkov.models.player.Player;

public class SpellCrafter extends Unit {
    protected SpellCraft spellcraft;
    
    public SpellCrafter(SpellCraft spellcraft){
        super();
        this.spellcraft = spellcraft;
        type = List.of(Type.NAGA);
    }
    
    public SpellCraft getSpellcraft(){
        return spellcraft;
    }
    
    public void onStartTurn(Game game, Player player) {
        super.onStartTurn(game,player);
        addSpellCraft(player);
    }
    
    public void onPlayed(Game game, Player player, int index) {
        super.onPlayed(game,player,index);
        addSpellCraft(player);
    }
    
    private void addSpellCraft(Player player){
        player.addToHand(Card.of(spellcraft));
    }
}
