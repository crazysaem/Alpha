package com.crazysaem.alpha;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.utils.Disposable;
import com.crazysaem.alpha.hud.HUD;

/**
 * Created by crazysaem on 23.05.2014.
 */
public class World implements Disposable
{
  private PerspectiveCamera cam;
  private CameraInputController camController;
  private RenderBatch renderBatch;

  private HUD hud;
  private Pet pet;

  public World()
  {
    cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    cam.position.set(0f, 0f, 5f);
    cam.lookAt(0, 0, 0);
    cam.near = 1f;
    cam.far = 300f;
    cam.update();
    camController = new CameraInputController(cam);

    renderBatch = new RenderBatch();

    hud = new HUD();
    pet = new Pet();

    InputMultiplexer inputMultiplexer = new InputMultiplexer();
    inputMultiplexer.addProcessor(hud.getInputProcessor());
    inputMultiplexer.addProcessor(camController);
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  public void update(float delta)
  {
    camController.update();
    hud.update(delta);
    pet.update(delta);
  }

  public void render()
  {
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    renderBatch.begin(cam);
    pet.render(renderBatch);
    renderBatch.end();

    hud.render();
  }

  @Override
  public void dispose()
  {
    renderBatch.dispose();
  }
}
