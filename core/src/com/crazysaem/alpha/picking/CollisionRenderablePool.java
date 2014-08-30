package com.crazysaem.alpha.picking;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by crazysaem on 07.06.2014.
 */
public class CollisionRenderablePool
{
  private List<CollisionRenderable> collisionRenderables;
  private Vector3 intersection = null;

  public CollisionRenderablePool()
  {
    collisionRenderables = new ArrayList<CollisionRenderable>();
  }

  public CollisionRenderablePool(CollisionRenderable... _collisionRenderables)
  {
    this();
    add(_collisionRenderables);
  }

  public void add(CollisionRenderable... _collisionRenderables)
  {
    collisionRenderables.addAll(Arrays.asList(_collisionRenderables));
  }

  /**
   * Returns the EventTarget for the closest object which intersects with the ray
   *
   * @param ray
   * @return
   */
  public CollisionRenderable collisonCheck(Ray ray)
  {
    float distance = Float.MAX_VALUE;
    CollisionRenderable lastCollisionRenderable = null;

    for (CollisionRenderable collisionRenderable : collisionRenderables)
    {
      float distanceTemp = collisionRenderable.collisionTest(ray);
      if (distanceTemp > 0)
      {
        if (distanceTemp < distance)
        {
          distance = distanceTemp;
          lastCollisionRenderable = collisionRenderable;
        }
      }
    }

    if (lastCollisionRenderable != null)
    {
      intersection = lastCollisionRenderable.getLastIntersection();
    }

    return lastCollisionRenderable;
  }

  /**
   * Returns true for the first object which is smaller/equal to the given distance of the ray
   *
   * @param ray
   * @param distance
   * @return
   */
  public boolean collisonCheck(Ray ray, float distance)
  {
    for (CollisionRenderable collisionRenderable : collisionRenderables)
    {
      if (collisionRenderable.collisionTest(ray, distance))
      {
        intersection = collisionRenderable.getLastIntersection();
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
