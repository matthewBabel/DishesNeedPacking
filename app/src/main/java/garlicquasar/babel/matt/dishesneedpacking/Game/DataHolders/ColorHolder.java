package garlicquasar.babel.matt.dishesneedpacking.Game.DataHolders;

import androidx.core.content.ContextCompat;

import garlicquasar.babel.matt.dishesneedpacking.Game.View.DishWasherPackerView;
import garlicquasar.babel.matt.dishesneedpacking.R;

public class ColorHolder {
    public int getMainBackgroundColor() {
        return ContextCompat.getColor(DishWasherPackerView.context, R.color.background);
    }
}
