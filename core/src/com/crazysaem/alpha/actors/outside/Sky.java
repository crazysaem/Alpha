package com.crazysaem.alpha.actors.outside;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.graphics.Renderable;

/**
 * Created by crazysaem on 15.06.2014.
 */
public class Sky extends Renderable
{
  private Environment environment;

  public Sky()
  {
    environment = new Environment();
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1.0f, 1.0f, 1.0f, 1f));
  }

  @Override
  protected void finishLoading()
  {
    super.finishLoading("Sky");
  }

  @Override
  public void render(RenderBatch renderBatch)
  {
    if (loading)
      return;

    renderBatch.render(modelInstance, environment);
  }
}