package com.ninjarun;

public class Utils {

    //  PROPIEDADES DEL MUNDO Y LA PANTALLA
    public static final int SCREEN_WIDTH = 480;
    public static final int SCREEN_HEIGHT = 800;
    //  Para poder operar más fácilmente con los actores, aplicamos una división entre 100 a la resolución de pantalla y obtenemos las proporciones del mundo   .
    public static final float WORLD_WIDTH = SCREEN_WIDTH/100f;
    public static final float WORLD_HEIGHT = SCREEN_HEIGHT/100f;

    //  ID DE LOS ACTORES (detección de  colisiones entre sus fixtures.)
    public static final String USER_NINJA = "ninja";
    public static final String USER_FLOOR = "floor";
    public static final String USER_PLATFORM = "platform";
    public static final String USER_BRIDGE = "bridge";
    public static final String USER_COUNTER = "counter";


    //  FUENTE
    public static final String FONT_FNT = "NRFont.fnt";
    public static final String FONT_PNG = "NRFont.png";



    //  SPRITES
    public static final String NR_ATLAS = "NinjaRunAtlas.atlas";
    public static final String BACKGROUND = "background";
    public static final String LOGO_IMG = "logo";
    public static final String FOOTER_IMG = "footer.png";
    public static final String GAMEOVER_IMG  = "gameover";
    public static final String BTN_START = "button_start";
    public static final String BTN_EXIT  = "button_exit";


    public static final String PLATFORM = "platform";
    public static final String BRIDGE = "bridge";

    public static final String IDLE_0 = "idle0";
    public static final String IDLE_1 = "idle1";
    public static final String IDLE_2 = "idle2";
    public static final String IDLE_3 = "idle3";
    public static final String IDLE_4 = "idle4";
    public static final String IDLE_5 = "idle5";
    public static final String IDLE_6 = "idle6";
    public static final String IDLE_7 = "idle7";
    public static final String IDLE_8 = "idle8";
    public static final String IDLE_9 = "idle9";

    public static final String RUN_0 = "run0";
    public static final String RUN_1 = "run1";
    public static final String RUN_2 = "run2";
    public static final String RUN_3 = "run3";
    public static final String RUN_4 = "run4";
    public static final String RUN_5 = "run5";
    public static final String RUN_6 = "run6";
    public static final String RUN_7 = "run7";
    public static final String RUN_8 = "run8";
    public static final String RUN_9 = "run9";


    //  SONIDOS + MÚSICA
    public static final String SOUND_SCORE = "score.mp3";
    public static final String SOUND_BUILD = "build.mp3";
    public static final String SOUND_FALL = "fall.mp3";
    public static final String BGM_GAME = "bgm_game.mp3";



}
