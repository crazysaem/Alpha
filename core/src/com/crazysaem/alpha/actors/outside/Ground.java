package com.crazysaem.alpha.actors.outside;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.crazysaem.alpha.events.Event;
import com.crazysaem.alpha.events.EventHandler;
import com.crazysaem.alpha.events.HitEvent;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.graphics.Renderable;
import com.crazysaem.alpha.picking.StaticRenderable;

/**
 * Created by crazysaem on 08.06.2014.
 */
public class Ground extends StaticRenderable implements EventHandler
{
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
  public void handleEvent(Event event)
  {
    if (event instanceof HitEvent)
    {
      HitEvent hitEvent = (HitEvent) event;
      System.out.println("Ground recieved hit event: " + hitEvent.getHitPos().toString());
    }
    else
    {
      System.out.println("Ground recieved unknown event: " + event.getAction());
    }
  }
}
