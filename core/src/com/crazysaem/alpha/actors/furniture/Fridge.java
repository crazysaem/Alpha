package com.crazysaem.alpha.actors.furniture;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.graphics.RenderUtils;
import com.crazysaem.alpha.picking.StaticRenderable;

/**
 * Created by crazysaem on 24.06.2014.
 */
public class Fridge extends StaticRenderable
{
  private Environment environment;

  public Fridge()
  {
    environment = new Environment();
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1.0f, 1.0f, 1.0f, 1.0f));
  }

  @Override
  protected void finishLoading()
  {
    super.finishLoading("Fridge");

    Material selectedMaterial;
    if ((selectedMaterial = RenderUtils.getMaterial(modelInstance, "Fridge")) != null)
    {
      selectedMaterial.set(new ColorAttribute(ColorAttribute.Diffuse, 192.0f / 256.0f, 237.0f / 256.0f, 97.0f / 256.0f, 1.0f));
    }
  }
  /*
  @Override
  public void render(RenderBatch renderBatch)
  {
    if (loading)
      return;

    renderBatch.render(modelInstance, environment);
  }*/
}
