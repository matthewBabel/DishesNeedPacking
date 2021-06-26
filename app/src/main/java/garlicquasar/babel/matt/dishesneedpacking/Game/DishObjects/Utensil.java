package garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects;

import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.Map;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.TouchZone;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.UtensilZone;
import garlicquasar.babel.matt.dishesneedpacking.Game.View.DishWasherPackerView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;

import java.util.List;
import java.util.Random;

/**
 * A utensil can only be placed in utensil zones, when setGame is called it randomly rotates itself.
 */
public abstract class Utensil extends Dishware {

    public Point point;
    public Bitmap image;
    public RectF dishBoundingBox;
    public RectF gameBoundingBox;
    public Paint paint;

    public int takenUtensilZoneIndex;

    private int randomRotation = 0;

    public Utensil(String name, Bitmap img) {
        this.name = name;
        this.image = img;

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setAntiAlias(true);
    }

    public void drawGame(Canvas canvas) {
        canvas.save();
        canvas.rotate(randomRotation, point.x, point.y);
        canvas.drawBitmap(image, null, gameBoundingBox, paint);
        canvas.restore();
    }

    public void drawDish(Canvas canvas) {
        canvas.drawBitmap(image, null, dishBoundingBox, paint);
    }

    public void setGame(int[] values) {
        point = new Point(values[0], values[1]);
        float width = dishBoundingBox.width();
        float height = dishBoundingBox.height();

        gameBoundingBox = new RectF(point.x - (width/2), point.y - (height/2),
                point.x + (width/2), point.y + (height/2));

        randomRotation = new Random().nextInt(360); // [0, 359]
    }

    public void setDish(int[] values) {
        dishBoundingBox = new RectF(values[0], values[1], values[2], values[3]);
    }

    public void rotate() {
        // not used
    }

    @Override
    public void remove(List<TouchZone> plateZones, List<TouchZone> cupZones, List<UtensilZone> utensilZones) {
        remove(utensilZones);
    }

    public void remove(List<UtensilZone> utensilZones) {
            UtensilZone zone = utensilZones.get(takenUtensilZoneIndex);
            zone.removeRandomUtensil();
    }

    @Override
    public boolean contains(int x, int y) {
        List<UtensilZone> zones = DishWasherPackerView.rackLayouts[Map.curFloor].utensilTouchZones;
        TouchZone zone = zones.get(takenUtensilZoneIndex);
        return zone.getZone().contains(x, y);
    }
}
