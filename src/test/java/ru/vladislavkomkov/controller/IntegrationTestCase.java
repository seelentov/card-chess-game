package ru.vladislavkomkov.controller;

import java.util.function.BooleanSupplier;

public class IntegrationTestCase
{
  protected boolean waitForCondition(BooleanSupplier condition, long timeoutMs) throws InterruptedException
  {
    long start = System.currentTimeMillis();
    while (!condition.getAsBoolean())
    {
      if (System.currentTimeMillis() - start > timeoutMs)
      {
        return false;
      }
      Thread.sleep(50);
    }
    return true;
  }
}
