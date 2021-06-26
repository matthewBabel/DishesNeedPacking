package garlicquasar.babel.matt.dishesneedpacking.Game.Rack;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * The layout for a single floor, AKA rack.
 */
public class RackLayout {

    public List<Point> interceptPoints;
    public List<TouchZone> plateTouchZones;
    public List<TouchZone> cupTouchZones;
    public List<UtensilZone> utensilTouchZones;

    public int zoneSize = 0;

    private int gridRows;
    private int gridColumns;

    private int prongSize;

    //utensil parameters
    //0 - top left, 1 - top right, 2 - bottom right, 3 bottom left
    private int utensilZoneCorner = -1;
    // 0 vertical, 1 horizontal
    private int utensilZoneDirection = -1;
    // number of spaces
    private int utensilZoneSize = -1;
    private boolean utensilZone;


    public RackLayout(int columns, int width, int[] utensilParamters) {
        gridColumns = columns;
        gridRows = generateRows();
        Log.d(null, "Rows: " + gridRows);

        int prongSizeDivisor = 20;
        prongSize = (width / columns) / prongSizeDivisor;

        if (utensilParamters.length == 3) {
            utensilZoneCorner = utensilParamters[0];
            utensilZoneDirection = utensilParamters[1];
            utensilZoneSize = utensilParamters[2];
            utensilZone = true;
        } else {
            utensilZone = false;
        }
    }

    /**
     * Generate the rows according to the columns so that each Touch Zone is a perfect square;
     * This way when rotating a dish the size doesn't change.
     *
     * @return
     */
    private int generateRows() {
        int width = Map.GAME_RECT.width();
        int height = Map.GAME_RECT.height();

        return height / (width / gridColumns);
    }

    /**
     * Generates the a grid layout, and adds the intercept points between rows and columns.
     * The interceptPoints are drawn as the prongs.
     */
    public void generateLayout() {
        List<Float> yIntercepts = new ArrayList<>();
        List<Float> xIntercepts = new ArrayList<>();

        int horzLineYIncrement = (Map.GAME_RECT.bottom - Map.GAME_RECT.top) / (gridRows);
        // add horz lines
        for (int i = 1; i < gridRows; i++) {
            yIntercepts.add(Float.valueOf(Map.GAME_RECT.top + (horzLineYIncrement * i)));
        }

        int vertLineXIncrement = (Map.GAME_RECT.right - Map.GAME_RECT.left) / (gridColumns);
        // add vert lines
        for (int i = 1; i < gridColumns; i++) {
            xIntercepts.add(Float.valueOf(Map.GAME_RECT.left + (vertLineXIncrement * i)));
        }

        interceptPoints = new ArrayList<>();

        for (Float y : yIntercepts) {
            for (Float x : xIntercepts) {
                interceptPoints.add(new Point((int) (float) x, (int) (float) y));
            }
        }

        generateTouchZones();
    }

    /**
     * Generate the Touch Zones of the Rack.
     */
    private void generateTouchZones() {
        plateTouchZones = new ArrayList<>();
        cupTouchZones = new ArrayList<>();
        utensilTouchZones = new ArrayList<>();

        int row = 1;
        int col = 1;

        zoneSize = interceptPoints.get(1).x - interceptPoints.get(0).x;

        // init cup zones
        for (int i = 0; i < interceptPoints.size(); i++) {
            cupTouchZones.add(new TouchZone(new Rect(interceptPoints.get(i).x - (zoneSize / 2), interceptPoints.get(i).y - (zoneSize / 2),
                    interceptPoints.get(i).x + (zoneSize / 2), interceptPoints.get(i).y + (zoneSize / 2)),
                    new int[]{-1, -1, -1, -1}, true,  false));
        }


        int zoneIndex = 0;
        int interceptPointIndex = 0;

        for (int i = 0; i < (gridRows * gridColumns); i++) {
            int leftIndex = (col == 1) ? -1 : zoneIndex - 1;
            int topIndex = (row == 1) ? -1 : zoneIndex - (gridColumns);
            int rightIndex = (col == (gridColumns) ? -1 : zoneIndex + 1);
            int bottomIndex = (row == (gridRows) ? -1 : zoneIndex + (gridColumns));

            // passing a new rect when creating a new TouchZone and adding this to plateTouchZones
            boolean allowTupper = true;
            boolean edge = false;
            int left;
            int right;

            if (col == 1) {
                left = interceptPoints.get(0).x - zoneSize;
                allowTupper = false;
            } else if (col == gridColumns) {
                left = interceptPoints.get(gridColumns - 2).x;
                allowTupper = false;
            } else {
                if (row == gridRows) {
                    left = interceptPoints.get(interceptPointIndex - (gridColumns - col)).x;
                } else {
                    left = interceptPoints.get(interceptPointIndex).x;
                }
            }

            right = left + zoneSize;


            int top;
            if (row == 1) {
                top = interceptPoints.get(0).y - zoneSize;
                allowTupper = false;
            } else if (row == gridRows) {
                top = interceptPoints.get(interceptPointIndex).y;
                allowTupper = false;
            } else {
                top = interceptPoints.get(interceptPointIndex).y - zoneSize;
            }

            int bottom = top + zoneSize;


            if (col == 1 || col == gridColumns || row == 1 || row == gridRows) {
                edge = true;
            }


            plateTouchZones.add(new TouchZone(new Rect(left, top, right, bottom),
                    new int[]{leftIndex, topIndex, rightIndex, bottomIndex}, allowTupper, edge));

            if (!(col == 1 || row == gridRows)) {
                cupTouchZones.get(interceptPointIndex).setBottomNeighbor(zoneIndex + gridColumns); // down right,
                cupTouchZones.get(interceptPointIndex).setLeftNeighbor(zoneIndex + (gridColumns - 1)); // down left
                cupTouchZones.get(interceptPointIndex).setRightNeighbor(zoneIndex); // up right
                cupTouchZones.get(interceptPointIndex).setTopNeighbor(zoneIndex - 1); // up left

                if (!(row == gridRows - 1 && col == gridColumns)) {
                    interceptPointIndex++;
                }
            }

            if (utensilZone) {
                setUtensilZones(row, col, zoneIndex);
            }

            if (col == gridColumns) {
                row++;
                col = 1;
            } else {
                col++;
            }

            zoneIndex++;
        }
    }

    /**
     * Check if the current row x column zone is specified to be a utensil zone;
     * This is specified by the variables utensilZoneCorner and utensilZoneDirection.
     *
     * @param row       current row
     * @param col       current column
     * @param zoneIndex
     */
    private void setUtensilZones(int row, int col, int zoneIndex) {
        if (utensilZoneCorner == 0 && utensilZoneDirection == 0) { // top left going down
            if (col == 1 && row <= utensilZoneSize) {
                addUtensilZone(plateTouchZones.get(zoneIndex));
            }
        } else if (utensilZoneCorner == 0 && utensilZoneDirection == 1) { // top left going right
            if (row == 1 && col <= utensilZoneSize) {
                addUtensilZone(plateTouchZones.get(zoneIndex));
            }
        } else if (utensilZoneCorner == 1 && utensilZoneDirection == 0) { // top right going down
            if (col == gridColumns && row <= utensilZoneSize) {
                addUtensilZone(plateTouchZones.get(zoneIndex));
            }
        } else if (utensilZoneCorner == 1 && utensilZoneDirection == 1) { // top right going left
            if (row == 1 && col > (gridColumns) - utensilZoneSize) {
                addUtensilZone(plateTouchZones.get(zoneIndex));
            }
        } else if (utensilZoneCorner == 2 && utensilZoneDirection == 0) { // bottom right going up
            if (col == gridColumns && row > (gridRows) - utensilZoneSize) {
                addUtensilZone(plateTouchZones.get(zoneIndex));
            }
        } else if (utensilZoneCorner == 2 && utensilZoneDirection == 1) { // bottom right going left
            if (row == gridRows && col > (gridColumns) - utensilZoneSize) {
                addUtensilZone(plateTouchZones.get(zoneIndex));
            }
        } else if (utensilZoneCorner == 3 && utensilZoneDirection == 0) { // bottom left going up
            if (col == 1 && row > (gridRows) - utensilZoneSize) {
                addUtensilZone(plateTouchZones.get(zoneIndex));
            }
        } else if (utensilZoneCorner == 3 && utensilZoneDirection == 1) { // bottom left going right
            if (row == gridRows && col <= utensilZoneSize) {
                addUtensilZone(plateTouchZones.get(zoneIndex));
            }
        }
    }

    /**
     * Set zone as a utensil zone and add a new UtensilZone.
     *
     * @param zone
     */
    private void addUtensilZone(TouchZone zone) {
        zone.setUtensilZone(true);
        zone.setTaken(true);
        zone.setTupperwareOverflowIsAllowed(false);
        final int OVERHANG = 4;
        utensilTouchZones.add(new UtensilZone(expandZone(zone.getZone(), OVERHANG), zone.getNeighbors()));
    }

    private Rect expandZone(Rect r, int num) {
        return new Rect(r.left - num, r.top - num, r.right + num, r.bottom + num);
    }

    /**
     * Draws the intercept points skipping over utensilZones, then draws utensilZones.
     *
     * @param canvas
     * @param paint        intercept points paint
     * @param utensilPaint utensil zone paint
     */
    public void drawGrid(Canvas canvas, Paint paint, Paint utensilPaint) {
        for (Point p : interceptPoints) {
            boolean pass = true;
            for (TouchZone z : utensilTouchZones) {
                if (z.getZone().contains(p.x, p.y)) {
                    pass = false;
                }
            }

            if (pass) {
                canvas.drawCircle(p.x, p.y, prongSize, paint);
            }
        }

        for (TouchZone z : utensilTouchZones) {
            canvas.drawRect(z.getZone(), utensilPaint);
        }
    }

    public int getArea() {
        return gridColumns * gridRows;
    }

    public int getEdges() {return ((gridRows * 2) + (gridColumns * 2)) - 4;}

    public int getMaxPlate() {
        return plateTouchZones.size() - utensilTouchZones.size();
    }

    public int getMaxCups() {
        return cupTouchZones.size() - utensilTouchZones.size();
    }

    public int getMaxBowls() {
        int colProngs = gridColumns-1;
        int rowProngs = gridRows-1;
        int taken = 0;

        if (utensilZoneDirection != -1 && utensilZoneSize != -1 && utensilZoneSize != 0) {
            if (utensilZoneDirection == 0) {//vertical utensil zones
                if (colProngs % 2 == 0) {
                    taken = (int) Math.ceil(utensilZoneSize / 2);
                }
            } else {
                if (rowProngs % 2 == 0) {
                    taken = (int) Math.ceil(utensilZoneSize / 2);
                }
            }
        }

        int evenRows = (int)Math.floor(rowProngs/2);
        int evenCols = (int)Math.floor(colProngs/2);
        return (evenRows * evenCols) - taken;
    }

    public int getMaxBowlsPerRow() {
        return (int)Math.floor((gridColumns-1)/2);
    }

    public int getMaxUtensils() {
       return utensilTouchZones.size();
    }

    public int getRows() {
        return gridRows;
    }

    public int getColumns() {
        return gridColumns;
    }
}
