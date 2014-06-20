package com.crazysaem.alpha.actors.furniture;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.graphics.Renderable;

/**
 * Created by crazysaem on 15.06.2014.
 */
public class Shelf extends Renderable
{
  private ShelfBox shelfBox;

  public Shelf()
  {
    shelfBox = new ShelfBox();
  }

  @Override
  public void update(float delta)
  {
    super.update(delta);
    shelfBox.update(delta);
  }

  @Override
  public void render(RenderBatch renderBatch)
  {
    super.render(renderBatch);
    shelfBox.render(renderBatch);
  }

  @Override
  protected void finishLoading()
  {
    super.finishLoading(false, "Shelf");
  }

  class ShelfBox extends Renderable
  {
    @Override
    protected void finishLoading()
    {
      super.finishLoading(false, "ShelfBox");

      Material material = modelInstance.materials.first();
      material.set(new BlendingAttribute(true, 1.0f));
    }
  }
}
