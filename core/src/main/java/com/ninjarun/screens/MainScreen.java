package com.ninjarun.screens;

import static com.ninjarun.MainGame.assetMan;
import static com.ninjarun.MainGame.soundMan;
import static com.ninjarun.Utils.SCREEN_HEIGHT;
import static com.ninjarun.Utils.SCREEN_WIDTH;
import static com.ninjarun.Utils.USER_BRIDGE;
import static com.ninjarun.Utils.USER_COUNTER;
import static com.ninjarun.Utils.USER_FLOOR;
import static com.ninjarun.Utils.USER_NINJA;
import static com.ninjarun.Utils.WORLD_HEIGHT;
import static com.ninjarun.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.ninjarun.MainGame;
import com.ninjarun.actors.Bridge;
import com.ninjarun.actors.Floor;
import com.ninjarun.actors.Ninja;
import com.ninjarun.actors.Platform;

//  PANTALLA PRINCIPAL DEL JUEGO.

public class MainScreen extends AbstractScreen implements ContactListener {

    //  Depurador de físicas
    private Box2DDebugRenderer debugRenderer;
    //  Cámara del mundo
    private OrthographicCamera worldCamera;
    //  Cámara de la fuente
    private OrthographicCamera counterCamera;
    //  Fuente
    private BitmapFont counterFont;

    //  Mundo y Stage
    private Stage stage;    //  Se puede considerar como el director del juego. A él pertenecen todos los actores que incorporamos y los gestiona al mismo tiempo, al igual que contiene la vista del juego.
    private World world;    //  El mundo de juego, propio de Box2D. Contiene los cuerpos físicos que añadamos.

    //  Actores
    private Ninja ninja;
    private Bridge bridge;
    private Array<Platform> platforms;

    //  Variables auxiliares para determinar los estados del juego y de las plataformas.
    private boolean hasCrossed;
    private boolean areMoving;
    private boolean gameOver;

    //  Contador de puntuación
    private int counter = 0;

    public MainScreen(MainGame mainGame) {
        super(mainGame);
        this.stage = new Stage(fitViewport);
        //this.debugRenderer = new Box2DDebugRenderer();
        this.worldCamera = (OrthographicCamera)stage.getCamera();   //  Se obtiene la cámara del mundo para poder depurar el juego.
        this.world = new World(new Vector2(0, -9.80665f), true);    //  Se crea una nueva instancia del mundo, donde estarán todos los cuerpos físicos del juego. Se establece una gravedad negativa (la terrestre) para que caigan.
        this.world.setContactListener(this); //  Establecemos el control de las colisiones en el mundo.
    }

    //  Método para añadir un suelo.
    private void addFloor(){
        if (world != null){
            Floor floor = new Floor(world);
        }
    }
    //  Añadir el fondo.
    private void addBackground(){
        Image background = new Image( assetMan.getBackground());
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        background.setPosition(0,0);
        stage.addActor(background);
    }
    //  Método que controla la generación de plataformas.
    private void addPlatforms(){
        final float MAX_DISTANCE = WORLD_WIDTH/2.5f;    //  Distancia añadida máxima a la que aparecerá la plataforma.
        final float MIN_DISTANCE = WORLD_WIDTH/3f;      //  Distancia añadida mínima a la que aparecerá la plataforma.
        float posX;

        //  El bucle gestiona para cada plataforma su posición de manera distinta.
        for (int i = platforms.size; i < 3; i++) {
            //  La primera se genera justo debajo del personaje cada vez.
            if (i == 0){
                Platform platform1 = new Platform(new Vector2(WORLD_WIDTH/5f, 0f), world, true);
                platforms.add(platform1);
                this.stage.addActor(platform1);
            }
            //  La segunda se generará siempre más o menos a la misma distancia de la primera.
            if (i == 1){
                posX = platforms.get(platforms.size-1).getBody().getPosition().x + MathUtils.random(MIN_DISTANCE, MAX_DISTANCE);
                Platform platform = new Platform(new Vector2(posX, 0f), world, false);
                platforms.add(platform);
                this.stage.addActor(platform);
            }
            //  Las siguientes se generan fuera de la pantalla a una distancia aleatoria.
            if (i == 2){
                posX = WORLD_WIDTH + MathUtils.random(MIN_DISTANCE, MAX_DISTANCE);
                Platform platform = new Platform(new Vector2(posX, 0f), world, false);
                platforms.add(platform);
                this.stage.addActor(platform);
            }
        }

    }
    //  Método que mueve las plataformas, los sensores y al personaje.
    private void movePlatforms(){
        ninja.getBody().setLinearVelocity(-2.5f,0);
        for(Platform platform : platforms){
            if (platform.counterBody != null)
                platform.counterBody.setLinearVelocity(-2.5f,0);
            platform.body.setLinearVelocity(-2.5f,0);
        }
        areMoving = true;
    }

    //  Método que para los elementos anteriores en cuanto la plataforma sobre la que se sitúa el personaje llega al límite de la pantalla.
    private void stopPlatforms(){
        if ((platforms.get(1).getBody().getPosition().x-platforms.get(1).getPlatformWidth()/2) <= 0f){
            areMoving = false;  //  Cambio del estado para que se tenga en cuenta en el siguiente render.
            ninja.getBody().setLinearVelocity(0f,0f);
            for(Platform platform : platforms){
                if (platform.counterBody != null)
                    platform.counterBody.setLinearVelocity(0f,0);
                platform.body.setLinearVelocity(0f,0);
            }
            //  Se borra la plataforma sobre la que estábamos anteriormente.
            platforms.get(0).dispose();
            platforms.get(0).remove();
            platforms.removeValue(platforms.get(0),false);
            //  Se crea un nuevo puente con posición relativa al personaje y la plataforma.
            bridge = new Bridge(world, new Vector2(platforms.get(0).body.getPosition().x + platforms.get(0).getPlatformWidth()/3,ninja.getBodyPosition().y -0.20f), ninja);
            stage.addActor(bridge);
            addPlatforms();
        }
    }
    //  Añade el contador de puntuación a la pantalla.
    private void addCounter(){
        this.counterFont =  assetMan.getFont();
        this.counterFont.getData().scale(0.5f);    //  Escalado de la fuente
        this.counterFont.setColor(Color.BLACK);
        this.counterCamera = new OrthographicCamera();  //  Se crea una nueva cámara para la fuente. Esta sigue las proporciones de la pantalla, no las del mundo.
        this.counterCamera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);  //  Asignación de las dimensiones de la pantalla para la cámara.
        this.counterCamera.update();    //  Aplicar cambios.
    }

    //  En Show() se añaden todos los actores y elementos físicos del escenario en el Stage y el mundo, y se activa la música de fondo.
    @Override
    public void show() {
        soundMan.playBGM(true);
        addBackground();
        this.platforms = new Array<>();
        addPlatforms();
        this.ninja = new Ninja(new Vector2(WORLD_WIDTH/5f, WORLD_HEIGHT/5), world);
        this.bridge = new Bridge(world, new Vector2(platforms.get(0).body.getPosition().x + platforms.get(0).getPlatformWidth()/3,ninja.getBodyPosition().y -0.25f), ninja);
        this.stage.addActor(ninja);
        this.stage.addActor(bridge);
        addFloor();
        addCounter();
    }
    //  Se liberan todos los recursos de memoria con estos dos métodos.
    @Override
    public void hide() {
        super.hide();
        this.ninja.dispose();
        dispose();

    }

    @Override
    public void dispose() {
        this.stage.dispose();
        this.world.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);   //  Limpia la pantalla.

        //  Para evitar problemas a la hora de manipular elementos uso las variables de estado.
        //  Esto debe hacerse antes del step, motivo por el que coloco este código aquí. PostRunnable pone en cola cada hilo lanzado.

        //  Lanza la pantalla de GameOver.
        if (gameOver){
            mainGame.setScreen(new GameOverScreen(mainGame));
        }
        //  Para los elementos cuando se están moviendo.
        if (areMoving){
            Gdx.app.postRunnable(new Runnable(){
                @Override
                public void run() {
                    stopPlatforms();
                }
            });
        }
        // Indica que el personaje ha cruzado para que se quiten tanto el puente como el sensor de la plataforma.
        if (hasCrossed){
            Gdx.app.postRunnable(new Runnable(){
                @Override
                public void run() {
                    bridge.dispose();
                    bridge.remove();
                    platforms.get(1).disposeCounter();
                    hasCrossed = false; //  Se reinicia el estado.
                }
            });
        }
        this.world.step(delta, 6, 2);   //  Avanza a la siguiente iteración del mundo.
        this.stage.act();   //  Se llama al Act() de todos los actores.
        this.stage.draw();  //  Se llama al Draw() de todos los actores.
        //  Se pasa la matriz de la cámara de puntuación al lote de dibujado del Stage.
        this.stage.getBatch().setProjectionMatrix(this.counterCamera.combined);
        this.stage.getBatch().begin();  //  Inicio
        this.counterFont.draw(this.stage.getBatch(), String.valueOf(counter), SCREEN_WIDTH/2, SCREEN_HEIGHT/1.2f);  //  Dibujado de la puntuación.
        this.stage.getBatch().end();    //  Fin
        //this.debugRenderer.render(world, worldCamera.combined);   //  Renderizado del modo depuración.
    }



    //  Método para detectar las colisiones entre dos objetos.
    private boolean isCollision(Contact contact, Object objA, Object objB) {
        return (contact.getFixtureA().getUserData().equals(objA) && contact.getFixtureB().getUserData().equals(objB)) ||
                (contact.getFixtureA().getUserData().equals(objB) && contact.getFixtureB().getUserData().equals(objA));
    }

    //  Método de ContactListener que actúa cuándo se está empezando a dar la colisión entre los objetos.
    @Override
    public void beginContact(Contact contact) {
        if (isCollision(contact, USER_NINJA, USER_COUNTER)){    //  COLISIÓN ENTRE NINJA Y SENSOR
            hasCrossed = true;
            ninja.startIdle();
            soundMan.playScoreSound();
            counter++;
            movePlatforms();
        }
        if (isCollision(contact, USER_NINJA, USER_FLOOR)){  //  COLISIÓN ENTRE NINJA Y SUELO
            soundMan.playFallSound();
            soundMan.stopBGM();
            gameOver = true;

        }
    }
    //  Método de ContactListener que actúa cuándo se termina la colisión. Útil para indicar que el personaje ha cruzado y cambiar el estado de este.
    @Override
    public void endContact(Contact contact) {
        if (isCollision(contact, USER_NINJA, USER_BRIDGE) && !hasCrossed){  //  FIN DE LA COLISIÓN ENTRE PUENTE Y NINJA CUANDO ESTE AÚN NO HA LLEGADO A OTRA PLATAFORMA.
            ninja.startIdle();
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
