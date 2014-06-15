package com.crazysaem.alpha.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.crazysaem.alpha.actors.food.Carrot;
import com.crazysaem.alpha.actors.furniture.ArmChair;
import com.crazysaem.alpha.actors.furniture.Shelf;
import com.crazysaem.alpha.actors.house.House;
import com.crazysaem.alpha.actors.outside.Ground;
import com.crazysaem.alpha.actors.outside.Sky;
import com.crazysaem.alpha.actors.protagonist.Elephant;
import com.crazysaem.alpha.events.EventManager;
import com.crazysaem.alpha.events.EventTarget;
import com.crazysaem.alpha.graphics.CameraController;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.graphics.Renderable;
import com.crazysaem.alpha.hud.HUD;
import com.crazysaem.alpha.pathfinding.AStarAlgorithm;
import com.crazysaem.alpha.pathfinding.AStarGraph;
import com.crazysaem.alpha.picking.RayPicking;
import com.crazysaem.alpha.picking.StaticTarget;
import com.crazysaem.alpha.picking.StaticTargetPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by crazysaem on 23.05.2014.
 */
public class World implements Disposable
{
  private PerspectiveCamera cam;
  private CameraController camController;
  private RenderBatch renderBatch;
  private EventManager eventManager;
  private HUD hud;
  private List<Renderable> renderables;
  private AStarGraph aStarGraph;
  private boolean finishedLoading;
  private Elephant elephant;

  public World()
  {
    finishedLoading = false;
    Gdx.gl.glClearColor(70f / 256f, 94f / 256f, 140f / 256f, 1.0f);

    cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    //Place camera
    cam.position.set(0.0f, 1.12f, 8.0f);
    cam.lookAt(0.0f, 1.12f, 0.0f);
    cam.near = 1.0f;
    cam.far = 300.0f;
    cam.update();
    camController = new CameraController(cam);
    camController.target.y = 1.12f;

    renderBatch = new RenderBatch();
    renderables = new ArrayList<Renderable>();

    eventManager = new EventManager();
    hud = new HUD(eventManager);

    Carrot carrot = new Carrot();
    ArmChair armChair = new ArmChair();
    House house = new House();
    Sky sky = new Sky();
    Ground ground = new Ground();
    Shelf shelf = new Shelf();
    elephant = new Elephant();

    eventManager.registerEventHandler(EventTarget.NONE, null);
    eventManager.registerEventHandler(EventTarget.ELEPHANT, elephant);
    eventManager.registerEventHandler(EventTarget.CARROT, carrot);
    eventManager.registerEventHandler(EventTarget.ARMCHAIR, armChair);
    eventManager.registerEventHandler(EventTarget.HOUSE, house);
    eventManager.registerEventHandler(EventTarget.GROUND, ground);

    renderables.add(elephant);
    renderables.add(carrot);
    renderables.add(armChair);
    renderables.add(sky);
    renderables.add(ground);
    renderables.add(house);
    renderables.add(shelf);

    StaticTargetPool staticTargetPoolGraph = new StaticTargetPool();
    staticTargetPoolGraph.add(new StaticTarget(house.houseParts.get(1), EventTarget.HOUSE));
    staticTargetPoolGraph.add(new StaticTarget(house.houseParts.get(2), EventTarget.HOUSE));
    staticTargetPoolGraph.add(new StaticTarget(armChair, EventTarget.ARMCHAIR));

    aStarGraph = new AStarGraph(staticTargetPoolGraph);
    AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(aStarGraph, eventManager, elephant);
    eventManager.registerEventHandler(EventTarget.ASTAR_GROUND, aStarAlgorithm);
    eventManager.registerEventHandler(EventTarget.ASTAR_FLOOR, aStarAlgorithm);
    eventManager.registerEventHandler(EventTarget.ASTAR_ARMCHAIR, aStarAlgorithm);

    StaticTargetPool staticTargetPoolInteraction = new StaticTargetPool();
    staticTargetPoolInteraction.add(new StaticTarget(ground, EventTarget.ASTAR_GROUND));
    staticTargetPoolInteraction.add(new StaticTarget(house.houseParts.get(0), EventTarget.ASTAR_FLOOR));
    staticTargetPoolInteraction.add(new StaticTarget(armChair, EventTarget.ASTAR_ARMCHAIR));

    StaticTargetPool staticTargetPoolObfuscation = new StaticTargetPool();
    staticTargetPoolObfuscation.add(new StaticTarget(house.houseParts.get(1), EventTarget.NONE));
    staticTargetPoolObfuscation.add(new StaticTarget(house.houseParts.get(2), EventTarget.NONE));

    InputMultiplexer inputMultiplexer = new InputMultiplexer();
    inputMultiplexer.addProcessor(hud.getInputProcessor());
    inputMultiplexer.addProcessor(new RayPicking(cam, eventManager, staticTargetPoolInteraction, staticTargetPoolObfuscation));
    inputMultiplexer.addProcessor(camController);
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  private void finishedLoading()
  {
    //All Models have been initialized here
    aStarGraph.recalculateGraph(-9, -9, 9, 9);
    //aStarGraph.createDebugRenderGraphics();

    finishedLoading = true;
  }

  public void update(float delta)
  {
    camController.update();
    eventManager.update();
    hud.update(delta);
    for (Renderable renderable : renderables)
      renderable.update(delta);

    if (!finishedLoading)
    {
      for (Renderable renderable : renderables)
        if (!renderable.isFinished())
          return;

      finishedLoading();
    }

    if (elephant.isMoving())
    {
      cam.lookAt(elephant.getX(), 1.12f, elephant.getZ());
      cam.up.x = 0.0f;
      cam.up.y = 1.0f;
      cam.up.z = 0.0f;
      cam.translate(elephant.getDeltaX(), 0.0f, elephant.getDeltaZ());
      cam.update();
      camController.target.x = elephant.getX();
      camController.target.z = elephant.getZ();
    }
  }

  public void render()
  {
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    renderBatch.begin(cam);
    for (Renderable renderable : renderables)
    {
      renderable.render(renderBatch);
      //If renderbatch is not flushed here, the texture of the pet is also applied to the carrot
      //TODO: This seems to be a bug of libgdx? (It only happens on Android HTC DESIRE Z, but not on Nexus 7), find proper way to do this
      renderBatch.flush();
    }
    //aStarGraph.debugRender(renderBatch);
    renderBatch.end();
    hud.render();
  }

  @Override
  public void dispose()
  {
    renderBatch.dispose();
  }
}
