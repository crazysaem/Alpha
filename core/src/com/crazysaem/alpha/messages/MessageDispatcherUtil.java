package com.crazysaem.alpha.messages;

import com.badlogic.gdx.ai.Agent;
import com.badlogic.gdx.ai.msg.MessageDispatcher;

/**
 * Created by crazysaem on 13.08.2014.
 */
public class MessageDispatcherUtil
{
  public static void addListeners(int msg, Agent... listeners)
  {
    for (Agent listener : listeners)
    {
      MessageDispatcher.getInstance().addListener(msg, listener);
    }
  }
}
