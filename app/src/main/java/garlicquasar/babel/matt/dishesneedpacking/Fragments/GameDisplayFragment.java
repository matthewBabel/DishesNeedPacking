package garlicquasar.babel.matt.dishesneedpacking.Fragments;


import garlicquasar.babel.matt.dishesneedpacking.Acitivities.MainActivity;
import garlicquasar.babel.matt.dishesneedpacking.Audio.MusicServiceHandler;
import garlicquasar.babel.matt.dishesneedpacking.Game.View.DishWasherPackerView;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Holds the Game View and game loop timer, once game is finished goes to final score screen and passes score data.
 */
public class GameDisplayFragment extends Fragment {

    private int columnInput;
    private int floorInput;
    private int tupperInput;
    private int bowlInput;
    private double cupInput;
    private int utensilCorner;
    private int utensilDirection;
    private int utensilAmount;
    private int level;
    private int plateSubtractions;
    private int runs;
    private boolean fromGameInput;

    private DishWasherPackerView gameView;

    private Handler handler = new Handler();
    private final static long Interval = 30;
    private static Timer timer;


    public GameDisplayFragment(int col, int floor, int tupper, int bowl, double cup, int corner, int direction, int amt, int level, int plateSubtractions, int runs, boolean fromGameInput) {
        columnInput = col;
        floorInput = floor;
        tupperInput = tupper;
        bowlInput = bowl;
        cupInput = cup;
        utensilCorner = corner;
        utensilDirection = direction;
        utensilAmount = amt;
        this.level = level;
        this.runs = runs;
        this.plateSubtractions = plateSubtractions;
        this.fromGameInput = fromGameInput;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MusicServiceHandler.pauseMenuAudio();
        MusicServiceHandler.startGameAudio();

        gameView = new DishWasherPackerView(getActivity(), columnInput,
                floorInput, tupperInput, bowlInput, cupInput, utensilCorner,
                utensilDirection, utensilAmount, level, plateSubtractions, runs, fromGameInput);

        return gameView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (gameView.isGameOver()) {
                    displayScore();
                }

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        gameView.invalidate();
                    }
                });
            }
        }, 0, Interval);
    }

    private void displayScore() {
        timer.cancel();
        MusicServiceHandler.stopGameAudio();
        MusicServiceHandler.startMenuAudio();


        FinalScoreFragment fragment = new FinalScoreFragment(gameView.gameData.readableTime,
                                                            gameView.gameData.totalDishes, gameView.gameData.puzzleDimensions,gameView.gameData.dishesPickedUp,
                gameView.gameData.nextDishes, gameView.gameData.runs, gameView.gameData.RUNSNEEDED, gameView.gameData.dishesPerSecond,
                gameView.gameData.takenZones, gameView.gameData.score, gameView.gameData.level);


        MainActivity.startFragment(fragment, "Final Score");
    }

    public static void stopTimer() {
        timer.cancel();
    }
}
