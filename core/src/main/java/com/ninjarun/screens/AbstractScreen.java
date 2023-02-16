package com.ninjarun.screens;

import static com.ninjarun.Utils.WORLD_HEIGHT;
import static com.ninjarun.Utils.WORLD_WIDTH;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ninjarun.MainGame;
import com.ninjarun.actors.Floor;
import com.ninjarun.managers.AssetMan;
import com.ninjarun.managers.ScreenMan;

//  Creamos una clase abstracta que implementa Screen
public abstract class AbstractScreen implements Screen {

    public MainGame mainGame;

    public AbstractScreen(MainGame mainGame){
        this.mainGame = mainGame;
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
