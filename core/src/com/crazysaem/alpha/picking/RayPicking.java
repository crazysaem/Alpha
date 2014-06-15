package com.crazysaem.alpha.picking;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.collision.Ray;
import com.crazysaem.alpha.events.*;

/**
 * Created by crazysaem on 07.06.2014.
 */
public class RayPicking implements InputProcessor
{
  private int touchDownX, touchDownY;
  private Camera cam;
  private EventManager eventManager;
  private StaticTargetPool staticTargetPoolInteraction;
  private StaticTargetPool staticTargetPoolObfuscation;

  public RayPicking(Camera cam, EventManager eventManager, StaticTargetPool staticTargetPoolInteraction, StaticTargetPool staticTargetPoolObfuscation)
  {
    this.cam = cam;
    this.eventManager = eventManager;
    this.staticTargetPoolInteraction = staticTargetPoolInteraction;
    this.staticTargetPoolObfuscation = staticTargetPoolObfuscation;
  }

  @Override
  public boolean keyDown(int keycode)
  {
    return false;
  }

  @Override
  public boolean keyUp(int keycode)
  {
    return false;
  }

  @Override
  public boolean keyTyped(char character)
  {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button)
  {
    touchDownX = screenX;
    touchDownY = screenY;
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button)
  {
    if (touchDownX == screenX && touchDownY == screenY)
    {
      Ray ray = cam.getPickRay(screenX, screenY);
      EventTarget eventTarget = staticTargetPoolInteraction.collisonCheck(ray);
      if (eventTarget != null)
      {
        if (eventTarget == EventTarget.ASTAR_GROUND && staticTargetPoolObfuscation.collisonCheck(ray) != null)
          return false;

        eventManager.addEvent(new HitEvent(eventTarget, "TAP", staticTargetPoolInteraction.getLastIntersection()));
      }
    }
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer)
  {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY)
  {
    return false;
  }

  @Override
  public boolean scrolled(int amount)
  {
    return false;
  }
}
