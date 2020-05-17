package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class MenuScreen extends BaseScreen {

    @Override
    public void initialize() {
        // background
        BaseActor ocean = new BaseActor(0, 0, mainStage);
        ocean.loadTexture("water.jpg");
        ocean.setSize(800, 600);
        BaseActor.setWorldBounds(ocean);

        BaseActor title = new BaseActor(0, 0, mainStage);
        title.loadTexture("starfish-collector.png");
        title.centerAtPosition(ocean.getWidth() / 2, ocean.getHeight() / 2);
        title.moveBy(0, 100);

        BaseActor start = new BaseActor(0, 0, mainStage);
        start.loadTexture("message-start.png");
        start.centerAtPosition(ocean.getWidth() / 2, ocean.getHeight() / 2);
        start.moveBy(0, -100);
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            StarFishGame.setActiveScreen(new LevelScreen());
    }
}
