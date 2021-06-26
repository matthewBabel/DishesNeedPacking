package garlicquasar.babel.matt.dishesneedpacking.Audio;

import android.content.Context;
import android.media.MediaPlayer;

import garlicquasar.babel.matt.dishesneedpacking.Acitivities.MainActivity;
import garlicquasar.babel.matt.dishesneedpacking.R;

/**
 * Sound Effect player controlled by calling sounds effects statically
 */
public class SoundEffectPlayer {
    private static MediaPlayer[] players = new MediaPlayer[8]; // up to this many sound effects can play at once

    public static void playNextPiece(Context c) {
        playSound(c, R.raw.next_piece);
    }

    public static void playPickupPiece(Context c) {
        playSound(c, R.raw.pickup_piece);
    }

    public static void playPlacePiece(Context c) {
        playSound(c, R.raw.place_piece);
    }

    public static void playTakenSpot(Context c) {
        playSound(c, R.raw.taken_spot);
    }

    public static void playVictory(Context c) {
        playSound(c, R.raw.victory);
    }

    public static void playClick(Context c) {
        playSound(c, R.raw.click);
    }

    public static void playBackButton(Context c) {
        playSound(c, R.raw.back_button);
    }

    public static void playRotateButton(Context c) {
        playSound(c, R.raw.rotate_button);
    }

    public static void playRunButton(Context c) {
        playSound(c, R.raw.run_button);
    }

    /**
     * Look for free sound player, and first one found create media player and start.
     * On completion stop, reset, and release media player.
     * @param c context
     * @param sound raw sound
     */
    private static void playSound(Context c, int sound) {
        if (MainActivity.sfxOn) {
            boolean foundFreePlayer = false;
            int index = 0;
            for (MediaPlayer player : players) {
                if (player == null) {
                    if (!foundFreePlayer) {
                        player = MediaPlayer.create(c, sound);

                        player.start();
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.stop();
                                mp.reset();
                                mp.release();
                            }
                        });
                        foundFreePlayer = true;
                    }
                } else {
                    if (!player.isPlaying()) {
                        player.stop();
                        player.reset();
                        player.release();
                        players[index] = null;
                    }
                }
                index++;
            }
        }
    }
}
