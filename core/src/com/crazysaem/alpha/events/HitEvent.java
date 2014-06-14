package com.crazysaem.alpha.events;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by crazysaem on 14.06.2014.
 */
public class HitEvent extends Event
{
  private Vector3 hitPos;

  public HitEvent(EventTarget eventTarget, String action, Vector3 hitPos)
  {
    super(eventTarget, action);

    this.hitPos = hitPos;
  }

  public Vector3 getHitPos()
  {
    return hitPos;
  }
}
