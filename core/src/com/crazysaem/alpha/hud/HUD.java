package com.crazysaem.alpha.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

    final Button runButton = new Button(style.getSkin(), "run");
    runButton.addListener(new ChangeListener()
    {
      @Override
      public void changed(ChangeEvent event, Actor actor)
      {
        System.out.println("Telling the Pet to run");
        eventManager.addEvent(new Event(EventTarget.PET, "walk"));
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
        eventManager.addEvent(new Event(EventTarget.PET, "carrot"));
        eventManager.addEvent(new Event(EventTarget.CARROT, "carrot"));
      }
    });
    table.add(carrotButton);

    table.bottom().left();
  }

  public void update(float delta)
  {
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
