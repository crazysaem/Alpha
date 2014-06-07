package com.crazysaem.alpha.picking;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.collision.Ray;
import com.crazysaem.alpha.events.Event;
import com.crazysaem.alpha.events.EventHandler;
import com.crazysaem.alpha.events.EventManager;
import com.crazysaem.alpha.events.EventTarget;

/**
 * Created by crazysaem on 07.06.2014.
 */
public class RayPicking implements InputProcessor
{
  private int touchDownX, touchDownY;
  private Camera cam;
  private EventManager eventManager;
  private StaticTargetPool staticTargetPool;

  public RayPicking(Camera cam, EventManager eventManager, StaticTargetPool staticTargetPool)
  {
    this.cam = cam;
    this.eventManager = eventManager;
    this.staticTargetPool = staticTargetPool;
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
      EventTarget eventTarget = staticTargetPool.collisonCheck(ray);
      if (eventTarget != null)
      {
        eventManager.addEvent(new Event(eventTarget, "HIT"));
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
