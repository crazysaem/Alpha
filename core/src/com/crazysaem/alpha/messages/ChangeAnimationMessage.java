package com.crazysaem.alpha.messages;

/**
 * Created by crazysaem on 30.08.2014.
 */
public class ChangeAnimationMessage
{
  public static final int MESSAGE_CODE = MessageCodeGenerator.getNewMessageCode();

  private String animation;
  private int loopCount;
  private float speed;

  public ChangeAnimationMessage(String animation, int loopCount, float speed)
  {
    this.animation = animation;
    this.loopCount = loopCount;
    this.speed = speed;

  }

  public String getAnimation()
  {
    return animation;
  }

  public int getLoopCount()
  {
    return loopCount;
  }

  public float getSpeed()
  {
    return speed;
  }
}
