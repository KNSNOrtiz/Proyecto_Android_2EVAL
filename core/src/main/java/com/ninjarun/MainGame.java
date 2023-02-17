package com.ninjarun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.ninjarun.managers.AssetMan;
import com.ninjarun.screens.GameOverScreen;
import com.ninjarun.screens.MainScreen;
import com.ninjarun.screens.MenuScreen;

public class MainGame extends Game {

	private MenuScreen menuScreen;
	public static AssetMan assetMan;
	//public static InputMultiplexer inputMultiplexer;

	@Override
	public void create() {
		assetMan = new AssetMan();
		/*inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);*/
		this.menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

}