package com.crazysaem.alpha.pathfinding;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crazysaem on 15.06.2014.
 */
public class Path implements PositionPerTime
{
  private List<Vector3> positions;

  public Path(Node goalNode)
  {
    positions = new ArrayList<Vector3>();

    addPositionRecursively(goalNode);
  }

  private void addPositionRecursively(Node n)
  {
    if (n.parent == null)
    {
      positions.add(new Vector3(n.x, 0.0f, n.z));
      return;
    }

    addPositionRecursively(n.parent);
    positions.add(new Vector3(n.x, 0.0f, n.z));
  }

  private void addAdditionalPosition(Vector3 position)
  {
    positions.add(position);
  }

  @Override
  public boolean getPosition(float speed, float time, Vector3 position)
  {
    float interpolation = speed * time;
    int pos = (int)interpolation;
    if (pos >= positions.size() - 1)
    {
      Vector3 posFromArray = positions.get(positions.size() - 1);
      position.x = posFromArray.x;
      position.z = posFromArray.z;

      return true;
    }

    Vector3 posFromArray = positions.get(pos);
    Vector3 posFromArray2 = positions.get(pos + 1);
    float interpolationDelta = interpolation - pos;
    float xDelta = (posFromArray2.x - posFromArray.x) * interpolationDelta;
    float zDelta = (posFromArray2.z - posFromArray.z) * interpolationDelta;

    position.x = posFromArray.x + xDelta;
    position.z = posFromArray.z + zDelta;

    return false;
  }
}
