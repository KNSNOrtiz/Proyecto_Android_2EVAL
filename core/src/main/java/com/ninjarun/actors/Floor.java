package com.ninjarun.actors;

import static com.ninjarun.Utils.USER_FLOOR;
import static com.ninjarun.Utils.WORLD_WIDTH;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;


public class Floor {
    World world;
    Body body;
    Fixture fixture;

    public Floor(World world){
        this.world = world;
        addBody();
        addFixture();
    }

    private void addBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(WORLD_WIDTH /2f, 0f);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body= world.createBody(bodyDef);
    }

    private void addFixture(){
        EdgeShape edge = new EdgeShape();
        edge.set(-3,0,WORLD_WIDTH,0);
        fixture = body.createFixture(edge, 3);
        fixture.setUserData(USER_FLOOR);
        edge.dispose();
    }

}