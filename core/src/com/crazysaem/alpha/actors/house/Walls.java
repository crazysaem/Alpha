package com.crazysaem.alpha.actors.house;

import com.crazysaem.alpha.picking.CollisionRenderable;

/**
 * Created by crazysaem on 19.06.2014.
 */
public class Walls extends CollisionRenderable
{
  @Override
  protected void finishLoading()
  {
    super.finishLoading("Wall");
  }
}
