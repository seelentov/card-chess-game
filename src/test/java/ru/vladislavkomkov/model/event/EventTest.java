package ru.vladislavkomkov.model.event;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;

public class EventTest
{
  @Test
  void testCreateEventWithString()
  {
    String i = "111";
    Event event = new Event(
        null,
        null,
        Event.Type.ERROR,
        i);
    
    byte[] bytes = event.getBytes();
    
    Event revEvent = new Event(bytes);
    
    assertEquals(i, revEvent.getDataAsString());
  }
  
  @Test
  void testCreateEventWithInt()
  {
    int i = 111;
    Event event = new Event(
        null,
        null,
        Event.Type.ERROR,
        i);
    
    byte[] bytes = event.getBytes();
    
    Event revEvent = new Event(bytes);
    
    assertEquals(i, revEvent.getDataAsInt());
  }
  
  @Test
  void testCreateEventWithObject()
  {
    Unit unit = new Cat();
    
    int updAttack = 10;
    unit.setBaseAttack(updAttack);
    
    int updHealth = 10;
    unit.setHealth(updHealth);
    
    unit.setIsBubbled(true);
    
    Event event = new Event(
        null,
        null,
        Event.Type.ERROR,
        unit);
    
    byte[] bytes = event.getBytes();
    
    Event revEvent = new Event(bytes);
    
    Map revUnit = revEvent.getData(Map.class);
    
    assertEquals(new Cat().getName(), revUnit.get(Unit.F_NAME));
    assertEquals(updAttack, revUnit.get(Unit.F_ATTACK));
    assertEquals(updHealth, revUnit.get(Unit.F_HEALTH));
    assertEquals(true, revUnit.get(Unit.F_IS_BUBBLED));
  }
  
  @Test
  void testCreateEventWithList()
  {
    Unit unit = new Cat();
    Unit unit2 = new Cat();
    
    int updAttack = 10;
    unit2.setBaseAttack(updAttack);
    
    int updHealth = 10;
    unit2.setHealth(updHealth);
    
    unit2.setIsBubbled(true);
    
    Event event = new Event(
        null,
        null,
        Event.Type.ERROR,
        List.of(
            unit,
            unit2));
    
    byte[] bytes = event.getBytes();
    
    Event revEvent = new Event(bytes);
    
    List<Map> revUnits = revEvent.getData(List.class);
    Map revUnit = revUnits.get(0);
    
    assertEquals(new Cat().getName(), revUnit.get(Unit.F_NAME));
    assertEquals(new Cat().getAttack(), revUnit.get(Unit.F_ATTACK));
    assertEquals(new Cat().getHealth(), revUnit.get(Unit.F_HEALTH));
    
    Map revUnit2 = revUnits.get(1);
    
    assertEquals(new Cat().getName(), revUnit2.get(Unit.F_NAME));
    assertEquals(updAttack, revUnit2.get(Unit.F_ATTACK));
    assertEquals(updHealth, revUnit2.get(Unit.F_HEALTH));
    assertEquals(true, revUnit2.get(Unit.F_IS_BUBBLED));
  }
  
  @Test
  void testCreateEvent()
  {
    Event event = new Event(
        null,
        null,
        Event.Type.ERROR);
    
    byte[] bytes = event.getBytes();
    
    Event revEvent = new Event(bytes);
    
    assertEquals(0, revEvent.getData().length);
    assertEquals(Event.Type.ERROR, revEvent.getType());
  }
}
