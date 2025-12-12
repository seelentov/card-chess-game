package ru.vladislavkomkov.model.event;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ru.vladislavkomkov.model.card.Card;
import ru.vladislavkomkov.model.entity.unit.Unit;
import ru.vladislavkomkov.model.entity.unit.impl.trash.beast.first.Cat;
import ru.vladislavkomkov.model.event.data.SenderWaiterDataReq;

public class EventTest
{
  final static String TEST_UUID = "";
  
  @Test
  void testCreateEventWAIT_REQ()
  {
    String key = "123123";
    
    Event.Type type = Event.Type.WAIT_REQ;
    
    Unit unit = new Cat();
    int unitAttack = 10;
    int unitHealth = 20;
    unit.setAttack(unitAttack);
    unit.setHealth(unitHealth);
    
    SenderWaiterDataReq data = new SenderWaiterDataReq(key, Card.of(unit));
    
    var event = new Event(
        null,
        null,
        type,
        data);
    
    byte[] bytes = event.getBytes();
    
    Event revEvent = new Event(bytes);
    
    assertEquals(type, revEvent.getType());
    
    SenderWaiterDataReq revData = revEvent.getData(SenderWaiterDataReq.class);
    
    assertEquals(key, revData.getKey());
    
    Card card = (Card) revData.getData();
    Unit unitNew = (Unit) card.getEntity();
    
    assertEquals(unitAttack, unitNew.getAttack());
    assertEquals(unitHealth, unitNew.getHealth());
  }
}
