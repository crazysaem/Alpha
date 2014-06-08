package com.crazysaem.alpha.actors;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.crazysaem.alpha.events.Event;
import com.crazysaem.alpha.events.EventHandler;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.picking.StaticRenderable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crazysaem on 07.06.2014.
 */
public class Furniture extends StaticRenderable implements EventHandler
{
  private ModelInstance debugModelInstance;
  private List<ModelInstance> debugParts;

  @Override
  public void render(RenderBatch renderBatch)
  {
    if (loading)
      return;

    super.render(renderBatch);

    for (ModelInstance modelInstance : debugParts)
      renderBatch.render(modelInstance);
    //renderBatch.render(debugModelInstance);
  }

  @Override
  public void handleEvent(Event event)
  {
    System.out.println("ARMCHAIR recieved: " + event.getAction());
  }

  @Override
  protected void finishLoading()
  {
    super.finishLoading(false, "ArmChair");

    ModelBuilder modelBuilder = new ModelBuilder();
    /*Vector3 dim = boundingBox.getDimensions();
    Vector3 center = boundingBox.getCenter();
    Model debugModel = modelBuilder.createBox(dim.x, dim.y, dim.z, new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    debugModelInstance = new ModelInstance(debugModel);
    debugModelInstance.transform.setToTranslation(center.x, center.y, center.z);*/

    debugParts = new ArrayList<ModelInstance>();

    int inc = vertexSize * 4;
    int size = vertices.length;

    for (int i = 0; i < size; i += inc)
    {
      Model debugModel = modelBuilder.createRect(
          //Position:
          vertices[i + vertexSize * 0], vertices[i + 1 + vertexSize * 0], vertices[i + 2 + vertexSize * 0],
          vertices[i + vertexSize * 1], vertices[i + 1 + vertexSize * 1], vertices[i + 2 + vertexSize * 1],
          vertices[i + vertexSize * 2], vertices[i + 1 + vertexSize * 2], vertices[i + 2 + vertexSize * 2],
          vertices[i + vertexSize * 3], vertices[i + 1 + vertexSize * 3], vertices[i + 2 + vertexSize * 3],
          //Normal:
          vertices[i + 3], vertices[i + 4], vertices[i + 5],
          new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
      debugParts.add(new ModelInstance(debugModel));
    }
    /*
    Vector3 pos00 = new Vector3(0.04824961f, 12.521918f, -3.773724f);
    Vector3 pos01 = new Vector3(0.04824961f + -0.008715264f * 10f, 12.521918f + -0.9931869f * 10f, -3.773724f + -0.11620633f * 10f);
    Model arrow0 = modelBuilder.createArrow(pos00, pos01, new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    debugParts.add(new ModelInstance(arrow0));

    Vector3 pos10 = new Vector3(0.04824961f, -(-3.773724f), 12.521918f);
    Vector3 pos11 = new Vector3(0.04824961f + -0.008715264f * 10f, -(-3.773724f + -0.11620633f * 10f), 12.521918f + -0.9931869f * 10f);
    Model arrow1 = modelBuilder.createArrow(pos10, pos11, new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    debugParts.add(new ModelInstance(arrow1));*/
  }
}
