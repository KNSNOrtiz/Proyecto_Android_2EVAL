package com.ninjarun.screens;

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
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ninjarun.MainGame;
import com.ninjarun.actors.Bridge;
import com.ninjarun.actors.Floor;
import com.ninjarun.actors.Ninja;
import com.ninjarun.actors.Platform;
import com.ninjarun.managers.AssetMan;

import jdk.tools.jmod.Main;


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
    private Stage stage;
    private World world;

    //  Actores
    private Ninja ninja;
    private Bridge bridge;
    private Array<Platform> platforms;

    //  Variables auxiliares
    private boolean hasCrossed;
    private boolean areMoving;
    private boolean tooLong;

    //  Contador de puntuación
    private int counter = 0;


    //  Sonidos/Música
    private Sound counterSound;
    private Sound fallSound;
    private Music bgm;


    public MainScreen(MainGame mainGame) {
        super(mainGame);
        FitViewport fitViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        this.stage = new Stage(fitViewport);
        this.debugRenderer = new Box2DDebugRenderer();
        this.counterSound = this.mainGame.assetMan.getScoreSound();
        this.fallSound =  this.mainGame.assetMan.getFallSound();
        this.bgm = this.mainGame.assetMan.getGameBGM();
        bgm.setVolume(0.2f);
        this.worldCamera = (OrthographicCamera)stage.getCamera();
        this.world = new World(new Vector2(0, -9.80665f), true);
        this.world.setContactListener(this); //  Establecemos el control de las colisiones en el mundo.
    }

    private void addFloor(){
        if (world != null){
            Floor floor = new Floor(world);
        }
    }
    private void addBackground(){
        Image background = new Image( this.mainGame.assetMan.getBackground());
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        background.setPosition(0,0);
        stage.addActor(background);
    }

    private void addPlatforms(){
        final float MAX_DISTANCE = 1.7f;
        final float MIN_DISTANCE = 1.0f;
        float posX;

        for (int i = platforms.size; i < 3; i++) {
            if (i == 0){
                Platform platform1 = new Platform(new Vector2(WORLD_WIDTH/5f, 0f), world, true);
                platforms.add(platform1);
                this.stage.addActor(platform1);
            }
            if (i == 1){
                posX = platforms.get(platforms.size-1).getBody().getPosition().x + MathUtils.random(MIN_DISTANCE, MAX_DISTANCE);
                Platform platform = new Platform(new Vector2(posX, 0f), world, false);
                platforms.add(platform);
                this.stage.addActor(platform);
            }
            if (i == 2){
                posX = WORLD_WIDTH + MathUtils.random(MIN_DISTANCE, MAX_DISTANCE);
                Platform platform = new Platform(new Vector2(posX, 0f), world, false);
                platforms.add(platform);
                this.stage.addActor(platform);
            }
        }

    }

    private void movePlatforms(){
        ninja.body.setLinearVelocity(-2.5f,0);
        for(Platform platform : platforms){
            if (platform.counterBody != null)
                platform.counterBody.setLinearVelocity(-2.5f,0);
            platform.body.setLinearVelocity(-2.5f,0);
        }
        areMoving = true;
    }

    private void stopPlatforms(){
        if ((platforms.get(1).getBody().getPosition().x-platforms.get(1).getPlatformWidth()/2) <= 0f){
            areMoving = false;
            ninja.body.setLinearVelocity(0f,0f);
            for(Platform platform : platforms){
                if (platform.counterBody != null)
                    platform.counterBody.setLinearVelocity(0f,0);
                platform.body.setLinearVelocity(0f,0);
            }
            platforms.get(0).detach();
            platforms.get(0).remove();
            System.out.println("Borrando plataforma");
            platforms.removeValue(platforms.get(0),false);

            System.out.println("Plataformas: " + platforms.size);
            bridge = new Bridge(world, new Vector2(platforms.get(0).body.getPosition().x + platforms.get(0).getPlatformWidth()/3,ninja.getBodyPosition().y -0.20f), ninja);
            stage.addActor(bridge);
            addPlatforms();
        }
    }

    private void addCounter(){
        this.counterFont =  this.mainGame.assetMan.getFont();
        this.counterFont.getData().scale(0.5f);    //  Escalado de la fuente
        this.counterFont.setColor(Color.BLACK);
        this.counterCamera = new OrthographicCamera();
        this.counterCamera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);  //  Asignación de las dimensiones de la pantalla para la cámara.
        this.counterCamera.update();    //  Aplicar cambios.
    }


    @Override
    public void show() {
        bgm.play();
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //  Para evitar problemas a la hora de quitar el puente, uso una variable auxiliar que dice al juego cuándo tiene que borrar según qué elementos, y esto
        //  debe ser antes del step, motivo por el que coloco este código aquí. PostRunnable intenta que el código se haga fuera de cualquier step
        //  de World por si acaso.
        if (areMoving){
            Gdx.app.postRunnable(new Runnable(){
                @Override
                public void run() {
                    stopPlatforms();
                }
            });
        }
        if (hasCrossed){
            Gdx.app.postRunnable(new Runnable(){
                @Override
                public void run() {
                    bridge.detach();
                    bridge.remove();
                    platforms.get(1).detachCounter();
                    hasCrossed = false;
                }
            });
        }
        if (tooLong){
            Gdx.app.postRunnable(new Runnable(){
                @Override
                public void run() {
                    platforms.get(1).detachCounter();
                    tooLong = false;
                }
            });
        }
        this.world.step(delta, 6, 2);
        this.stage.act();
        this.stage.draw();
        this.stage.getBatch().setProjectionMatrix(this.counterCamera.combined);
        this.stage.getBatch().begin();
        this.counterFont.draw(this.stage.getBatch(), String.valueOf(counter), SCREEN_WIDTH/2, SCREEN_HEIGHT/1.2f);
        this.stage.getBatch().end();
        this.debugRenderer.render(world, worldCamera.combined);
    }

    //  Método para detectar las colisiones entre dos objetos.
    private boolean isCollision(Contact contact, Object objA, Object objB) {
        return (contact.getFixtureA().getUserData().equals(objA) && contact.getFixtureB().getUserData().equals(objB)) ||
                (contact.getFixtureA().getUserData().equals(objB) && contact.getFixtureB().getUserData().equals(objA));
    }


    @Override
    public void beginContact(Contact contact) {
        if (isCollision(contact, USER_NINJA, USER_COUNTER)){
            hasCrossed = true;
            ninja.startIdle();
            counterSound.play();
            counter++;
            movePlatforms();
        }
        if (isCollision(contact, USER_BRIDGE, USER_COUNTER)){
            System.out.println("Se han tocado");
            tooLong = true;
        }
        if (isCollision(contact, USER_NINJA, USER_FLOOR)){
            fallSound.play();
            if (bgm.isPlaying()){
                bgm.stop();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (isCollision(contact, USER_NINJA, USER_BRIDGE) && !hasCrossed){
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
