package com.ninjarun.actors;

import static com.ninjarun.MainGame.assetMan;
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
import com.ninjarun.MainGame;
import com.ninjarun.managers.AssetMan;

//  PERSONAJE QUE SE MOVERÁ ENTRE CADA PLATAFORMA (PLATFORM) A TRAVÉS DEL PUENTE (BRIDGE).

public class Ninja extends Actor {

    /*
    Los estados del personaje se determinan mediante métodos en lugar de constantes, porque usamos variables auxiliares booleanas más adelante
    para determinar qué evento se lanza.
     */

    //  ANIMACIONES
    private Animation<TextureRegion> idleAnimation; //  Animación para el estado parado.
    private Animation<TextureRegion> runAnimation;  //  Animación para el estado en movimiento.
    private Animation<TextureRegion> currentAnimation;  //  Animación que se dibujará en Draw().

    //  PROPIEDADES PRINCIPALES DEL ACTOR
    private final float NINJA_WIDTH = WORLD_WIDTH/20;   //  ANCHO DEL NINJA
    private final float NINJA_HEIGHT = WORLD_HEIGHT/20; //  ALTO DEL NINJA
    private final float NINJA_RUNNING_WIDTH = WORLD_WIDTH/17;   //  Dispongo de una proporción distinta para esta animación para evitar deformaciones.
    private float speed = 2f;   //  Velocidad que se aplicará a su body.
    private float currentWidth; //  Ancho actual con el que se dibujará en Draw().
    private World world;        //  Instancia del mundo, necesaria para crear sus físicas.
    private Body body;           //  Cuerpo del Ninja, representando el lugar que ocupa en el mundo. Define cómo se comporta con las fuerzas físicas.
    private Fixture fixture;    //  Forma que el Ninja tiene en el mundo. Define sus proporciones y otras propiedades físicas.
    private Vector2 position;   //  Posición que ocupará el personaje. Se aplica a su body.
    private float stateTime = 0f;  //  StateTime es el tiempo acumulado de Delta, necesario para determinar el frame de la animación que se muestra.
                                    //  Delta a su vez es el tiempo transcurrido entre cada renderización/frame.

    public Ninja(Vector2 position, World world){
        assetMan = new AssetMan();
        this.position = position;
        this.world = world;
        //  Carga de las animaciones a través del Asset Manager.
        this.idleAnimation = assetMan.getNinjaIdle();
        this.runAnimation = assetMan.getNinjaRun();
        addBody();
        addFixture();
        startIdle();    //  Estado inicial parado.
    }

    private void addBody(){
        BodyDef bodyDef = new BodyDef();    //  BodyDef permite definir las propiedades principales del Body: su posición y tipo.
        bodyDef.position.set(position);
        bodyDef.type = BodyDef.BodyType.DynamicBody;    //  El Ninja será dinámico, para que sea afectado por la gravedad y otras fuerzas, y detecte colisiones entre sí mismo y los otros cuerpos.
        body = world.createBody(bodyDef);   //  World crea el body con las propiedades dadas.
        body.setFixedRotation(true);        //  Para evitar que el cuerpo se gire, fijo la rotación
    }

    private void addFixture(){
        PolygonShape shape = new PolygonShape();    // La forma física del personaje será una caja, por tanto, deberá hacerse mediante PolygonShape.
        shape.setAsBox(NINJA_WIDTH/100, NINJA_HEIGHT/2);    //  El ancho se establece así de delgado para aumentar la dificultad del juego y tener menos margen de error. La altura es la del Sprite.
        fixture = body.createFixture(shape, 8); //  Se crea la forma mediante el Body. La densidad sirve para calcular la masa del cuerpo, pero no es relevante.
        fixture.setFriction(0); //  Para que no pierda velocidad, elimino la fricción de la forma.
        fixture.setUserData(USER_NINJA);    //  Asignación de ID.
        shape.dispose();    //  Se libera de la memoria el generador de la forma.
    }

    //  ESTADO CORRIENDO
    public void startRunning(){
        currentWidth = NINJA_RUNNING_WIDTH;
        currentAnimation = runAnimation;
        body.setLinearVelocity(speed,0f);   //  Aumento de la velocidad para que empiece a correr.
    }

    //  ESTADO PARADO
    public void startIdle(){
        currentWidth = NINJA_WIDTH;
        currentAnimation = idleAnimation;
        body.setLinearVelocity(0f,0f);
    }

    //  Getter de la posición del personaje.
    public Vector2 getBodyPosition(){
        return body.getPosition();
    }
    //  Getter del body del personaje. Necesario para moverlo en MainScreen mientras está parado.
    public Body getBody(){
        return body;
    }

    //  Método encargado de dibujar los sprites, llamado por Stage en cada render.
    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(body.getPosition().x - NINJA_WIDTH/2, body.getPosition().y - NINJA_HEIGHT/2);
        batch.draw(currentAnimation.getKeyFrame(stateTime, true), getX(), getY(), currentWidth, NINJA_HEIGHT);
        stateTime += Gdx.graphics.getDeltaTime();
    }
    //  Método encargado de realizar acciones/comprobar eventos en cada render. Es la parte funcional del actor, llamada también por Stage.
    @Override
    public void act(float delta) {

    }
    //  Método encargado de quitar de la memoria el body y fixture del actor.
    public void dispose(){
        if (!world.isLocked()){     //  El mundo se bloquea cuando está renderizando, así que en mitad de un render no se pueden eliminar los elementos; de ahí la comprobación.
            this.body.destroyFixture(fixture);
            this.world.destroyBody(body);
        }
    }
}
