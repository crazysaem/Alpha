package com.crazysaem.alpha.assets;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by crazysaem on 30.05.2014.
 */
public class AssetManager implements Disposable
{
  private com.badlogic.gdx.assets.AssetManager assetManager;

  private static final String FATMODEL = "models/AllAssets.g3db";

  private static final AssetManager instance = new AssetManager();
  private Model fatModel;
  private boolean loading;
  private boolean isDisposed;

  private AssetManager()
  {
    loading = true;
    isDisposed = false;

    assetManager = new com.badlogic.gdx.assets.AssetManager();
    assetManager.load(FATMODEL, Model.class);
  }

  public static AssetManager getInstance()
  {
    return instance;
  }

  private void finishLoading()
  {
    fatModel = assetManager.get(FATMODEL, Model.class);
    loading = false;
  }

  public boolean isReady()
  {
    if (loading)
      if (assetManager.update())
        finishLoading();
      else
        return false;

    return true;
  }

  public ModelInstance getModelInstance(final String... rootNodeIds)
  {
    return new ModelInstance(fatModel, rootNodeIds);
  }

  @Override
  public void dispose()
  {
    if (!isDisposed)
      assetManager.dispose();

    isDisposed = true;
  }
}
