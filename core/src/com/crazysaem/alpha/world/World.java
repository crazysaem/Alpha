package com.crazysaem.alpha.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Disposable;
import com.crazysaem.alpha.actors.*;
import com.crazysaem.alpha.events.EventManager;
import com.crazysaem.alpha.events.EventTarget;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.graphics.Renderable;
import com.crazysaem.alpha.hud.HUD;
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
  private CameraInputController camController;
  private RenderBatch renderBatch;

  private EventManager eventManager;
  private HUD hud;

  private List<Renderable> renderables;

  public World()
  {
    Gdx.gl.glClearColor(70f / 256f, 94f / 256f, 140f / 256f, 1.0f);

    cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    //Place camera a little higher
    cam.position.set(0f, 1.12f, 8f);
    cam.lookAt(0, 1.12f, 0);
    cam.near = 1f;
    cam.far = 300f;
    cam.update();
    camController = new CameraInputController(cam);

    renderBatch = new RenderBatch();
    renderables = new ArrayList<Renderable>();

    eventManager = new EventManager();
    hud = new HUD(eventManager);

    Pet pet = new Pet();
    Carrot carrot = new Carrot();
    Furniture furniture = new Furniture();
    House house = new House();
    Outside outside = new Outside();

    eventManager.registerEventHandler(EventTarget.PET, pet);
    eventManager.registerEventHandler(EventTarget.CARROT, carrot);
    eventManager.registerEventHandler(EventTarget.ARMCHAIR, furniture);
    eventManager.registerEventHandler(EventTarget.HOUSE, house);

    renderables.add(pet);
    renderables.add(carrot);
    renderables.add(furniture);
    renderables.add(outside);
    renderables.add(house);

    StaticTargetPool staticTargetPool = new StaticTargetPool();
    staticTargetPool.add(new StaticTarget(house.houseParts.get(0), EventTarget.HOUSE));
    staticTargetPool.add(new StaticTarget(furniture, EventTarget.ARMCHAIR));

    InputMultiplexer inputMultiplexer = new InputMultiplexer();
    inputMultiplexer.addProcessor(hud.getInputProcessor());
    inputMultiplexer.addProcessor(new RayPicking(cam, eventManager, staticTargetPool));
    inputMultiplexer.addProcessor(camController);
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  public void update(float delta)
  {
    camController.update();
    eventManager.update();
    hud.update(delta);
    for (Renderable renderable : renderables)
      renderable.update(delta);
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
      //TODO: This seems to be a bug of libgdx, find proper way to do this
      renderBatch.flush();
    }
    renderBatch.end();
    hud.render();
  }

  @Override
  public void dispose()
  {
    renderBatch.dispose();
  }
}
