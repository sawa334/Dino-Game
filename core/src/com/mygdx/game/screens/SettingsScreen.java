package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.game.GameResources;
import com.mygdx.game.game.MyGdxGame;
import com.mygdx.game.components.ButtonView;
import com.mygdx.game.components.ImageView;
import com.mygdx.game.components.MovingBackgroundView;
import com.mygdx.game.components.TextView;
import com.mygdx.game.managers.MemoryManager;

import java.util.ArrayList;

public class SettingsScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    MovingBackgroundView backgroundView;
    TextView titleTextView;
    ImageView blackoutImageView;
    ButtonView returnButton;
    TextView musicSettingView;
    TextView soundSettingView;
    TextView clearSettingView;

    private String translateStateToText(boolean state) {
        return state ? "ON" : "OFF";
    }
    public SettingsScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);
        titleTextView = new TextView(myGdxGame.largeWhiteFont,540, 600, "Settings");
        blackoutImageView = new ImageView(345, 100, GameResources.BLACKOUT_MIDDLE_IMG_PATH);
        musicSettingView = new TextView(myGdxGame.commonWhiteFont, 540, 500, "music: " + "ON");
        soundSettingView = new TextView(myGdxGame.commonWhiteFont, 540, 400, "sound: " + "ON");
        clearSettingView = new TextView(myGdxGame.commonWhiteFont, 540, 300, "clear records");
        returnButton = new ButtonView(
                540, 170,
                160, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_BACKGROUND_SHORT_IMG_PATH,
                "return"
        );
    }
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (returnButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                myGdxGame.setScreen(myGdxGame.menuScreen);
            }
            if (clearSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                clearSettingView.setText("clear records (cleared)");
                MemoryManager.saveTableOfRecords(new ArrayList<Integer>());
            }
            if (musicSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                MemoryManager.saveMusicSettings(!MemoryManager.loadIsMusicOn());
                musicSettingView.setText("music: " + translateStateToText(MemoryManager.loadIsMusicOn()));
                myGdxGame.audioManager.updateMusicFlag();
            }
            if (soundSettingView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                MemoryManager.saveSoundSettings(!MemoryManager.loadIsSoundOn());
                soundSettingView.setText("sound: " + translateStateToText(MemoryManager.loadIsSoundOn()));
                myGdxGame.audioManager.updateSoundFlag();

            }

        }
    }
    @Override
    public void render(float delta) {
        handleInput();
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        backgroundView.draw(myGdxGame.batch);
        titleTextView.draw(myGdxGame.batch);
        blackoutImageView.draw(myGdxGame.batch);
        returnButton.draw(myGdxGame.batch);
        musicSettingView.draw(myGdxGame.batch);
        soundSettingView.draw(myGdxGame.batch);
        clearSettingView.draw(myGdxGame.batch);


        myGdxGame.batch.end();
    }
    public void dispose(){
        backgroundView.dispose();
        titleTextView.dispose();
        blackoutImageView.dispose();
        returnButton.dispose();
        musicSettingView.dispose();
        soundSettingView.dispose();
        clearSettingView.dispose();
    }
}