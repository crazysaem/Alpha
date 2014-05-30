package com.crazysaem.alpha.events;

/**
 * Created by crazysaem on 30.05.2014.
 */
public class Event
{
  private EventTarget eventTarget;
  private String action;

  public Event(EventTarget eventTarget, String action)
  {
    this.eventTarget = eventTarget;
    this.action = action;
  }

  public EventTarget getEventTarget()
  {
    return eventTarget;
  }

  public String getAction()
  {
    return action;
  }
}
