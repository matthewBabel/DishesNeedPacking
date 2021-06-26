package garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects;

import garlicquasar.babel.matt.dishesneedpacking.Game.DataHolders.MarginHolder;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.TouchZone;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.UtensilZone;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

/**
 * Plates can be rotates, are always 1 space wide and spacesTaken spaces long.
 */
public abstract class Plate extends Dishware {
    public int spaces;
    public Paint paint;
    public int thickness;
    public List<Integer> takenPlateZoneIndexes;

    Rect gameRect; // rect in game screen
    private Rect horzRect;
    private Rect vertRect;
    private Path dishPaths[];
    int innerMargin;

    final int ID;
    // small part facing left, top, right, bottom
    public int direction = 0;

    private Path gamePath;

    public Rect gameRectSelection;



    public Plate(boolean isHorizontal, int spacesTaken, String name, int thickness, int[] argb, int id) {
        paint = new Paint();
        paint.setARGB(argb[0], argb[1], argb[2], argb[3]);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setAntiAlias(true);

        horizontal = isHorizontal;
        spaces = spacesTaken;
        this.name = name;
        this.thickness = thickness;
        ID = id;

        takenPlateZoneIndexes = new ArrayList<>();
    }

    public void drawGame(Canvas canvas) {
        canvas.drawPath(gamePath, paint);
    }

    public void drawDish(Canvas canvas) {
        canvas.drawPath(dishPaths[direction], paint);
    }

    public void setGame(int[] values) {
        int plateMargin = MarginHolder.plateSideMargin;
        plateMargin = (spaces == 1) ? 0 : plateMargin;

        if (horizontal) {
            gameRect = new Rect(values[0]+plateMargin, values[1], values[2]-plateMargin, values[3]);
        } else {
            gameRect = new Rect(values[0], values[1]+plateMargin, values[2], values[3]-plateMargin);
        }
        gamePath = new Path();

        if (horizontal) {
            if (direction == 1) { // top
                gamePath.moveTo(gameRect.left+innerMargin, gameRect.top);
                gamePath.lineTo(gameRect.right-innerMargin, gameRect.top);
                gamePath.lineTo(gameRect.right, gameRect.bottom);
                gamePath.lineTo(gameRect.left, gameRect.bottom);
                gamePath.lineTo(gameRect.left+innerMargin, gameRect.top);

            } else if (direction == 3) { // bottom
                gamePath.moveTo(gameRect.left, gameRect.top);
                gamePath.lineTo(gameRect.right, gameRect.top);
                gamePath.lineTo(gameRect.right-innerMargin, gameRect.bottom);
                gamePath.lineTo(gameRect.left+innerMargin, gameRect.bottom);
                gamePath.lineTo(gameRect.left, gameRect.top);
            }
        } else {
            if (direction == 0) { // left
                gamePath.moveTo(gameRect.left, gameRect.top+innerMargin);
                gamePath.lineTo(gameRect.right, gameRect.top);
                gamePath.lineTo(gameRect.right, gameRect.bottom);
                gamePath.lineTo(gameRect.left, gameRect.bottom-innerMargin);
                gamePath.lineTo(gameRect.left, gameRect.top+innerMargin);
            } else if (direction == 2) { // right
                gamePath.moveTo(gameRect.left, gameRect.top);
                gamePath.lineTo(gameRect.right, gameRect.top+innerMargin);
                gamePath.lineTo(gameRect.right, gameRect.bottom-innerMargin);
                gamePath.lineTo(gameRect.left, gameRect.bottom);
                gamePath.lineTo(gameRect.left, gameRect.top);
            }
        }
    }

    public void setDish(int[] values) {
        int plateMargin = MarginHolder.plateSideMargin;
        plateMargin = (spaces == 1) ? 0 : plateMargin;

        horzRect = new Rect(values[0]+plateMargin, values[1], values[2]-plateMargin, values[3]);

        int centerX = horzRect.right-(horzRect.width()/2);
        int centerY = horzRect.bottom-(horzRect.height()/2);
        int size = values[4];

        vertRect = new Rect((centerX-(size/2)), centerY-(horzRect.width()/2), centerX+(size/2), centerY+(horzRect.width()/2));
        innerMargin = horzRect.width()/30;

        Path leftPath = new Path();
        leftPath.moveTo(vertRect.left, vertRect.top+innerMargin);
        leftPath.lineTo(vertRect.right, vertRect.top);
        leftPath.lineTo(vertRect.right, vertRect.bottom);
        leftPath.lineTo(vertRect.left, vertRect.bottom-innerMargin);
        leftPath.lineTo(vertRect.left, vertRect.top+innerMargin);

        Path topPath = new Path();
        topPath.moveTo(horzRect.left+innerMargin, horzRect.top);
        topPath.lineTo(horzRect.right-innerMargin, horzRect.top);
        topPath.lineTo(horzRect.right, horzRect.bottom);
        topPath.lineTo(horzRect.left, horzRect.bottom);
        topPath.lineTo(horzRect.left+innerMargin, horzRect.top);

        Path rightPath = new Path();
        rightPath.moveTo(vertRect.left, vertRect.top);
        rightPath.lineTo(vertRect.right, vertRect.top+innerMargin);
        rightPath.lineTo(vertRect.right, vertRect.bottom-innerMargin);
        rightPath.lineTo(vertRect.left, vertRect.bottom);
        rightPath.lineTo(vertRect.left, vertRect.top);

        Path bottomPath = new Path();
        bottomPath.moveTo(horzRect.left, horzRect.top);
        bottomPath.lineTo(horzRect.right, horzRect.top);
        bottomPath.lineTo(horzRect.right-innerMargin, horzRect.bottom);
        bottomPath.lineTo(horzRect.left+innerMargin, horzRect.bottom);
        bottomPath.lineTo(horzRect.left, horzRect.top);

        dishPaths = new Path[] {leftPath, topPath, rightPath, bottomPath};

        if (horizontal && direction != 3) {
            direction = 1;
        }
    }
    public void rotate() {
        if (horizontal) {
            if (direction == 1) {
                direction = 0;
            } else if (direction == 3) {
                direction = 2;
            }

            horizontal = false;
        } else {
            if (direction == 0) {
                direction = 3;
            } else if (direction == 2) {
                direction = 1;
            }

            horizontal = true;
        }
    }

    @Override
    public void remove(List<TouchZone> plateZones, List<TouchZone> cupZones, List<UtensilZone> utensilZones) {
        remove(plateZones);
    }

    public void remove(List<TouchZone> plateZones) {

        for (int n : takenPlateZoneIndexes) {
            TouchZone zone = plateZones.get(n);

            zone.removePlateID(ID);
            zone.setTaken(false);

            if (horizontal) {
                zone.setZoneHorizontal(false);
            } else {
                zone.setZoneVertical(false);
            }
        }
    }


    @Override
    public boolean contains(int x, int y) {
        return gameRectSelection.contains(x, y);
    }

    public void setGameRectSelectionBound(Rect r) {
        gameRectSelection = r;
    }

    public int getID() {
        return ID;
    }
}
