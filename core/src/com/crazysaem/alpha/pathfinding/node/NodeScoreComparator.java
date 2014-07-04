package com.crazysaem.alpha.pathfinding.node;

import java.util.Comparator;

/**
 * Created by crazysaem on 04.07.2014.
 */
public class NodeScoreComparator implements Comparator<Node>
{
  @Override
  public int compare(Node n1, Node n2)
  {
    if (n1.f > n2.f)
      return 1;
    else if (n1.f < n2.f)
      return -1;
    else if (n1.x > n2.x)
      return 1;
    else if (n1.x < n2.x)
      return -1;
    else if (n1.z > n2.z)
      return 1;
    else if (n1.z < n2.z)
      return -1;
    else
      return 0;
  }
}