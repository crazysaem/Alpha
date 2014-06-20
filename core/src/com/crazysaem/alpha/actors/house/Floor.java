package com.crazysaem.alpha.actors.house;

import com.crazysaem.alpha.picking.StaticRenderable;

/**
 * Created by crazysaem on 20.06.2014.
 */
public class Floor extends StaticRenderable
{
  @Override
  protected void finishLoading()
  {
    super.finishLoading("Floor");
  }
}
