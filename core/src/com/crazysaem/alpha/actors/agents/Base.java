package com.crazysaem.alpha.actors.agents;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.crazysaem.alpha.actors.protagonist.Elephant;
import com.crazysaem.alpha.messages.ChangeAnimationMessage;
import com.crazysaem.alpha.messages.FinishedMessage;
import com.crazysaem.alpha.messages.MoveMessage;
import com.crazysaem.alpha.picking.CollisionRenderable;

/**
 * Created by crazysaem on 30.08.2014.
 */
public abstract class Base extends CollisionRenderable implements Telegraph
{
  @Override
  public boolean handleMessage(Telegram msg)
  {
    if (msg.message == FinishedMessage.MESSAGE_CODE && msg.extraInfo instanceof FinishedMessage &&
        ((FinishedMessage) msg.extraInfo).getFinishedMessageCode() == MoveMessage.MESSAGE_CODE)
    {
      MessageDispatcher.getInstance().dispatchMessage(0.0f, this, msg.sender, ChangeAnimationMessage.MESSAGE_CODE, new ChangeAnimationMessage(Elephant.STANDING, -1, 1.0f));

      return true;
    }

    return false;
  }
}
