package com.crazysaem.alpha;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by crazysaem on 23.05.2014.
 */
public class Pet implements Disposable
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
    assetManager = new AssetManager();
    assetManager.load(MODEL, Model.class);

    loading = true;
  }

  private void finishLoading()
  {
    Model model = assetManager.get(MODEL, Model.class);
    modelInstance = new ModelInstance(model);
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
      if (assetManager.update())
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
  public void dispose()
  {
    assetManager.dispose();
  }
}
