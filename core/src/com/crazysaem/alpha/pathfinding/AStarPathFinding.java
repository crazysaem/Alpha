package com.crazysaem.alpha.pathfinding;

import com.crazysaem.alpha.events.*;
import com.crazysaem.alpha.pathfinding.node.Node;
import com.crazysaem.alpha.pathfinding.node.NodeScoreComparator;

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

  public AStarPathFinding(AStarGraph aStarGraph, EventManager eventManager, Map<EventTarget, AStarPosition> astarPositions)
  {
    this.aStarGraph = aStarGraph;
    this.eventManager = eventManager;
    this.astarPositions = astarPositions;

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

  public Path calculatePath(float startX, float startZ, float goalX, float goalZ)
  {
    if (startX == goalX && startZ == goalZ)
      return null;

    openList.clear();
    openListFSorted.clear();
    closedList.clear();

    Node startingNode = aStarGraph.getApproximateNode(startX, startZ);
    boolean prependStartingNode = aStarGraph.getApproximateNodeFlag();
    if (startingNode == null)
      return null;

    Node goalNode = aStarGraph.getApproximateNode(goalX, goalZ);
    boolean appendGoalNode = aStarGraph.getApproximateNodeFlag();
    if (goalNode == null)
      return null;

    if (startingNode == goalNode)
      return null;

    //Get starting Node:
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
          Path path = new Path(successor);

          if (prependStartingNode)
            path.prependPosition(startX, startZ);

          if (appendGoalNode)
            path.appendPosition(goalX, goalZ);

          return path;
        }

        if (!closedList.containsKey(successor))
        {
          int g = 14;
          if (successor.x == q.x || successor.z == q.z)
            g = 10;

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
