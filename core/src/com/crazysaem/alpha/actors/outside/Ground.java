package com.crazysaem.alpha.actors.outside;

import com.badlogic.gdx.graphics.Texture;
import com.crazysaem.alpha.graphics.RenderUtils;
import com.crazysaem.alpha.picking.StaticRenderable;

/**
 * Created by crazysaem on 08.06.2014.
 */
public class Ground extends StaticRenderable
{
  @Override
  protected void finishLoading()
  {
    super.finishLoading("Ground");

    RenderUtils.activateMipMap(modelInstance.materials.first(), "models/ground.jpg", Texture.TextureFilter.MipMapNearestNearest);
  }
}
