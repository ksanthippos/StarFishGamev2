package com.mygdx.game.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.StarFishGame;

public class DesktopLauncher {
	public static void main (String[] arg) {

		Game game = new StarFishGame();
		new LwjglApplication(game, "Starfish Collector", 800, 600);

	}
}
