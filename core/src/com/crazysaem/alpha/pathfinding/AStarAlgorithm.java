package com.crazysaem.alpha.pathfinding;

import java.util.*;

/**
 * Created by crazysaem on 14.06.2014.
 */
public class AStarAlgorithm
{
  private AStarGraph aStarGraph;
  private TreeSet<ScoredNode> closedList;
  private TreeSet<ScoredNode> openList;

  private int goalX;
  private int goalZ;

  public class ScoredNodeComparator implements Comparator<ScoredNode>
  {
    @Override
    public int compare(ScoredNode n1, ScoredNode n2) {
      if(n1.getScore() > n2.getScore())
        return 1;
      else
        if(n1.getScore() < n2.getScore()) return -1;
      else
        return 0;
    }
  }

  public AStarAlgorithm(AStarGraph aStarGraph)
  {
    this.aStarGraph = aStarGraph;
    closedList = new TreeSet<ScoredNode>(new ScoredNodeComparator());
    openList = new TreeSet<ScoredNode>(new ScoredNodeComparator());
  }

  public boolean calculatePath(int startX, int startZ, int goalX, int goalZ)
  {
    openList.clear();
    closedList.clear();

    this.goalX = goalX;
    this.goalZ = goalZ;

    //Get starting Node:
    ScoredNode startingNode = new ScoredNode(null, aStarGraph.getNode(startX, startZ));
    openList.add(startingNode);

    while (!openList.isEmpty())
    {
      ScoredNode q = getNodeWithLowestFScoreFromOpenList();
      openList.remove(q);
      closedList.add(q);
      List<ScoredNode> adjacentNodes = getAdjacentNodes(q);
      for (ScoredNode successor : adjacentNodes)
      {
        if (successor.x == goalX && successor.z == goalZ)
        {
          //We found the goal node! Now calculate the path via the parents.
          return true;
        }
        boolean openFlag = isNodeWithLowerFScoreFromOpenListAvaivable(successor);
        boolean closedFlag = isNodeWithLowerFScoreFromClosedListAvaivable(successor);
        if (!openFlag && ! closedFlag)
        {
          openList.add(successor);
        }
      }
    }

    return false;
  }

  public List<ScoredNode> getAdjacentNodes(ScoredNode node)
  {
    Node[] adjacentNodes = {node.L, node.TL, node.T, node.TR, node.R, node.BR, node.B, node.BL};
    boolean isDiagonal = false;
    List<ScoredNode> ret = new ArrayList<ScoredNode>();

    for (Node adjacentNode : adjacentNodes)
    {
      if (adjacentNode != null)
        ret.add(new ScoredNode(node, adjacentNode, isDiagonal, goalX, goalZ));
      isDiagonal = !isDiagonal;
    }

    return ret;
  }

  public ScoredNode getNodeWithLowestFScoreFromOpenList()
  {
    /*if (openList.size() > 0)
      return openList.first();

    return null;*/

    ScoredNode retNode = null;
    int minF = Integer.MAX_VALUE;

    for (ScoredNode node : openList)
    {
      if (node.getScore() < minF)
      {
        retNode = node;
        minF = node.getScore();
      }
    }

    return retNode;
  }

  public boolean isNodeWithLowerFScoreFromOpenListAvaivable(ScoredNode node)
  {
    for (ScoredNode n : openList)
    {
      //if (n.getScore() > node.getScore())
      //  return false;

      if (n.x == node.x && n.z == node.z && n.getScore() < node.getScore())
        return true;
    }

    return false;
  }

  public boolean isNodeWithLowerFScoreFromClosedListAvaivable(ScoredNode node)
  {
    for (ScoredNode n : closedList)
    {
      //if (n.getScore() > node.getScore())
      //  return false;

      if (n.x == node.x && n.z == node.z && n.getScore() < node.getScore())
        return true;
    }

    return false;
  }
}
