package ru.vladislavkomkov.models.card;

import ru.vladislavkomkov.models.entity.Entity;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.player.Player;
import ru.vladislavkomkov.models.entity.spell.Spell;
import ru.vladislavkomkov.models.entity.unit.Unit;

public abstract class Card {
    public static Card of(Entity entity){
        if(entity instanceof Unit unit){
            return new UnitCard(unit);
        } else if (entity instanceof Spell spell){
            return new SpellCard(spell);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void play(Game game, Player player, int index){
        this.get().onPlayed(game,player,index);
    }

    public Entity get(){
        if (this instanceof SpellCard){
            return ((SpellCard) this).getSpell();
        } else if (this instanceof UnitCard){
            return ((UnitCard) this).getUnit();
        } else {
            throw new IllegalArgumentException("Wrong instance of card");
        }
    }
}
