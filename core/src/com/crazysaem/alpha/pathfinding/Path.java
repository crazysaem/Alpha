package com.crazysaem.alpha.pathfinding;

import com.badlogic.gdx.math.Vector3;
import com.crazysaem.alpha.pathfinding.node.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crazysaem on 15.06.2014.
 */
public class Path implements PositionPerTime
{
  private float angle;
  private List<Vector3> positions;
  private PathPoint currentPathPoint;
  private float time;
  public Path(Node lastNode)
  {
    positions = new ArrayList<Vector3>();
    addPositionRecursively(lastNode);

    Object[] positionsArray = positions.toArray();

    PathPoint startPoint = new PathPoint((Vector3) positionsArray[0]);
    PathPoint pathPoint = startPoint;

    for (int i = 0; i < positionsArray.length - 1; i++)
    {
      pathPoint.nextPathPoint = new PathPoint((Vector3) positionsArray[i + 1]);
      //pathPoint.calculateDistance();
      pathPoint = pathPoint.nextPathPoint;
    }

    currentPathPoint = startPoint;
    angle = -1.0f;
  }

  public void prependPosition(float x, float z)
  {
    PathPoint pathPoint = new PathPoint(x, z);
    pathPoint.nextPathPoint = currentPathPoint;
    currentPathPoint = pathPoint;
    //pathPoint.calculateDistance();
  }

  public void appendPosition(float x, float z)
  {
    if (currentPathPoint.nextPathPoint == null)
    {
      return;
    }

    PathPoint pathPoint = new PathPoint(x, z);
    PathPoint lastPathPoint = currentPathPoint;
    do
    {
      lastPathPoint = lastPathPoint.nextPathPoint;
    } while (lastPathPoint.nextPathPoint != null);

    lastPathPoint.nextPathPoint = pathPoint;
    //lastPathPoint.calculateDistance();
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

  public void initialize(AStarGraph aStarGraph)
  {
    optimizePath(aStarGraph);
    calculateDistances();
  }

  private void optimizePath(AStarGraph aStarGraph)
  {
    PathPoint point0 = currentPathPoint;
    PathPoint point1 = null;

    while (true)
    {
      if (point1 == null)
      {
        if (point0.nextPathPoint != null)
        {
          if (point0.nextPathPoint.nextPathPoint != null)
          {
            point1 = point0.nextPathPoint.nextPathPoint;
          }
          else
          {
            return;
          }
        }
        else
        {
          return;
        }
      }

      if (aStarGraph.isLineInWalkableArea(point0.x, point0.z, point1.x, point1.z))
      {
        point0.nextPathPoint = null;
        point0.nextPathPoint = point1;

        if (point1.nextPathPoint != null)
        {
          point1 = point1.nextPathPoint;
        }
        else
        {
          return;
        }
      }
      else
      {
        if (point0.nextPathPoint != null)
        {
          point0 = point0.nextPathPoint;
        }
        else
        {
          return;
        }
        point1 = null;
      }
    }
  }

  private void calculateDistances()
  {
    PathPoint point = currentPathPoint;

    while (point != null)
    {
      point.calculateDistance();
      point = point.nextPathPoint;
    }
  }

  @Override
  public boolean getPosition(float speed, float delta, Vector3 position)
  {
    time += delta;

    float interpolation = speed * time;

    if (currentPathPoint.distance < interpolation)
    {
      time -= currentPathPoint.distance / speed;
      interpolation = speed * time;
      currentPathPoint = currentPathPoint.nextPathPoint;
    }

    if (currentPathPoint.nextPathPoint == null)
    {
      position.x = currentPathPoint.x;
      position.z = currentPathPoint.z;

      return true;
    }

    float inter = interpolation / currentPathPoint.distance;
    if (inter > 1.0f)
    {
      inter = 1.0f;
    }
    float xDelta = (currentPathPoint.nextPathPoint.x - currentPathPoint.x) * inter;
    float zDelta = (currentPathPoint.nextPathPoint.z - currentPathPoint.z) * inter;

    position.x = currentPathPoint.x + xDelta;
    position.z = currentPathPoint.z + zDelta;

    return false;
  }

  @Override
  public float getAngle()
  {
    return angle;
  }

  public void setAngle(float angle)
  {
    this.angle = angle;
  }

  class PathPoint
  {
    public float x, z;
    public float distance;
    public PathPoint nextPathPoint;

    public PathPoint(Vector3 position)
    {
      x = position.x;
      z = position.z;
    }

    public PathPoint(float x, float z)
    {
      this.x = x;
      this.z = z;
    }

    public void calculateDistance()
    {
      if (nextPathPoint != null)
      {
        distance = (float) Math.sqrt((float) Math.pow(x - nextPathPoint.x, 2) + (float) Math.pow(z - nextPathPoint.z, 2));
      }
    }
  }
}
