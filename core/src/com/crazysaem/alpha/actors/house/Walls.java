package com.crazysaem.alpha.actors.house;

import com.crazysaem.alpha.graphics.Renderable;

/**
 * Created by crazysaem on 19.06.2014.
 */
public class Walls extends Renderable
{
  @Override
  protected void finishLoading()
  {
    super.finishLoading("Wall0", "Wall1");
  }
}
