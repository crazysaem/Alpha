package com.crazysaem.alpha.actors.protagonist;

import com.badlogic.gdx.math.Vector3;
import com.crazysaem.alpha.events.Event;
import com.crazysaem.alpha.events.EventHandler;
import com.crazysaem.alpha.events.MoveEvent;
import com.crazysaem.alpha.graphics.Renderable;
import com.crazysaem.alpha.pathfinding.Position;
import com.crazysaem.alpha.pathfinding.PositionPerTime;

/**
 * Created by crazysaem on 23.05.2014.
 */
public class Elephant extends Renderable implements EventHandler, Position
{
  private static final String IDLE = "idle";
  private static final String WALK = "walk";
  private static final String CARROT = "carrot";

  private Vector3 position, deltaPosition, initialDirection, direction, upVector;
  private PositionPerTime positionPerTime;
  private Vector3 movePosition;
  private float time;
  private boolean isMoving;
  private float directionAngle;

  protected void finishLoading()
  {
    super.finishLoading("Elephant", "ElephantArmature");

    animationController.setAnimation(IDLE, -1);
    position = new Vector3();
    deltaPosition = new Vector3();
    initialDirection = new Vector3(0.0f, 0.0f, 1.0f);
    direction = new Vector3(0.0f, 0.0f, 1.0f);
    upVector = new Vector3(0.0f, 1.0f, 0.0f);
    movePosition = new Vector3();
  }

  @Override
  public void update(float delta)
  {
    super.update(delta);

    if (isMoving)
    {
      time += delta;
      if (positionPerTime.getPosition(2.0f, time, movePosition))
      {
        isMoving = false;
        deltaPosition.x = 0.0f;
        deltaPosition.y = 0.0f;
        deltaPosition.z = 0.0f;
        animationController.setAnimation(IDLE, -1);
      }

      direction.x = movePosition.x - position.x;
      direction.z = movePosition.z - position.z;
      deltaPosition.x = direction.x;
      deltaPosition.z = direction.z;
      direction = direction.nor();

      directionAngle = (float) Math.acos((double) initialDirection.dot(direction)) * 57.3f;
      if (direction.x < 0)
        directionAngle = 360 - directionAngle;

      //modelInstance.transform.setToLookAt(direction, upVector);
      //modelInstance.transform.setToTranslation(movePosition.x, 0.0f, movePosition.z).setToLookAt(direction, upVector);
      //modelInstance.transform.setToLookAt(movePosition, direction, upVector);

      /*Matrix4 m0 = modelInstance.transform.setToTranslation(movePosition.x, 0.0f, movePosition.z).cpy();
      Matrix4 m1 = modelInstance.transform.setToLookAt(direction, upVector).cpy();
      modelInstance.transform.idt().mul(m0).mul(m1);*/
      modelInstance.transform.setToTranslation(movePosition.x, 0.0f, movePosition.z).rotate(upVector, directionAngle);

      position.x = movePosition.x;
      position.z = movePosition.z;
    }
  }

  @Override
  public void handleEvent(Event event)
  {
    if (event instanceof MoveEvent)
    {
      MoveEvent moveEvent = (MoveEvent) event;
      positionPerTime = moveEvent.getPositionPerTime();
      animationController.setAnimation(WALK, -1);
      time = 0.0f;
      isMoving = true;
    }
    else
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

  public boolean isMoving()
  {
    return isMoving;
  }

  public float getX()
  {
    return position.x;
  }

  public float getZ()
  {
    return position.z;
  }

  @Override
  public float getDeltaX()
  {
    return deltaPosition.x;
  }

  @Override
  public float getDeltaZ()
  {
    return deltaPosition.z;
  }
}
