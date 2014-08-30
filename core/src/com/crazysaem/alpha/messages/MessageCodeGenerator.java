package com.crazysaem.alpha.messages;

/**
 * Created by crazysaem on 30.08.2014.
 */
public class MessageCodeGenerator
{
  private static int messageCode = -1;

  public static int getNewMessageCode()
  {
    messageCode++;
    return messageCode;
  }
}
