package com.crazysaem.alpha.pathfinding;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.picking.StaticTargetPool;

import java.util.ArrayList;
import java.util.List;

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
  ;

  public AStarGraph(StaticTargetPool staticTargetPool)
  {
    this.staticTargetPool = staticTargetPool;
    ray = new Ray(new Vector3(0.0f, 0.5f, 0.0f), new Vector3(0.0f, 0.0f, 0.0f));
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
  }

  private void setNode(Node node, int x, int z)
  {
    nodes[xShift + x][zShift + z] = node;
  }

  public Node getNode(int x, int z)
  {
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
          }
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
