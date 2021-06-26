package garlicquasar.babel.matt.dishesneedpacking.Acitivities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import garlicquasar.babel.matt.dishesneedpacking.Audio.MusicServiceHandler;
import garlicquasar.babel.matt.dishesneedpacking.R;

public class SplashActivity extends AppCompatActivity {

    private boolean skipped = false;
    private boolean goingToMain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // top bar re-color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.background));
        }

        MusicServiceHandler.setup(getApplicationContext());

        Thread audioThread = new Thread()
        {
            public void run() {
                while(!MusicServiceHandler.menuAudioBound) {
                    try {
                       sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                MusicServiceHandler.startMenuAudio();
            }
        };

        Thread thread = new Thread()
        {
            public void run() {
                try {
                    sleep(3000);
                }
                catch (Exception e) {
                        e.printStackTrace();
                }
                finally {
                    if (!skipped) {
                        toMain();
                    }
                }
            }
        };

        audioThread.start();
        thread.start();
    }

    private void toMain() {
        goingToMain = true;
        skipped = true;
        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(mainIntent);

    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!goingToMain) {
            MusicServiceHandler.killAll();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!goingToMain) {
            MusicServiceHandler.pauseMenuAudio();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MusicServiceHandler.startMenuAudio();
    }

    // go to main on any touch
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            skipped = true;
            toMain();
            return true;
        } else {
            return false;
        }
    }
}
