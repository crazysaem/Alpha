package com.crazysaem.alpha.picking;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.crazysaem.alpha.graphics.Renderable;

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
  private Ray geometryRay = new Ray(new Vector3(), new Vector3());
  private Vector3 intersection = new Vector3();

  public float collisionTest(Ray ray)
  {
    if (Intersector.intersectRayBoundsFast(ray, boundingBox))
    {
      //Because the raw geometry is still stored like the Blender coordinate system, we have to map the ray
      //from the libGDX coordinate system to the Blender one.
      geometryRay.set(ray.origin.x, -ray.origin.z, ray.origin.y, ray.direction.x, -ray.direction.z, ray.direction.y);
      if (Intersector.intersectRayTriangles(geometryRay, vertices, indices, vertexSize, intersection))
      {
        return Math.abs(geometryRay.origin.dst(intersection));
      }
    }

    return -1;
  }

  public boolean collisionTest(Ray ray, float distance)
  {
    if (Intersector.intersectRayBoundsFast(ray, boundingBox))
    {
      //Because the raw geometry is still stored like the Blender coordinate system, we have to map the ray
      //from the libGDX coordinate system to the Blender one.
      geometryRay.set(ray.origin.x, -ray.origin.z, ray.origin.y, ray.direction.x, -ray.direction.z, ray.direction.y);
      if (Intersector.intersectRayTriangles(geometryRay, vertices, indices, vertexSize, intersection))
      {
        float distanceTest =  Math.abs(geometryRay.origin.dst(intersection));
        if (distance > distanceTest)
          return true;
      }
    }

    /*
    if (Intersector.intersectRayBounds(ray, boundingBox, intersection))
    {
      if (distance > Math.abs(ray.origin.dst(intersection)))
        return true;
    }*/

    return false;
  }

  public boolean collisionTestFast(Ray ray)
  {
    if (Intersector.intersectRayBoundsFast(ray, boundingBox))
    {
      return true;
    }

    return false;
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

    //TODO: Share vertices array between different static Renderables, because it is often the same
    int numVertices = mesh.getNumVertices();
    vertices = new float[numVertices * vertexSize];
    mesh.getVertices(vertices);
  }

  @Override
  public void dispose()
  {
    vertices = null;
    indices = null;
    super.dispose();
  }
}
