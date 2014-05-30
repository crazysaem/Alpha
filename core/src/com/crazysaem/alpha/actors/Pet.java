package com.crazysaem.alpha.actors;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;
import com.crazysaem.alpha.events.Event;
import com.crazysaem.alpha.events.EventHandler;
import com.crazysaem.alpha.events.EventTarget;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.assets.AssetManager;
import com.crazysaem.alpha.graphics.Renderable;

/**
 * Created by crazysaem on 23.05.2014.
 */
public class Pet implements Renderable, EventHandler, Disposable
{
  private AssetManager assetManager;
  private AnimationController animationController;
  private ModelInstance modelInstance;

  private static final String MODEL = "models/AllAssets.g3db";
  private static final String IDLE = "idle";
  private static final String WALK = "walk";
  private boolean loading;

  public Pet()
  {
    assetManager = AssetManager.getInstance();
    loading = true;
  }

  private void finishLoading()
  {
    modelInstance = assetManager.getModelInstance("Elephant", "ElephantArmature");
    BoundingBox box = new BoundingBox();
    modelInstance.calculateBoundingBox(box);
    modelInstance.transform.setToTranslation(0, -box.getDimensions().y / 2, 0);
    animationController = new AnimationController(modelInstance);
    animationController.setAnimation(IDLE, -1);

    loading = false;
  }

  public void update(float delta)
  {
    if (loading)
      if (assetManager.isReady())
        finishLoading();
      else
        return;

    animationController.update(delta);
  }

  public void render(RenderBatch renderBatch)
  {
    if (loading)
      return;

    renderBatch.render(modelInstance);
  }

  @Override
  public void handleEvent(Event event)
  {
    System.out.println("Pet recieved event: " + event.getAction());
  }

  @Override
  public void dispose()
  {
    assetManager.dispose();
  }
}
