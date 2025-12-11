package ru.vladislavkomkov.model.event;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.fasterxml.jackson.core.type.TypeReference;

import ru.vladislavkomkov.util.ObjectUtils;

public class Event
{
  private final String gameUUID;
  private final String playerUUID;
  private final Type type;
  private final byte[] data;
  
  public Event(String gameUUID, String playerUUID, Type type)
  {
    this(
        gameUUID,
        playerUUID,
        type,
        new byte[0]);
  }
  
  public Event(String gameUUID, String playerUUID, Type type, int data)
  {
    this(
        gameUUID,
        playerUUID,
        type,
        ByteBuffer.allocate(4).putInt(data).array());
  }
  
  public Event(String gameUUID, String playerUUID, Type type, Object data)
  {
    this(
        gameUUID,
        playerUUID,
        type,
        ObjectUtils.writeValue(data));
  }
  
  public Event(String gameUUID, String playerUUID, Type type, byte[] data)
  {
    this.gameUUID = gameUUID;
    this.playerUUID = playerUUID;
    this.type = type;
    this.data = data;
  }
  
  public Event(byte[] bytes)
  {
    this(ByteBuffer.wrap(bytes));
  }
  
  public Event(ByteBuffer buffer)
  {
    this.gameUUID = readUUIDFromBuffer(buffer);
    this.playerUUID = readUUIDFromBuffer(buffer);
    
    this.type = Type.values()[buffer.getInt()];
    
    this.data = new byte[buffer.getInt()];
    buffer.get(this.data);
  }
  
  private String readUUIDFromBuffer(ByteBuffer buffer)
  {
    byte[] uuidBytes = new byte[36];
    buffer.get(uuidBytes);
    return new String(uuidBytes, StandardCharsets.UTF_8);
  }
  
  public byte[] getBytes()
  {
    int size = 36 + 36 + 4 + 4 + data.length;
    ByteBuffer buffer = ByteBuffer.allocate(size);
    
    writeUUIDToBuffer(buffer, gameUUID);
    writeUUIDToBuffer(buffer, playerUUID);
    
    buffer.putInt(type.ordinal());
    
    buffer.putInt(data.length);
    
    buffer.put(data);
    
    return buffer.array();
  }
  
  private void writeUUIDToBuffer(ByteBuffer buffer, String uuid)
  {
    byte[] uuidBytes = uuid.getBytes(StandardCharsets.UTF_8);
    if (uuidBytes.length != 36)
    {
      throw new IllegalArgumentException("UUID must be 36 characters long");
    }
    buffer.put(uuidBytes);
  }
  
  public String getGameUUID()
  {
    return gameUUID;
  }
  
  public String getPlayerUUID()
  {
    return playerUUID;
  }
  
  public Type getType()
  {
    return type;
  }
  
  public byte[] getData()
  {
    return data;
  }
  
  public <T> T getData(Class<T> clazz)
  {
    return ObjectUtils.readValue(data, clazz);
  }
  
  public <T> T getData(TypeReference<T> typeReference)
  {
    return ObjectUtils.readValue(data, typeReference);
  }
  
  @Override
  public String toString()
  {
    return "Event{" +
        "gameUUID='" + gameUUID + '\'' +
        ", playerUUID='" + playerUUID + '\'' +
        ", type=" + type +
        ", data=" + Arrays.toString(data) +
        '}';
  }
  
  public enum Type
  {
    // В обе стороны
    CONNECTED, // Подключение к игре
    
    // Входящие
    BYU, // Покупка карты
    PLAY, // Разыгровка карты
    SELL, // Продажа карты
    FREEZE, // Заморозка таверны
    LVL_UP, // Подьем уровня таверны
    ROLL, // Ролл таверны
    MOVE, // Перемещение карты на столе
    RES, // Получение ответа на ивенты типа WAIT_REQ
    RESET_TAVERN,  // Ролл таверны
    APPLY_DAMAGE,
    
    
    // Исходящие
    PRE_FIGHT_TIMER, // Таймер начала боя
    WAIT_REQ,
    CLEAR_WAITERS,
    ARMOR, // Изменение щита
    MONEY, // Изменение кол-ва золота
    MAX_MONEY, // Изменение макс. кол-ва золота
    WIN, // Конец игры - победа
    LOSE, // Конец игры - поражение
    START, // Старт игры
    ADD_TO_TABLE, // Добавление на стол
    ADD_TO_FIGHT_TABLE, // Добавление на стол
    REMOVE_FROM_TABLE,
    ADD_TO_HAND, // Добавление в руку
    REMOVE_FROM_HAND
  }
}