package com.ninjarun.actors;

import static com.ninjarun.MainGame.assetMan;
import static com.ninjarun.MainGame.soundMan;
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

//  CLASE QUE REPRESENTA EL PUENTE USADO PARA ATRAVESAR CADA FOSO.
public class Bridge extends Actor {

    //  PROPIEDADES VISUALES
    private final float WIDTH = 0.05f;
    private final float MAX_HEIGHT = WORLD_WIDTH -0.25f;
    private float currentHeight;
    private TextureRegion sprite;

    //  FÍSICAS
    private World world;
    private Body body;
    private Fixture fixture;
    private Vector2 position;

    //  INSTANCIA DE NINJA. NECESARIA PARA CAMBIAR EL ESTADO DE ESTE.
    private Ninja ninja;

    //  ESTADOS DEL PUENTE.
    private boolean isBuilding;
    private boolean isBuilt;



    public Bridge(World world, Vector2 position, Ninja ninja) {
        this.sprite = assetMan.getBridge();
        this.world = world;
        this.position = position;
        this.ninja = ninja;
        this.isBuilding = false;
        this.isBuilt = false;

    }

    //  MÉTODOS PARA LA FÍSICA
    private void addBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        this.body = this.world.createBody(bodyDef);

    }

    private void addFixture(){
        EdgeShape edge = new EdgeShape();
        edge.set(getOriginX(), getOriginY()-0.005f, currentHeight, 0f);     //  Se crea una forma de línea para el puente. Se coloca muy ligeramente por debajo de su punto de inicio para que Ninja pueda subirse sin pararse.
        fixture = body.createFixture(edge, 0);
        fixture.setUserData(USER_BRIDGE);
        edge.dispose();
    }

    //  MÉTODOS DEL ACTOR
    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(position.x, position.y);
        //  Esta sobrecarga de Draw() permite rotar el sprite y cambiar su centro.
        batch.draw(sprite,getX(), getY(), getOriginX(), getOriginY(), WIDTH, currentHeight,getScaleX(),getScaleY(),getRotation());
    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isTouched()){ //  DEFINICIÓN DE LA ENTRADA/CONTROLES. Mientras se toque la pantalla pasará al estado CONSTRUYENDO PUENTE.
            isBuilding = true;
        }
        else{
            if (isBuilding){    // Cuando no se toque, si previamente se estaba construyendo pasará al estado CONSTRUIDO.
                isBuilt = true;
                isBuilding = false;
            }
        }
        //  Esta condición hace que mientras el puente se esté construyendo y aún no haya alcanzado su tamaño máximo, siga aumentando su tamaño constantemente.
        if (currentHeight < MAX_HEIGHT && isBuilding && !isBuilt){
            this.currentHeight += 0.04f;
            soundMan.playBuildSound();
        }
        //  Si pasa al estado construido, se rota 90º para que quede recto y el personaje pueda cruzar.
        else if(isBuilt){
            //  Si no tienen un body y un fixture, quiere decir que es la primera vez que se entra a esta sección de código.
            if (this.body == null && this.fixture == null){
                if (this.getRotation() > -90f){
                    this.setRotation(getRotation()-5f); //  Se rota progresivamente de 5 en 5 grados para que tenga una pequeña animación en el Draw().
                }
                //  Cuando ya esté rotado, se añaden las físicas para que se pueda cruzar por él.
                else{
                    soundMan.stopBuildSound();
                    addBody();
                    addFixture();
                    ninja.startRunning();   //  Se manda al Ninja a correr por el puente una vez establecidas las físicas.
                   soundMan.playBuildSound();
                }
            }

        }
    }

    //  LIBERACIÓN DE MEMORIA.
    public void dispose(){
        if (!world.isLocked()){
            this.body.destroyFixture(fixture);
            this.world.destroyBody(body);
        }
    }

}
