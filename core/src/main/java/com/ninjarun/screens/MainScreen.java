package com.ninjarun.screens;

import static com.ninjarun.Utils.WORLD_HEIGHT;
import static com.ninjarun.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ninjarun.actors.Ninja;
import com.ninjarun.actors.Platforms;

public class MainScreen extends AbstractScreen{

    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera worldCamera;
    private Ninja ninja;
    private Array<Platforms> platforms;

    public MainScreen() {
        super();
        debugRenderer = new Box2DDebugRenderer();
        worldCamera = (OrthographicCamera) stage.getCamera();
        world = new World(new Vector2(0, -4), true);
        platforms = new Array<>();
        addBackground();
        Platforms platform1 = new Platforms(new Vector2(WORLD_WIDTH/5, 0f), world);
        Platforms platform2 = new Platforms(new Vector2(WORLD_WIDTH/1.5f, 0f), world);
        platforms.add(platform1, platform2);
        for (Platforms platform:platforms){
            stage.addActor(platform);
        }
        ninja = new Ninja(new Vector2(WORLD_WIDTH/5, WORLD_HEIGHT), world);
        stage.addActor(ninja);
        addFloor();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        world.step(delta, 6, 2);
        stage.act();

        debugRenderer.render(world, worldCamera.combined);
    }
}
