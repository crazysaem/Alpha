package com.crazysaem.alpha.graphics;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.utils.Disposable;
import com.crazysaem.alpha.assets.AssetManager;

/**
 * Created by crazysaem on 30.05.2014.
 */
public abstract class Renderable implements Disposable
{
  protected AssetManager assetManager;
  protected AnimationController animationController;
  protected ModelInstance modelInstance;
  protected boolean loading;

  public Renderable()
  {
    assetManager = AssetManager.getInstance();

    loading = true;
  }

  protected abstract void finishLoading();

  protected void finishLoading(final String... rootNodeIds)
  {
    finishLoading(true, rootNodeIds);
  }

  protected void finishLoading(boolean useAnimationController, final String... rootNodeIds)
  {
    modelInstance = assetManager.getModelInstance(rootNodeIds);
    animationController = new AnimationController(modelInstance);

    loading = false;
  }

  public void update(float delta)
  {
    if (loading)
      if (assetManager.isReady())
        finishLoading();
      else
        return;

    if (animationController != null)
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
