package com.crazysaem.alpha.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by crazysaem on 03.06.2014.
 */
public class HUDButton extends Button
{
  private BitmapFont font;
  private ShaderProgram fontShader;
  private Texture texture;

  public HUDButton(Skin skin, String styleName)
  {
    super(skin, styleName);

    fontShader = new ShaderProgram(Gdx.files.internal("fonts/font.vert"), Gdx.files.internal("fonts/font2.frag"));
    if (!fontShader.isCompiled()) {
      Gdx.app.error("fontShader", "compilation failed:\n" + fontShader.getLog());
    }



    texture = new Texture(Gdx.files.internal("fonts/firaSansRegular.png"), true); // true enables mipmaps
    texture.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear); // linear filtering in nearest mipmap image

    font = new BitmapFont(Gdx.files.internal("fonts/firaSansRegular.fnt"), new TextureRegion(texture), false);
    //font.setScale(1f);
  }

  @Override
  public void draw(Batch batch, float parentAlpha)
  {
    super.draw(batch, parentAlpha);

    batch.setShader(fontShader);
    font.draw(batch, "99 22RSd3wdwäöüß", this.getX() + 80, 35);
    batch.setShader(null);
  }
}
