package com.crazysaem.alpha.assets;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by crazysaem on 30.05.2014.
 */
public class AssetManager implements Disposable
{
  private static final String FATMODEL = "models/AllAssets.g3db";
  private static AssetManager instance = null;
  private com.badlogic.gdx.assets.AssetManager assetManager;
  private Model fatModel;
  private boolean loading;

  private AssetManager()
  {
    loading = true;

    assetManager = new com.badlogic.gdx.assets.AssetManager();
    assetManager.load(FATMODEL, Model.class);
  }

  public static AssetManager getInstance()
  {
    if (instance == null)
    {
      instance = new AssetManager();
    }

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
    {
      if (assetManager.update())
      {
        finishLoading();
      }
      else
      {
        return false;
      }
    }

    return true;
  }

  public void reload()
  {
    assetManager.finishLoading();
  }

  public ModelInstance getModelInstance(final String... rootNodeIds)
  {
    return new ModelInstance(fatModel, rootNodeIds);
  }

  @Override
  public void dispose()
  {
    loading = true;
    assetManager.unload(FATMODEL);
    assetManager.dispose();
    instance = null;
  }
}
