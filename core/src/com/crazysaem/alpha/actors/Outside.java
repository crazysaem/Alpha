package com.crazysaem.alpha.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.graphics.Renderable;

/**
 * Created by crazysaem on 08.06.2014.
 */
public class Outside extends Renderable
{
  private Sky sky;

  public Outside()
  {
    sky = new Sky();
  }

  @Override
  protected void finishLoading()
  {
    super.finishLoading("Ground");

    final TextureAttribute textureAttribute = (TextureAttribute) modelInstance.materials.first().get(TextureAttribute.Diffuse);
    textureAttribute.textureDescription.texture.dispose();

    //TODO: This is a terrible way to activate mipmaps for the ground texture. Fix this

    Texture t = new Texture(Gdx.files.internal("models/ground.jpg"), null, true);
    t.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
    textureAttribute.textureDescription.texture = t;

    textureAttribute.textureDescription.magFilter = Texture.TextureFilter.MipMapNearestNearest;
    textureAttribute.textureDescription.minFilter = Texture.TextureFilter.MipMapNearestNearest;
    modelInstance.materials.first().set(textureAttribute);
  }

  @Override
  public void update(float delta)
  {
    super.update(delta);

    sky.update(delta);
  }

  @Override
  public void render(RenderBatch renderBatch)
  {
    if (loading)
      return;

    renderBatch.render(modelInstance);
    renderBatch.flush();
    sky.render(renderBatch);
  }

  class Sky extends Renderable
  {
    private Environment skyEnvironment;

    public Sky()
    {
      skyEnvironment = new Environment();
      skyEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1.0f, 1.0f, 1.0f, 1f));
    }

    @Override
    protected void finishLoading()
    {
      super.finishLoading("Sky");
    }

    @Override
    public void render(RenderBatch renderBatch)
    {
      if (loading)
        return;

      renderBatch.render(modelInstance, skyEnvironment);
    }
  }
}
