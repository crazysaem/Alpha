package com.crazysaem.alpha.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by crazysaem on 15.07.2014.
 */
public class HUDIndicator extends Image
{
  public int test;
  private Texture baseTexturer;
  private TextureRegionDrawable outlineTextureDrawable;
  private float value;

  public HUDIndicator(String alpha, String base, String outline)
  {
    super(new Texture(alpha));

    baseTexturer = new Texture(base);
    outlineTextureDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(outline)));

    test = 0;
    setValue(0.75f);
  }

  @Override
  public void draw(Batch batch, float parentAlpha)
  {
    super.draw(batch, parentAlpha);

    //batch.draw(baseTexturer, getX() + getImageX(), getY() + getImageY() - baseTexturer.getHeight() * value, 0, baseTexturer.getHeight() * value, baseTexturer.getWidth(), baseTexturer.getHeight(), 1.0f, 1.0f, 0.0f, 0, (int)(baseTexturer.getHeight() * value), baseTexturer.getWidth(), baseTexturer.getHeight(), false, false);
    batch.draw(baseTexturer, getX() + getImageX(), getY() + getImageY() - (int) (baseTexturer.getHeight() * value), 0, (int) (baseTexturer.getHeight() * value), baseTexturer.getWidth(), baseTexturer.getHeight(), 1.0f, 1.0f, 0.0f, 0, (int) (baseTexturer.getHeight() * value), baseTexturer.getWidth(), baseTexturer.getHeight(), false, false);
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
}
