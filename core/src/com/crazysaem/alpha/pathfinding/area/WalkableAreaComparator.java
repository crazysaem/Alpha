package com.crazysaem.alpha.pathfinding.area;

import java.util.Comparator;

/**
 * Created by crazysaem on 04.07.2014.
 */
public class WalkableAreaComparator implements Comparator<WalkableArea>
{
  @Override
  public int compare(WalkableArea area1, WalkableArea area2)
  {
    if (area1.x0 > area2.x0)
    {
      return 1;
    }
    else if (area1.x0 < area2.x0)
    {
      return -1;
    }
    else if (area1.z0 > area2.z0)
    {
      return 1;
    }
    else if (area1.z0 < area2.z0)
    {
      return -1;
    }
    else
    {
      return 0;
    }
  }
}