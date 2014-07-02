package com.crazysaem.alpha.pathfinding;

import com.crazysaem.alpha.events.*;

import java.util.*;

/**
 * Created by crazysaem on 14.06.2014.
 */
public class AStarPathFinding implements EventHandler
{
  private AStarGraph aStarGraph;
  private EventManager eventManager;
  private Map<EventTarget, AStarPosition> astarPositions;
  private HashMap<Node, Node> closedList;
  private HashMap<Node, Node> openList;
  private TreeSet<Node> openListFSorted;
  private Stack<Node> backedupNodes;

  public AStarPathFinding(AStarGraph aStarGraph, EventManager eventManager, Map<EventTarget, AStarPosition> astarPositions)
  {
    this.aStarGraph = aStarGraph;
    this.eventManager = eventManager;
    this.astarPositions = astarPositions;

    closedList = new HashMap<Node, Node>();
    openList = new HashMap<Node, Node>();
    openListFSorted = new TreeSet<Node>(new NodeScoreComparator());
    backedupNodes = new Stack<Node>();
  }

  @Override
  public void handleEvent(Event event)
  {
    if (event instanceof HitEvent)
    {
      HitEvent hitEvent = (HitEvent) event;
      Path path;
      AStarPosition elephantPosition = astarPositions.get(EventTarget.ELEPHANT);

      switch (hitEvent.getEventTarget())
      {
        case ASTAR_GROUND:
        case ASTAR_FLOOR:
          //TODO: get closest Node position for given hitPosition
          float goalX = hitEvent.getHitPos().x;
          float goalZ = hitEvent.getHitPos().z;
          path = calculatePath(elephantPosition.getX(), elephantPosition.getZ(), goalX, goalZ);
          if (path != null)
            eventManager.addEvent(new MoveEvent(EventTarget.ELEPHANT, "WALKING", path));
          break;

        case ASTAR_ARMCHAIR:
          AStarPosition armChairGoal = astarPositions.get(EventTarget.ARMCHAIR);
          path = calculatePath(elephantPosition.getX(), elephantPosition.getZ(), armChairGoal.getX(), armChairGoal.getZ());
          if (path != null)
            eventManager.addEvent(new MoveEvent(EventTarget.ELEPHANT, "SITTING", path));
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

  public void backupNodes(int x, int z)
  {
    if (aStarGraph.getNode(x + 0, z + 0) != null)
      backedupNodes.add(aStarGraph.getNode(x + 0, z + 0).TR);
    if (aStarGraph.getNode(x + 1, z + 0) != null)
      backedupNodes.add(aStarGraph.getNode(x + 1, z + 0).TL);
    if (aStarGraph.getNode(x + 0, z + 1) != null)
      backedupNodes.add(aStarGraph.getNode(x + 0, z + 1).BR);
    if (aStarGraph.getNode(x + 1, z + 1) != null)
      backedupNodes.add(aStarGraph.getNode(x + 1, z + 1).BL);
  }

  public void restoreNodes(int x, int z)
  {
    if (aStarGraph.getNode(x + 1, z + 1) != null)
      aStarGraph.getNode(x + 1, z + 1).BL = backedupNodes.pop();
    if (aStarGraph.getNode(x + 0, z + 1) != null)
      aStarGraph.getNode(x + 0, z + 1).BR = backedupNodes.pop();
    if (aStarGraph.getNode(x + 1, z + 0) != null)
      aStarGraph.getNode(x + 1, z + 0).TL = backedupNodes.pop();
    if (aStarGraph.getNode(x + 0, z + 0) != null)
      aStarGraph.getNode(x + 0, z + 0).TR = backedupNodes.pop();
  }

  public void setNodeFloat(int x, int z, Node node)
  {
    Node n;
    if ((n = aStarGraph.getNode(x + 0, z + 0)) != null)
    {
      n.TR = node;
      node.BL = n;
    }
    if ((n = aStarGraph.getNode(x + 1, z + 0)) != null)
    {
      n.TL = node;
      node.BR = n;
    }
    if ((n = aStarGraph.getNode(x + 0, z + 1)) != null)
    {
      n.BR = node;
      node.TL = n;
    }
    if ((n = aStarGraph.getNode(x + 1, z + 1)) != null)
    {
      n.BL = node;
      node.TR = n;
    }
  }

  public void cleanup(int startXInt, int startZInt, int goalXInt, int goalZInt)
  {
    restoreNodes(goalXInt, goalZInt);
    restoreNodes(startXInt, startZInt);
  }

  public boolean isPosAvailable(float x_, float z_)
  {
    int x = (int) x_;
    int z = (int) z_;

    if (aStarGraph.getNode(x + 0, z + 0) != null && aStarGraph.getNode(x + 0, z + 0).TR != null &&
        aStarGraph.getNode(x + 1, z + 0) != null && aStarGraph.getNode(x + 1, z + 0).TL != null &&
        aStarGraph.getNode(x + 0, z + 1) != null && aStarGraph.getNode(x + 0, z + 1).BR != null &&
        aStarGraph.getNode(x + 1, z + 1) != null && aStarGraph.getNode(x + 1, z + 1).BL != null)
      return true;

    return false;
  }

  public Path calculatePath(float startX, float startZ, float goalX, float goalZ)
  {
    if (startX == goalX && startZ == goalZ || !isPosAvailable(goalX, goalZ))
      return null;

    openList.clear();
    openListFSorted.clear();
    closedList.clear();

    Node startingNode = new Node(startX, startZ);
    startingNode.isSpecial = true;
    int startXInt = (int) startX;
    int startZInt = (int) startZ;
    backupNodes(startXInt, startZInt);
    setNodeFloat(startXInt, startZInt, startingNode);

    Node goalNode = new Node(goalX, goalZ);
    goalNode.isSpecial = true;
    int goalXInt = (int) goalX;
    int goalZInt = (int) goalZ;
    backupNodes(goalXInt, goalZInt);
    setNodeFloat(goalXInt, goalZInt, goalNode);

    //Get starting Node:
    //Node startingNode = aStarGraph.getNode(startX, startZ);
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
        if (successor == goalNode)
        {
          //We found the goal node! Now calculate the path via the parents.
          successor.parent = q;
          cleanup(startXInt, startZInt, goalXInt, goalZInt);
          return new Path(successor);
        }

        if (!closedList.containsKey(successor))
        {
          int g = 14;
          if (successor.isSpecial || q.isSpecial)
          {
            float xDiff = successor.x - q.x;
            float zDiff = successor.z - q.z;
            g = (int) Math.sqrt(xDiff * xDiff + zDiff + zDiff);
          }
          else
          {
            if (successor.x == q.x || successor.z == q.z)
              g = 10;
          }

          successor.h = (int) ((Math.abs(successor.x - goalX)) + Math.abs(successor.z - goalZ)) * 10;

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
    cleanup(startXInt, startZInt, goalXInt, goalZInt);
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
