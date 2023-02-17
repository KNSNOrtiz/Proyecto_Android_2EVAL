package com.ninjarun.actors;

import static com.ninjarun.MainGame.assetMan;
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

//  CLASE QUE REPRESENTA CADA PLATAFORMA DEL JUEGO Y SU RESPECTIVO SENSOR.

public class Platform extends Actor {

    //  PROPIEDADES VISUALES
    private final float MIN_WIDTH = WORLD_WIDTH/18f;
    private final float MAX_WIDTH = WORLD_WIDTH/6f;
    private float platformWidth;
    private final float PLATFORM_HEIGHT = WORLD_HEIGHT / 3f;
    private TextureRegion sprite;


    //  FÍSICAS
    private World world;
    public Body body;
    public Body counterBody;
    private Vector2 position;
    private Fixture counterFixture;
    private Fixture fixture;

    public Platform(Vector2 position, World world, boolean isFirst){

        this.sprite = assetMan.getPlatform();
        this.world = world;
        this.position = position;
        this.platformWidth = MathUtils.random(MIN_WIDTH, MAX_WIDTH);    //  El ancho se calcula de forma aleatoria entre su máximo y mínimo.
        addBody();
        addFixture();
        //  Este parámetro indica si va a tener un sensor para la puntuación o no; solo la primera no lo tendrá.
        if (!isFirst){
            addCounter();   //  Añade el body/fixture que representa al sensor.
        }
    }

    private void addBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.KinematicBody; //  El tipo es kinemático, ya que no le afectará la gravedad pero sí las fuerzas para que se pueda mover.
        body = world.createBody(bodyDef);
    }


    private void addFixture(){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(platformWidth/3, PLATFORM_HEIGHT /2);
        fixture = body.createFixture(shape, 8);
        fixture.setUserData(USER_PLATFORM);
        shape.dispose();
    }

    private void addCounter(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(body.getPosition().x, position.y+1.24f);   //  El sensor se posiciona ligeramente por encima de la plataforma para que sobresalga.
        bodyDef.type = BodyDef.BodyType.KinematicBody;  //  El tipo es kinemático, como las plataformas.
        counterBody = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(platformWidth/2.8f, 0.1f);   //  Se establece un tamaño que cubre el ancho de la plataforma, pero se hace el sensor delgado para impedir que el personaje lo toque si está el puente por encima.
        counterFixture = counterBody.createFixture(shape, 8);
        counterFixture.setUserData(USER_COUNTER);
        shape.dispose();
    }

    public Body getBody() {
        return body;
    }

    public float getPlatformWidth() {
        return platformWidth;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(body.getPosition().x - platformWidth/2, body.getPosition().y - PLATFORM_HEIGHT /2);
        batch.draw(sprite,getX(), getY(), platformWidth, PLATFORM_HEIGHT);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }


    //  LIBERACIÓN DE RECURSOS.

    public void dispose(){
        if (!world.isLocked()){
            this.body.destroyFixture(fixture);
            this.world.destroyBody(body);
        }
    }
    public void disposeCounter(){
        if (!world.isLocked()){
            if (counterBody != null){
                this.counterBody.destroyFixture(counterFixture);
                this.world.destroyBody(counterBody);
            }

        }
    }
}
