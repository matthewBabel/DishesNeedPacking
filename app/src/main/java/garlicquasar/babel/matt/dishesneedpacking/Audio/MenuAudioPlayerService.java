package garlicquasar.babel.matt.dishesneedpacking.Audio;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import garlicquasar.babel.matt.dishesneedpacking.R;

public class MenuAudioPlayerService extends Service {

    private final IBinder binder = new MenuAudioLocalBinder();
    private MediaPlayer player;

    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.menu_song);
        player.setLooping(true); // Set looping
        player.setVolume(1f,1f);
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class MenuAudioLocalBinder extends Binder {
        MenuAudioPlayerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MenuAudioPlayerService.this;
        }
    }

    public IBinder onUnBind(Intent arg0) {
        return null;
    }

    public void stop() {
            Log.d(null, "In stop");
            player.reset();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                try {
                    mp.prepare();
                    mp.stop();
                    mp.release();
                    player = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }
            });
    }

    public void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }

    public void start() {
        player.start();
    }

    public void restart() {
        player = MediaPlayer.create(this, R.raw.menu_song);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
        player = null;
    }

    @Override
    public void onLowMemory() {

    }

    public boolean isRunning() {
        return player.isPlaying();
    }
}
