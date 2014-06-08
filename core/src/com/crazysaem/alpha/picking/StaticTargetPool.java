package com.crazysaem.alpha.picking;

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

  public StaticTargetPool()
  {
    staticTargets = new ArrayList<StaticTarget>();
  }

  public void add(StaticTarget staticTarget)
  {
    staticTargets.add(staticTarget);
  }

  public EventTarget collisonCheck(Ray ray)
  {
    float distance = Float.MAX_VALUE;
    eventTarget = null;

    for (StaticTarget staticTarget : staticTargets)
    {
      float distanceTemp = staticTarget.getStaticRenderable().collisionTest(ray);
      if (distanceTemp > 0)
      {
        if (distanceTemp < distance)
        {
          distance = distanceTemp;
          eventTarget = staticTarget.getEventTarget();
        }
      }
    }

    return eventTarget;
  }
}
