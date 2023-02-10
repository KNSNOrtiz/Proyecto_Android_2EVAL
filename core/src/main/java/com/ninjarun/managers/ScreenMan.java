package com.ninjarun.managers;

import com.badlogic.gdx.Screen;
import com.ninjarun.MainGame;

//  En lugar de gestionar las pantallas desde MainGame, lo haremos desde esta clase. Así, evitamos referenciar a MainGame en cada una
//  de las pantallas y resulta más intuitivo, además de que llamamos a dispose(), cosa que el setScreen() de Game no hace de por sí,
//  ya que este llama a hide(), y realmente no estamos liberando toda la memoria ocupada por la anterior pantalla.
public class ScreenMan {
    private MainGame mainGame;  //  Necesitamos una instancia de Game en la clase para poder usar su setScreen().

    public ScreenMan(MainGame mainGame) {
        this.mainGame = mainGame;
    }
    //  Este método añade una funcionalidad al setScreen de Game, ya que libera de la memoria la pantalla actual, que se pasará mediante
    // this.
    public void setScreen(Screen thisScreen, Screen newScreen){
        mainGame.setScreen(newScreen);
        if (thisScreen != null)
            thisScreen.dispose();
    }


}

