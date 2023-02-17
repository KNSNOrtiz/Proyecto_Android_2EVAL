package com.ninjarun.screens;

import static com.ninjarun.Utils.WORLD_HEIGHT;
import static com.ninjarun.Utils.WORLD_WIDTH;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ninjarun.MainGame;

//  Creamos una clase abstracta que implementa Screen para incorporar en las pantallas únicamente los métodos que nos interese. También permitimos así que
//  compartan el mismo constructor y que todas tengan MainGame para cambiar de una pantalla a otra, así cómo tener ya la instancia de la vista (FitViewport)
//  en lugar de crearla en cada pantalla.
public abstract class AbstractScreen implements Screen {

    protected MainGame mainGame;
    protected FitViewport fitViewport;  //  Representa la vista. En este caso, FitViewport escala la vista de las dimensiones de la pantalla a las que tiene el mundo. Así todas las posiciones serán relativas.

    public AbstractScreen(MainGame mainGame){
        this.mainGame = mainGame;
        this.fitViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
    }


    //  Puede considerarse el constructor de la pantalla. Se ejecuta cuando MainGame llama a SetScreen.
    @Override
    public void show() {

    }
    //  Render es la ejecución constante del juego, se llama tantas veces por segundo como el procesador pueda.
    @Override
    public void render(float delta) {

    }
    //  Redimensiona la pantalla.
    @Override
    public void resize(int width, int height) {

    }
    //  Se ejecuta cuando la pantalla está en segundo plano.
    @Override
    public void pause() {

    }
    //  Se ejecuta cuando se vuelve a poner en primer plano.
    @Override
    public void resume() {

    }
    //  Se llama cuando se cambia de pantalla mediante el SetScreen de MainGame. Se puede usar para eliminar recursos
    @Override
    public void hide() {

    }
    //  Actúa como OnDestroy(). Su utilidad es liberar recursos de memoria.
    @Override
    public void dispose() {

    }
}
