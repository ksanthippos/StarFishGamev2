package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Shark extends BaseActor {

    private int angle;

    public Shark(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("sharky.png");
        setBoundaryPolygon(8);

        // sharks swim in random directions
        angle = 90 * MathUtils.random(0, 3);
        setAcceleration(200);

    }

    public void act(float dt) {

        super.act(dt);

        // turn around if near world bounds
        if (this.getX() < 100)
            angle = 0;
        else if (this.getX() > getWorldBounds().width - 150)
            angle = 180;
        else if (this.getY() < 100)
            angle = 90;
        else if (this.getY() > getWorldBounds().height - 100)
            angle = 270;

        accelerateAtAngle(angle);

        applyPhysics(dt);

        if (getSpeed() > 0)
            setRotation(getMotionAngle());

        boundToWorld();

    }
}
