package ru.vladislavkomkov.util;

import java.io.*;

public class SerializationUtils
{
  @SuppressWarnings("unchecked")
  public static <T extends Serializable> T deepCopy(T object)
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(object);
      oos.close();
      
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bais);
      return (T) ois.readObject();
    }
    catch (IOException | ClassNotFoundException e)
    {
      throw new RuntimeException("Deep copy failed", e);
    }
  }
}
