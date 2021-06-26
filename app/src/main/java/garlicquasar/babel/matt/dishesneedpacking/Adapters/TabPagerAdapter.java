package garlicquasar.babel.matt.dishesneedpacking.Adapters;

import garlicquasar.babel.matt.dishesneedpacking.Fragments.Highscores.GlobalHighscoreFragment;
import garlicquasar.babel.matt.dishesneedpacking.Fragments.Highscores.LevelSelectHighscoreFragment;
import garlicquasar.babel.matt.dishesneedpacking.Fragments.Highscores.LocalHighscoreFragment;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * A bridge between UI component and data source that helps us to fill data in UI component
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public TabPagerAdapter(FragmentManager fm,  Context context) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new LevelSelectHighscoreFragment();
            case 1:
                Log.d(null, "Level: " + LevelSelectHighscoreFragment.levelSelected);
                return new LocalHighscoreFragment(LevelSelectHighscoreFragment.levelSelected, context);
            case 2:
                return new GlobalHighscoreFragment(LevelSelectHighscoreFragment.levelSelected, context);
            default:
                return new LevelSelectHighscoreFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
