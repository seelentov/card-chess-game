package ru.vladislavkomkov.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
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
    
    mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    mapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
    mapper.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
    mapper.setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.NONE);
    mapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
  }
  
  public static byte[] writeValue(Object object)
  {
    if(object == null){
      return null;
    }
    
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
