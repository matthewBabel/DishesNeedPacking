package garlicquasar.babel.matt.dishesneedpacking.Fragments;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import garlicquasar.babel.matt.dishesneedpacking.Acitivities.MainActivity;
import garlicquasar.babel.matt.dishesneedpacking.HttpRequest.HttpHandler;
import garlicquasar.babel.matt.dishesneedpacking.Audio.SoundEffectPlayer;
import garlicquasar.babel.matt.dishesneedpacking.Database.DBManager;
import garlicquasar.babel.matt.dishesneedpacking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinalScoreFragment extends Fragment {

    private String time;
    private int dishes;
    private String puzzleSize;
    private int dishesPickedUp;
    private int nextDishes;
    private int runs;
    private int runsNeeded;
    private String dishesPerSec;
    private int takenZones;
    private int finalScore;
    private int level;

    private TextInputEditText usernameEditText;

    public FinalScoreFragment(String time, int dishes, String puzzleSize, int dishesPickedUp, int nextDishes, int runs, int runsNeeded, String dishesPerSec, int takenZones, int finalScore, int level) {
        this.time = time;
        this.dishes = dishes;
        this.puzzleSize = puzzleSize;
        this.dishesPickedUp = dishesPickedUp;
        this.nextDishes = nextDishes;
        this.runs = runs;
        this.runsNeeded = runsNeeded;
        this.dishesPerSec = dishesPerSec;
        this.takenZones = takenZones;
        this.finalScore = finalScore;
        this.level = level;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_final_score, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv1 = view.findViewById(R.id.dishesPerSecondTV);
        tv1.setText("Dishes Per Second - " + dishesPerSec);

        TextView tv2 = view.findViewById(R.id.timeTV);
        tv2.setText("Time - " + time);

        TextView tv3 = view.findViewById(R.id.dishesTV);
        tv3.setText("Total Dishes - " + dishes);

        TextView tv4 = view.findViewById(R.id.totalSpacesTV);
        tv4.setText("Puzzle Size - " + puzzleSize);

        TextView tv5 = view.findViewById(R.id.takenZonesTV);
        tv5.setText("Pressed a Taken Zone - " + takenZones);

        TextView tv6 = view.findViewById(R.id.nextDishesTV);
        tv6.setText("Next Dishes - " + nextDishes);

        TextView tv7 = view.findViewById(R.id.dishePickedUpTV);
        tv7.setText("Dishes Picked Up - " + dishesPickedUp);

        TextView tv8 = view.findViewById(R.id.runsTV);
        tv8.setText("Had " + runs + ((runs == 1) ? " Wash : " : " Washes : ") + runsNeeded + ((runsNeeded == 1) ? " Wash Needed" : " Washes Needed"));

        TextView tv9 = view.findViewById(R.id.finalScoreTV);
        tv9.setText("Final Score - " + finalScore);

        TextView tv10 = view.findViewById(R.id.finalScoreTitle);
        tv10.setText(level == 0 ? "Stats for Custom Game" : "Stats for Level " + level);

        TextView tv11 = view.findViewById(R.id.mishapsTV);
        tv11.setText("Score Subtractions");

        ImageView bronzeBubble = view.findViewById(R.id.bubbleBronze);
        ImageView silverBubble = view.findViewById(R.id.silverBubble);
        ImageView goldBubble = view.findViewById(R.id.goldBubble);

        if (level == 0) {
            bronzeBubble.setAlpha(0f);
            silverBubble.setAlpha(0f);
            goldBubble.setAlpha(0f);
        }
        else if (finalScore < LevelDetailFragment.scores[level-1][0]) {
            greyOut(bronzeBubble);
            greyOut(silverBubble);
            greyOut(goldBubble);
        } else if (finalScore < LevelDetailFragment.scores[level-1][1]) {
            greyOut(silverBubble);
            greyOut(goldBubble);
        } else if (finalScore < LevelDetailFragment.scores[level-1][2]) {
            greyOut(goldBubble);
        }

        usernameEditText = view.findViewById(R.id.finalScoreUsernameET);
        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), getActivity().MODE_PRIVATE);
        usernameEditText.setText(preferences.getString("username", ""));

        Button nextLevelBtn = view.findViewById(R.id.nextLevelBtn);
        Button retryBtn = view.findViewById(R.id.retryBtn);
        if (scoredBronzeOrHigher()) {
            nextLevelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextLevel();
                }
            });
            retryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retry();
                }
            });
        } else {
            nextLevelBtn.setVisibility(View.INVISIBLE);
            retryBtn.setVisibility(View.INVISIBLE);

            Button retryCenteredBtn = view.findViewById(R.id.retryCenteredBtn);
            retryCenteredBtn.setVisibility(View.VISIBLE);
            retryCenteredBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    retry();
                }
            });
        }

        ImageButton backBtn = view.findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        if(preferences.getString("loggedIn", "").equals("false")) {
            MainActivity.makeToast(getActivity(), "Sign In to Save Score", 2000);
        }
    }

    private void goBack() {
        SoundEffectPlayer.playClick(getActivity());

        if (usernamePass()) {
            enterData();
            MainActivity.backToMain(true);
        }
    }

    private void nextLevel() {
        SoundEffectPlayer.playClick(getActivity());

        if (usernamePass()) {
            enterData();
            MainActivity.goToLevelDetailScreen(level+1);
        }
    }

    private void retry() {
        SoundEffectPlayer.playClick(getActivity());

        // custom game
        if (level == 0) {
            MainActivity.goToGameDisplayScreenFromCustom(GameInputFragment.columnInput,
                    GameInputFragment.floorInput, GameInputFragment.tupperInput, GameInputFragment.bowlInput,
                    GameInputFragment.cupInput, GameInputFragment.cornerInt, GameInputFragment.directionInt,
                    GameInputFragment.utensilAmount);
            return;
        }

        if (usernamePass()) {
            enterData();
            MainActivity.goToGameDisplayScreen(level);
        }
    }

    private boolean scoredBronzeOrHigher() {
        if (level == 0) {
            return false;
        }

        if (finalScore > LevelDetailFragment.scores[level-1][0]) {
            return true;
        } else {
            DBManager db = new DBManager(getActivity());
            db.open();
            Cursor cursor = db.fetchByLevel(level);
            if (cursor.getCount() != 0 && cursor.getInt(cursor.getColumnIndex("score")) > LevelDetailFragment.scores[level - 1][0]) {
                db.close();
                return true;
            } else {
                db.close();
                return false;
            }
        }
    }

    private boolean usernamePass() {
        String username = usernameEditText.getText().toString();
        boolean pass = MainActivity.isProfanity(username);
        if (!pass) {
            MainActivity.makeToast(getActivity(), "No Profanity Please", 4000);
            return false;
        } else if (username.length() > 30) {
            MainActivity.makeToast(getActivity(), "Username Too Long", 4000);
            return false;
        }
        return true;
    }

    private void enterData() {
        String username = usernameEditText.getText().toString();

        if (level == 0) {
            MainActivity.makeToast(getActivity(), "Custom Game Score is not Saved", 4000);
        } else {
            String date = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(new Date());

            SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), getActivity().MODE_PRIVATE);
            if (preferences.getString("loggedIn", "").equals("true")) {

                // inserting new score into local db anyway in case api call hasn't finished yet
                // api.updateUserHighscoreData will rewrite data once finished
                DBManager db = new DBManager(getActivity());
                db.open();
                db.insert(usernameEditText.getText().toString(), date, level, finalScore);
                db.close();

                HttpHandler api = new HttpHandler();
                api.createHighscore(getActivity(), username, String.valueOf(finalScore), String.valueOf(level), date);
                api.updateUserHighscoreData(getActivity());
                api.updateGlobalHighscoreData();


            } else {
                // if not logged in write into db to track scores
                // but once logged in these scores will be overridden forever
                DBManager db = new DBManager(getActivity());
                db.open();
                db.insert(usernameEditText.getText().toString(), date, level, finalScore);
                db.close();
            }
        }
    }


    private void greyOut(ImageView imageView) {
        final ColorMatrix grayscaleMatrix = new ColorMatrix();
        grayscaleMatrix.setSaturation(0);
        imageView.setAlpha(0.5f);
        imageView.setColorFilter(new ColorMatrixColorFilter(grayscaleMatrix));
    }
}

