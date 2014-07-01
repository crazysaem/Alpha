package com.crazysaem.alpha.pathfinding;

/**
 * Created by crazysaem on 14.06.2014.
 */
public class Node
{
  //Position
  public float x;
  public float z;

  //Connection to other Nodes
  public Node
      TL, T, TR,
      L, R,
      BL, B, BR;

  //Used for scoring:
  public Node parent;

  public int g;
  public int h;
  public int f;

  public boolean isSpecial;

  protected Node() {}

  public Node(float x, float z)
  {
    this.x = x;
    this.z = z;
  }

  public Node(int x, int z)
  {
    this.x = x;
    this.z = z;
  }
}
