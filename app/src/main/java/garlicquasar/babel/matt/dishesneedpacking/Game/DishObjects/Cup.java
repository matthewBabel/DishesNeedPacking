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
 * A cup if different from other pieces because it is placed on cup Touch Zones, instead of plate Touch Zones.
 * This means it is placed over the intercepts AKA prongs instead of between the prongs like all other pieces.
 * A cup can only be placed next to other cups, all other pieces can be placed within the touch zone (besides bowl).
 */
public class Cup extends Dishware {

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

    public List<Integer> takenCupZoneIndexes;

    private final int ID;

    public Cup(String name, Bitmap img, int dir, int id) {
        paint = new Paint();
        paint.setTypeface(Typeface.DEFAULT);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        this.name = name;
        this.image = img;
        this.ID = id;

        direction = dir;
        takenCupZoneIndexes = new ArrayList<>();
    }

    public void drawDish(Canvas canvas) {
        canvas.save();
        canvas.rotate(degrees[direction], dishPt.x, dishPt.y);
        canvas.drawBitmap(image, null, dishRect, paint);
        canvas.restore();
    }

    public void drawGame(Canvas canvas) {
        canvas.save();
        canvas.rotate(degrees[direction], gamePt.x, gamePt.y);
        canvas.drawBitmap(image, null, gameRect, paint);
        canvas.restore();
    }

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

    public void setGame(int[] values) {
        gamePt = new Point(values[0], values[1]);
        gameRect = new Rect(gamePt.x - (width / 2), gamePt.y - (height / 2), gamePt.x + (width / 2), gamePt.y + (height / 2));
    }

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
        remove(plateZones, cupZones);
    }

    public void remove(List<TouchZone> plateZones, List<TouchZone> cupZones) {
        for (int n : takenCupZoneIndexes) {
            TouchZone cupZone = cupZones.get(n);

            int left = cupZone.getLeftNeighbor();
            int up = cupZone.getTopNeighbor();
            int right = cupZone.getRightNeighbor();
            int down = cupZone.getBottomNeighbor();

            if (left != -1) {
                plateZones.get(left).removeCupID(ID);
                plateZones.get(left).setTakenByCup(false);
            }

            if (up != -1) {
                plateZones.get(up).removeCupID(ID);
                plateZones.get(up).setTakenByCup(false);
            }

            if (right != -1) {
                plateZones.get(right).removeCupID(ID);
                plateZones.get(right).setTakenByCup(false);
            }

            if (down != -1) {
                plateZones.get(down).removeCupID(ID);
                plateZones.get(down).setTakenByCup(false);
            }

            cupZone.setTaken(false);
        }

    }

    public int getDirection() {
        return direction;
    }

    public int getId() {
        return ID;
    }
}
