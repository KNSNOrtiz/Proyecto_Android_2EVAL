package com.ninjarun.screens;

import static com.ninjarun.MainGame.assetMan;
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

//  MENÚ PRINCIPAL DEL JUEGO. ESTA PANTALLA SOLAMENTE SE MUESTRA UNA ÚNICA VEZ.
public class MenuScreen extends AbstractScreen{

    private Stage stage;
    private boolean startGame;

    public MenuScreen(MainGame mainGame){
        super(mainGame);
        FitViewport fitViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        this.stage = new Stage(fitViewport);
        this.startGame = false;
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.act();
        this.stage.draw();
        if (startGame){
            startGame = false;
            mainGame.setScreen(new MainScreen(mainGame));
        }

    }
    //  FONDO DEL MENÚ.
    private void addBackground(){
        Image background = new Image(assetMan.getBackground());
        background.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        background.setPosition(0,0);
        stage.addActor(background);
    }
    //  ENCABEZADO DEL MENÚ.
    private void addLogo(){
        Image logo = new Image( assetMan.getLogoImage());
        logo.setSize(WORLD_WIDTH, WORLD_HEIGHT/3);
        logo.setPosition(0,WORLD_HEIGHT/1.8f);
        this.stage.addActor(logo);
    }
    //  PIE DEL MENÚ.
    private void addFooter(){
        Image footer = new Image( assetMan.getFooterImage());
        footer.setSize(WORLD_WIDTH/2f, WORLD_HEIGHT /8f);
        footer.setPosition(WORLD_WIDTH/2f,0f);
        this.stage.addActor(footer);
    }
    //  BOTÓN DE INICIO DEL JUEGO.
    private void addStartButton(){
        Image button = new Image( assetMan.getStartButton());
        button.setSize(WORLD_WIDTH/3f, WORLD_HEIGHT /9);
        button.setPosition(WORLD_WIDTH/3f,WORLD_HEIGHT/2.25F);
        button.addListener(new ClickListener(){ //  Asignación de entrada al botón.
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                startGame = true;
                return true;
            }
        });
        this.stage.addActor(button);
    }
    //  BOTÓN DE SALIDA.
    private void addExitButton(){
        Image button = new Image( assetMan.getExitButton());
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
    public void hide() {
        super.hide();
        this.stage.dispose();
    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }
}
