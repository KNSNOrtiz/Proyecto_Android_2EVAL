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

    //  Declaramos los dos gestores que necesitaremos usar en todas las pantallas de nuestro juego.
    protected MainGame mainGame;
    protected ScreenMan screenMan;
    protected AssetMan assetMan;
    //  Todas las pantallas van a necesitar Stage para gestionar a los actores, así que declaramos ya una instancia.
    protected Stage stage;
    protected World world;
    //  Stage necesita el viewport para poder instanciarse, así que declaramos un FitViewPort. He elegido este tipo de ViewPort porque
    //  el mundo ocupará toda la pantalla, las plataformas son generadas fuera del mundo.
    protected FitViewport fitViewport;



    public AbstractScreen(){
        mainGame = new MainGame();
        screenMan = new ScreenMan(mainGame);
        assetMan = new AssetMan();
        fitViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        stage = new Stage(fitViewport);
    }

    //  Para evitar volver a escribir los métodos en las siguientes pantallas, definimos en la pantalla base los métodos necesarios para colocar
    //  a los actores que vayan haciendo falta.


    protected void addBackground(){
        Image background = new Image(assetMan.getBackground());
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        background.setPosition(0,0);
        stage.addActor(background);
    }
    protected void addFloor(){
        if (world != null){
            Floor floor = new Floor(world);
        }
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
