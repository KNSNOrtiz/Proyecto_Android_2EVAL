package com.ninjarun.screens;

import static com.ninjarun.Utils.WORLD_HEIGHT;
import static com.ninjarun.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ninjarun.MainGame;

import java.awt.Menu;

public class MenuScreen extends AbstractScreen{

    private Stage stage;
    private boolean startGame;

    public MenuScreen(MainGame mainGame){
        super(mainGame);
        FitViewport fitViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        this.stage = new Stage(fitViewport);
        this.startGame = false;
        MainGame.inputMultiplexer.addProcessor(this.stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.act();
        this.stage.draw();
        if (startGame)
            mainGame.setScreen(new MainScreen(mainGame));
            //MainGame.inputMultiplexer.removeProcessor(this.stage);
            startGame = false;
    }

    private void addBackground(){
        Image background = new Image( this.mainGame.assetMan.getBackground());
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        background.setPosition(0,0);
        stage.addActor(background);
    }

    private void addLogo(){
        Image logo = new Image( this.mainGame.assetMan.getLogoImage());
        logo.setSize(WORLD_WIDTH, WORLD_HEIGHT/3);
        logo.setPosition(0,WORLD_HEIGHT/1.8f);
        this.stage.addActor(logo);
    }
    private void addFooter(){
        Image footer = new Image( this.mainGame.assetMan.getFooterImage());
        footer.setSize(WORLD_WIDTH/2f, WORLD_HEIGHT /8f);
        footer.setPosition(WORLD_WIDTH/2f,0f);
        this.stage.addActor(footer);
    }
    private void addStartButton(){
        Image button = new Image( this.mainGame.assetMan.getStartButton());
        button.setSize(WORLD_WIDTH/3f, WORLD_HEIGHT /9);
        button.setPosition(WORLD_WIDTH/3f,WORLD_HEIGHT/2.25F);
        button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startGame = true;
                return true;
            }
        });
        this.stage.addActor(button);
    }
    private void addExitButton(){
        Image button = new Image( this.mainGame.assetMan.getExitButton());
        button.setSize(WORLD_WIDTH/3f, WORLD_HEIGHT /9f);
        button.setPosition(WORLD_WIDTH/3f,WORLD_HEIGHT/4f);
        button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });
        this.stage.addActor(button);
    }

    @Override
    public void show() {
        addBackground();
        addLogo();
        addFooter();
        addStartButton();
        addExitButton();
    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }
}
