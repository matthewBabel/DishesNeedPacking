package garlicquasar.babel.matt.dishesneedpacking.Audio;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import garlicquasar.babel.matt.dishesneedpacking.Acitivities.MainActivity;

/**
 * Overall Music Handler to wrap around Game and Menu Music Players
 */
public class MusicServiceHandler {
    private static MenuAudioPlayerService menuAudioService = null;
    private static GameAudioPlayerService gameAudioService = null;
    public static boolean menuAudioBound = false;
    public static boolean gameAudioBound = false;
    private static Context context;

    public static void setup(Context c) {
        context = c;
        Intent menuIntent = new Intent(context, MenuAudioPlayerService.class);
        context.bindService(menuIntent, menuAudioConnection, Context.BIND_AUTO_CREATE);

        Intent gameIntent = new Intent(context, GameAudioPlayerService.class);
        context.bindService(gameIntent, gameAudioConnection, Context.BIND_AUTO_CREATE);
    }

    public static boolean isNotInitialized() {
        return (menuAudioService == null || gameAudioService == null);
    }

    public static void killAll() {
        context.unbindService(menuAudioConnection);
        menuAudioBound = false;

        context.unbindService(gameAudioConnection);
        gameAudioBound = false;
    }

    public static void startMenuAudio() {
        if (MainActivity.musicOn && menuAudioService != null) {
            menuAudioService.start();
        }
    }

    public static void startGameAudio() {
        if (MainActivity.musicOn && gameAudioService != null) {
            gameAudioService.start();
        }
    }

    public static void pauseMenuAudio() {
        if (MainActivity.musicOn && menuAudioService != null) {
            menuAudioService.pause();
        }
    }

    public static void pauseGameAudio() {
        if (MainActivity.musicOn && gameAudioService != null) {
            gameAudioService.pause();
        }
    }

    public static void stopMenuAudio() {
        Log.d(null, "In service handler stop");
        if (menuAudioService.isRunning()) {
            menuAudioService.stop();
            menuAudioService.restart();
        }
    }

    public static void stopGameAudio() {
        if (gameAudioService.isRunning()) {
            gameAudioService.stop();
            gameAudioService.restart();
        }
    }



    /** Defines callbacks for service binding, passed to bindService() */
    private static ServiceConnection menuAudioConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MenuAudioPlayerService.MenuAudioLocalBinder binder = (MenuAudioPlayerService.MenuAudioLocalBinder) service;
            menuAudioService = binder.getService();
            menuAudioBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            menuAudioBound = false;
        }
    };

    /** Defines callbacks for service binding, passed to bindService() */
    private static ServiceConnection gameAudioConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            GameAudioPlayerService.GameAudioLocalBinder binder = (GameAudioPlayerService.GameAudioLocalBinder) service;
            gameAudioService = binder.getService();
            gameAudioBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            gameAudioBound = false;
        }
    };
}
