package garlicquasar.babel.matt.dishesneedpacking.Fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import garlicquasar.babel.matt.dishesneedpacking.Acitivities.MainActivity;
import garlicquasar.babel.matt.dishesneedpacking.Audio.SoundEffectPlayer;
import garlicquasar.babel.matt.dishesneedpacking.Database.DBManager;
import garlicquasar.babel.matt.dishesneedpacking.R;

/**
 * User can select level here and displays level bronze, silver, or gold medals
 */
public class LevelSelectFragment extends Fragment {

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;

    Button[] levelBtns;
    ImageView[][] levelMedals;

    ImageButton leftArrow;
    ImageButton rightArrow;
    ImageButton backBtn;

    private int displayLevel;

    public static final int MAX_LEVELS = 100;

    public LevelSelectFragment(int displayLevel) {
        this.displayLevel = displayLevel;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_level_select, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn1 = view.findViewById(R.id.levelBtn1);
        btn2 = view.findViewById(R.id.levelBtn2);
        btn3 = view.findViewById(R.id.levelBtn3);
        btn4 = view.findViewById(R.id.levelBtn4);
        btn5 = view.findViewById(R.id.levelBtn5);
        btn6 = view.findViewById(R.id.levelBtn6);
        btn7 = view.findViewById(R.id.levelBtn7);
        btn8 = view.findViewById(R.id.levelBtn8);
        btn9 = view.findViewById(R.id.levelBtn9);

        backBtn = view.findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        levelBtns = new Button[] {btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9};

        for(Button btn : levelBtns) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    levelSelect(v);
                }
            });
        }

        levelMedals = new ImageView[][] {{view.findViewById(R.id.level1BronzeImg), view.findViewById(R.id.level1SilverImg), view.findViewById(R.id.level1GoldImg)},
                {view.findViewById(R.id.level2BronzeImg), view.findViewById(R.id.level2SilverImg), view.findViewById(R.id.level2GoldImg)},
                {view.findViewById(R.id.level3BronzeImg), view.findViewById(R.id.level3SilverImg), view.findViewById(R.id.level3GoldImg)},
                {view.findViewById(R.id.level4BronzeImg), view.findViewById(R.id.level4SilverImg), view.findViewById(R.id.level4GoldImg)},
                {view.findViewById(R.id.level5BronzeImg), view.findViewById(R.id.level5SilverImg), view.findViewById(R.id.level5GoldImg)},
                {view.findViewById(R.id.level6BronzeImg), view.findViewById(R.id.level6SilverImg), view.findViewById(R.id.level6GoldImg)},
                {view.findViewById(R.id.level7BronzeImg), view.findViewById(R.id.level7SilverImg), view.findViewById(R.id.level7GoldImg)},
                {view.findViewById(R.id.level8BronzeImg), view.findViewById(R.id.level8SilverImg), view.findViewById(R.id.level8GoldImg)},
                {view.findViewById(R.id.level9BronzeImg), view.findViewById(R.id.level9SilverImg), view.findViewById(R.id.level9GoldImg)}};

        leftArrow = view.findViewById(R.id.leftArrowBtn);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLevelsLeft();
            }
        });

        rightArrow = view.findViewById(R.id.rightArrowBtn);
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLevelsRight();
            }
        });

        leftArrow.setVisibility(View.INVISIBLE);

        while (Integer.valueOf(String.valueOf(btn9.getText())) < displayLevel) {
            moveLevelsRight();
        }

        validateLevels();
    }


    private void back() {
        SoundEffectPlayer.playBackButton(getActivity());
        MainActivity.backToMain(true);
    }

    private void validateLevels() {
        DBManager db = new DBManager(getActivity());
        db.open();

        int count = 0;
        boolean invalidateRest = false;
        for (Button btn : levelBtns) {

            if (invalidateRest) {
                btn.setEnabled(false);
                levelMedals[count][0].setVisibility(View.INVISIBLE);
                levelMedals[count][1].setVisibility(View.INVISIBLE);
                levelMedals[count][2].setVisibility(View.INVISIBLE);
            } else {
                btn.setEnabled(true);

                int buttonLvl = Integer.valueOf(String.valueOf(btn.getText()));
                Cursor cursor = db.fetchByLevel(buttonLvl);
                if (cursor.getCount() == 0) {
                    levelMedals[count][0].setVisibility(View.INVISIBLE);
                    levelMedals[count][1].setVisibility(View.INVISIBLE);
                    levelMedals[count][2].setVisibility(View.INVISIBLE);

                    invalidateRest = true;

                    if (count == 0 && buttonLvl != 1) {
                       Cursor cursorBefore = db.fetchByLevel((buttonLvl-1));

                       if (cursorBefore.getCount() != 0 && cursorBefore.getInt(cursorBefore.getColumnIndex("score")) > LevelDetailFragment.scores[buttonLvl - 2][0]) {
                            btn.setEnabled(true);
                       } else {
                           btn.setEnabled(false);
                       }
                    }
                } else {
                    if (cursor.moveToFirst()) {
                        int highscore = cursor.getInt(cursor.getColumnIndex("score"));

                        if (highscore < LevelDetailFragment.scores[buttonLvl - 1][0]) {
                            levelMedals[count][0].setVisibility(View.INVISIBLE);
                            levelMedals[count][1].setVisibility(View.INVISIBLE);
                            levelMedals[count][2].setVisibility(View.INVISIBLE);

                            invalidateRest = true;
                        } else if (highscore < LevelDetailFragment.scores[buttonLvl - 1][1]) {
                            levelMedals[count][0].setVisibility(View.VISIBLE);
                            levelMedals[count][1].setVisibility(View.INVISIBLE);
                            levelMedals[count][2].setVisibility(View.INVISIBLE);
                        } else if (highscore < LevelDetailFragment.scores[buttonLvl - 1][2]) {
                            levelMedals[count][0].setVisibility(View.VISIBLE);
                            levelMedals[count][1].setVisibility(View.VISIBLE);
                            levelMedals[count][2].setVisibility(View.INVISIBLE);
                        } else {
                            levelMedals[count][0].setVisibility(View.VISIBLE);
                            levelMedals[count][1].setVisibility(View.VISIBLE);
                            levelMedals[count][2].setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            count++;
        }

        db.close();
    }

    public void moveLevelsRight() {
        SoundEffectPlayer.playClick(getActivity());
        boolean pass = true;
        for (Button btn : levelBtns) {
            pass = moveLevelsUp(btn, 9);

            if (!pass) {
                btn.setVisibility(View.INVISIBLE);
            } else {
                btn.setVisibility(View.VISIBLE);
            }
        }

        leftArrow.setVisibility(View.VISIBLE);
        if (!pass) {
            rightArrow.setVisibility(View.INVISIBLE);
        }

        validateLevels();
    }

    public void moveLevelsLeft() {
        SoundEffectPlayer.playClick(getActivity());
        for (Button btn : levelBtns) {
            moveLevelsDown(btn, 9);
            btn.setVisibility(View.VISIBLE);
        }

        rightArrow.setVisibility(View.VISIBLE);
        if (levelBtns[0].getText().equals("1")) {
            leftArrow.setVisibility(View.INVISIBLE);
        }

        validateLevels();
    }

    private boolean moveLevelsUp(Button btn, int increment) {
        int num = Integer.valueOf(String.valueOf(btn.getText()));
        btn.setText(""+(num+increment));

        return !(num + increment > MAX_LEVELS);
    }

    private boolean moveLevelsDown(Button btn, int decrement) {
        int num = Integer.valueOf(String.valueOf(btn.getText()));
        btn.setText(""+(num-decrement));

        return !(num - decrement <= 0);
    }

    public void levelSelect(View view) {
        SoundEffectPlayer.playClick(getActivity());
        Button btn = view.findViewById(view.getId());
        LevelDetailFragment fragment = new LevelDetailFragment(Integer.valueOf(String.valueOf(btn.getText())));
        MainActivity.startFragment(fragment, "Level Detail");
    }
}
