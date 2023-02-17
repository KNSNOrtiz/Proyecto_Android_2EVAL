package com.ninjarun.actors;

import static com.ninjarun.Utils.USER_FLOOR;
import static com.ninjarun.Utils.WORLD_WIDTH;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.ninjarun.MainGame;

//  CLASE QUE SOLAMENTE TIENE FORMA FÍSICA PARA REPRESENTAR EL SUELO ABAJO DE LA PANTALLA.
public class Floor {
    World world;
    Body body;
    Fixture fixture;

    public Floor(World world){
        this.world = world;
        addBody();
        addFixture();
    }

    //  MÉTODOS PARA LA FÍSICA
    private void addBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(WORLD_WIDTH/2f, 0f);   //  SE POSICIONA EN MITAD DEL ESCENARIO.
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body= world.createBody(bodyDef);
    }

    private void addFixture(){
        EdgeShape edge = new EdgeShape();
        edge.set(-3,-0.8f,WORLD_WIDTH,-0.8f);   //  SE ASIGNAN LOS VÉRTICES PARA QUE OCUPE TODO EL TERRENO, PERO POR DEBAJO DE LA PANTALLA.
        fixture = body.createFixture(edge, 3);
        fixture.setUserData(USER_FLOOR);
        edge.dispose();
    }
}
