package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class LevelScreen extends BaseScreen {

    private Turtle turtle;
    private boolean win;
    private boolean lose;
    private Label starfishLabel;
    private DialogBox dialogBox;

    @Override
    public void initialize() {

        // background
        BaseActor ocean = new BaseActor(0, 0, mainStage);
        ocean.loadTexture("water-border.jpg");
        ocean.setSize(1200, 900);
        BaseActor.setWorldBounds(ocean);

        // rocks & starfishes placed in fixed locations
        new Rock(300, 200, mainStage);
        new Rock(100, 300, mainStage);
        new Rock(400, 500, mainStage);
        new Rock(600, 700, mainStage);

        new Starfish(200, 300, mainStage);
        new Starfish(500, 800, mainStage);
        new Starfish(900, 600, mainStage);
        new Starfish(700, 100, mainStage);
        new Starfish(300, 400, mainStage);

        // signs
        Sign sign1 = new Sign(20, 400, mainStage);
        sign1.setText("West Starsfish Bay");
        Sign sign2 = new Sign(600, 300, mainStage);
        sign2.setText("East Starfish Bay");

        // dialogbox
        dialogBox = new DialogBox(0, 0, uiStage);
        dialogBox.setBackgroundColor(Color.TAN);
        dialogBox.setFontColor(Color.BROWN);
        dialogBox.setDialogSize(600, 100);
        dialogBox.setFontScale(0.8f);
        dialogBox.alignCenter();
        dialogBox.setVisible(false);



        // sharks placed randomly
        for (int i = 0; i < 5; i++) {
            int x = MathUtils.random(2, 10)*100;
            int y = MathUtils.random(2, 10)*100;
            Shark shark = new Shark(x, y, mainStage);
            shark.setMaxSpeed(50);
        }

        // player
        turtle = new Turtle(20, 20, mainStage);
        win = false;
        lose = false;

        // UI: starfish state and restart button
        starfishLabel = new Label("Starfish left:", BaseGame.labelStyle);
        starfishLabel.setColor(Color.CYAN);
        uiStage.addActor(starfishLabel);

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        Texture buttonText = new Texture(Gdx.files.internal("undo.png"));
        TextureRegion buttonRegion = new TextureRegion(buttonText);
        buttonStyle.up = new TextureRegionDrawable(buttonRegion);
        Button restartButton = new Button(buttonStyle);
        restartButton.setStyle(buttonStyle);
        restartButton.setColor(Color.CYAN);
        uiStage.addActor(restartButton);

        restartButton.addListener((Event e) -> {
           if (!(e instanceof InputEvent) || !((InputEvent) e).getType().equals(InputEvent.Type.touchDown))
               return false;
           StarFishGame.setActiveScreen(new LevelScreen());
           return false;
        });

        // set label and restart on top, dialogbox on bottom
        uiTable.pad(10);
        uiTable.add(starfishLabel).top();
        uiTable.add().expandX().expandY();
        uiTable.add(restartButton).top();
        uiTable.row();
        uiTable.add(dialogBox).colspan(3);

    }

    @Override
    public void update(float dt) {

        // quit game
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();

        // shark hit --> game over
        for (BaseActor sharkActor: BaseActor.getList(mainStage, Shark.class.getCanonicalName())) {
            if (turtle.overlaps(sharkActor) && !lose && !win) {
                lose = true;
                turtle.remove();
                BaseActor loseMessage = new BaseActor(0, 0, uiStage);
                loseMessage.loadTexture("game-over.png");
                loseMessage.centerAtPosition(400, 300);
                loseMessage.setOrigin(0);
                loseMessage.addAction(Actions.delay(1));
                loseMessage.addAction(Actions.after(Actions.fadeIn(1)));
            }
        }

        // rock collision
        for (BaseActor rockActor: BaseActor.getList(mainStage, Rock.class.getCanonicalName()))
            turtle.preventOverlap(rockActor);

        // sign proximity triggers dialogbox
        for (BaseActor signActor: BaseActor.getList(mainStage, Sign.class.getCanonicalName())) {

            Sign sign = (Sign) signActor;
            turtle.preventOverlap(sign);
            boolean nearby = turtle.isWithinDistance(4, sign);

            if (nearby && !sign.isViewing()) {
                dialogBox.setText(sign.getText());
                dialogBox.setVisible(true);
                sign.setViewing(true);
            }

            if (!nearby && sign.isViewing()) {
                dialogBox.setText(" ");
                dialogBox.setVisible(false);
                sign.setViewing(false);
            }
        }

        // starfish collecting and win conditions check
        for (BaseActor starfishActor: BaseActor.getList(mainStage, Starfish.class.getCanonicalName())) {

            Starfish starfish = (Starfish) starfishActor;
            if (turtle.overlaps(starfish) && !starfish.collected) {
                starfish.collected = true;
                starfish.clearActions();
                starfish.addAction(Actions.fadeOut(1));
                starfish.addAction(Actions.after(Actions.removeActor()));
                starfishActor.remove();

                Whirlpool whirlpool = new Whirlpool(0, 0, mainStage);
                whirlpool.centerAtActor(starfish);
                whirlpool.setOpacity(0.25f);
            }

            if (BaseActor.getList(mainStage, Starfish.class.getCanonicalName()).size() == 0 && !win) {
                win = true;
                BaseActor winMessage = new BaseActor(0, 0, uiStage);
                winMessage.loadTexture("you-win.png");
                winMessage.centerAtPosition(400, 300);
                winMessage.setOrigin(0);
                winMessage.addAction(Actions.delay(1));
                winMessage.addAction(Actions.after(Actions.fadeIn(1)));
            }

            // how many SF left
            starfishLabel.setText("Starfish left: " + BaseActor.count(mainStage, Starfish.class.getCanonicalName()));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
