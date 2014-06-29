package com.crazysaem.alpha.actors.furniture;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.graphics.RenderUtils;
import com.crazysaem.alpha.picking.StaticRenderable;

/**
 * Created by crazysaem on 24.06.2014.
 */
public class Fridge extends StaticRenderable implements AnimationController.AnimationListener
{
  private Environment environment;
  private String[] animationIDs = {"fridge_open", "fridge_tray", "fridge_tray", "fridge_freezer", "fridge_freezer", "fridge_open"};
  private int currentAnimation = 0;

  public Fridge()
  {
    environment = new Environment();
    environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1.0f, 1.0f, 1.0f, 1.0f));
  }

  @Override
  protected void finishLoading()
  {
    super.finishLoading("Fridge", "FridgeArmature");

    Material selectedMaterial;
    if ((selectedMaterial = RenderUtils.getMaterial(modelInstance, "Fridge")) != null)
    {
      selectedMaterial.set(new ColorAttribute(ColorAttribute.Diffuse, 192.0f / 256.0f, 237.0f / 256.0f, 97.0f / 256.0f, 1.0f));
    }

    animationController.setAnimation(animationIDs[0], 1, 1.0f, this);
    currentAnimation = 0;
  }

  @Override
  public void onEnd(AnimationController.AnimationDesc animation)
  {
    currentAnimation++;
    currentAnimation = currentAnimation % 6;

    if (currentAnimation == 2 || currentAnimation == 4 || currentAnimation == 5)
      animationController.queue(animationIDs[currentAnimation], 1, -1.0f, this, 0.05f);
    else
      animationController.queue(animationIDs[currentAnimation], 1, 1.0f, this, 0.05f);
  }

  @Override
  public void onLoop(AnimationController.AnimationDesc animation)
  {

  }
}
