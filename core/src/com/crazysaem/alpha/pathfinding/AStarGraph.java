package com.crazysaem.alpha.pathfinding;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.pathfinding.area.WalkableArea;
import com.crazysaem.alpha.pathfinding.area.WalkableAreaComparator;
import com.crazysaem.alpha.pathfinding.node.Node;
import com.crazysaem.alpha.picking.StaticTargetPool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by crazysaem on 09.06.2014.
 */
public class AStarGraph
{
  private StaticTargetPool staticTargetPool;
  private Node[][] nodes;
  private Ray ray;
  private int xShift, zShift;
  private int x0, z0, x1, z1;
  private List<ModelInstance> debugModelInstances = new ArrayList<ModelInstance>();
  private boolean approximateNodeFlag;
  //private List<WalkableArea> walkableAreas;
  private TreeSet<WalkableArea> walkableAreas;

  public AStarGraph(StaticTargetPool staticTargetPool)
  {
    this.staticTargetPool = staticTargetPool;
    ray = new Ray(new Vector3(0.0f, 0.5f, 0.0f), new Vector3(0.0f, 0.0f, 0.0f));
    walkableAreas = new TreeSet<WalkableArea>(new WalkableAreaComparator());
  }

  public void recalculateGraph(int x0, int z0, int x1, int z1)
  {
    this.x0 = x0;
    this.z0 = z0;
    this.x1 = x1;
    this.z1 = z1;
    xShift = -x0;
    zShift = -z0;

    Node node;
    int xLength = x1 - x0;
    int zLength = z1 - z0;
    int xMin = Math.min(x0, x1);
    int xMax = Math.max(x0, x1);
    int zMin = Math.min(z0, z1);
    int zMax = Math.max(z0, z1);
    boolean leftFlag, topFlag, rightFlag, botFlag;

    nodes = new Node[xLength + 1][zLength + 1];

    // LibGDX Coordinate System:
    //
    // FRONT VIEW:
    //    ^y
    //    |
    //    |
    //    +---->x
    //   /
    //  /
    // vz
    //
    // TOP VIEW:
    //
    //    +---->x
    //   /|
    //  / |
    // vy vz
    //

    float size = 1.0f;
    float sizeHalf = size / 2;
    float distance = (float) Math.sqrt(Math.sqrt(Math.pow(size, 2) + Math.pow(size / 2, 2)) + Math.pow(size, 2));
    for (int x = x0; x <= x1; x++)
    {
      for (int z = z0; z <= z1; z++)
      {
        float xf = (float) x;
        float zf = (float) z;

        //Left -> TopRight
        boolean check0 = collisionCheck(xf - sizeHalf, 0.0f, zf, xf + sizeHalf, size, zf - sizeHalf, distance);
        //Top -> BottomRight
        boolean check1 = collisionCheck(xf, 0.0f, zf - sizeHalf, xf + sizeHalf, size, zf + sizeHalf, distance);
        //Right -> BottomLeft
        boolean check2 = collisionCheck(xf + sizeHalf, 0.0f, zf, xf - sizeHalf, size, zf + sizeHalf, distance);
        //Bottom -> TopLeft
        boolean check3 = collisionCheck(xf, 0.0f, zf + sizeHalf, xf - sizeHalf, size, zf - sizeHalf, distance);

        if (check0 && check1 && check2 && check3)
          setNode(new Node(x, z), x, z);
      }
    }

    for (int x = x0; x <= x1; x++)
    {
      for (int z = z0; z <= z1; z++)
      {
        node = getNode(x, z);

        if (node != null)
        {

          leftFlag = x > xMin;
          botFlag = z < zMax;
          rightFlag = x < xMax;
          topFlag = z > zMin;

          if (leftFlag)
            node.L = getNode(x - 1, z);

          if (topFlag)
            node.T = getNode(x, z - 1);

          if (rightFlag)
            node.R = getNode(x + 1, z);

          if (botFlag)
            node.B = getNode(x, z + 1);

          if (node.T != null && node.L != null)
            node.TL = getNode(x - 1, z - 1);

          if (node.T != null && node.R != null)
            node.TR = getNode(x + 1, z - 1);

          if (node.B != null && node.L != null)
            node.BL = getNode(x - 1, z + 1);

          if (node.B != null && node.R != null)
            node.BR = getNode(x + 1, z + 1);
        }
      }
    }

    //Calculate walkable area-rectangles:
    walkableAreas.clear();
    getAllWalkableAreas(walkableAreas, x0, z0, x1, z1);
    optimizeWalkableAreas(walkableAreas, 0);
  }

  private void getAllWalkableAreas(TreeSet<WalkableArea> walkableAreas, int x0, int z0, int x1, int z1)
  {
    int xEnd = x0, zEnd = z0;

    if (isAreaRectWalkable(x0, z0))
    {
      while (true)
      {
        xEnd = getXLineEnd(x0, zEnd, x1);
        zEnd++;

        while (getXLineEnd(x0, zEnd, x1) == xEnd)
          zEnd++;

        walkableAreas.add(new WalkableArea(x0, z0, xEnd, zEnd));
        break;
      }

      if (xEnd < x1)
        getAllWalkableAreas(walkableAreas, xEnd, z0, x1, z1);
      if (zEnd < z1)
        getAllWalkableAreas(walkableAreas, x0, zEnd, xEnd, z1);
    }
    else
    {
      if (x0 + 1 < x1)
        getAllWalkableAreas(walkableAreas, x0 + 1, z0, x1, z1);

      if (z0 + 1 < z1)
        getAllWalkableAreas(walkableAreas, x0, z0 + 1, x0 + 1, z1);
    }
  }

  private void optimizeWalkableAreas(TreeSet<WalkableArea> walkableAreas, int pos)
  {
    Iterator<WalkableArea> it = walkableAreas.iterator();

    for (int i = 0; i < pos ; i++)
    {
      if (it.hasNext())
        it.next();
      else
        return;
    }

    WalkableArea walkableArea0;
    if (it.hasNext())
      walkableArea0 = it.next();
    else
      return;

    WalkableArea walkableArea1;
    if (it.hasNext())
      walkableArea1 = it.next();
    else
      return;

    while(true)
    {
      if (walkableArea0.x0 == walkableArea1.x0 && walkableArea0.x1 == walkableArea1.x1 && walkableArea0.z1 == walkableArea1.z0)
      {
        walkableArea0.z1 = walkableArea1.z1;
        walkableAreas.remove(walkableArea1);
        optimizeWalkableAreas(walkableAreas, pos);
        return;
      }
      else if (walkableArea0.z0 == walkableArea1.z0 && walkableArea0.z1 == walkableArea1.z1 && walkableArea0.x1 == walkableArea1.x0)
      {
        walkableArea0.x1 = walkableArea1.x1;
        walkableAreas.remove(walkableArea1);
        optimizeWalkableAreas(walkableAreas, pos);
        return;
      }
      else
      {
        if (it.hasNext())
        {
          walkableArea1 = it.next();
        }
        else
        {
          optimizeWalkableAreas(walkableAreas, pos + 1);
          return;
        }
      }
    }
  }

  public boolean isLineWalkable(float x0, float z0, float x1, float z1)
  {
    return false;
  }

  private int getXLineEnd(int x, int z, int xMax)
  {
    while (isAreaRectWalkable(x, z) && x < xMax)
      x++;

    return x;
  }

  private boolean isAreaRectWalkable(int xTL, int zTL)
  {
    if (getNode(xTL, zTL) != null && getNode(xTL + 1, zTL) != null && getNode(xTL, zTL + 1) != null && getNode(xTL + 1, zTL + 1) != null)
      return true;

    return false;
  }

  private void setNode(Node node, int x, int z)
  {
    nodes[xShift + x][zShift + z] = node;
  }

  public Node getApproximateNode(float x_, float z_)
  {
    int x = (int)x_;
    int z = (int)z_;

    approximateNodeFlag = true;
    float distance = Float.MAX_VALUE;
    float tempDistance;
    Node returnNode = null;
    Node node;
    int[] positions = {x, z, x + 1, z, x, z + 1, x + 1, z + 1};

    for (int i = 0; i < 8; i+=2)
    {
      if ((node = getNode(positions[i], positions[i + 1])) != null)
      {
        if (returnNode == null)
        {
          returnNode = node;
          distance = node.calcDiastance(x_, z_);
        }
        else if ((tempDistance = returnNode.calcDiastance(node)) < distance)
        {
          distance = tempDistance;
          returnNode = node;
        }
      }
      else
      {
        approximateNodeFlag = false;
      }
    }

    return returnNode;
  }

  public boolean getApproximateNodeFlag()
  {
    return approximateNodeFlag;
  }

  public Node getNode(int x, int z)
  {
    if ((x > x1) || (z > z1) || (x < x0) || (z < z0))
      return null;

    return nodes[xShift + x][zShift + z];
  }

  private boolean collisionCheck(float p0x, float p0y, float p0z, float p1x, float p1y, float p1z, float distance)
  {
    ray.origin.x = p0x;
    ray.origin.y = p0y;
    ray.origin.z = p0z;
    ray.direction.x = p1x - p0x;
    ray.direction.y = p1y - p0y;
    ray.direction.z = p1z - p0z;
    ray.direction.nor();
    return !staticTargetPool.collisonCheck(ray, distance);
  }

  public void createDebugRenderGraphics()
  {
    Node node;
    ModelBuilder modelBuilder = new ModelBuilder();
    Model sphereModel = modelBuilder.createSphere(0.5f, 0.5f, 0.5f, 2, 2, new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    ModelInstance sphere;

    Vector3 v0 = new Vector3(0.0f, 0.5f, 0.0f);
    Model arrowModelL = modelBuilder.createArrow(v0, new Vector3(-0.8f, 0.5f, 0.0f), new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    Model arrowModelTL = modelBuilder.createArrow(v0, new Vector3(-0.8f, 0.5f, -0.8f), new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    Model arrowModelT = modelBuilder.createArrow(v0, new Vector3(0.0f, 0.5f, -0.8f), new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    Model arrowModelTR = modelBuilder.createArrow(v0, new Vector3(0.8f, 0.5f, -0.8f), new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    Model arrowModelR = modelBuilder.createArrow(v0, new Vector3(0.8f, 0.5f, 0.0f), new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    Model arrowModelBR = modelBuilder.createArrow(v0, new Vector3(0.8f, 0.5f, 0.8f), new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    Model arrowModelB = modelBuilder.createArrow(v0, new Vector3(0.0f, 0.5f, 0.8f), new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    Model arrowModelBL = modelBuilder.createArrow(v0, new Vector3(-0.8f, 0.5f, 0.8f), new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    ModelInstance arrow;

    for (int x = x0; x <= x1; x++)
    {
      for (int z = z0; z <= z1; z++)
      {
        node = getNode(x, z);

        if (node != null)// && (x == 2 && z == -3))
        {
          if (true)
          {
            sphere = new ModelInstance(sphereModel);
            sphere.transform.setToTranslation(x, 1.0f, z);
            debugModelInstances.add(sphere);
          }
          /*
          if (node.L != null)
          {
            arrow = new ModelInstance(arrowModelL);
            arrow.transform.setToTranslation(x, 0.5f, z);
            debugModelInstances.add(arrow);
          }
          if (node.TL != null)
          {
            arrow = new ModelInstance(arrowModelTL);
            arrow.transform.setToTranslation(x, 0.5f, z);
            debugModelInstances.add(arrow);
          }
          if (node.T != null)
          {
            arrow = new ModelInstance(arrowModelT);
            arrow.transform.setToTranslation(x, 0.5f, z);
            debugModelInstances.add(arrow);
          }
          if (node.TR != null)
          {
            arrow = new ModelInstance(arrowModelTR);
            arrow.transform.setToTranslation(x, 0.5f, z);
            debugModelInstances.add(arrow);
          }
          if (node.R != null)
          {
            arrow = new ModelInstance(arrowModelR);
            arrow.transform.setToTranslation(x, 0.5f, z);
            debugModelInstances.add(arrow);
          }
          if (node.BR != null)
          {
            arrow = new ModelInstance(arrowModelBR);
            arrow.transform.setToTranslation(x, 0.5f, z);
            debugModelInstances.add(arrow);
          }
          if (node.B != null)
          {
            arrow = new ModelInstance(arrowModelB);
            arrow.transform.setToTranslation(x, 0.5f, z);
            debugModelInstances.add(arrow);
          }
          if (node.BL != null)
          {
            arrow = new ModelInstance(arrowModelBL);
            arrow.transform.setToTranslation(x, 0.5f, z);
            debugModelInstances.add(arrow);
          }*/
        }
      }
    }
  }

  public void debugRender(RenderBatch renderBatch)
  {
    for (ModelInstance modelInstance : debugModelInstances)
      renderBatch.render(modelInstance);
  }
}
