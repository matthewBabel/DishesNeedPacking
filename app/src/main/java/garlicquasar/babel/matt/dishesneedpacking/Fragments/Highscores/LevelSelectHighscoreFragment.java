package garlicquasar.babel.matt.dishesneedpacking.Fragments.Highscores;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import garlicquasar.babel.matt.dishesneedpacking.Audio.SoundEffectPlayer;
import garlicquasar.babel.matt.dishesneedpacking.Fragments.LevelSelectFragment;
import garlicquasar.babel.matt.dishesneedpacking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LevelSelectHighscoreFragment extends Fragment {

    private Button[] levelBtns;
    private ImageButton leftArrow;
    private ImageButton rightArrow;

    public static int levelSelected = 1;
    public static Button pressedButton = null;

    public LevelSelectHighscoreFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_level_highscore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn1 = view.findViewById(R.id.levelScoreSelectBtn1);
        Button btn2 = view.findViewById(R.id.levelScoreSelectBtn2);
        Button btn3 = view.findViewById(R.id.levelScoreSelectBtn3);
        Button btn4 = view.findViewById(R.id.levelScoreSelectBtn4);
        Button btn5 = view.findViewById(R.id.levelScoreSelectBtn5);
        Button btn6 = view.findViewById(R.id.levelScoreSelectBtn6);
        Button btn7 = view.findViewById(R.id.levelScoreSelectBtn7);
        Button btn8 = view.findViewById(R.id.levelScoreSelectBtn8);
        Button btn9 = view.findViewById(R.id.levelScoreSelectBtn9);
        Button btn10 = view.findViewById(R.id.levelScoreSelectBtn10);
        Button btn11 = view.findViewById(R.id.levelScoreSelectBtn11);
        Button btn12 = view.findViewById(R.id.levelScoreSelectBtn12);

        levelBtns = new Button[] {btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12};

        for (Button btn : levelBtns) {
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                  levelSelected((Button)v);
                }
            });
        }

        leftArrow = view.findViewById(R.id.scoreTabLeftArrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moveLevelScoresLeft(v);
            }
        });
        leftArrow.setVisibility(View.INVISIBLE);

        rightArrow = view.findViewById(R.id.scoreTabRightArrow);
        rightArrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moveLevelScoresRight(v);
            }
        });

        resetFragment(view, btn1);
    }

    private void resetFragment(@NonNull View view, Button btn1) {
        if (pressedButton == null) {
            pressedButton = btn1;
        } else {
            while (!findPressedButton()) {
                moveLevelScoresRight(view);
            }
        }
        setPressedButton();
    }

    private boolean findPressedButton() {
        for (Button btn : levelBtns) {
            if (Integer.valueOf(String.valueOf(btn.getText())) == levelSelected) {
                pressedButton = btn;
                setPressedButton();
                return true;
            }
        }

        return false;
    }

    private void levelSelected(Button btn)  {
        SoundEffectPlayer.playClick(getActivity());
        levelSelected = Integer.valueOf(String.valueOf(btn.getText()));
        resetPressedButton();
        pressedButton = btn;
        setPressedButton();
    }

    private void resetPressedButton() {
        pressedButton.setSelected(false);

    }

    private void setPressedButton() {
        pressedButton.setSelected(true);
    }

    private void moveLevelScoresRight(View view) {
        SoundEffectPlayer.playClick(getActivity());
        resetPressedButton();

        boolean pass = true;
        for (Button btn : levelBtns) {
            pass = moveLevelsUp(btn, 12);

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
    }

    private void moveLevelScoresLeft(View view) {
        SoundEffectPlayer.playClick(getActivity());
        resetPressedButton();

        for (Button btn : levelBtns) {
            moveLevelsDown(btn, 12);
            btn.setVisibility(View.VISIBLE);
        }

        rightArrow.setVisibility(View.VISIBLE);
        if (levelBtns[0].getText().equals("1")) {
            leftArrow.setVisibility(View.INVISIBLE);
        }

    }

    private boolean moveLevelsUp(Button btn, int increment) {
        int num = Integer.valueOf(String.valueOf(btn.getText()));
        btn.setText(""+(num+increment));

        if (num+increment == levelSelected) {
            pressedButton = btn;
            setPressedButton();
        }

        return !(num + increment > LevelSelectFragment.MAX_LEVELS);
    }

    private boolean moveLevelsDown(Button btn, int decrement) {
        int num = Integer.valueOf(String.valueOf(btn.getText()));
        btn.setText(""+(num-decrement));

        if (num-decrement == levelSelected) {
            pressedButton = btn;
            setPressedButton();
        }

        return !(num - decrement <= 0);
    }

}
