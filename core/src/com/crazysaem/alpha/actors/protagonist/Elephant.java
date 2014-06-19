package com.crazysaem.alpha.actors.protagonist;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.crazysaem.alpha.events.Event;
import com.crazysaem.alpha.events.EventHandler;
import com.crazysaem.alpha.events.MoveEvent;
import com.crazysaem.alpha.graphics.Renderable;
import com.crazysaem.alpha.pathfinding.AstarPosition;
import com.crazysaem.alpha.pathfinding.PositionPerTime;

/**
 * Created by crazysaem on 23.05.2014.
 */
public class Elephant extends Renderable implements EventHandler, AstarPosition, AnimationController.AnimationListener
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
  private ElephantPose currentPose, nextPose;
  private boolean animationInProgress;

  protected void finishLoading()
  {
    super.finishLoading("Elephant", "TShirt", "Trousers", "ElephantArmature");

    animationController.setAnimation(IDLE, -1);
    position = new Vector3();
    deltaPosition = new Vector3();
    initialDirection = new Vector3(0.0f, 0.0f, 1.0f);
    direction = new Vector3(0.0f, 0.0f, 1.0f);
    upVector = new Vector3(0.0f, 1.0f, 0.0f);
    movePosition = new Vector3();
    currentPose = ElephantPose.STANDING;
    nextPose = ElephantPose.NONE;
    animationInProgress = false;
  }

  @Override
  public void update(float delta)
  {
    super.update(delta);

    if (animationInProgress)
      return;

    if (isMoving)
    {
      if (currentPose == ElephantPose.SITTING)
      {
        currentPose = ElephantPose.STANDING;
        animationController.setAnimation("sitting", 1, -1.0f, this);
        animationController.queue(WALK, -1, 1.0f, null, 0.0f);
        animationInProgress = true;
        return;
      }

      time += delta;
      if (positionPerTime.getPosition(2.0f, time, movePosition))
      {
        isMoving = false;
        deltaPosition.x = 0.0f;
        deltaPosition.y = 0.0f;
        deltaPosition.z = 0.0f;
        switch (nextPose)
        {
          case SITTING:
            currentPose = ElephantPose.SITTING;
            nextPose = ElephantPose.NONE;
            animationController.setAnimation("sitting", 1, 1.0f, this);
            animationInProgress = true;
            direction.x = 0.0f;
            direction.z = 0.0f;
            direction = direction.nor();
            modelInstance.transform.setToTranslation(movePosition.x, 0.0f, movePosition.z).rotate(upVector, 0);
            break;

          default:
            animationController.setAnimation(IDLE, -1);
            break;
        }
      }
      else
      {
        direction.x = movePosition.x - position.x;
        direction.z = movePosition.z - position.z;
        deltaPosition.x = direction.x;
        deltaPosition.z = direction.z;
        direction = direction.nor();

        directionAngle = (float) Math.acos(initialDirection.dot(direction)) * 57.3f;
        if (direction.x < 0)
          directionAngle = 360 - directionAngle;

        modelInstance.transform.setToTranslation(movePosition.x, 0.0f, movePosition.z).rotate(upVector, directionAngle);
      }

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
      if (currentPose == ElephantPose.STANDING)
        animationController.setAnimation(WALK, -1);
      time = 0.0f;
      if (moveEvent.getAction() == "SITTING")
      {
        nextPose = ElephantPose.SITTING;
      }
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

  @Override
  public float getX()
  {
    return position.x;
  }

  @Override
  public float getZ()
  {
    return position.z;
  }

  public float getDeltaX()
  {
    return deltaPosition.x;
  }

  public float getDeltaZ()
  {
    return deltaPosition.z;
  }

  public float getDirectionAngle()
  {
    return directionAngle;
  }

  @Override
  public void onEnd(AnimationController.AnimationDesc animation)
  {
    animationInProgress = false;
  }

  @Override
  public void onLoop(AnimationController.AnimationDesc animation)
  {

  }
}
