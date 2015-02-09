package com.crazysaem.alpha.messages;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegraph;

/**
 * Created by crazysaem on 13.08.2014.
 */
public class MessageDispatcherUtil
{
  public static void addListeners(int msg, Telegraph... listeners)
  {
    for (Telegraph listener : listeners)
    {
      MessageManager.getInstance().addListener(listener, msg);
    }
  }
}
