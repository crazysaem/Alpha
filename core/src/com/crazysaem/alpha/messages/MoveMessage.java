package com.crazysaem.alpha.messages;

import com.crazysaem.alpha.pathfinding.PositionPerTime;

/**
 * Created by crazysaem on 12.08.2014.
 */
public class MoveMessage
{
  public static final int MESSAGE_CODE = MessageCodeGenerator.getNewMessageCode();

  private PositionPerTime positionPerTime;

  public MoveMessage(PositionPerTime positionPerTime/*, ElephantPose elephantPose*/)
  {
    this.positionPerTime = positionPerTime;
  }

  public PositionPerTime getPositionPerTime()
  {
    return positionPerTime;
  }

}
