package garlicquasar.babel.matt.dishesneedpacking.Fragments.Highscores;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;

import garlicquasar.babel.matt.dishesneedpacking.HttpRequest.HttpHandler;
import garlicquasar.babel.matt.dishesneedpacking.Adapters.HighscoreListAdapter;
import garlicquasar.babel.matt.dishesneedpacking.Objects.LevelHighscore;
import garlicquasar.babel.matt.dishesneedpacking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GlobalHighscoreFragment extends Fragment {

    int highscoreLevel;
    Context context;

    public GlobalHighscoreFragment(int position, Context context) {
        highscoreLevel = position;
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_global_highscore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView mListView = getView().findViewById(R.id.listViewGlobal);

        View header = getLayoutInflater().inflate(R.layout.highscore_header, mListView, false);
        mListView.addHeaderView(header);
        setListViewAdapter(mListView);
    }

    private void setListViewAdapter(ListView lv) {
        ArrayList<LevelHighscore> highscores = new ArrayList<>();

        Collections.addAll(highscores, HttpHandler.globalLevelHighscores);
        HighscoreListAdapter adapter = new HighscoreListAdapter(context, R.layout.adapter_view_layout, highscores, true);
        lv.setAdapter(adapter);
    }
}
