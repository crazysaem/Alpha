package com.crazysaem.alpha.picking;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.graphics.Renderable;

import java.nio.ShortBuffer;
import java.util.ArrayList;

/**
 * Created by crazysaem on 07.06.2014.
 */
public abstract class StaticRenderable extends Renderable
{
  protected BoundingBox boundingBox = new BoundingBox();
  protected float[] vertices;
  protected short[] indices;
  protected int vertexSize;
  private Ray geometryRay = new Ray(new Vector3(), new Vector3());
  private Vector3 intersection = new Vector3();
  private ModelBuilder modelBuilder = new ModelBuilder();
  ModelInstance arrow0 = null, arrow1 = null;
  ModelInstance sphere = new ModelInstance(modelBuilder.createSphere(0.5f, 0.5f, 0.5f, 10, 10, new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));

  public float collisionTest(Ray ray)
  {
    if (Intersector.intersectRayBoundsFast(ray, boundingBox))
    {
      geometryRay.set(ray.origin.x, -ray.origin.z, ray.origin.y, ray.direction.x, -ray.direction.z, ray.direction.y);
      if (Intersector.intersectRayTriangles(geometryRay, vertices, indices, vertexSize, intersection))
      {
        //if (intersection.x == 0 && intersection.y == 0 && intersection.z == 0)
        //  return -1;

        Vector3 pos0 = new Vector3(ray.origin.x + ray.direction.x * 20, ray.origin.y + ray.direction.y * 20, ray.origin.z + ray.direction.z * 20);
        Model a0 = modelBuilder.createArrow(ray.origin, pos0, new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        arrow0 = new ModelInstance(a0);

        Vector3 pos1 = new Vector3(geometryRay.origin.x + geometryRay.direction.x * 20, geometryRay.origin.y + geometryRay.direction.y * 20, geometryRay.origin.z + geometryRay.direction.z * 20);
        Model a1 = modelBuilder.createArrow(geometryRay.origin, pos1, new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        arrow1 = new ModelInstance(a1);

        sphere.transform.setToTranslation(intersection.x, intersection.y, intersection.z);

        return Math.abs(geometryRay.origin.dst(intersection));
      }
    }

    return -1;
  }

  @Override
  public void render(RenderBatch renderBatch)
  {
    super.render(renderBatch);

    if (arrow0 != null && arrow1 != null)
    {
      //renderBatch.render(arrow0);
      //renderBatch.render(arrow1);
      //renderBatch.render(sphere);
    }
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

    int numVertices = mesh.getNumVertices();
    vertices = new float[numVertices * vertexSize];
    mesh.getVertices(vertices);
  }

  @Override
  public void dispose()
  {
    /*vertices = null;
    indices = null;*/
    super.dispose();
  }
}
