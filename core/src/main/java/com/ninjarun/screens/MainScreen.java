package com.ninjarun.screens;

import static com.ninjarun.Utils.USER_COUNTER;
import static com.ninjarun.Utils.USER_NINJA;
import static com.ninjarun.Utils.WORLD_HEIGHT;
import static com.ninjarun.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.ninjarun.actors.Bridge;
import com.ninjarun.actors.Ninja;
import com.ninjarun.actors.Platform;


public class MainScreen extends AbstractScreen implements ContactListener {

    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera worldCamera;
    private Ninja ninja;
    private Bridge bridge;
    private Array<Platform> platforms;
    private boolean borrarPuente;
    private int counter = 0;


    public MainScreen() {
        super();

        debugRenderer = new Box2DDebugRenderer();
        worldCamera = (OrthographicCamera) stage.getCamera();
        world = new World(new Vector2(0, -4.5f), true);
        world.setContactListener(this);

        BodyDef lineDef = new BodyDef();
        lineDef.type = BodyDef.BodyType.StaticBody;

        platforms = new Array<>();
        addBackground();
        ninja = new Ninja(new Vector2(WORLD_WIDTH/5f, WORLD_HEIGHT/5), world);
        bridge = new Bridge(world, new Vector2(ninja.getBodyPosition().x + 0.25f,ninja.getBodyPosition().y -0.25f), ninja);
        stage.addActor(ninja);
        stage.addActor(bridge);
        addPlatforms();
        addFloor();
    }

    private void addPlatforms(){
        final float MAX_DISTANCE = 3f;
        final float MIN_DISTANCE = 1.5f;
        int i = 0;

        while (platforms.size < 3) {
            if (i == 0){
                Platform platform1 = new Platform(new Vector2(WORLD_WIDTH/5f, 0f), world, true);
                platforms.add(platform1);
                this.stage.addActor(platform1);
            }

            else{
                float posX = platforms.get(platforms.size-1).getBody().getPosition().x + MathUtils.random(MIN_DISTANCE, MAX_DISTANCE);
                Platform platform = new Platform(new Vector2(posX, 0f), world, false);
                platforms.add(platform);
                this.stage.addActor(platform);
            }
            i++;

        }

    }

    //ToDo Añadir un sensor en la esquina izquierda del mundo para que detecte que la segunda plataforma ha llegado y borrar la primera + crear otra.
    private void movePlatforms(){
        ninja.body.setLinearVelocity(-2.5f,0);
        for(Platform platform : platforms){
            if (platform.counterBody != null)
                platform.counterBody.setLinearVelocity(-2.5f,0);
            platform.body.setLinearVelocity(-2.5f,0);
        }
        this.stage.addAction(
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        if (platforms.get(0).isOutOfScreen()){
                            ninja.body.setLinearVelocity(0f,0f);
                            for(Platform platform : platforms){
                                if (platform.counterBody != null)
                                    platform.counterBody.setLinearVelocity(0f,0);
                                platform.body.setLinearVelocity(0f,0);
                            }
                            System.out.println(platforms.size);
                            bridge = new Bridge(world, new Vector2(ninja.getBodyPosition().x + 0.25f,ninja.getBodyPosition().y -0.30f), ninja);
                            stage.addActor(bridge);
                        }

                    }
                })
        );
    }

    public void removePlatforms(){
        for (Platform platform : platforms) {
            if(!world.isLocked()) {
                if(platform.isOutOfScreen()) {
                    platform.detach();
                    platform.remove();
                    platforms.removeValue(platform,false);
                }
            }
        }
    }


    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //  Para evitar problemas a la hora de quitar el puente, uso una variable auxiliar que dice al juego cuándo tiene que borrar el puente, y esto
        //  debe ser antes del step, motivo por el que coloco este código aquí. PostRunnable intenta que el código se haga fuera de cualquier step
        //  de World por si acaso.
        if (borrarPuente){
            Gdx.app.postRunnable(new Runnable(){
                @Override
                public void run() {
                    final float MAX_DISTANCE = 3f;
                    final float MIN_DISTANCE = 1.5f;
                    bridge.detach();
                    bridge.remove();
                    removePlatforms();
                    float posX = platforms.get(platforms.size-1).getBody().getPosition().x + MathUtils.random(MIN_DISTANCE);
                    Platform platform = new Platform(new Vector2(posX, 0f), world, false);
                    platforms.add(platform);
                    stage.addActor(platform);
                    borrarPuente = false;
                }
            });
        }
        world.step(delta, 6, 2);
        stage.draw();
        stage.act();


        debugRenderer.render(world, worldCamera.combined);
    }

    private boolean isCollision(Contact contact, Object objA, Object objB) {
        return (contact.getFixtureA().getUserData().equals(objA) && contact.getFixtureB().getUserData().equals(objB)) ||
                (contact.getFixtureA().getUserData().equals(objB) && contact.getFixtureB().getUserData().equals(objA));
    }


    @Override
    public void beginContact(Contact contact) {
        if (isCollision(contact, USER_NINJA, USER_COUNTER)){
            borrarPuente = true;
            ninja.startIdle();
            counter++;
            System.out.println("Contador: " + counter);
            movePlatforms();
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
