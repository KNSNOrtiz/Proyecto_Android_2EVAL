package com.ninjarun;

import com.badlogic.gdx.Game;
import com.ninjarun.managers.AssetMan;
import com.ninjarun.managers.SoundMan;
import com.ninjarun.screens.MenuScreen;

public class MainGame extends Game {

	//	Instancia de la pantalla de menú, por donde se entrará al juego.
	private MenuScreen menuScreen;
	//	Hago que AssetManager sea público y estático para acceder a los recursos fácilmente desde cualquier sitio.
	//	Podría hacerse como Singleton, pero ya MainGame se asegura de que está instanciado en cualquier momento.
	public static AssetMan assetMan;
	//	SoundMan va a controlar el sonido de todo el juego de forma cómoda y eficiente.
	public static SoundMan soundMan;

	@Override
	public void create() {
		assetMan = new AssetMan();
		soundMan = new SoundMan();
		//	Se crea la instancia de la pantalla y se le pasa la clase principal, que es la que permite gestionar cambios de pantalla.
		this.menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

}