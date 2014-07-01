package com.crazysaem.alpha.picking;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
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
  private Vector3 intersection = new Vector3();
  protected boolean hasArmature = false;

  public float collisionTest(Ray ray)
  {
    if (Intersector.intersectRayBoundsFast(ray, boundingBox))
    {
      if (Intersector.intersectRayTriangles(ray, vertices, indices, 3, intersection))
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
      if (Intersector.intersectRayTriangles(ray, vertices, indices, 3, intersection))
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
   *
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

    boundingBox.clr();
    boundingBox.inf();

    int numIndices = 0;
    int numVertices = 0;

    //Get total number of indices
    for (Node node : modelInstance.nodes)
      for (NodePart nodePart : node.parts)
        numIndices += nodePart.meshPart.numVertices;

    indices = new short[numIndices];

    //Copy all indices and get number of vertices
    Mesh mesh;
    int destOffset = 0;
    for (Node node : modelInstance.nodes)
    {
      for (NodePart nodePart : node.parts)
      {
        mesh = nodePart.meshPart.mesh;
        getIndices(mesh.getIndicesBuffer(), indices, nodePart.meshPart.indexOffset, destOffset, nodePart.meshPart.numVertices);

        int minIndex = indices[destOffset];
        int maxIndex = getMax(indices, destOffset, indices.length);
        numVertices += maxIndex - minIndex + 1;

        destOffset += nodePart.meshPart.numVertices;
      }
    }

    vertices = new float[numVertices * 3];

    //Copy all vertices
    int indexOffset = 0;
    destOffset = 0;
    for (Node node : modelInstance.nodes)
    {
      for (NodePart nodePart : node.parts)
      {
        mesh = nodePart.meshPart.mesh;
        int vertexSize = mesh.getVertexSize() / 4;
        int minIndex = indices[indexOffset];
        int maxIndex = getMax(indices, indexOffset, indexOffset + nodePart.meshPart.numVertices);
        numVertices = maxIndex - minIndex + 1;
        getVertices(mesh.getVerticesBuffer(), vertices, vertexSize, minIndex, destOffset * 3, numVertices);

        indexOffset += nodePart.meshPart.numVertices;
        destOffset += numVertices;
      }
    }

    //Correct indices
    indexOffset = 0;
    int minIndex = indices[indexOffset];
    for (Node node : modelInstance.nodes)
    {
      for (NodePart nodePart : node.parts)
      {
        for (int i = indexOffset; i < indexOffset + nodePart.meshPart.numVertices; i++)
        {
          indices[i] -= minIndex;
        }

        indexOffset += nodePart.meshPart.numVertices;
      }
    }
  }

  private short getMax(short[] array, int startIndex, int endIndex)
  {
    short max = 0;
    for (int i = startIndex; i < endIndex; i++)
    {
      if (array[i] > max)
        max = array[i];
    }

    return max;
  }

  private void getIndices(ShortBuffer indicesBuffer, short[] indices, int srcOffset, int destOffset, int count)
  {
    int pos = indicesBuffer.position();
    indicesBuffer.position(srcOffset);
    indicesBuffer.get(indices, destOffset, count);
    indicesBuffer.position(pos);
  }

  private void getVertices(FloatBuffer verticesBuffer, float[] vertices, int vertexSize, int srcOffset, int destOffset, int count)
  {
    int pos = verticesBuffer.position();
    verticesBuffer.position(0);
    float[] vTemp = new float[3];

    for (int i = 0; i < count; i++)
    {
      verticesBuffer.position((srcOffset + i) * vertexSize);
      verticesBuffer.get(vTemp, 0, 3);
      //Because the raw geometry is still stored in the Blender coordinate system, we have to map it to
      //the libGDX coordinate by flipping y & z and negate y.
      vertices[destOffset + i * 3 + 0] = vTemp[0];
      vertices[destOffset + i * 3 + 1] = vTemp[2];
      vertices[destOffset + i * 3 + 2] = -vTemp[1];

      boundingBox.ext(vTemp[0], vTemp[2], -vTemp[1]);
    }
    verticesBuffer.position(pos);
  }

  @Override
  public void dispose()
  {
    vertices = null;
    indices = null;
    super.dispose();
  }
}
