package com.crazysaem.alpha.pathfinding;

/**
 * Created by crazysaem on 14.06.2014.
 */
public class ScoredNode extends Node
{
  private Node parent;

  private int g;
  private int h;
  private int f;

  public ScoredNode(ScoredNode parent, Node node)
  {
    this.parent = parent;

    this.x = node.x;
    this.z = node.z;

    this.L = node.L;
    this.TL = node.TL;
    this.T = node.T;
    this.TR = node.TR;
    this.R = node.R;
    this.BR = node.BR;
    this.B = node.B;
    this.BL = node.BL;
  }

  public ScoredNode(ScoredNode parent, Node node, boolean isDiagonal, int goalX, int goalZ)
  {
    this(parent, node);

    if (isDiagonal)
      g = 14;
    else
      g = 10;

    g += parent.g;
    h = (Math.abs(x - goalX) + Math.abs(z - goalZ)) * 10;
    f = h + g;
  }

  public int getScore()
  {
    return f;
  }
}
