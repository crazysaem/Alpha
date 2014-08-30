package com.crazysaem.alpha.messages;

/**
 * Created by crazysaem on 12.08.2014.
 */
public class AStarMessage
{
  public static final int MESSAGE_CODE = MessageCodeGenerator.getNewMessageCode();

  private float x, z;

  public AStarMessage(float x, float z)
  {
    this.x = x;
    this.z = z;
  }

  public float getX()
  {
    return x;
  }

  public void setX(float x)
  {
    this.x = x;
  }

  public float getZ()
  {
    return z;
  }

  public void setZ(float z)
  {
    this.z = z;
  }
}
