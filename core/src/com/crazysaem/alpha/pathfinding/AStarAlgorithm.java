package com.crazysaem.alpha.pathfinding;

import java.util.*;

/**
 * Created by crazysaem on 14.06.2014.
 */
public class AStarAlgorithm
{
  private AStarGraph aStarGraph;
  private HashMap<Node, Node> closedList;
  private HashMap<Node, Node> openList;
  private TreeSet<Node> openListFSorted;

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

  public AStarAlgorithm(AStarGraph aStarGraph)
  {
    this.aStarGraph = aStarGraph;
    closedList = new HashMap<Node, Node>();
    openList = new HashMap<Node, Node>();
    openListFSorted = new TreeSet<Node>(new NodeScoreComparator());
  }

  public boolean calculatePath(int startX, int startZ, int goalX, int goalZ)
  {
    openList.clear();
    openListFSorted.clear();
    closedList.clear();

    //Get starting Node:
    Node startingNode = aStarGraph.getNode(startX, startZ);
    startingNode.f = 0;
    openList.put(startingNode, startingNode);
    openListFSorted.add(startingNode);

    while (!openList.isEmpty())
    {
      Node q = getNodeWithLowestFScoreFromOpenList();
      openList.remove(q);
      openListFSorted.remove(q);
      closedList.put(q, q);
      List<Node> adjacentNodes = getAdjacentNodes(q);
      for (Node successor : adjacentNodes)
      {
        if (successor.x == goalX && successor.z == goalZ)
        {
          //We found the goal node! Now calculate the path via the parents.
          successor.parent = q;
          return true;
        }

        if (!closedList.containsKey(successor))
        {
          int g = 14;
          if (successor.x == q.x || successor.z == q.z)
            g = 10;

          successor.h = (Math.abs(successor.x - goalX) + Math.abs(successor.z - goalZ)) * 10;

          if (openList.containsKey(successor))
          {
            //Check if the path from successor to q is shorter than the current path to q
            if ((q.g + g) < successor.g)
            {
              successor.parent = q;
              successor.g = q.g + g;
              //Remove from list and add again after f was recalculated to keep the tree sorted
              openListFSorted.remove(successor);
              successor.f = successor.g + successor.h;
              openListFSorted.add(successor);
            }
          }
          else
          {
            successor.parent = q;
            successor.g = g + q.g;
            successor.f = successor.g + successor.h;
            openList.put(successor, successor);
            openListFSorted.add(successor);
          }
        }
      }
    }

    //No route was found
    return false;
  }

  public List<Node> getAdjacentNodes(Node node)
  {
    Node[] adjacentNodes = {node.L, node.TL, node.T, node.TR, node.R, node.BR, node.B, node.BL};
    List<Node> ret = new ArrayList<Node>();

    for (Node adjacentNode : adjacentNodes)
      if (adjacentNode != null)
        ret.add(adjacentNode);

    return ret;
  }

  public Node getNodeWithLowestFScoreFromOpenList()
  {
    if (openListFSorted.size() > 0)
      return openListFSorted.first();

    return null;
  }
}
