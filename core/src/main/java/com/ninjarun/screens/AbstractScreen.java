package com.ninjarun.screens;

import static com.ninjarun.Utils.WORLD_HEIGHT;
import static com.ninjarun.Utils.WORLD_WIDTH;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ninjarun.MainGame;

//  Creamos una clase abstracta que implementa Screen
public abstract class AbstractScreen implements Screen {

    protected MainGame mainGame;
    protected FitViewport fitViewport;

    public AbstractScreen(MainGame mainGame){
        this.mainGame = mainGame;
        this.fitViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
