package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.components.ButtonView;
import com.mygdx.game.components.RecordsListView;
import com.mygdx.game.managers.ContactManager;
import com.mygdx.game.game.GameSession;
import com.mygdx.game.game.GameSettings;
import com.mygdx.game.game.GameState;
import com.mygdx.game.components.ImageView;
import com.mygdx.game.components.LiveView;
import com.mygdx.game.components.MovingBackgroundView;
import com.mygdx.game.game.MyGdxGame;
import com.mygdx.game.components.TextView;
import com.mygdx.game.managers.MemoryManager;
import com.mygdx.game.objects.DinoObject;
import com.mygdx.game.game.GameResources;
import com.mygdx.game.objects.GroundObject;
import com.mygdx.game.objects.StoneObject;


import java.util.ArrayList;

public class GameScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    DinoObject dinoObject;

    GameSession gameSession;


    ArrayList<StoneObject> stonesArray;

    MovingBackgroundView backgroundView;
    LiveView liveView;
    TextView scoreTextView;
    ButtonView pauseButton;
    ButtonView homeButton;
    ButtonView continueButton;

    TextView pauseTextView;

    ImageView fullBlackoutView;

    TextView recordsTextView;
    RecordsListView recordsListView;
    ButtonView homeButton2;
    GroundObject ground;





    private void updateStones() {
        for (int i = 0; i < stonesArray.size(); i++) {
            stonesArray.get(i).putInFrame();
            boolean hasToBeDestroyed = !stonesArray.get(i).isAlive() || !stonesArray.get(i).isInFrame();

            if (!stonesArray.get(i).isAlive()) {
                gameSession.destructionRegistration();

            }

            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(stonesArray.get(i).body);
                stonesArray.remove(i--);
            }
        }
    }


    ContactManager contactManager;




    public GameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();
        pauseButton = new ButtonView(1159, 620, 46, 54, GameResources.PAUSE_IMG_PATH);
        liveView = new LiveView(1000, 630);
        fullBlackoutView = new ImageView(0,0, GameResources.BLACKOUT_IMG_PATH);
        homeButton = new ButtonView(500, 350, 150, 50, myGdxGame.commonWhiteFont, GameResources.BUTTON_BACKGROUND_SHORT_IMG_PATH, "Home");
        continueButton = new ButtonView(700, 350, 150, 50, myGdxGame.commonWhiteFont, GameResources.BUTTON_BACKGROUND_SHORT_IMG_PATH, "Continue");
        pauseTextView = new TextView(myGdxGame.commonWhiteFont, 640, 500, "Pause");
        ground = new GroundObject(GameSettings.SCREEN_WIDTH, 0, myGdxGame.world);

        contactManager = new ContactManager(myGdxGame.world);
        scoreTextView = new TextView(myGdxGame.commonWhiteFont, 1000, 600);

        stonesArray = new ArrayList<>();


        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);



        dinoObject = new DinoObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );


        recordsListView = new RecordsListView(myGdxGame.commonWhiteFont, 350);
        recordsTextView = new TextView(myGdxGame.largeWhiteFont, 206, 842, "Last records");
        homeButton2 = new ButtonView(
                640, 400,
                160, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_BACKGROUND_SHORT_IMG_PATH,
                "Home"
        );
    }

    @Override
    public void show() {
        restartGame();
    }

    @Override
    public void render(float delta) {

        handleInput();

        if (gameSession.state == GameState.PLAYING) {
            if (gameSession.shouldSpawnTrash()) {
                StoneObject trashObject =
                        new StoneObject(GameSettings.TRASH_WIDTH, GameSettings.TRASH_HEIGHT, GameResources.TRASH_IMG_PATH, myGdxGame.world, 1);



                stonesArray.add(trashObject);

                if (!dinoObject.isAlive()) {
                    System.out.println("Game over!");
                }
            }


            if (!dinoObject.isAlive()) {
                gameSession.endGame();
                recordsListView.setRecords(MemoryManager.loadRecordsTable());
            }

            updateStones();
            backgroundView.move();
            gameSession.updateScore();
            scoreTextView.setText("Score: " + gameSession.getScore());
            liveView.setLeftLives(dinoObject.getLiveLeft());

            myGdxGame.stepWorld();
        }

        draw();
    }


    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            switch (gameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.pauseGame();
                    }
                    dinoObject.jump();
                    break;

                case PAUSED:
                    if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.resumeGame();
                    }
                    if (homeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;
                case ENDED:
                    if (homeButton2.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;
            }
        }
    }
    private void restartGame() {

        for (int i = 0; i < stonesArray.size(); i++) {
            myGdxGame.world.destroyBody(stonesArray.get(i).body);
            stonesArray.remove(i--);
        }

        if (dinoObject != null) {
            myGdxGame.world.destroyBody(dinoObject.body);
        }
        dinoObject = new DinoObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );

        gameSession.startGame();
    }

    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        backgroundView.draw(myGdxGame.batch);
        dinoObject.draw(myGdxGame.batch);

        for (StoneObject trash : stonesArray) trash.draw(myGdxGame.batch);

        scoreTextView.draw(myGdxGame.batch);
        liveView.draw(myGdxGame.batch);
        pauseButton.draw(myGdxGame.batch);
        if (gameSession.state == GameState.PAUSED){
            fullBlackoutView.draw(myGdxGame.batch);
            pauseTextView.draw(myGdxGame.batch);
            homeButton.draw(myGdxGame.batch);
            continueButton.draw(myGdxGame.batch);

        } else if (gameSession.state == GameState.ENDED) {
            fullBlackoutView.draw(myGdxGame.batch);
            recordsTextView.draw(myGdxGame.batch);
            recordsListView.draw(myGdxGame.batch);
            homeButton2.draw(myGdxGame.batch);
        }


        myGdxGame.batch.end();
    }


}