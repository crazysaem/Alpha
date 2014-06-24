package com.crazysaem.alpha.actors.furniture;

import com.crazysaem.alpha.picking.StaticRenderable;

/**
 * Created by crazysaem on 24.06.2014.
 */
public class Fridge extends StaticRenderable
{
  @Override
  protected void finishLoading()
  {
    super.finishLoading("Fridge", "FridgeDoor");
  }
}
