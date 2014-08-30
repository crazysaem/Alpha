package com.crazysaem.alpha.pathfinding.node;

/**
 * Created by crazysaem on 14.06.2014.
 */
public class Node
{
  //Position
  public int x;
  public int z;

  //Connection to other Nodes
  public Node TL, T, TR, L, R, BL, B, BR;

  //Used for scoring:
  public Node parent;

  public int g;
  public int h;
  public int f;

  protected Node() {}

  public Node(int x, int z)
  {
    this.x = x;
    this.z = z;
  }

  public float calcDiastance(Node node)
  {
    return (float) Math.sqrt(Math.sqrt(Math.pow(Math.abs(x - node.x), 2) + Math.pow(Math.abs(z - node.z), 2)));
  }

  public float calcDiastance(float x, float z)
  {
    return (float) Math.sqrt(Math.sqrt(Math.pow(Math.abs(this.x - x), 2) + Math.pow(Math.abs(this.z - z), 2)));
  }
}
