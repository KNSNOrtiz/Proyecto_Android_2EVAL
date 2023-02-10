package com.ninjarun;

import com.badlogic.gdx.Game;
import com.ninjarun.screens.MainScreen;

//	La clase principal heredará de Game en lugar de ApplicationAdapter para poder usar setScreen. La única función de MainGame en toda la aplicación será iniciar
//	la primera pantalla, ya que después gestionaremos las pantallas mediante una clase dedicada a ello.
public class MainGame extends Game {

	@Override
	public void create() {
		setScreen(new MainScreen());
	}
}