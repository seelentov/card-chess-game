package ru.vladislavkomkov.model.event;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Event
{
  private final String gameUUID;
  private final String playerUUID;
  private final Type type;
  private final List<Integer> data;
  
  public Event(String gameUUID, String playerUUID, Type type, List<Integer> data)
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
    
    int dataLength = buffer.getInt();
    this.data = new ArrayList<>();
    for (int i = 0; i < dataLength; i++)
    {
      data.add(buffer.getInt());
    }
  }
  
  private String readUUIDFromBuffer(ByteBuffer buffer)
  {
    byte[] uuidBytes = new byte[36];
    buffer.get(uuidBytes);
    return new String(uuidBytes, StandardCharsets.UTF_8);
  }
  
  public byte[] getBytes()
  {
    int size = 36 + 36 + 4 + 4 + (data.size() * 4); // UUIDs + type + dataLength + data
    ByteBuffer buffer = ByteBuffer.allocate(size);
    
    writeUUIDToBuffer(buffer, gameUUID);
    writeUUIDToBuffer(buffer, playerUUID);
    
    buffer.putInt(type.ordinal());
    buffer.putInt(data.size());
    
    for (Integer value : data)
    {
      buffer.putInt(value);
    }
    
    return buffer.array();
  }
  
  private void writeUUIDToBuffer(ByteBuffer buffer, String uuid)
  {
    // Способ 1: Записываем как строку фиксированной длины
    byte[] uuidBytes = uuid.getBytes(StandardCharsets.UTF_8);
    if (uuidBytes.length != 36)
    {
      throw new IllegalArgumentException("UUID must be 36 characters long");
    }
    buffer.put(uuidBytes);
    
    // ИЛИ Способ 2: Записываем с указанием длины
    // buffer.putInt(uuid.length());
    // buffer.put(uuid.getBytes(StandardCharsets.UTF_8));
  }
  
  // Геттеры
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
  
  public List<Integer> getData()
  {
    return new ArrayList<>(data);
  }
  
  @Override
  public String toString()
  {
    return "Event{" +
        "gameUUID='" + gameUUID + '\'' +
        ", playerUUID='" + playerUUID + '\'' +
        ", type=" + type +
        ", data=" + data +
        '}';
  }
  
  public enum Type
  {
    CONNECTED
  }
}