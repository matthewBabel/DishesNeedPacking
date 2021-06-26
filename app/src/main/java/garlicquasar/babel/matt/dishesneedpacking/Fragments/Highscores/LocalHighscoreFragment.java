package garlicquasar.babel.matt.dishesneedpacking.Fragments.Highscores;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import garlicquasar.babel.matt.dishesneedpacking.Adapters.HighscoreListAdapter;
import garlicquasar.babel.matt.dishesneedpacking.Database.DBManager;
import garlicquasar.babel.matt.dishesneedpacking.Objects.LevelHighscore;
import garlicquasar.babel.matt.dishesneedpacking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalHighscoreFragment extends Fragment {

    int highscoreLevel;
    Context context;

    public LocalHighscoreFragment(int position, Context context) {
        highscoreLevel = position;
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_highscore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView mListView = getView().findViewById(R.id.listView);
        DBManager db = new DBManager(context);

        View header = getLayoutInflater().inflate(R.layout.highscore_header, mListView, false);
        mListView.addHeaderView(header);
        setListViewAdapter(mListView, db);
    }

    private void setListViewAdapter(ListView lv, DBManager db) {
        db.open();

        Cursor mCursor = db.fetchByLevel(highscoreLevel);

        ArrayList<LevelHighscore> highscores = new ArrayList<>();

        int rankCount = 1;
        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            LevelHighscore scoreData = new LevelHighscore(mCursor.getString(mCursor.getColumnIndex("name")),
                    mCursor.getString(mCursor.getColumnIndex("date")),
                    mCursor.getInt(mCursor.getColumnIndex("score")),
                    rankCount, ""); // added default id value here because local highscore won't need to use userID data


            highscores.add(scoreData);
            rankCount++;
        }

        HighscoreListAdapter adapter = new HighscoreListAdapter(context, R.layout.adapter_view_layout, highscores, false);
        lv.setAdapter(adapter);
        db.close();
    }
}
