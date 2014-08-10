package com.crazysaem.alpha.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.crazysaem.alpha.events.Event;
import com.crazysaem.alpha.events.EventManager;
import com.crazysaem.alpha.events.EventTarget;

/**
 * Created by crazysaem on 28.05.2014.
 */
public class HUD implements Disposable
{
  private Stage stage;
  private SpriteBatch batch;
  private Table table;
  private BitmapFont font;

  private Style style;
  private EventManager eventManager;

  private HUDIndicator indicator;

  public HUD(final EventManager eventManager)
  {
    this.eventManager = eventManager;
    stage = new Stage();
    stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    //Gdx.input.setInputProcessor(stage);
    batch = (SpriteBatch)stage.getBatch();

    table = new Table();
    //table.debug();
    //table.debugTable();
    table.setFillParent(true);
    stage.addActor(table);

    font = new BitmapFont();

    style = new Style();
    /*
    TextureAtlas textureAtlas = new TextureAtlas("texturepacks/texturepack.atlas");
    NinePatch ninePatch = textureAtlas.createPatch("nine");

    ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
    Texture texture = new Texture(Gdx.files.internal("ui/progress.png"), true);
    TextureRegion textureRegion = new TextureRegion(texture);

    progressBarStyle.background = new NinePatchDrawable(ninePatch);

    ProgressBar progressBar = new ProgressBar(0.0f, 100.0f, 1.0f, false, progressBarStyle);
    System.out.println(progressBar.setValue(100.0f));
    table.add(progressBar);*/
    /*
    final Button runButton = new Button(style.getSkin(), "run");
    runButton.addListener(new ChangeListener()
    {
      @Override
      public void changed(ChangeEvent event, Actor actor)
      {
        System.out.println("Telling the Pet to run");
        eventManager.addEvent(new Event(EventTarget.ELEPHANT, "walk"));
        eventManager.addEvent(new Event(EventTarget.CARROT, "idle"));
      }
    });
    table.add(runButton);

    final Button carrotButton = new HUDButton(style.getSkin(), "carrot");
    carrotButton.addListener(new ChangeListener()
    {
      @Override
      public void changed(ChangeEvent event, Actor actor)
      {
        System.out.println("Telling the Pet to eat a carrot");
        eventManager.addEvent(new Event(EventTarget.ELEPHANT, "carrot"));
        eventManager.addEvent(new Event(EventTarget.CARROT, "carrot"));
      }
    });
    table.add(carrotButton);*/

    indicator = new HUDIndicator("icons/carrot/alpha.png", "icons/carrot/base.png", "icons/carrot/outline.png");
    table.add(indicator);

    table.bottom().left();
  }

  public void update(float delta)
  {
    if (indicator.getvalue() >= 1.0f)
      indicator.setValue(0.0f);

    indicator.setValue(indicator.getvalue() + delta * 0.5f);
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
    style.dispose();
  }
}
