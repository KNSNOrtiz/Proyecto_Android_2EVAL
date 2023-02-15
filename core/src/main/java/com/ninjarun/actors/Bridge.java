package com.ninjarun.actors;

import static com.ninjarun.Utils.USER_BRIDGE;
import static com.ninjarun.Utils.USER_FLOOR;
import static com.ninjarun.Utils.USER_NINJA;
import static com.ninjarun.Utils.WORLD_HEIGHT;
import static com.ninjarun.Utils.WORLD_WIDTH;
import static com.ninjarun.Utils.fixtureList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.ninjarun.managers.AssetMan;

public class Bridge extends Actor {

    AssetMan assetMan;

    private final float WIDTH = 0.05f;
    private final float MAX_HEIGHT = WORLD_HEIGHT;
    private float currentHeight;
    private TextureRegion sprite;

    private World world;
    private Body body;
    private Fixture fixture;
    private Vector2 position;
    private Ninja ninja;
    private boolean isBuilding;
    private boolean isBuilt;


    public Bridge(World world, Vector2 position, Ninja ninja) {
        this.assetMan = new AssetMan();
        this.sprite = assetMan.getBridge();
        this.world = world;
        this.position = position;
        this.ninja = ninja;
        this.isBuilding = false;
        this.isBuilt = false;
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                isBuilding = true;
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                isBuilding = false;
                isBuilt = true;
                return true;
            }
        });
    }
    private void addBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);
    }

    private void addFixture(){
        EdgeShape edge = new EdgeShape();
        edge.set(getOriginX(), getOriginY()-0.01f, currentHeight, 0f);
        fixture = body.createFixture(edge, 3);
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
        if (currentHeight < MAX_HEIGHT && isBuilding && !isBuilt){
            currentHeight += 0.05f;
        }
        else if(isBuilt){
            if (this.body == null && this.fixture == null){
                if (this.getRotation() > -90f){
                    System.out.println(this.getRotation());
                    this.setRotation(getRotation()-5f);
                }
                else{
                    addBody();
                    addFixture();
                    ninja.startRunning();
                }
            }

        }
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
