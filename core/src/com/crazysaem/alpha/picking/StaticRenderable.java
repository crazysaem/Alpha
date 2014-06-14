package com.crazysaem.alpha.picking;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.crazysaem.alpha.graphics.Renderable;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by crazysaem on 07.06.2014.
 */
public abstract class StaticRenderable extends Renderable
{
  private BoundingBox boundingBox = new BoundingBox();
  private float[] vertices;
  private short[] indices;
  private int vertexSize;
  private Vector3 intersection = new Vector3();

  public float collisionTest(Ray ray)
  {
    if (Intersector.intersectRayBoundsFast(ray, boundingBox))
    {
      if (Intersector.intersectRayTriangles(ray, vertices, indices, vertexSize, intersection))
      {
        return Math.abs(ray.origin.dst(intersection));
      }
    }

    return -1;
  }

  public boolean collisionTest(Ray ray, float distance)
  {
    if (Intersector.intersectRayBoundsFast(ray, boundingBox))
    {
      if (Intersector.intersectRayTriangles(ray, vertices, indices, vertexSize, intersection))
      {
        float distanceTest = Math.abs(ray.origin.dst(intersection));
        if (distance > distanceTest)
          return true;
      }
    }

    return false;
  }

  /**
   * Returns the intersection point of the last collisionTest
   * @return Vector3 intersection
   */
  public Vector3 getLastIntersection()
  {
    return intersection;
  }

  @Override
  protected void finishLoading(boolean useAnimationController, String... rootNodeIds)
  {
    super.finishLoading(useAnimationController, rootNodeIds);

    modelInstance.calculateBoundingBox(boundingBox);
    MeshPart meshPart = modelInstance.nodes.get(0).parts.get(0).meshPart;
    Mesh mesh = meshPart.mesh;
    vertexSize = mesh.getVertexSize() / 4;

    indices = new short[meshPart.numVertices];
    ShortBuffer indicesBuffer = mesh.getIndicesBuffer();
    int pos = indicesBuffer.position();
    indicesBuffer.position(meshPart.indexOffset);
    indicesBuffer.get(indices, 0, meshPart.numVertices);
    indicesBuffer.position(pos);

    int minIndex = indices[0];
    int maxIndex = indices[indices.length - 1];
    int numVertices = maxIndex - minIndex + 1;

    FloatBuffer verticesBuffer = mesh.getVerticesBuffer();
    pos = verticesBuffer.position();
    verticesBuffer.position(0);
    vertices = new float[numVertices * 3];
    float[] vTemp = new float[3];

    for (int i = 0; i < numVertices; i++)
    {
      verticesBuffer.position((minIndex + i) * vertexSize);
      verticesBuffer.get(vTemp, 0, 3);
      //Because the raw geometry is still stored in the Blender coordinate system, we have to map it to
      //the libGDX coordinate by flipping y & z and negate y.
      vertices[i * 3 + 0] = vTemp[0];
      vertices[i * 3 + 1] = vTemp[2];
      vertices[i * 3 + 2] = -vTemp[1];
    }
    verticesBuffer.position(pos);
    vertexSize = 3;

    for (int i = 0; i < indices.length; i++)
    {
      indices[i] -= minIndex;
    }
  }

  @Override
  public void dispose()
  {
    vertices = null;
    indices = null;
    super.dispose();
  }
}
