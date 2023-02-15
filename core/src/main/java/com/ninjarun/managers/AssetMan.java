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

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetMan {
    AssetManager assetManager;
    TextureAtlas textureAtlas;

    public AssetMan(){
        assetManager = new AssetManager();
        assetManager.load(NR_ATLAS, TextureAtlas.class);
        assetManager.finishLoading();
        textureAtlas = assetManager.get(NR_ATLAS);
    }

    // IMAGENES SIMPLES
    public TextureRegion getBackground(){
        return textureAtlas.findRegion(BACKGROUND);
    }
    public TextureRegion getPlatform(){
        return textureAtlas.findRegion(PLATFORM);
    }
    public TextureRegion getBridge(){return textureAtlas.findRegion(BRIDGE);}

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
