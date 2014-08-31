package com.crazysaem.alpha.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.msg.MessageDispatcher;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.Disposable;
import com.crazysaem.alpha.actors.food.Carrot;
import com.crazysaem.alpha.actors.furniture.ArmChair;
import com.crazysaem.alpha.actors.furniture.Fridge;
import com.crazysaem.alpha.actors.furniture.Shelf;
import com.crazysaem.alpha.actors.house.Floor;
import com.crazysaem.alpha.actors.house.Walls;
import com.crazysaem.alpha.actors.outside.Ground;
import com.crazysaem.alpha.actors.outside.Sky;
import com.crazysaem.alpha.actors.protagonist.Elephant;
import com.crazysaem.alpha.assets.AssetManager;
import com.crazysaem.alpha.graphics.CameraController;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.graphics.Renderable;
import com.crazysaem.alpha.hud.HUD;
import com.crazysaem.alpha.messages.AStarMessage;
import com.crazysaem.alpha.messages.MessageDispatcherUtil;
import com.crazysaem.alpha.messages.MoveMessage;
import com.crazysaem.alpha.pathfinding.AStarGraph;
import com.crazysaem.alpha.pathfinding.AStarPathFinding;
import com.crazysaem.alpha.picking.CollisionRenderable;
import com.crazysaem.alpha.picking.CollisionRenderablePool;
import com.crazysaem.alpha.picking.RayPicking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by crazysaem on 23.05.2014.
 */
public class World implements Disposable
{
  private static final float CAMERA_HEIGHT = 2.0f;
  private PerspectiveCamera cam;
  private CameraController camController;
  private RenderBatch renderBatch;
  private HUD hud;
  private List<Renderable> renderables;
  private AStarGraph aStarGraph;
  private boolean finishedLoading;
  private Elephant elephant;
  private Shelf shelf;

  public World()
  {
    finishedLoading = false;
    Gdx.gl.glClearColor(70f / 256f, 94f / 256f, 140f / 256f, 1.0f);

    cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    //Place camera
    cam.position.set(0.0f, CAMERA_HEIGHT, 8.0f);
    cam.lookAt(0.0f, CAMERA_HEIGHT, 0.0f);
    cam.near = 1.0f;
    cam.far = 300.0f;
    cam.update();
    camController = new CameraController(cam);
    camController.target.y = CAMERA_HEIGHT;

    renderBatch = new RenderBatch();
    renderables = new ArrayList<Renderable>();

    hud = new HUD();

    Carrot carrot = new Carrot();
    ArmChair armChair = new ArmChair();
    Fridge fridge = new Fridge();
    Walls walls = new Walls();
    Floor floor = new Floor();
    Sky sky = new Sky();
    Ground ground = new Ground();
    shelf = new Shelf();
    elephant = new Elephant();

    renderables.addAll(Arrays.asList(elephant, carrot, armChair, fridge, sky, ground, floor, walls));

    CollisionRenderablePool collisionRenderablePoolGraph = new CollisionRenderablePool(walls, armChair, fridge);
    aStarGraph = new AStarGraph(collisionRenderablePoolGraph);
    AStarPathFinding aStarPathFinding = new AStarPathFinding(aStarGraph, elephant);
    MessageDispatcherUtil.addListeners(AStarMessage.MESSAGE_CODE, aStarPathFinding);
    MessageDispatcherUtil.addListeners(MoveMessage.MESSAGE_CODE, elephant);

    CollisionRenderablePool collisionRenderablePoolInteraction = new CollisionRenderablePool(ground, floor, armChair);
    InputMultiplexer inputMultiplexer = new InputMultiplexer();
    inputMultiplexer.addProcessor(hud.getInputProcessor());
    inputMultiplexer.addProcessor(new RayPicking(cam, collisionRenderablePoolInteraction, new CollisionRenderablePool(walls), Arrays.<CollisionRenderable>asList(ground)));
    inputMultiplexer.addProcessor(camController);
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  private void finishedLoading()
  {
    //All Models/Renderables have been initialized at this point in time
    aStarGraph.recalculateGraph(-19, -19, 19, 19);
    //aStarGraph.createDebugRenderGraphics();

    finishedLoading = true;
  }

  public void update(float delta)
  {
    camController.update();
    MessageDispatcher.getInstance().dispatchDelayedMessages();
    hud.update(delta);
    for (Renderable renderable : renderables)
    {
      renderable.update(delta);
    }

    if (!finishedLoading)
    {
      for (Renderable renderable : renderables)
      {
        if (!renderable.isFinished())
        {
          return;
        }
      }

      finishedLoading();
    }

    if (elephant.isMoving())
    {
      cam.lookAt(elephant.getX(), CAMERA_HEIGHT, elephant.getZ());
      cam.up.x = 0.0f;
      cam.up.y = 1.0f;
      cam.up.z = 0.0f;
      cam.translate(elephant.getDeltaX(), 0.0f, elephant.getDeltaZ());
      cam.update();
      camController.target.x = elephant.getX();
      camController.target.z = elephant.getZ();
    }

    shelf.update(delta);
  }

  public void render()
  {
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    renderBatch.begin(cam);
    for (Renderable renderable : renderables)
    {
      renderable.render(renderBatch);
    }

    //We need to flush once to enable blending
    renderBatch.flush();
    shelf.render(renderBatch);

    //aStarGraph.debugRender(renderBatch);
    renderBatch.end();

    hud.render();
  }

  @Override
  public void dispose()
  {
    for (Renderable renderable : renderables)
    {
      renderable.dispose();
    }
    shelf.dispose();

    AssetManager.getInstance().dispose();
  }
}
