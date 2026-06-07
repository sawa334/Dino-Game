package com.mygdx.game.objects;

import static com.mygdx.game.game.GameSettings.JUMP_COOL_DOWN;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.game.GameSettings;

public class DinoObject extends GameObject {
    public int livesLeft;
    public DinoObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.SHIP_BIT, world);
        body.setLinearDamping(0);
        body.setGravityScale(20);
        MassData  m = new MassData();
        m.mass = 1;
        body.setMassData(m);
        livesLeft = 3;
    }
    private void putInFrame() {
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
    private long lastJump = 0;

    public  void  jump(){
        if(lastJump + JUMP_COOL_DOWN > System.currentTimeMillis()){
            return;
        }
        lastJump = System.currentTimeMillis();
        body.applyForceToCenter(
                 new Vector2(
                        0,
                         GameSettings.SHIP_FORCE_RATIO
                ),
                true
        );
      //  body.setLinearVelocity(0, GameSettings.SHIP_FORCE_RATIO);

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