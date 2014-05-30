package com.crazysaem.alpha.graphics;

import com.crazysaem.alpha.graphics.RenderBatch;

/**
 * Created by crazysaem on 30.05.2014.
 */
public interface Renderable
{
  public void update(float delta);
  public void render(RenderBatch renderBatch);
}
