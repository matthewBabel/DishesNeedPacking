package garlicquasar.babel.matt.dishesneedpacking.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import garlicquasar.babel.matt.dishesneedpacking.Objects.LevelHighscore;
import garlicquasar.babel.matt.dishesneedpacking.R;

/**
 * A bridge between UI component and data source that helps us to fill data in UI component
 */
public class HighscoreListAdapter extends ArrayAdapter<LevelHighscore> {

    private static final String TAG = "HighscoreListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    // toggle between local scores and global scores
    private boolean mGlobal;

    // used in global scores to find which scores are the users
    private String userID;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView name;
        TextView date;
        TextView score;
        TextView rank;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public HighscoreListAdapter(Context context, int resource, ArrayList<LevelHighscore> objects, boolean global) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mGlobal = global;

        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), context.MODE_PRIVATE);
        userID = preferences.getString("userID", "def");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the users information
        String name = getItem(position).getName();
        String date = getItem(position).getDate();
        int score = getItem(position).getScore();
        int rank = getItem(position).getRank();
        String id = getItem(position).getUserID();

        //Create the highscore object with the information
        LevelHighscore levelHighscore = new LevelHighscore(name, date, score, rank, id);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new ViewHolder();
            holder.name =  convertView.findViewById(R.id.adapterItemName);
            holder.date =  convertView.findViewById(R.id.adapterItemDate);
            holder.score = convertView.findViewById(R.id.adapterItemScore);
            holder.rank = convertView.findViewById(R.id.adapterItemRank);



            result = convertView;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.name.setText(levelHighscore.getName());
        holder.date.setText(levelHighscore.getDate());
        holder.score.setText(levelHighscore.getScore()+"");
        holder.rank.setText(levelHighscore.getRank()+".");

        // offset color for every other score
        if (levelHighscore.getRank() % 2 == 0) {
            convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.background));
        } else {
            convertView.setBackgroundColor(Color.rgb(190, 197, 204));
        }

        // if global then for user's highscores make apparent by giving name a different color and making bold
        // need to reset holder to original design if not user's highscore in case it was users previously
        ViewHolder returnedViewHolder = (ViewHolder)convertView.getTag();
        if (mGlobal && !userID.isEmpty() && userID.equals(levelHighscore.getUserID())) {
            returnedViewHolder.name.setTypeface(null, Typeface.BOLD);
            returnedViewHolder.name.setTextColor(Color.rgb(11, 77, 140));
        } else {
            returnedViewHolder.name.setTypeface(null, Typeface.NORMAL);
            returnedViewHolder.name.setTextColor(Color.BLACK);
        }

        convertView.setTag(returnedViewHolder);

        return convertView;
    }
}
