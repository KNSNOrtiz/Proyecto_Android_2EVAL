package com.ninjarun.managers;
import static com.ninjarun.MainGame.assetMan;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

//  CLASE QUE GESTIONA LA REPRODUCCIÓN DE SONIDOS Y MÚSICA.
//  Nos evita tener que recoger una y otra vez los sonidos del Asset Manager cada vez que creamos una instancia de las pantallas o los actores.
//  De no hacerlo, el MediaPlayer de Android puede saturarse y cortar todos los sonidos del juego.
public class SoundMan {
    //  Sonidos/Música
    private Sound counterSound;
    private Sound fallSound;
    private Music bgm;
    private Music buildSound;   //  Se carga como música para poder gestionar su estado (si está sonando, se para; si no, reproducirlo...)

    public SoundMan(){
        counterSound = assetMan.getScoreSound();
        fallSound = assetMan.getFallSound();
        bgm = assetMan.getGameBGM();
        buildSound = assetMan.getBuildSound();
    }

    //  Reproduce la música de fondo del MainScreen, y recibe como parámetro si se quiere reproducir en bucle o no.
    public void playBGM(boolean loop){
        bgm.setLooping(loop);
        bgm.setVolume(0.5f);
        if (!bgm.isPlaying())
            bgm.play();
    }
    //  Para la música.
    public void stopBGM(){
        if (bgm.isPlaying()){
            bgm.stop();
        }
    }
    //  Reproduce el sonido de construcción del puente.
    public void playBuildSound(){
        if (!buildSound.isPlaying()){
            buildSound.play();
        }
    }
    //  Detiene el sonido de construcción.
    public void stopBuildSound(){
        if (buildSound.isPlaying()){
            buildSound.stop();
        }
    }
    //  Reproduce el sonido de puntuación.
    public void playScoreSound(){
        counterSound.play(0.5f);
    }
    //  Reproduce el sonido de colisión entre el personaje y el suelo.
    public void playFallSound(){
        fallSound.play();
    }
}
