package com.crazysaem.alpha.events;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Created by crazysaem on 30.05.2014.
 */
public class EventManager
{
  private Map<EventTarget, EventHandler> eventHandlers;
  private Queue<Event> eventQueue;

  public EventManager()
  {
    eventHandlers = new HashMap<EventTarget, EventHandler>();
    eventQueue = new LinkedList<Event>();
  }

  public void registerEventHandler(EventTarget eventTarget, EventHandler eventHandler)
  {
    eventHandlers.put(eventTarget, eventHandler);
  }

  public void addEvent(Event event)
  {
    eventQueue.add(event);
  }

  public void update()
  {
    Event event;

    while (!eventQueue.isEmpty())
    {
      event = eventQueue.poll();
      EventTarget eventTarget = event.getEventTarget();
      EventHandler eventHandler = eventHandlers.get(eventTarget);
      eventHandler.handleEvent(event);
    }
  }
}
