package com.crazysaem.alpha.messages;

/**
 * Created by crazysaem on 30.08.2014.
 */
public class FinishedMessage
{
  public static final int MESSAGE_CODE = MessageCodeGenerator.getNewMessageCode();

  private int finishedMessageCode;

  public FinishedMessage(int finishedMessageCode)
  {
    this.finishedMessageCode = finishedMessageCode;
  }

  public int getFinishedMessageCode()
  {
    return finishedMessageCode;
  }
}
