package com.crazysaem.alpha.picking;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.crazysaem.alpha.events.EventTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crazysaem on 07.06.2014.
 */
public class StaticTargetPool
{
  private List<StaticTarget> staticTargets;
  private EventTarget eventTarget = null;
  private Vector3 intersection = null;

  public StaticTargetPool()
  {
    staticTargets = new ArrayList<StaticTarget>();
  }

  public void add(StaticTarget staticTarget)
  {
    staticTargets.add(staticTarget);
  }

  /**
   * Returns the EventTarget for the closest object which intersects with the ray
   * @param ray
   * @return
   */
  public EventTarget collisonCheck(Ray ray)
  {
    float distance = Float.MAX_VALUE;
    eventTarget = null;
    StaticTarget lastStaticTarget = null;

    for (StaticTarget staticTarget : staticTargets)
    {
      float distanceTemp = staticTarget.getStaticRenderable().collisionTest(ray);
      if (distanceTemp > 0)
      {
        if (distanceTemp < distance)
        {
          distance = distanceTemp;
          eventTarget = staticTarget.getEventTarget();
          lastStaticTarget = staticTarget;
        }
      }
    }

    if (lastStaticTarget != null)
      intersection = lastStaticTarget.getStaticRenderable().getLastIntersection();

    return eventTarget;
  }

  /**
   * Returns true for the first object which is smaller/equal to the given distance to the ray
   * @param ray
   * @param distance
   * @return
   */
  public boolean collisonCheck(Ray ray, float distance)
  {
    for (StaticTarget staticTarget : staticTargets)
    {
      if (staticTarget.getStaticRenderable().collisionTest(ray, distance))
      {
        intersection = staticTarget.getStaticRenderable().getLastIntersection();
        return true;
      }
    }

    return false;
  }

  public Vector3 getLastIntersection()
  {
    return intersection;
  }
}
