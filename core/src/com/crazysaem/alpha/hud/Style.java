package com.crazysaem.alpha.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by crazysaem on 29.05.2014.
 */
public class Style implements Disposable
{
  private Skin skin;

  public Style()
  {
    skin = new Skin();
    /*
    // Generate a 1x1 white texture and store it in the skin named "white".
    Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pixmap.setColor(Color.WHITE);
    pixmap.fill();
    skin.add("white", new Texture(pixmap));

    // Store the default libgdx font under the name "default".
    skin.add("default", new BitmapFont());

    // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
    TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
    textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
    textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
    textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
    textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
    textButtonStyle.font = skin.getFont("default");
    skin.add("default", textButtonStyle);

    Label.LabelStyle labelStyle = new Label.LabelStyle();
    labelStyle.font = skin.getFont("default");
    labelStyle.fontColor = Color.BLACK;
    labelStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
    skin.add("default", labelStyle);*/

    Button.ButtonStyle runButtonStyle = new Button.ButtonStyle();
    skin.add("tex-run-128", new Texture(Gdx.files.internal("icons/run-128.png")));
    runButtonStyle.up = skin.newDrawable("tex-run-128");
    skin.add("run", runButtonStyle);

    Button.ButtonStyle carrotButtonStyle = new Button.ButtonStyle();
    skin.add("tex-carrot-128", new Texture(Gdx.files.internal("icons/carrot-128.png")));
    carrotButtonStyle.up = skin.newDrawable("tex-carrot-128");
    skin.add("carrot", carrotButtonStyle);
  }

  public Skin getSkin()
  {
    return skin;
  }

  @Override
  public void dispose()
  {
    skin.dispose();
  }
}
