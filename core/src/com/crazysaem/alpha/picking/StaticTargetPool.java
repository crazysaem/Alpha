package com.crazysaem.alpha.picking;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.collision.BoundingBox;
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
    for (StaticTarget staticTarget : staticTargets)
    {
      if (staticTarget.getStaticRenderable().collisionTest(ray))
        return staticTarget.getEventTarget();
    }

    return null;
  }
}
