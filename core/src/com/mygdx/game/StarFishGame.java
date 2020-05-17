package com.mygdx.game;

public class StarFishGame extends BaseGame {

	@Override
	public void create() {
		super.create();
		setActiveScreen(new MenuScreen());
	}

}
