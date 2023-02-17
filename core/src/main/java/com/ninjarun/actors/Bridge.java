package com.ninjarun.actors;

import static com.ninjarun.Utils.USER_BRIDGE;
import static com.ninjarun.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ninjarun.MainGame;
import com.ninjarun.managers.AssetMan;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

public class Bridge extends Actor {

    AssetMan assetMan;

    private final float WIDTH = 0.05f;
    private final float MAX_HEIGHT = WORLD_WIDTH -0.25f;
    private float currentHeight;
    private TextureRegion sprite;
    InputAdapter bridgeInput;

    private World world;
    private Body body;
    private Fixture fixture;
    private Vector2 position;
    private Ninja ninja;
    private boolean isBuilding;
    private boolean isBuilt;

    private Music buildSound;   //  Se carga como música para poder gestionar su estado (si está sonando, se para; si no, reproducirlo...)


    public Bridge(World world, Vector2 position, Ninja ninja) {
        this.assetMan = new AssetMan();
        this.sprite = assetMan.getBridge();
        this.world = world;
        this.position = position;
        this.ninja = ninja;
        this.buildSound = this.assetMan.getBuildSound();
        this.isBuilding = false;
        this.isBuilt = false;

    }
    private void addBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        this.body = this.world.createBody(bodyDef);

    }

    private void addFixture(){
        EdgeShape edge = new EdgeShape();
        edge.set(getOriginX(), getOriginY()-0.005f, currentHeight, 0f);
        fixture = body.createFixture(edge, 0);
        fixture.setUserData(USER_BRIDGE);
        edge.dispose();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(position.x, position.y);
        batch.draw(sprite,getX(), getY(), getOriginX(), getOriginY(), WIDTH, currentHeight,getScaleX(),getScaleY(),getRotation());
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isTouched()){
            isBuilding = true;
        }
        else{
            if (isBuilding){
                isBuilt = true;
                isBuilding = false;
            }
        }
        if (currentHeight < MAX_HEIGHT && isBuilding && !isBuilt){
            this.currentHeight += 0.03f;
            if (!buildSound.isPlaying()){
                this.buildSound.play();
            }
        }
        else if(isBuilt){
            if (this.body == null && this.fixture == null){
                if (this.getRotation() > -90f){
                    this.setRotation(getRotation()-5f);
                }
                else{
                    if (buildSound.isPlaying())
                        buildSound.stop();
                    addBody();
                    addFixture();
                    ninja.startRunning();
                    System.out.println("CONSTRUIDO");
                    buildSound.play();
                }
            }

        }
    }

    public void dispose(){
        if (!world.isLocked()){
            System.out.println("ELIMINADO INPUT");
            Gdx.input.setInputProcessor(null);
            this.body.destroyFixture(fixture);
            this.world.destroyBody(body);
        } else{
            System.out.println("El mundo está locked.");
        }
    }

}
