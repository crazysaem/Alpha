package com.crazysaem.alpha.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by crazysaem on 28.05.2014.
 */
public class HUD implements Disposable
{
  private Stage stage;
  private SpriteBatch batch;
  private Table table;
  private BitmapFont font;

  private HUDIndicator indicator;

  public HUD()
  {
    stage = new Stage();
    stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    batch = (SpriteBatch) stage.getBatch();

    table = new Table();
    //table.debug();
    //table.debugTable();
    table.setFillParent(true);
    stage.addActor(table);

    font = new BitmapFont();

    indicator = new HUDIndicator(HudIndicatorType.HUNGER, "icons/carrot/alpha.png", "icons/carrot/base.png", "icons/carrot/outline.png");
    table.add(indicator);

    table.bottom().left();
  }

  public void update(float delta)
  {
//    if (indicator.getvalue() >= 1.0f)
//    {
//      indicator.setValue(0.0f);
//    }
//
//    indicator.setValue(indicator.getvalue() + delta * 0.5f);
    stage.act(delta);
  }

  public void render()
  {
    stage.draw();

    //Table.drawDebug(stage);
  }

  public InputProcessor getInputProcessor()
  {
    return stage;
  }

  @Override
  public void dispose()
  {
    stage.dispose();
    font.dispose();
  }
}
