package garlicquasar.babel.matt.dishesneedpacking.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import garlicquasar.babel.matt.dishesneedpacking.Acitivities.MainActivity;
import garlicquasar.babel.matt.dishesneedpacking.Audio.SoundEffectPlayer;
import garlicquasar.babel.matt.dishesneedpacking.R;

/**
 * Custom Game Fragment to choose your own game parameters
 */
public class GameInputFragment extends Fragment {

    Spinner columnSpinner;
    Spinner floorSpinner;
    Spinner tupperSpinner;
    Spinner bowlSpinner;
    Spinner cupSpinner;
    Spinner cornerSpinner;
    Spinner directionSpinner;
    Spinner utensilAmtSpinner;

    public static int columnInput;
    public static int floorInput;
    public static int tupperInput;
    public static int bowlInput;
    public static double cupInput;
    public static int cornerInt;
    public static int utensilAmount;
    public static int directionInt;


    public GameInputFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.activity_game_input, container, false);

        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        columnSpinner = (Spinner) view.findViewById(R.id.ColumnsSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.columns, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        columnSpinner.setAdapter(adapter);

        floorSpinner = (Spinner) view.findViewById(R.id.FloorSpinner);
        ArrayAdapter<CharSequence> floorAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.floors, R.layout.spinner_item);
        floorAdapter.setDropDownViewResource(R.layout.spinner_item);
        floorSpinner.setAdapter(floorAdapter);

        tupperSpinner = (Spinner) view.findViewById(R.id.TupperwareSpinner);
        ArrayAdapter<CharSequence> tupperAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.tupperware, R.layout.spinner_item);
        tupperAdapter.setDropDownViewResource(R.layout.spinner_item);
        tupperSpinner.setAdapter(tupperAdapter);

        bowlSpinner = (Spinner) view.findViewById(R.id.mixingBowlSpinner);
        ArrayAdapter<CharSequence> bowlAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.mixingbowl, R.layout.spinner_item);
        bowlAdapter.setDropDownViewResource(R.layout.spinner_item);
        bowlSpinner.setAdapter(bowlAdapter);

        cupSpinner = (Spinner) view.findViewById(R.id.CupSpinner);
        ArrayAdapter<CharSequence> cupAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.cupPercentage, R.layout.spinner_item);
        cupAdapter.setDropDownViewResource(R.layout.spinner_item);
        cupSpinner.setAdapter(cupAdapter);

        cornerSpinner = (Spinner) view.findViewById(R.id.UtensilCornerSpinner);
        ArrayAdapter<CharSequence> cornerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.utensilCorner, R.layout.spinner_item);
        cornerAdapter.setDropDownViewResource(R.layout.spinner_item);
        cornerSpinner.setAdapter(cornerAdapter);
        cornerSpinner.setSelection(3);

        directionSpinner = (Spinner) view.findViewById(R.id.UtensilOrientationSpinner);
        ArrayAdapter<CharSequence> directionAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.utensilDirection, R.layout.spinner_item);
        directionAdapter.setDropDownViewResource(R.layout.spinner_item);
        directionSpinner.setAdapter(directionAdapter);

        utensilAmtSpinner = (Spinner) view.findViewById(R.id.UtensilAmountSpinner);
        ArrayAdapter<CharSequence> amtAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.utensilAmount, R.layout.spinner_item);
        amtAdapter.setDropDownViewResource(R.layout.spinner_item);
        utensilAmtSpinner.setAdapter(amtAdapter);
        utensilAmtSpinner.setSelection(2);

        Button playBtn = view.findViewById(R.id.enterBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGame(v);
            }
        });

        ImageButton backBtn = view.findViewById(R.id.gameInputBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    public void playGame(View view) {
        columnInput = Integer.valueOf(columnSpinner.getSelectedItem().toString());
        floorInput = Integer.valueOf(floorSpinner.getSelectedItem().toString());
        tupperInput = Integer.valueOf(tupperSpinner.getSelectedItem().toString());
        bowlInput = Integer.valueOf(bowlSpinner.getSelectedItem().toString());
        cupInput = Double.valueOf(cupSpinner.getSelectedItem().toString().replace("%", ""));

        String corner = cornerSpinner.getSelectedItem().toString();

        // top-left, top-right, bottom-right, bottom-left,
        cornerInt = 0;
        switch (corner) {
            case "Top-Left":
                cornerInt = 0;
                break;
            case "Top-Right":
                cornerInt = 1;
                break;
            case "Bottom-Right":
                cornerInt = 2;
                break;
            case "Bottom-Left":
                cornerInt = 3;
                break;
            default:
                break;
        }

        directionInt = (directionSpinner.getSelectedItem().toString().equals("Horizontal")) ? 1 : 0;
        utensilAmount = Integer.valueOf(utensilAmtSpinner.getSelectedItem().toString());


        SoundEffectPlayer.playClick(getActivity());
        GameDisplayFragment fragment = new GameDisplayFragment(columnInput, floorInput, tupperInput, bowlInput, cupInput, cornerInt, directionInt, utensilAmount, 0, 0, 0, true);
        MainActivity.startFragment(fragment, "Game Display");
    }

    private void back() {
        SoundEffectPlayer.playBackButton(getActivity());
        MainActivity.backToMain(true);
    }
}
