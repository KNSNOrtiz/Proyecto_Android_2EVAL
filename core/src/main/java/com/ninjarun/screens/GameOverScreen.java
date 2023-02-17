package com.ninjarun.screens;

import static com.ninjarun.MainGame.assetMan;
import static com.ninjarun.Utils.WORLD_HEIGHT;
import static com.ninjarun.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ninjarun.MainGame;

public class GameOverScreen extends AbstractScreen{

    private Stage stage;

    public GameOverScreen(MainGame mainGame){
        super(mainGame);
        this.stage = new Stage(fitViewport);
    }
    private void addBackground(){
        Image background = new Image(assetMan.getBackground());
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        background.setPosition(0,0);
        stage.addActor(background);
    }
    private void addGameOver(){
        Image image = new Image(assetMan.getGameOverImage());
        image.setSize(WORLD_WIDTH, WORLD_HEIGHT/3);
        image.setPosition(0,WORLD_HEIGHT/1.8f);
        stage.addActor(image);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.act();
        this.stage.draw();
        if (Gdx.input.justTouched()){
            mainGame.setScreen(new MainScreen(mainGame));
        }
    }

    @Override
    public void show() {
        addBackground();
        addGameOver();
    }

    @Override
    public void hide() {
        super.hide();
        this.stage.dispose();
    }
}
