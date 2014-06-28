package com.crazysaem.alpha.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by crazysaem on 23.05.2014.
 */
public class RenderBatch implements Disposable
{
  private ModelBatch modelBatch;
  private Environment defaultEnvironment;

  public RenderBatch()
  {
    modelBatch = new ModelBatch();
    defaultEnvironment = new Environment();
    //defaultEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1.0f, 1.0f, 1.0f, 1.0f));
    defaultEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1.0f));
    defaultEnvironment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 1.0f, -1.0f, 1.0f));
    defaultEnvironment.add(new DirectionalLight().set(0.6f, 0.6f, 0.6f, 0.0f, 0.0f, -1.0f));
  }

  public void begin(Camera cam)
  {
    modelBatch.begin(cam);
  }

  public void render(ModelInstance modelInstance)
  {
    render(modelInstance, defaultEnvironment);
  }

  public void render(ModelInstance modelInstance, Environment environment)
  {
    modelBatch.render(modelInstance, environment);
  }

  public void flush()
  {
    modelBatch.flush();
  }

  public void end()
  {
    modelBatch.end();
  }

  @Override
  public void dispose()
  {
    modelBatch.dispose();
  }
}
