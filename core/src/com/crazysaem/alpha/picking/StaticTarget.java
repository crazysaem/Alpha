package com.crazysaem.alpha.picking;

import com.crazysaem.alpha.events.EventTarget;

/**
 * Created by crazysaem on 07.06.2014.
 */
public class StaticTarget
{
  private StaticRenderable staticRenderable;
  private EventTarget eventTarget;

  public StaticTarget(StaticRenderable staticRenderable, EventTarget eventTarget)
  {
    this.staticRenderable = staticRenderable;
    this.eventTarget = eventTarget;
  }

  public StaticRenderable getStaticRenderable()
  {
    return staticRenderable;
  }

  public EventTarget getEventTarget()
  {
    return eventTarget;
  }
}
