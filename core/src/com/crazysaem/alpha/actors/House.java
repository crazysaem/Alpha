package com.crazysaem.alpha.actors;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.graphics.Renderable;
import com.crazysaem.alpha.picking.StaticRenderable;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by crazysaem on 31.05.2014.
 */
public class House extends Renderable
{
  private List<HousePart> houseParts;

  public House()
  {
    houseParts = new LinkedList<HousePart>();
  }

  @Override
  protected void finishLoading()
  {
    houseParts.add(new HousePart("Floor"));
    houseParts.add(new HousePart("Wall0"));
    houseParts.add(new HousePart("Wall1"));
    //houseParts.add(new HousePart("ArmChair"));
    houseParts.add(new HousePart("Shelf"));
    houseParts.add(new ShelfBox());

    loading = false;
  }

  @Override
  public void update(float delta)
  {
    super.update(delta);
    for (HousePart housePart : houseParts)
    {
      housePart.update(delta);
    }
  }

  @Override
  public void render(RenderBatch renderBatch)
  {
    for (HousePart housePart : houseParts)
    {
      housePart.render(renderBatch);
      renderBatch.flush();
    }
  }

  class HousePart extends StaticRenderable
  {
    private String housePart;

    public HousePart(String housePart)
    {
      this.housePart = housePart;
    }

    @Override
    protected void finishLoading()
    {
      super.finishLoading(false, housePart);
    }
  }

  class ShelfBox extends HousePart
  {
    public ShelfBox()
    {
      super("ShelfBox");
    }

    @Override
    protected void finishLoading()
    {
      super.finishLoading();

      Material material = modelInstance.materials.get(0);
      material.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
    }
  }
}
