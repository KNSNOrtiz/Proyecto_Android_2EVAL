package com.ninjarun.managers;

import static com.ninjarun.Utils.BACKGROUND;
import static com.ninjarun.Utils.*;
import static com.ninjarun.Utils.RUN_2;
import static com.ninjarun.Utils.RUN_3;
import static com.ninjarun.Utils.RUN_4;
import static com.ninjarun.Utils.RUN_5;
import static com.ninjarun.Utils.RUN_6;
import static com.ninjarun.Utils.RUN_7;
import static com.ninjarun.Utils.RUN_8;
import static com.ninjarun.Utils.RUN_9;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetMan {
    AssetManager assetManager;  //  Instancia de AssetManager, la clase que gestiona los recursos del videojuego.
    TextureAtlas textureAtlas;  //  TextureAtlas permite acceder a regiones del Atlas mediante el nombre de estas para obtener los sprites.

    public AssetMan(){
        assetManager = new AssetManager();
        //  CARGA DE RECURSOS
        assetManager.load(NR_ATLAS, TextureAtlas.class);
        assetManager.load(SOUND_SCORE, Sound.class);
        assetManager.load(SOUND_FALL, Sound.class);
        assetManager.load(SOUND_BUILD, Music.class);
        assetManager.load(BGM_GAME, Music.class);
        assetManager.finishLoading();
        //  OBTENCIÓN DEL ATLAS
        textureAtlas = assetManager.get(NR_ATLAS);
    }

    // IMAGENES SIMPLES
    public TextureRegion getBackground(){
        return textureAtlas.findRegion(BACKGROUND);
    }
    public TextureRegion getLogoImage(){
        return textureAtlas.findRegion(LOGO_IMG);
    }
    public TextureRegion getFooterImage(){
        return textureAtlas.findRegion(FOOTER_IMG);
    }
    public TextureRegion getGameOverImage(){
        return textureAtlas.findRegion(GAMEOVER_IMG);
    }
    public TextureRegion getStartButton(){
        return textureAtlas.findRegion(BTN_START);
    }
    public TextureRegion getExitButton(){
        return textureAtlas.findRegion(BTN_EXIT);
    }

    public TextureRegion getPlatform(){
        return textureAtlas.findRegion(PLATFORM);
    }
    public TextureRegion getBridge(){return textureAtlas.findRegion(BRIDGE);}

    //  FUENTE EN FORMATO  BITMAP MEDIANTE LOS ARCHIVOS .FNT Y .PNG, PARA PODER USARSE EN EL JUEGO.
    public BitmapFont getFont(){
        return new BitmapFont(Gdx.files.internal(FONT_FNT),Gdx.files.internal(FONT_PNG), false);
    }

    //  SONIDOS
    public Sound getScoreSound(){
        return assetManager.get(SOUND_SCORE);
    }
    public Sound getFallSound(){
        return assetManager.get(SOUND_FALL);
    }
    public Music getBuildSound(){   //  Este sonido es cargado como Music para facilitar su gestión.
        return assetManager.get(SOUND_BUILD);
    }
    public Music getGameBGM(){
        return assetManager.get(BGM_GAME);
    }

    // ANIMACIONES
    public Animation<TextureRegion> getNinjaIdle(){
        Animation<TextureRegion> animation = new Animation<TextureRegion>(0.1f,
                textureAtlas.findRegion(IDLE_0), textureAtlas.findRegion(IDLE_1),
                textureAtlas.findRegion(IDLE_2), textureAtlas.findRegion(IDLE_3),
                textureAtlas.findRegion(IDLE_4), textureAtlas.findRegion(IDLE_5),
                textureAtlas.findRegion(IDLE_6), textureAtlas.findRegion(IDLE_7),
                textureAtlas.findRegion(IDLE_8), textureAtlas.findRegion(IDLE_9));
        return animation;
    }
    public Animation<TextureRegion> getNinjaRun(){
        Animation<TextureRegion> animation = new Animation<TextureRegion>(0.05f,
                textureAtlas.findRegion(RUN_0), textureAtlas.findRegion(RUN_1),
                textureAtlas.findRegion(RUN_2), textureAtlas.findRegion(RUN_3),
                textureAtlas.findRegion(RUN_4), textureAtlas.findRegion(RUN_5),
                textureAtlas.findRegion(RUN_6), textureAtlas.findRegion(RUN_7),
                textureAtlas.findRegion(RUN_8), textureAtlas.findRegion(RUN_9));
        return animation;
    }
}
