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
  public void render()
  {
    world.update(Gdx.graphics.getDeltaTime());
    world.render();
  }

  @Override
  public void dispose()
  {
    world.dispose();
  }
}
