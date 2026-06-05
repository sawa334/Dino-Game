package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.game.GameSettings;

public class DinoObject extends GameObject {
    public int livesLeft;
    public DinoObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.SHIP_BIT, world);
        body.setLinearDamping(10);
        body.setGravityScale(50);
        livesLeft = 3;
    }
    private void putInFrame() {
        if (getY() > (GameSettings.SCREEN_HEIGHT / 2f - height / 2f)) {
            setY(GameSettings.SCREEN_HEIGHT / 2 - height / 2);
        }
        if (getY() <= (height / 2f)) {
            setY(height / 2);
        }

        setX(180);
    }
    @Override
    public void draw(SpriteBatch batch) {
        putInFrame();
        super.draw(batch);
    }

    public  void  move(Vector3 vector3){
        body.applyForceToCenter(
                new Vector2(
                        (vector3.x - getX()) * GameSettings.SHIP_FORCE_RATIO,
                        (vector3.y - getY()) * GameSettings.SHIP_FORCE_RATIO
                ),
                true
        );

    }
    @Override
    public void hit(){
        livesLeft -= 1;
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }


    long lastShotTime;
    public boolean needToShoot(){

        if(TimeUtils.millis() - lastShotTime >= GameSettings.SHOOTING_COOL_DOWN){
            lastShotTime = TimeUtils.millis();
            return true;
        }
        return false;

    }

    public int getLiveLeft() {
        return livesLeft;
    }
}