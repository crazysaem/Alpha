package com.crazysaem.alpha.actors.furniture;

import com.crazysaem.alpha.events.Event;
import com.crazysaem.alpha.events.EventHandler;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.picking.StaticRenderable;

/**
 * Created by crazysaem on 07.06.2014.
 */
public class ArmChair extends StaticRenderable implements EventHandler
{
  @Override
  public void render(RenderBatch renderBatch)
  {
    if (loading)
      return;

    super.render(renderBatch);
  }

  @Override
  public void handleEvent(Event event)
  {
    System.out.println("ARMCHAIR recieved event: " + event.getAction());
  }

  @Override
  protected void finishLoading()
  {
    super.finishLoading(false, "ArmChair");
  }
}
