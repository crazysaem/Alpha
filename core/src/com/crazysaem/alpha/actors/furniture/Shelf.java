package com.crazysaem.alpha.actors.furniture;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
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
    renderBatch.flush();
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
      material.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
    }
  }
}
