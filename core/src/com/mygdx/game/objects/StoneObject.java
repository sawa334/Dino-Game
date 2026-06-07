package com.mygdx.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.game.GameSettings;

import java.util.Random;

public class StoneObject extends GameObject {


    private int livesLeft;
    public StoneObject( int width, int height,String texturePath, World world, int lives) {
        super(texturePath,
                GameSettings.SCREEN_WIDTH + width / 2,
                height / 2,
                width, height,
                GameSettings.TRASH_BIT,
                world
        );
        body.setLinearVelocity(new Vector2(-GameSettings.TRASH_VELOCITY, 0 ));
        livesLeft = lives;

    }

    public boolean isInFrame() {
        return getX() + width / 2 > 0;
    }

    public void putInFrame(){
        if(getY() <= height / 2 ){
            setY(height / 2);
        }
    }

    @Override
    public void hit() {
        livesLeft -= 1;
    }
    public boolean isAlive() {
        return livesLeft > 0;
    }





}