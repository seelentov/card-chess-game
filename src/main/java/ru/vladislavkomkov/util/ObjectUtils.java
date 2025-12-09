package ru.vladislavkomkov.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ObjectUtils
{
  static ObjectMapper mapper;
  
  static
  {
    mapper = new ObjectMapper();

    mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
  }
  
  public static byte[] writeValue(Object object)
  {
    try
    {
      return mapper.writeValueAsBytes(object);
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }

    public static <T> T readValue(byte[] data, TypeReference<T> typeReference)
    {
        try
        {
            return mapper.readValue(data, typeReference);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T readValue(byte[] data, Class<T> clazz)
    {
        try
        {
            return mapper.readValue(data, clazz);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
