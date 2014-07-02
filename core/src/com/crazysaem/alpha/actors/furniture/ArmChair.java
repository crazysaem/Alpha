package com.crazysaem.alpha.actors.furniture;

import com.crazysaem.alpha.events.Event;
import com.crazysaem.alpha.events.EventHandler;
import com.crazysaem.alpha.pathfinding.AStarPosition;
import com.crazysaem.alpha.picking.StaticRenderable;

/**
 * Created by crazysaem on 07.06.2014.
 */
public class ArmChair extends StaticRenderable implements EventHandler, AStarPosition
{
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

  @Override
  public float getX()
  {
    return 0;
  }

  @Override
  public float getZ()
  {
    return -3;
  }
}
