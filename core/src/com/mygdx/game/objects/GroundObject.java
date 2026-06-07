package com.mygdx.game.objects;

import static com.mygdx.game.game.GameSettings.GROUND_BIT;
import static com.mygdx.game.game.GameSettings.SCALE;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GroundObject {
    Body body;

    private Body createBody(float width, float y, World world){

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(width /2, y);


        Body groundBody = world.createBody(groundBodyDef);


        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(width, 1.0f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = GROUND_BIT;
        fixtureDef.shape = groundBox;
        fixtureDef.density = 0.0f;


        groundBody.createFixture(fixtureDef);


        groundBox.dispose();

        groundBody.setTransform(width / 2 * SCALE,y * SCALE, 0);

        return groundBody;
    }
    public GroundObject(float width,float y, World world){
        body = createBody(width, y, world);


    }
}
