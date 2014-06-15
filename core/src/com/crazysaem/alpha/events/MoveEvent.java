package com.crazysaem.alpha.events;

import com.crazysaem.alpha.pathfinding.PositionPerTime;

/**
 * Created by crazysaem on 15.06.2014.
 */
public class MoveEvent extends Event
{
  private PositionPerTime positionPerTime;

  public MoveEvent(EventTarget eventTarget, String action, PositionPerTime positionPerTime)
  {
    super(eventTarget, action);

    this.positionPerTime = positionPerTime;
  }

  public PositionPerTime getPositionPerTime()
  {
    return positionPerTime;
  }
}
