package com.crazysaem.alpha.picking;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.crazysaem.alpha.graphics.Renderable;

/**
 * Created by crazysaem on 07.06.2014.
 */
public abstract class StaticRenderable extends Renderable
{
  protected BoundingBox boundingBox = new BoundingBox();
  /*private float[] vertices;
  private short[] indices;
  private int numVertices;*/

  public boolean collisionTest(Ray ray)
  {
    if (Intersector.intersectRayBoundsFast(ray, boundingBox))
      return true;
    //TODO: If the bondingBox intersection was true, make a more precise check if the target was really it or not
    //return (Intersector.intersectRayTriangles(ray, vertices, indices, numVertices, null));

    return false;
  }

  @Override
  protected void finishLoading(boolean useAnimationController, String... rootNodeIds)
  {
    super.finishLoading(useAnimationController, rootNodeIds);

    modelInstance.calculateBoundingBox(boundingBox);
    /*
    MeshPart meshPart = modelInstance.nodes.get(0).parts.get(0).meshPart;
    Mesh mesh = meshPart.mesh;
    numVertices = mesh.getNumVertices();
    int numIndices = mesh.getNumIndices();
    vertices = new float[numVertices];
    indices = new short[numIndices];
    mesh.getVertices(vertices);
    mesh.getIndices(indices);*/
  }

  @Override
  public void dispose()
  {
    /*vertices = null;
    indices = null;*/
    super.dispose();
  }
}
