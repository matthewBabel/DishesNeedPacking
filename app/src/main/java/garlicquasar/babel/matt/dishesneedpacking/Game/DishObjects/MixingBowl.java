package garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects;

import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.TouchZone;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.UtensilZone;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

/**
 * Mixing bowl has to be placed over 4 prongs, it takes up 9 touch zones,
 * when placed next to another bowl the new bowl takes up 6 touch zones.
 * Cups placed next to mixing bowls take up less touch zones than placed alone,
 * so it is efficient to place cups around mixing bowls.
 * For example a cup placed in the wedge of 3 mixing bowls takes up 0 touchzones: O O
 *                                                                                O cup
 */
public class MixingBowl extends Dishware {

    public Paint paint;

    private int width;
    private int height;

    private Point gamePt;
    private Point dishPt;

    private Rect dishRect;
    private Rect gameRect;
    private Bitmap image;

    //left, top, right, bottom
    private int direction;
    private int[] degrees = {180, 90, 0, 270};

    public List<Integer> takenBowlZoneIndexes;
    public List<Integer> takenBowlEdgeZoneIndexes;
    public List<Integer> takenBowlCornerZoneIndexes;

    private final int ID;

    public MixingBowl(String name, Bitmap img, int dir, int id) {
        paint = new Paint();
        paint.setTypeface(Typeface.DEFAULT);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        this.name = name;
        this.image = img;
        this.ID = id;

        direction = dir;
        takenBowlZoneIndexes = new ArrayList<>();
        takenBowlEdgeZoneIndexes = new ArrayList<>();
        takenBowlCornerZoneIndexes = new ArrayList<>();
    }

    @Override
    public void drawDish(Canvas canvas) {
        canvas.save();
        canvas.rotate(degrees[direction], dishPt.x, dishPt.y);
        canvas.drawBitmap(image, null, dishRect, paint);
        canvas.restore();
    }

    @Override
    public void drawGame(Canvas canvas) {
        canvas.save();
        canvas.rotate(degrees[direction], gamePt.x, gamePt.y);
        canvas.drawBitmap(image, null, gameRect, paint);
        canvas.restore();
    }


    @Override
    public void setDish(int[] values) {
        Rect bounds = new Rect(values[0], values[1], values[2], values[3]);
        width = values[4];
        height = values[5];
        int centerX = bounds.centerX();
        int centerY = bounds.centerY();

        dishRect = new Rect(centerX - (width / 2), centerY - (height / 2),
                centerX + (width / 2), centerY + (height / 2));

        dishPt = new Point(centerX, centerY);
    }


    @Override
    public void setGame(int[] values) {
        gamePt = new Point(values[0], values[1]);
        gameRect = new Rect(gamePt.x - (width / 2), gamePt.y - (height / 2), gamePt.x + (width / 2), gamePt.y + (height / 2));
    }

    @Override
    public void rotate() {
        switch (direction) {
            case 0:
                direction = 1;
                break;
            case 1:
                direction = 2;
                break;
            case 2:
                direction = 3;
                break;
            case 3:
            default:
                direction = 0;
                break;
        }
    }

    @Override
    public boolean contains(int x, int y) {
        return gameRect.contains(x, y);
    }

    @Override
    public void remove(List<TouchZone> plateZones, List<TouchZone> cupZones, List<UtensilZone> utensilZones) {
        remove(plateZones);
    }

    public void remove(List<TouchZone> plateZones) {
        for (int n : takenBowlZoneIndexes) {
            plateZones.get(n).removeBowlID(ID);

            plateZones.get(n).setTakenByCup(false);
            plateZones.get(n).setTaken(false);
            plateZones.get(n).setTakenByBowl(false);
            plateZones.get(n).setTakenByBowlCenter(false);
        }

        for (int n : takenBowlEdgeZoneIndexes) {
            plateZones.get(n).removeBowlID(ID);
            plateZones.get(n).setTakenByBowl(false);
        }

        for (int n : takenBowlCornerZoneIndexes) {
            plateZones.get(n).removeBowlID(ID);
            plateZones.get(n).setTakenByBowlCorner(false);
        }
    }


    public int getDirection() {
        return direction;
    }

    public int getId() {
        return ID;
    }
}
