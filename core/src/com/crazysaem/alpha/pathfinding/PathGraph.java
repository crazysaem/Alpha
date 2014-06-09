package com.crazysaem.alpha.pathfinding;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.crazysaem.alpha.picking.StaticTargetPool;

/**
 * Created by crazysaem on 09.06.2014.
 */
public class PathGraph
{
  class Node
  {
    //Position
    //public int x;
    //public int z;

    //Connection to other Nodes
    public Node
        TL, T, TR,
        L, R,
        BL, B, BR;
  }

  private StaticTargetPool staticTargetPool;
  private Node[][] nodes;
  private Ray ray;
  private int xShift, zShift;

  public PathGraph(StaticTargetPool staticTargetPool)
  {
    this.staticTargetPool = staticTargetPool;
    ray = new Ray(new Vector3(0.0f, 0.5f, 0.0f), new Vector3(0.0f, 0.0f, 0.0f));
  }

  public void recalculateGraph(int x0, int z0, int x1, int z1)
  {
    Node node;
    int xLength = x1 - x0;
    int zLength = z1 - z0;
    xShift = -x0;
    zShift = -z0;
    int xMin = Math.min(x0, x1);
    int xMax = Math.max(x0, x1);
    int zMin = Math.min(z0, z1);
    int zMax = Math.max(z0, z1);
    boolean leftFlag, topFlag, rightFlag, botFlag;

    nodes = new Node[xLength + 1][zLength + 1];

    for (int x = x0; x <= x1; x++)
    {
      for (int z = z0; z <= z1; z++)
      {
        setNode(new Node(), x, z);
      }
    }

    for (int x = x0; x <= x1; x++)
    {
      for (int z = z0; z <= z1; z++)
      {
        node = getNode(x, z);

        leftFlag = x > xMin;
        topFlag = z < zMax;
        rightFlag = x < xMax;
        botFlag = z > zMin;

        // LibGDX Coordinate System:
        //
        // FRONT VIEW:
        //    ^y ^z
        //    | /
        //    |/
        //    +---->x
        //
        // TOP VIEW:
        //    ^z
        //    |
        //    |
        //    +---->x
        //   /
        //  /
        // v y
        //

        if (leftFlag)
        {
          //Check if a path to the node on the LEFT is available
          if (collisionCheck(x, z, -1, 0))
            node.L = getNode(x - 1, z);

          if (topFlag)
          {
            //Check if a path to the node on the TOP_LEFT is available
            if (collisionCheck(x, z, -1, 1))
              node.TL = getNode(x - 1, z + 1);
          }
        }

        if (topFlag)
        {
          //Check if a path to the node on the TOP is available
          if (collisionCheck(x, z, 0, 1))
            node.T = getNode(x, z + 1);
        }

        if (rightFlag)
        {
          if (topFlag)
          {
            //Check if a path to the node on the TOP_RIGHT is available
            if (collisionCheck(x, z, 1, 1))
              node.TR = getNode(x + 1, z + 1);
          }

          //Check if a path to the node on the RIGHT is available
          if (collisionCheck(x, z, 1, 0))
            node.R = getNode(x + 1, z);
        }

        if (botFlag)
        {
          if (rightFlag)
          {
            //Check if a path to the node on the BOT_RIGHT is available
            if (collisionCheck(x, z, 1, -1))
              node.BR = getNode(x + 1, z - 1);
          }

          //Check if a path to the node on the BOT is available
          if (collisionCheck(x, z, 0, -1))
            node.B = getNode(x, z - 1);

          if (leftFlag)
          {
            //Check if a path to the node on the BOT_LEFT is available
            if (collisionCheck(x, z, -1, -1))
              node.BL = getNode(x - 1, z - 1);
          }
        }
      }
    }
  }

  private void setNode(Node node, int x, int z)
  {
    nodes[xShift + x][zShift + z] = node;
  }

  private Node getNode(int x, int z)
  {
    return nodes[xShift + x][zShift + z];
  }

  private boolean collisionCheck(int posX, int posZ, int dirX, int dirZ)
  {
    ray.origin.x = posX;
    ray.origin.z = posZ;
    ray.direction.x = dirX;
    ray.direction.y = 0.0f;
    ray.direction.z = dirZ;
    ray.direction.nor();
    //TODO: Try to Check Line vs AABB instead of Ray vs AABB and then checking the distance.
    //Could lead to a speed boost
    return !staticTargetPool.collisonCheck(ray, 1.8f);
  }
}
