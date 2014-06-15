package com.crazysaem.alpha.pathfinding;

import com.crazysaem.alpha.events.*;

import java.util.*;

/**
 * Created by crazysaem on 14.06.2014.
 */
public class AStarAlgorithm implements EventHandler
{
  private AStarGraph aStarGraph;
  private EventManager eventManager;
  private StartPosition startPosition;
  private HashMap<Node, Node> closedList;
  private HashMap<Node, Node> openList;
  private TreeSet<Node> openListFSorted;

  public AStarAlgorithm(AStarGraph aStarGraph, EventManager eventManager, StartPosition startPosition)
  {
    this.aStarGraph = aStarGraph;
    this.eventManager = eventManager;
    this.startPosition = startPosition;

    closedList = new HashMap<Node, Node>();
    openList = new HashMap<Node, Node>();
    openListFSorted = new TreeSet<Node>(new NodeScoreComparator());
  }

  @Override
  public void handleEvent(Event event)
  {
    if (event instanceof HitEvent)
    {
      HitEvent hitEvent = (HitEvent) event;

      switch (hitEvent.getEventTarget())
      {
        case ASTAR_GROUND:
        case ASTAR_FLOOR:
          //TODO: get closest Node position for given hitPosition
          int goalX = (int) hitEvent.getHitPos().x;
          int goalZ = (int) hitEvent.getHitPos().z;
          Path path = calculatePath(startPosition.getX(), startPosition.getZ(), goalX, goalZ);
          if (path != null)
            eventManager.addEvent(new MoveEvent(EventTarget.ELEPHANT, "MOVE", path));
          break;
      }
    }
  }

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

  public Path calculatePath(int startX, int startZ, int goalX, int goalZ)
  {
    if (startX == goalX && startZ == goalZ)
      return null;

    openList.clear();
    openListFSorted.clear();
    closedList.clear();

    //Get starting Node:
    Node startingNode = aStarGraph.getNode(startX, startZ);
    startingNode.parent = null;
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
          return new Path(successor);
        }

        if (!closedList.containsKey(successor))
        {
          int g = 14;
          if (successor.x == q.x || successor.z == q.z)
            g = 10;

          successor.h = (Math.abs(successor.x - goalX) + Math.abs(successor.z - goalZ)) * 10;

          if (openList.containsKey(successor))
          {
            //Check if the path to the successor through q is shorter
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
    return null;
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
