package com.crazysaem.alpha.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

/**
 * Created by crazysaem on 21.06.2014.
 */
public class Utils
{
  //TODO: This is a terrible way to activateMipMap mipmaps for the ground texture. Fix this
  public static void activateMipMap(Material material, String fileName, Texture.TextureFilter textureFilter)
  {
    final TextureAttribute textureAttribute = (TextureAttribute) material.get(TextureAttribute.Diffuse);
    textureAttribute.textureDescription.texture.dispose();

    Texture t = new Texture(Gdx.files.internal(fileName), null, true);
    t.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.MipMapLinearLinear);
    textureAttribute.textureDescription.texture = t;

    textureAttribute.textureDescription.magFilter = textureFilter;
    textureAttribute.textureDescription.minFilter = textureFilter;
    material.set(textureAttribute);
  }

  public static Material getMaterial(ModelInstance modelInstance, String partialName)
  {
    for (Material material : modelInstance.materials)
    {
      if (material.id.contains(partialName))
      {
        return material;
      }
    }

    return null;
  }
}
