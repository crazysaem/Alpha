package com.crazysaem.alpha.messages;

import com.crazysaem.alpha.hud.HudIndicatorType;

/**
 * Created by crazysaem on 23.01.15.
 */
public class HudIndicatorMessage
{

  public static final int MESSAGE_CODE = MessageCodeGenerator.getNewMessageCode();

  private HudIndicatorType hudIndicatorType;
  private float value;

  public HudIndicatorMessage(HudIndicatorType hudIndicatorType, float value)
  {
    this.hudIndicatorType = hudIndicatorType;
    this.value = value;
  }

  public HudIndicatorType getHudIndicatorType()
  {
    return hudIndicatorType;
  }

  public float getValue()
  {
    return value;
  }
}
