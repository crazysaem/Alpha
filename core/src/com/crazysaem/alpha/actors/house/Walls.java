package com.crazysaem.alpha.actors.house;

import com.crazysaem.alpha.picking.StaticRenderable;

/**
 * Created by crazysaem on 19.06.2014.
 */
public class Walls extends StaticRenderable
{
  @Override
  protected void finishLoading()
  {
    super.finishLoading("Wall");
  }
}
