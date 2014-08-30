package com.crazysaem.alpha.pathfinding;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by crazysaem on 15.06.2014.
 */
public interface PositionPerTime
{
  public boolean getPosition(float speed, float time, Vector3 position);

  public float getAngle();
}
