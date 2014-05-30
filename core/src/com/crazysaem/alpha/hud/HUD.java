package com.crazysaem.alpha.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.crazysaem.alpha.graphics.RenderBatch;
import com.crazysaem.alpha.graphics.Renderable;

/**
 * Created by crazysaem on 28.05.2014.
 */
public class HUD implements Disposable
{
  private Stage stage;
  private Table table;

  private Style style;

  public HUD()
  {
    stage = new Stage();
    stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    Gdx.input.setInputProcessor(stage);

    table = new Table();
    table.debug();
    table.debugTable();
    table.setFillParent(true);
    stage.addActor(table);

    style = new Style();

    // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
    final TextButton button = new TextButton("Click me!", style.getSkin());
    table.add(button);

    button.addListener(new ChangeListener() {
      public void changed (ChangeEvent event, Actor actor) {
        System.out.println("Clicked! Is checked: " + button.isChecked());
        button.setText("Good job!");
      }
    });

    // Add an image actor. Have to set the size, else it would be the size of the drawable (which is the 1x1 texture).
    table.add(new Image(style.getSkin().newDrawable("white", Color.PINK))).size(64);

    table.row();

    Label label = new Label("TEST", style.getSkin());
    table.add(label);

    Button carrotButton = new Button(style.getSkin());
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

    Table.drawDebug(stage);
  }

  public InputProcessor getInputProcessor()
  {
    return stage;
  }

  @Override
  public void dispose()
  {
    stage.dispose();
    style.dispose();
  }
}
