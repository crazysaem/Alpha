package com.crazysaem.alpha.actors;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.crazysaem.alpha.events.Event;
import com.crazysaem.alpha.events.EventHandler;
import com.crazysaem.alpha.graphics.Renderable;

/**
 * Created by crazysaem on 23.05.2014.
 */
public class Pet extends Renderable implements EventHandler
{
  private static final String IDLE = "idle";
  private static final String WALK = "walk";
  private static final String CARROT = "carrot";

  protected void finishLoading()
  {
    super.finishLoading("Elephant", "ElephantArmature");

    BoundingBox box = new BoundingBox();
    modelInstance.calculateBoundingBox(box);
    modelInstance.transform.setToTranslation(0, -box.getDimensions().y / 2, 0);
    animationController.setAnimation(IDLE, -1);
  }

  @Override
  public void handleEvent(Event event)
  {
    String action = event.getAction();

    if (action.equals(WALK))
    {
      animationController.animate(WALK, 5, null, 0.2f);
      animationController.queue(IDLE, -1, 1.0f, null, 0.3f);
    }
    else if (action.equals(CARROT))
    {
      animationController.animate(CARROT, 1, null, 0.2f);
      animationController.queue(IDLE, -1, 1.0f, null, 0.55f);
    }
    else
    {
      System.out.println("Pet received unknown event: " + event.getAction());
    }
  }
}
