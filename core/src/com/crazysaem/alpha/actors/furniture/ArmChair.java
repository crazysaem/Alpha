package com.crazysaem.alpha.actors.furniture;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.crazysaem.alpha.actors.protagonist.Elephant;
import com.crazysaem.alpha.messages.ChangeAnimationMessage;
import com.crazysaem.alpha.messages.FinishedMessage;
import com.crazysaem.alpha.messages.MoveMessage;
import com.crazysaem.alpha.pathfinding.Angle;
import com.crazysaem.alpha.pathfinding.Position;
import com.crazysaem.alpha.picking.CollisionRenderable;

/**
 * Created by crazysaem on 07.06.2014.
 */
public class ArmChair extends CollisionRenderable implements Position, Angle, Telegraph
{
  @Override
  protected void finishLoading()
  {
    super.finishLoading(false, "ArmChair");
  }

  @Override
  public float getX()
  {
    return 0.0f;
  }

  @Override
  public float getZ()
  {
    return -3.0f;
  }

  @Override
  public float getAngle()
  {
    return 0;
  }

  @Override
  public boolean handleMessage(Telegram msg)
  {
    if (msg.message == FinishedMessage.MESSAGE_CODE && msg.extraInfo instanceof FinishedMessage &&
        ((FinishedMessage) msg.extraInfo).getFinishedMessageCode() == MoveMessage.MESSAGE_CODE)
    {
      MessageDispatcher.getInstance().dispatchMessage(0.0f, this, msg.sender, ChangeAnimationMessage.MESSAGE_CODE, new ChangeAnimationMessage(Elephant.SITTING, 1, 1.0f));

      return true;
    }

    return false;
  }
}
