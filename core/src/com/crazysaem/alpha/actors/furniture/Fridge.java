package com.crazysaem.alpha.actors.furniture;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.PartialColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.crazysaem.alpha.actors.protagonist.Elephant;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.graphics.RenderUtils;
import com.crazysaem.alpha.messages.ChangeAnimationMessage;
import com.crazysaem.alpha.messages.FinishedMessage;
import com.crazysaem.alpha.messages.MoveMessage;
import com.crazysaem.alpha.pathfinding.Angle;
import com.crazysaem.alpha.pathfinding.Position;
import com.crazysaem.alpha.picking.CollisionRenderable;

/**
 * Created by crazysaem on 24.06.2014.
 */
public class Fridge extends CollisionRenderable implements AnimationController.AnimationListener, Position, Angle, Telegraph
{
  private Environment environment;
  private String[] animationIDs = {"fridge_open", "fridge_tray", "fridge_tray", "fridge_freezer", "fridge_freezer", "fridge_open"};
  private int currentAnimation = 0;

  public Fridge()
  {
    hasArmature = true;

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
      selectedMaterial.set(new PartialColorAttribute(503.0f / 1024.0f, 0.0f));
    }

//    animationController.setAnimation(animationIDs[0], 1, 1.0f, this);
    currentAnimation = 0;
  }

  @Override
  public void render(RenderBatch renderBatch)
  {
    if (loading)
    {
      return;
    }

    renderBatch.render(modelInstance, environment);
  }

  @Override
  public void onEnd(AnimationController.AnimationDesc animation)
  {
//    currentAnimation++;
//    currentAnimation = currentAnimation % 6;
//
//    if (currentAnimation == 2 || currentAnimation == 4 || currentAnimation == 5)
//    {
//      animationController.queue(animationIDs[currentAnimation], 1, -1.0f, this, 0.05f);
//    }
//    else
//    {
//      animationController.queue(animationIDs[currentAnimation], 1, 1.0f, this, 0.05f);
//    }
  }

  @Override
  public void onLoop(AnimationController.AnimationDesc animation)
  {

  }

  @Override
  public float getX()
  {
    return -2.0f;
  }

  @Override
  public float getZ()
  {
    return 1.5f;
  }

  @Override
  public float getAngle()
  {
    return 225.0f;
  }

  @Override
  public boolean handleMessage(Telegram msg)
  {
    if (msg.message == FinishedMessage.MESSAGE_CODE && msg.extraInfo instanceof FinishedMessage &&
        ((FinishedMessage) msg.extraInfo).getFinishedMessageCode() == MoveMessage.MESSAGE_CODE)
    {
      animationController.setAnimation(animationIDs[0], 1, 1.0f, this);
      animationController.queue(animationIDs[0], 1, -1.0f, this, 0.05f);

      MessageDispatcher.getInstance().dispatchMessage(0.0f, this, msg.sender, ChangeAnimationMessage.MESSAGE_CODE, new ChangeAnimationMessage(Elephant.STANDING, -1, 1.0f));

      return true;
    }

    return false;
  }
}
