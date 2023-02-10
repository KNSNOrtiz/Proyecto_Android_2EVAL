package com.ninjarun.actors;

import static com.ninjarun.Utils.USER_NINJA;
import static com.ninjarun.Utils.WORLD_HEIGHT;
import static com.ninjarun.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ninjarun.managers.AssetMan;

public class Ninja extends Actor {

    //  En lugar de usar constantes individuales, para mayor comodidad voy a usar un ENUM en el que voy a recoger todos los posibles estados
    //  del personaje principal, para poder determinar con facilidad la animación o acción a usar/ejecutar.
    enum NinjaState {
        IDLE, RUNNING
    }

    //  RECURSOS
    private AssetMan assetMan;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> currentAnimation;

    //  PROPIEDADES PRINCIPALES DEL ACTOR
    private final float NINJA_WIDTH = WORLD_WIDTH/7;
    private final float NINJA_HEIGHT = WORLD_HEIGHT/7;
    private final float NINJA_RUNNING_WIDTH = WORLD_WIDTH/5;
    private NinjaState currentState;
    private float speed = 2.5f;
    private float currentWidth;
    private World world;
    private Body body;
    private Fixture fixture;
    private Vector2 position;
    private float stateTime = 0f;  //  StateTime es el tiempo acumulado de Delta, necesario para determinar el frame de la animación que se muestra.

    public Ninja(Vector2 position, World world){
        assetMan = new AssetMan();
        this.position = position;
        this.world = world;
        currentState = NinjaState.IDLE;
        idleAnimation = assetMan.getNinjaIdle();
        runAnimation = assetMan.getNinjaRun();
        addBody();
        addFixture();
        loadState();
    }

    private void addBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
    }

    private void addFixture(){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(NINJA_WIDTH/2, NINJA_HEIGHT/2);
        fixture = body.createFixture(shape, 8);
        fixture.setUserData(USER_NINJA);
        shape.dispose();
    }

    private void loadState() {
       switch (currentState){
           case IDLE:
               currentAnimation = idleAnimation;
               startIdle();
               break;
           case RUNNING:
               currentAnimation = runAnimation;

               startRunning();
               break;
       }
    }
    private void startRunning(){
        currentWidth = NINJA_RUNNING_WIDTH;
        body.setLinearVelocity(speed,0f);
    }

    private void startIdle(){
        currentWidth = NINJA_WIDTH;
        body.setLinearVelocity(0f,0f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(body.getPosition().x - NINJA_WIDTH/2, body.getPosition().y - NINJA_HEIGHT/2);
        batch.draw(currentAnimation.getKeyFrame(stateTime, true), getX(), getY(), currentWidth, NINJA_HEIGHT);
        stateTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.justTouched()){
            if (currentState == NinjaState.IDLE)
                currentState = NinjaState.RUNNING;
            else
                currentState = NinjaState.IDLE;
            loadState();
        }

    }
}
