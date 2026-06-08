package com.mygdx.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.game.GameSettings;

public class MovingBackgroundView extends View {

    Texture texture;

    int texture1X;  // изменено с Y на X
    int texture2X;  // изменено с Y на X
    int speed = 2;

    public MovingBackgroundView(String pathToTexture,  int speed) {
        super(0, 0);
        this.speed = speed;
        texture1X = 0;
        texture2X = GameSettings.SCREEN_WIDTH;  // изменено с HEIGHT на WIDTH
        texture = new Texture(pathToTexture);
    }

    public void move() {
        texture1X -= speed;
        texture2X -= speed;

        if(texture1X <= -GameSettings.SCREEN_WIDTH){
            texture1X = GameSettings.SCREEN_WIDTH;
        }
        if(texture2X <= -GameSettings.SCREEN_WIDTH){
            texture2X = GameSettings.SCREEN_WIDTH;
        }
    }

    public void draw(SpriteBatch batch){
        batch.draw(texture, texture1X, 0, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        batch.draw(texture, texture2X, 0, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
    }

    @Override
    public void dispose(){
        texture.dispose();
    }

}