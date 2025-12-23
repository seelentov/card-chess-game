package ru.vladislavkomkov.model.event;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.fasterxml.jackson.core.type.TypeReference;

import ru.vladislavkomkov.util.ObjectUtils;

public class Event
{
  static final String EMPTY_UUID = "00000000-0000-0000-0000-000000000000";
  static final int UUID_LENGTH = 36;
  
  final String gameUUID;
  final String playerUUID;
  final Type type;
  final byte[] data;
  
  public Event(String gameUUID, String playerUUID, Type type)
  {
    this(gameUUID, playerUUID, type, new byte[0]);
  }
  
  public Event(String gameUUID, String playerUUID, Type type, boolean data)
  {
    this(gameUUID, playerUUID, type, new byte[] { (byte) (data ? 1 : 0) });
  }
  
  public Event(String gameUUID, String playerUUID, Type type, int data)
  {
    this(gameUUID, playerUUID, type, ByteBuffer.allocate(4).putInt(data).array());
  }
  
  public Event(String gameUUID, String playerUUID, Type type, String data)
  {
    this(gameUUID, playerUUID, type, data.getBytes(StandardCharsets.UTF_8));
  }
  
  public Event(String gameUUID, String playerUUID, Type type, Object data)
  {
    this(gameUUID, playerUUID, type, ObjectUtils.writeValue(data));
  }
  
  public Event(String gameUUID, String playerUUID, Type type, byte[] data)
  {
    this.gameUUID = gameUUID == null ? EMPTY_UUID : gameUUID;
    this.playerUUID = playerUUID == null ? EMPTY_UUID : playerUUID;
    this.type = type;
    this.data = data != null ? data : new byte[0];
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
    this.data = new byte[dataLength];
    buffer.get(this.data);
  }
  
  String readUUIDFromBuffer(ByteBuffer buffer)
  {
    byte[] uuidBytes = new byte[UUID_LENGTH];
    buffer.get(uuidBytes);
    return new String(uuidBytes, StandardCharsets.UTF_8);
  }
  
  void writeUUIDToBuffer(ByteBuffer buffer, String uuid)
  {
    byte[] uuidBytes = uuid.getBytes(StandardCharsets.UTF_8);
    if (uuidBytes.length != UUID_LENGTH)
    {
      throw new IllegalArgumentException("UUID must be " + UUID_LENGTH + " characters long");
    }
    buffer.put(uuidBytes);
  }
  
  public byte[] getBytes()
  {
    int size = UUID_LENGTH * 2 +
        Integer.BYTES +
        Integer.BYTES +
        data.length;
    
    ByteBuffer buffer = ByteBuffer.allocate(size);
    
    writeUUIDToBuffer(buffer, gameUUID);
    writeUUIDToBuffer(buffer, playerUUID);
    buffer.putInt(type.ordinal());
    buffer.putInt(data.length);
    buffer.put(data);
    
    return buffer.array();
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
    return Arrays.copyOf(data, data.length);
  }
  
  public <T> T getData(Class<T> clazz)
  {
    return ObjectUtils.readValue(data, clazz);
  }
  
  public <T> T getData(TypeReference<T> typeReference)
  {
    return ObjectUtils.readValue(data, typeReference);
  }
  
  public int getDataAsInt()
  {
    if (data.length != 4)
    {
      throw new IllegalStateException("Data is not an integer");
    }
    return ByteBuffer.wrap(data).getInt();
  }
  
  public String getDataAsString()
  {
    return new String(data, StandardCharsets.UTF_8);
  }
  
  public boolean getDataAsBool()
  {
    if (data.length != 1)
    {
      throw new IllegalStateException("Data is not a boolean");
    }
    return data[0] != 0;
  }
  
  public boolean hasData()
  {
    return data != null && data.length > 0;
  }
  
  @Override
  public String toString()
  {
    return "Event{" +
        "gameUUID='" + gameUUID + '\'' +
        ", playerUUID='" + playerUUID + '\'' +
        ", unitType=" + type +
        ", data=" + new String(data, StandardCharsets.UTF_8)+
        '}';
  }
  
  public enum Type
  {
    CONNECTED,
    DISCONNECTED,
    ERROR,
    BUY,
    PLAY,
    SELL,
    FREEZE,
    LVL,
    LVL_PRICE,
    MOVE,
    RES,
    RESET_TAVERN,
    HEALTH,
    PRE_FIGHT_TIMER,
    WAIT_REQ,
    CLEAR_WAITERS,
    ARMOR,
    MONEY,
    MAX_MONEY,
    WIN,
    LOSE,
    START,
    TABLE,
    FIGHT,
    HAND,
    TAVERN
  }
}