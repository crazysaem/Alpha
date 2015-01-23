package com.crazysaem.alpha.hud;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.crazysaem.alpha.messages.HudIndicatorMessage;
import com.crazysaem.alpha.messages.MessageDispatcherUtil;

/**
 * Created by crazysaem on 15.07.2014.
 */
public class HUDIndicator extends Image implements Telegraph
{
  private Texture baseTexture;
  private TextureRegionDrawable outlineTextureDrawable;
  private float value;
  private HudIndicatorType type;

  public HUDIndicator(HudIndicatorType type, String alpha, String base, String outline)
  {
    super(new Texture(alpha));

    this.type = type;

    baseTexture = new Texture(base);
    outlineTextureDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(outline)));

    MessageDispatcherUtil.addListeners(HudIndicatorMessage.MESSAGE_CODE, this);

    setValue(1.0f);
  }

  @Override
  public void draw(Batch batch, float parentAlpha)
  {
    super.draw(batch, parentAlpha);

    batch.draw(baseTexture, getX() + getImageX(), getY() + getImageY() - (int) (baseTexture.getHeight() * value), 0, (int) (baseTexture.getHeight() * value), baseTexture.getWidth(), baseTexture.getHeight(), 1.0f, 1.0f, 0.0f, 0, (int) (baseTexture.getHeight() * value), baseTexture.getWidth(), baseTexture.getHeight(), false, false);
    outlineTextureDrawable.draw(batch, getX() + getImageX(), getY() + getImageY(), getImageWidth() * getScaleX(), getImageHeight() * getScaleY());
  }

  public void setValue(float value)
  {
    this.value = (1 - Math.max(0.0f, Math.min(1.0f, value)));
  }

  public float getvalue()
  {
    return (1 - value);
  }

  @Override
  public boolean handleMessage(Telegram msg)
  {
    if (msg.message == HudIndicatorMessage.MESSAGE_CODE)
    {
      HudIndicatorMessage hudIndicatorMessage = (HudIndicatorMessage) msg.extraInfo;

      if (!hudIndicatorMessage.getHudIndicatorType().equals(type))
        return false;

      setValue(hudIndicatorMessage.getValue());

      return true;
    }

    return false;
  }
}
