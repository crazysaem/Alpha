package com.crazysaem.alpha.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.crazysaem.alpha.AlphaGame;

public class DesktopLauncher
{
  public static void main(String[] arg)
  {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    /*config.width = 1920;
    config.height = 1080;
    config.fullscreen = true;*/
    config.width = 1200;
    config.height = 720;
    config.fullscreen = false;
    config.samples = 2;
    config.vSyncEnabled = true;
    new LwjglApplication(new AlphaGame(), config);
  }
}
