package com.crazysaem.alpha.picking;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.Agent;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.crazysaem.alpha.actors.outside.Ground;
import com.crazysaem.alpha.messages.AStarMessage;
import com.crazysaem.alpha.pathfinding.Position;

import java.util.List;

/**
 * Created by crazysaem on 07.06.2014.
 */
public class RayPicking implements InputProcessor
{
  private int touchDownX, touchDownY;
  private Camera cam;
  private CollisionRenderablePool collisionRenderablePoolInteraction;
  private CollisionRenderablePool staticTargetPoolObfuscation;
  private List<CollisionRenderable> collisionObfuscatedRenderables;

  public RayPicking(Camera cam, CollisionRenderablePool collisionRenderablePoolInteraction, CollisionRenderablePool staticTargetPoolObfuscation, List<CollisionRenderable> collisionObfuscatedRenderables)
  {
    this.cam = cam;
    this.collisionRenderablePoolInteraction = collisionRenderablePoolInteraction;
    this.staticTargetPoolObfuscation = staticTargetPoolObfuscation;
    this.collisionObfuscatedRenderables = collisionObfuscatedRenderables;
  }

  @Override
  public boolean keyDown(int keycode)
  {
    return false;
  }

  @Override
  public boolean keyUp(int keycode)
  {
    return false;
  }

  @Override
  public boolean keyTyped(char character)
  {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button)
  {
    touchDownX = screenX;
    touchDownY = screenY;
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button)
  {
    if (touchDownX == screenX && touchDownY == screenY)
    {
      Ray ray = cam.getPickRay(screenX, screenY);

      CollisionRenderable collisionRenderable = collisionRenderablePoolInteraction.collisonCheck(ray);
      if (collisionRenderable != null)
      {
        //TODO: Terrible check! Possible fix is to change walls to always be 'cut off' so an obfuscation check isn't even necessary
        if (collisionObfuscatedRenderables.contains(collisionRenderable) && staticTargetPoolObfuscation.collisonCheck(ray) != null)
          return false;

        AStarMessage aStarMessage;
        if (collisionRenderable instanceof Position)
        {
          Position position = (Position) collisionRenderable;
          aStarMessage = new AStarMessage(position.getX(), position.getZ());
        }
        else
        {
          Vector3 lastIntersection = collisionRenderablePoolInteraction.getLastIntersection();
          aStarMessage = new AStarMessage(lastIntersection.x, lastIntersection.z);
        }

        Agent sender = null;
        if (collisionRenderable instanceof Agent)
          sender = (Agent) collisionRenderable;

        MessageDispatcher.getInstance().dispatchMessage(0.0f, sender, null, AStarMessage.MESSAGE_CODE, aStarMessage);
      }
    }

    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer)
  {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY)
  {
    return false;
  }

  @Override
  public boolean scrolled(int amount)
  {
    return false;
  }
}
