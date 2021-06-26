package garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects;

import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.TouchZone;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.UtensilZone;
import android.graphics.Canvas;

import java.util.List;

/**
 * abstract class of all dish pieces, holds a name and horizontal value.
 */
public abstract class Dishware {
    public String name;
    public boolean horizontal;

    public abstract void drawGame(Canvas canvas);
    public abstract void drawDish(Canvas canvas);
    public abstract void setGame(int[] values);
    public abstract void setDish(int[] values);
    public abstract void rotate();

    public abstract boolean contains(int x, int y);
    public abstract void remove(List<TouchZone> plateZones, List<TouchZone> cupZones, List<UtensilZone> utensilZones);
}
