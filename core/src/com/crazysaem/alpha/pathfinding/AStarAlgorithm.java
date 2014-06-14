package com.crazysaem.alpha.pathfinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crazysaem on 14.06.2014.
 */
public class AStarAlgorithm
{
  private AStarGraph aStarGraph;
  private List<ScoredNode> closedList;
  private List<ScoredNode> openList;

  private int goalX;
  private int goalZ;

  public AStarAlgorithm(AStarGraph aStarGraph)
  {
    this.aStarGraph = aStarGraph;
    closedList = new ArrayList<ScoredNode>();
    openList = new ArrayList<ScoredNode>();
  }

  public boolean calculatePath(int startX, int startZ, int goalX, int goalZ)
  {
    this.goalX = goalX;
    this.goalZ = goalZ;

    //Get starting Node:
    ScoredNode startingNode = new ScoredNode(null, aStarGraph.getNode(startX, startZ));
    openList.add(startingNode);

    while (!openList.isEmpty())
    {
      ScoredNode q = getNodeWithLowestFScoreFromOpenList();
      openList.remove(q);
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
          openList.add(successor);
      }
      closedList.add(q);
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
    for(ScoredNode n : openList)
    {
      if (n.x == node.x && n.z == node.z && n.getScore() < node.getScore())
        return true;
    }

    return false;
  }

  public boolean isNodeWithLowerFScoreFromClosedListAvaivable(ScoredNode node)
  {
    for(ScoredNode n : closedList)
    {
      if (n.x == node.x && n.z == node.z && n.getScore() < node.getScore())
        return true;
    }

    return false;
  }
}
