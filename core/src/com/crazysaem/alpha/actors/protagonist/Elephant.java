package com.crazysaem.alpha.actors.protagonist;

import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.crazysaem.alpha.graphics.RenderUtils;
import com.crazysaem.alpha.graphics.Renderable;
import com.crazysaem.alpha.messages.ChangeAnimationMessage;
import com.crazysaem.alpha.messages.FinishedMessage;
import com.crazysaem.alpha.messages.MoveMessage;
import com.crazysaem.alpha.pathfinding.Position;
import com.crazysaem.alpha.pathfinding.PositionPerTime;

/**
 * Created by crazysaem on 23.05.2014.
 */
public class Elephant extends Renderable implements Telegraph, Position, AnimationController.AnimationListener
{
  public static final String NONE = "";
  public static final String STANDING = "idle";
  public static final String SITTING = "sitting";
  public static final String WALKING = "walk";

  private Vector3 position, deltaPosition, initialDirection, direction, upVector;
  private PositionPerTime positionPerTime;
  private Vector3 movePosition;
  private float faceAnimationTime;
  private boolean isMoving;
  private float directionAngle;
  private String currentPose;
  private boolean animationInProgress;
  private int blinkingStep;
  private float uOffsetStep, vOffsetStep;
  private TextureAttribute textureAttributeElephantFace;
  private Telegraph lastSender;

  protected void finishLoading()
  {
    super.finishLoading("Scouter", "Elephant", "ElephantFace", "Cap", "TShirt", "Trousers", "ElephantArmature");

    uOffsetStep = 144.0f / 512.0f;
    vOffsetStep = 219.0f / 512.0f;

    Material selectedMaterial;
    if ((selectedMaterial = RenderUtils.getMaterial(modelInstance, "ElephantFace")) != null)
    {
      textureAttributeElephantFace = (TextureAttribute) selectedMaterial.get(TextureAttribute.Diffuse);
      selectedMaterial.set(new BlendingAttribute(true, 1.0f));
      selectedMaterial.set(new DepthTestAttribute(0));
    }

    if ((selectedMaterial = RenderUtils.getMaterial(modelInstance, "Cap")) != null)
    {
      RenderUtils.activateMipMap(selectedMaterial, "models/cap.jpg", Texture.TextureFilter.MipMapLinearLinear);
    }

    if ((selectedMaterial = RenderUtils.getMaterial(modelInstance, "Scouter")) != null)
    {
      selectedMaterial.set(new BlendingAttribute(true, 1.0f));
    }

    animationController.setAnimation(STANDING, -1);
    position = new Vector3();
    deltaPosition = new Vector3();
    initialDirection = new Vector3(0.0f, 0.0f, 1.0f);
    direction = new Vector3(0.0f, 0.0f, 1.0f);
    upVector = new Vector3(0.0f, 1.0f, 0.0f);
    movePosition = new Vector3();
    currentPose = STANDING;
    animationInProgress = false;
    lastSender = null;
  }

  @Override
  public void update(float delta)
  {
    super.update(delta);

    if (animationInProgress || loading)
    {
      return;
    }

    faceAnimationTime += delta;

    if (faceAnimationTime > 2.0f || blinkingStep > 0)
    {
      if (faceAnimationTime > 0.01f)
      {
        blinkingStep++;

        switch (blinkingStep)
        {
          case 1:
            textureAttributeElephantFace.offsetU = uOffsetStep;
            break;

          case 2:
            textureAttributeElephantFace.offsetU = uOffsetStep * 2;
            break;

          case 3:
            textureAttributeElephantFace.offsetU = 0.0f;
            textureAttributeElephantFace.offsetV = vOffsetStep;
            break;

          case 4:
            textureAttributeElephantFace.offsetU = 0.0f;
            textureAttributeElephantFace.offsetV = 0.0f;
            blinkingStep = 0;
            break;
        }

        faceAnimationTime = 0.0f;
      }
    }

    if (isMoving)
    {
      if (positionPerTime.getPosition(2.0f, delta, movePosition))
      {
        isMoving = false;
        deltaPosition.x = 0.0f;
        deltaPosition.y = 0.0f;
        deltaPosition.z = 0.0f;

        if (positionPerTime.getAngle() >= 0.0f)
        {
          modelInstance.transform.setToTranslation(position.x, 0.0f, position.z).rotate(upVector, positionPerTime.getAngle());
        }

        MessageDispatcher.getInstance().dispatchMessage(0.0f, this, lastSender, FinishedMessage.MESSAGE_CODE, new FinishedMessage(MoveMessage.MESSAGE_CODE));
      }
      else
      {
        direction.x = movePosition.x - position.x;
        direction.z = movePosition.z - position.z;
        deltaPosition.x = direction.x;
        deltaPosition.z = direction.z;
        direction = direction.nor();

        directionAngle = (float) Math.acos(initialDirection.dot(direction)) * 57.3f;
        if (direction.x < 0)
        {
          directionAngle = 360 - directionAngle;
        }

        modelInstance.transform.setToTranslation(movePosition.x, 0.0f, movePosition.z).rotate(upVector, directionAngle);
      }

      position.x = movePosition.x;
      position.z = movePosition.z;
    }
  }

  @Override
  public boolean handleMessage(Telegram msg)
  {
    lastSender = msg.sender;

    if (msg.message == MoveMessage.MESSAGE_CODE && msg.extraInfo instanceof MoveMessage)
    {
      MoveMessage moveMessage = (MoveMessage) msg.extraInfo;
      positionPerTime = moveMessage.getPositionPerTime();

      if (currentPose.equals(STANDING))
      {
        animationController.setAnimation(WALKING, -1);
      }
      else
      {
        animationController.setAnimation(currentPose, 1, -1.0f, this);
        animationController.queue(WALKING, -1, 1.0f, null, 0.05f);
        animationInProgress = true;
        currentPose = WALKING;
      }

      isMoving = true;

      return true;
    }
    else if (msg.message == ChangeAnimationMessage.MESSAGE_CODE && msg.extraInfo instanceof ChangeAnimationMessage)
    {
      ChangeAnimationMessage switchPositionMessage = (ChangeAnimationMessage) msg.extraInfo;
      currentPose = switchPositionMessage.getAnimation();
      animationController.setAnimation(currentPose, switchPositionMessage.getLoopCount(), switchPositionMessage.getSpeed(), this);

      if (switchPositionMessage.getLoopCount() >= 0)
      {
        animationInProgress = true;
      }
    }

    return false;
  }

  public boolean isMoving()
  {
    return isMoving;
  }

  @Override
  public float getX()
  {
    return position.x;
  }

  @Override
  public float getZ()
  {
    return position.z;
  }

  public float getDeltaX()
  {
    return deltaPosition.x;
  }

  public float getDeltaZ()
  {
    return deltaPosition.z;
  }

  public float getDirectionAngle()
  {
    return directionAngle;
  }

  @Override
  public void onEnd(AnimationController.AnimationDesc animation)
  {
    animationInProgress = false;
  }

  @Override
  public void onLoop(AnimationController.AnimationDesc animation)
  {

  }
}
