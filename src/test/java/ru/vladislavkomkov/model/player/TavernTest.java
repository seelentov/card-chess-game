package ru.vladislavkomkov.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.spell.Spell;
import ru.vladislavkomkov.model.entity.spell.impl.first.TavernCoin;
import ru.vladislavkomkov.model.entity.spell.impl.spellcraft.impl.DeepBlues;
import ru.vladislavkomkov.model.entity.unit.impl.beast.first.Alleycat;
import ru.vladislavkomkov.model.entity.unit.impl.mech.fourth.AccordoTron;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.util.SpellUtils;

public class TavernTest
{
  @Test
  void testAllLevels()
  {
    Tavern tavern = new Tavern();
    for (int i = 1; i < 7; i++)
    {
      tavern.reset(i, true);
      assertEquals(Tavern.getCountByLevel(i), tavern.cards.size() - 1);
      for (Tavern.Slot slot : tavern.cards)
      {
        assertTrue(slot.getEntity().getLevel() <= i);
      }
    }
  }
  
  @Test
  void testFreeze()
  {
    Tavern tavern = new Tavern();
    
    for (int j = 0; j < 1000; j++)
    {
      tavern.cards.clear();
      tavern.add(Card.of(new Cat()));
      tavern.add(Card.of(new Alleycat()));
      tavern.add(Card.of(new AccordoTron()));
      tavern.add(Card.of(new TavernCoin()));
      tavern.setFreeze(true);
      
      List<Tavern.Slot> cardsBefore = new ArrayList<>(tavern.cards);
      
      tavern.reset(1);
      
      List<Tavern.Slot> cardsAfter = new ArrayList<>(tavern.cards);
      
      for (int i = 0; i < cardsBefore.size(); i++)
      {
        Tavern.Slot slotBefore = cardsBefore.get(i);
        Tavern.Slot slotAfter = cardsAfter.get(i);
        
        String nameBefore = slotBefore.getEntity().getName();
        String nameAfter = slotAfter.getEntity().getName();
        
        assertEquals(nameBefore, nameAfter);
      }
      
      assertEquals(cardsBefore, cardsAfter);
    }
  }
  
  @Test
  void testSpellFreezed() {
    Tavern tavern = new Tavern();
    
    for (int j = 0; j < 1000; j++) {
      tavern.cards.add(new Tavern.Slot(Card.of(new Cat())));
      tavern.cards.add(new Tavern.Slot(Card.of(new Alleycat())));
      tavern.cards.add(new Tavern.Slot(Card.of(new AccordoTron())));
      tavern.cards.add(new Tavern.Slot(Card.of(new DeepBlues()), true));
      
      tavern.reset(1);
      
      Spell spell = (Spell) tavern.cards.get(0).getEntity();
      assertEquals(new DeepBlues().getName(), spell.getName());
    }
  }
  
  @Test
  void testSpellNotFreezed()
  {
    Tavern tavern = new Tavern();
    
    for (int j = 0; j < 1000; j++)
    {
      tavern.cards.add(new Tavern.Slot(Card.of(new Cat())));
      tavern.cards.add(new Tavern.Slot(Card.of(new Alleycat())));
      tavern.cards.add(new Tavern.Slot(Card.of(new AccordoTron())));
      tavern.cards.add(new Tavern.Slot(Card.of(new DeepBlues())));
      
      tavern.reset(1);
      
      Spell spell = (Spell) tavern.cards.get(tavern.cards.size() - 1).getEntity();
      assertNotEquals(new DeepBlues().getName(), spell.getName());
    }
  }
  
  @Test
  void testLastSpellNotFreezed()
  {
    Tavern tavern = new Tavern();
    
    for (int j = 0; j < 1000; j++)
    {
      tavern.cards.add(new Tavern.Slot(Card.of(new DeepBlues()), true));
      tavern.cards.add(new Tavern.Slot(Card.of(new DeepBlues()), true));
      tavern.cards.add(new Tavern.Slot(Card.of(new DeepBlues()), true));
      tavern.cards.add(new Tavern.Slot(Card.of(new DeepBlues())));
      
      tavern.reset(1);
      
      Spell spell = (Spell) tavern.cards.get(tavern.cards.size() - 1).getEntity();
      assertNotEquals(new DeepBlues().getName(), spell.getName());
    }
  }
  
  @Test
  void testOnlyLastSpellFreezed()
  {
    Tavern tavern = new Tavern();
    
    for (int j = 0; j < 1000; j++)
    {
      tavern.cards.add(new Tavern.Slot(Card.of(new DeepBlues())));
      tavern.cards.add(new Tavern.Slot(Card.of(new DeepBlues())));
      tavern.cards.add(new Tavern.Slot(Card.of(new DeepBlues())));
      tavern.cards.add(new Tavern.Slot(Card.of(new DeepBlues()), true));
      
      tavern.reset(1);
      
      Spell spell = (Spell) tavern.cards.get(0).getEntity();
      assertEquals(new DeepBlues().getName(), spell.getName());
      
      assertFalse(tavern.cards.get(1).getEntity() instanceof Spell);
      assertFalse(tavern.cards.get(2).getEntity() instanceof Spell);
      assertFalse(tavern.cards.get(3).getEntity() instanceof Spell);
    }
  }
}
