package com.crazysaem.alpha.actors.house;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.crazysaem.alpha.events.Event;
import com.crazysaem.alpha.events.EventHandler;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.graphics.Renderable;
import com.crazysaem.alpha.picking.StaticRenderable;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by crazysaem on 31.05.2014.
 */
public class House extends Renderable implements EventHandler
{
  public List<HousePart> houseParts;

  public House()
  {
    houseParts = new LinkedList<HousePart>();

    houseParts.add(new HousePart("Floor"));
    //houseParts.add(new HousePart("Wall0"));
    //houseParts.add(new HousePart("Wall1"));
  }

  @Override
  protected void finishLoading()
  {
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

  @Override
  public void handleEvent(Event event)
  {
    System.out.println(event.getAction());
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


}
