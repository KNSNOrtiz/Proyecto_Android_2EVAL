package com.ninjarun.actors;

import static com.ninjarun.Utils.USER_COUNTER;
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

public class Platform extends Actor {

    private final float MIN_WIDTH = WORLD_WIDTH/8f;
    private final float MAX_WIDTH = WORLD_WIDTH/3f;
    private float platformWidth = MAX_WIDTH;
    private float platformHeight = WORLD_HEIGHT / 3f;

    private AssetMan assetMan;
    private Vector2 position;
    private TextureRegion sprite;
    private World world;

    public Body body;
    public Body counterBody;

    private Fixture containerFixture;
    private Fixture fixture;

    public Platform(Vector2 position, World world, boolean isFirst){
        assetMan = new AssetMan();
        sprite = assetMan.getPlatform();
        this.world = world;
        this.position = position;
        platformWidth = MathUtils.random(MIN_WIDTH, MAX_WIDTH);
        addBody();
        addFixture();
        if (!isFirst){
            addCounter();
        }
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

    private void addCounter(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position.x + 0.35f, position.y+1.5f);
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        counterBody = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.1f, 0.1f);
        containerFixture = counterBody.createFixture(shape, 8);
        this.containerFixture.setSensor(true);
        containerFixture.setUserData(USER_COUNTER);
        shape.dispose();
    }

    public Body getBody() {
        return body;
    }

    public Body getCounterBody() {
        return counterBody;
    }

    public boolean isOutOfScreen(){
        if (body.getPosition().x <= -(platformWidth))
            return true;
        else{
            return false;
        }
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

    public void detach(){
        if (!world.isLocked()){
            this.body.destroyFixture(fixture);
            this.world.destroyBody(body);
        } else{
            System.out.println("El mundo está locked.");
        }

    }
}
