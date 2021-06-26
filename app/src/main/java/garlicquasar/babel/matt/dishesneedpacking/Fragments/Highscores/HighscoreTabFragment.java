package garlicquasar.babel.matt.dishesneedpacking.Fragments.Highscores;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.material.tabs.TabLayout;

import garlicquasar.babel.matt.dishesneedpacking.Acitivities.MainActivity;
import garlicquasar.babel.matt.dishesneedpacking.HttpRequest.HttpHandler;
import garlicquasar.babel.matt.dishesneedpacking.ViewPager.CustomViewPager;
import garlicquasar.babel.matt.dishesneedpacking.Adapters.TabPagerAdapter;
import garlicquasar.babel.matt.dishesneedpacking.Audio.SoundEffectPlayer;
import garlicquasar.babel.matt.dishesneedpacking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HighscoreTabFragment extends Fragment {

    boolean loggedIn;
    public static ViewGroup layout;

    public HighscoreTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_tabbed_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout = getActivity().findViewById(R.id.highscore_tabs);
        MainActivity.addBubblesToFragment("Highscore Tab");


        SharedPreferences preferences = getActivity().getSharedPreferences(getActivity().getString(R.string.preference_file_key), getActivity().MODE_PRIVATE);
        loggedIn = preferences.getString("loggedIn", "").equals("true");



        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#000000"));

        final CustomViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setPagingEnabled(false);

        PagerAdapter pagerAdapter = new TabPagerAdapter(getActivity().getSupportFragmentManager(), getActivity());
        viewPager.setAdapter(pagerAdapter);

        ImageButton backBtn = view.findViewById(R.id.highscoreTabsBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        tabLayout.addOnTabSelectedListener(new com.google.android.material.tabs.TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1 && loggedIn) {
                    while(!HttpHandler.localScoresUpdated) {
                        Log.d(null, "updating scores..");
                    }
                } else if (tab.getPosition() == 2) {
                    while(!HttpHandler.globalScoresUpdated) {
                        Log.d(null, "updating scores..");
                    }
                }

                viewPager.getAdapter().notifyDataSetChanged();
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    HttpHandler api = new HttpHandler();
                    api.updateLevelGlobalHighscoreData(LevelSelectHighscoreFragment.levelSelected);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void back() {
        SoundEffectPlayer.playBackButton(getActivity());
        MainActivity.removeBubblesFromFragment("Highscore Tab");
        MainActivity.backToMain(false);
    }
}
