package com.ninjarun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.ninjarun.managers.AssetMan;
import com.ninjarun.screens.MainScreen;
import com.ninjarun.screens.MenuScreen;
import com.ninjarun.screens.PruebaScreen;

public class MainGame extends Game {

	public MenuScreen menuScreen;
	public MainScreen mainScreen;
	public AssetMan assetMan;
	public static InputMultiplexer inputMultiplexer;

	@Override
	public void create() {
		this.assetMan = new AssetMan();
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
		this.mainScreen = new MainScreen(this);
		this.menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

}