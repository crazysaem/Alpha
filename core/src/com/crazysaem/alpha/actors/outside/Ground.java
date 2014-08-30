package com.crazysaem.alpha.actors.outside;

import com.badlogic.gdx.graphics.Texture;
import com.crazysaem.alpha.actors.agents.Base;
import com.crazysaem.alpha.graphics.RenderUtils;

/**
 * Created by crazysaem on 08.06.2014.
 */
public class Ground extends Base
{
  @Override
  protected void finishLoading()
  {
    super.finishLoading("Ground");

    RenderUtils.activateMipMap(modelInstance.materials.first(), "models/ground.jpg", Texture.TextureFilter.MipMapNearestNearest);
  }
}
