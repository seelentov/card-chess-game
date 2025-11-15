package ru.vladislavkomkov.models.card;

import ru.vladislavkomkov.models.Entity;
import ru.vladislavkomkov.models.Game;
import ru.vladislavkomkov.models.Player;
import ru.vladislavkomkov.models.spell.Spell;
import ru.vladislavkomkov.models.unit.Unit;

public abstract class Card {
    public static Card of(Entity entity){
        if(entity instanceof Unit){
            return of(entity);
        } else if (entity instanceof Spell){
            return of(entity);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static Card of(Unit unit){
        return new UnitCard(unit);
    }

    public static Card of(Spell spell){
        return new SpellCard(spell);
    }

    public void play(Game game, Player player, int index){
        if (this instanceof SpellCard){
            Spell spell = ((SpellCard) this).getSpell();
            spell.onPlayed.process(game,player,index);
        } else if (this instanceof UnitCard){
            Unit unit = ((UnitCard) this).getUnit();
            unit.onPlayed.process(game,player,index);
        } else {
            throw new IllegalArgumentException("Wrong instance of card");
        }
    }

    public void handled(Game game, Player player){
        if (this instanceof SpellCard){
            Spell spell = ((SpellCard) this).getSpell();
            spell.onHandled.process(game,player);
        } else if (this instanceof UnitCard){
            Unit unit = ((UnitCard) this).getUnit();
            unit.onHandled.process(game,player);
        } else {
            throw new IllegalArgumentException("Wrong instance of card");
        }
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
