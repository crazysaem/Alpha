package com.crazysaem.alpha.actors;

import com.crazysaem.alpha.events.Event;
import com.crazysaem.alpha.events.EventHandler;
import com.crazysaem.alpha.graphics.Renderable;

/**
 * Created by crazysaem on 30.05.2014.
 */
public class Carrot extends Renderable implements EventHandler
{
  private static final String IDLE = "idle";
  private static final String CARROT = "carrot";

  @Override
  protected void finishLoading()
  {
    super.finishLoading("Carrot", "CarrotArmature");

    //The magic number is half the height of the pet.
    //TODO: Change this to not use it like a magic number but get it dynamically
    modelInstance.transform.setToTranslation(0, -1.1198419332504272f, 0);
    animationController.setAnimation(IDLE, -1);
  }

  @Override
  public void handleEvent(Event event)
  {
    String action = event.getAction();

    if (action.equals(IDLE))
    {
      animationController.setAnimation(IDLE, -1);
    }
    else if (action.equals(CARROT))
    {
      animationController.setAnimation(CARROT, 1);
      animationController.queue(IDLE, -1, 1.0f, null, 0.0f);
    }
    else
    {
      System.out.println("Carrot received unknown event: " + event.getAction());
    }
  }
}
