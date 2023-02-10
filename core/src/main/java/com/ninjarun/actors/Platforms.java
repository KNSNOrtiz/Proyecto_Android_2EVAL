package com.ninjarun.actors;

import static com.ninjarun.Utils.USER_PLATFORM;
import static com.ninjarun.Utils.WORLD_HEIGHT;
import static com.ninjarun.Utils.WORLD_WIDTH;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ninjarun.managers.AssetMan;

public class Platforms extends Actor {

    private final float MIN_WIDTH = WORLD_WIDTH/8;
    private final float MAX_WIDTH = WORLD_WIDTH/3;
    private float platformWidth = MAX_WIDTH;
    private float platformHeight = WORLD_HEIGHT / 3f;

    AssetMan assetMan;
    Vector2 position;
    TextureRegion sprite;
    World world;
    Body body;
    Fixture fixture;

    public Platforms(Vector2 position, World world){
        assetMan = new AssetMan();
        sprite = assetMan.getPlatform();
        this.world = world;
        this.position = position;
        platformWidth = MathUtils.random(MIN_WIDTH, MAX_WIDTH);
        addBody();
        addFixture();
    }

    private void addBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bodyDef);
    }

    private void addFixture(){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(platformWidth/3, platformHeight/2);
        fixture = body.createFixture(shape, 8);
        fixture.setUserData(USER_PLATFORM);
        shape.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(body.getPosition().x - platformWidth/2, body.getPosition().y - platformHeight/2);
        batch.draw(sprite,getX(), getY(), platformWidth, platformHeight);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
