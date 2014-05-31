package com.crazysaem.alpha.actors;

import com.crazysaem.alpha.graphics.Renderable;

/**
 * Created by crazysaem on 31.05.2014.
 */
public class House extends Renderable
{
  @Override
  protected void finishLoading()
  {
    super.finishLoading("Floor");
    
    //The magic number is half the height of the pet.
    //TODO: Change this to not use it like a magic number but get it dynamically
    modelInstance.transform.setToTranslation(0, -1.1198419332504272f, 0);
  }
}
