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

/**
 * Created by crazysaem on 07.06.2014.
 */
public class Furniture extends StaticRenderable implements EventHandler
{
  private ModelInstance debugModelInstance;

  @Override
  public void render(RenderBatch renderBatch)
  {
    if (loading)
      return;

    super.render(renderBatch);

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
    Vector3 dim = boundingBox.getDimensions();
    Vector3 center = boundingBox.getCenter();
    Model debugModel = modelBuilder.createBox(dim.x, dim.y, dim.z, new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    debugModelInstance = new ModelInstance(debugModel);
    debugModelInstance.transform.setToTranslation(center.x, center.y, center.z);
  }
}
