package com.crazysaem.alpha;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.crazysaem.alpha.world.World;

public class AlphaGame extends ApplicationAdapter
{
  private World world;

  @Override
  public void create()
  {
    world = new World();
  }

  @Override
  public void resize(int width, int height)
  {
  }

  @Override
  public void render()
  {
    world.update(Gdx.graphics.getDeltaTime());
    world.render();
  }

  @Override
  public void pause()
  {
    System.out.println("ALPHA: PAUSE");
  }

  @Override
  public void resume()
  {
    System.out.println("ALPHA: RESUME");
  }

  @Override
  public void dispose()
  {
    System.out.println("ALPHA: DISPOSE");
    world.dispose();
  }
}
