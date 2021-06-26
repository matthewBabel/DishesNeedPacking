package garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects;

import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.TouchZone;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.UtensilZone;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

/**
 * Tupperware (copyrighted) AKA Plastic Container is a dish with two pieces, a container and lid. The container can be from (1 x 1) to (4 x 4).
 * The lid is always 1 space wide and as long as the longest side of container.
 * A piece can be selected to be placed first by touching the piece in the dish screen,
 * it defaults with container selected.
 */
public class Tupperware extends Dishware {

    public int spacesWide;
    public int spacesWideContainer;
    public int spacesLong;

    private Rect dishContainerHorz;
    private Rect dishContainerVert;
    private Rect dishContainer;

    private Rect dishLidHorz;
    private Rect dishLidVert;
    private Rect dishLid;

    private RectF[][] dishLidEdges;
    private int[][] dishLidEdgeAngles;

    public  Rect dishSelectionContainer;
    public Rect dishSelectionLid;

    private boolean containerSelected = true;

    // left, top, right, bottom
    public int edgeDirection = 1;

    private Rect gameContainer = null;
    private Rect gameLid = null;
    private RectF[] gameLidEdges;

    private Rect gameLidSelection;

    public boolean containerHolding = false;

    public List<Integer> takenTupperwareIndexes;
    public List<Integer> takenTupperwareDirectionIndex;
    public List<Integer> takenTupperwareZoneOnIndex;
    public List<Integer> takenLidIndexes;

    private int[] gameLidEdgeAngles;

    private Paint containerPaint;
    private Paint lidPaint;
    private Paint selectedPaint;

    private int thickness = 0;
    private int containerOverflow;
    private int lidSidesWidth;
    private int lidSidesHeight;
    private int selectionSize;

    // for splitting
    public boolean containerPlaced = false;
    public boolean lidPlaced = false;

    public boolean reset = false;

    public boolean containerDraw = true;
    public boolean lidDraw = true;

    public boolean isSingleContainer = false;
    public boolean isSingleLid = false;

    private boolean removedOnce = false;
    private boolean removedTwice = false;

    public int id;

    private int containerEdgeCurve = 50;

    private String containerName;
    private String lidName;



    public Tupperware(String name, boolean isHorizontal, int[] dimensions, int id) {
        spacesWide = dimensions[0];
        spacesLong = dimensions[1];
        spacesWideContainer = spacesWide;

        containerName = name;
        lidName = "Plastic Container Lid (1 x " + spacesLong + ")";

        this.name = name;
        this.horizontal = isHorizontal;



        containerPaint = new Paint();
        containerPaint.setARGB(160, 250, 57, 192);
        containerPaint.setTypeface(Typeface.DEFAULT);
        containerPaint.setAntiAlias(true);

        lidPaint = new Paint();
        lidPaint.setARGB(255, 250, 57, 192);
        lidPaint.setTypeface(Typeface.DEFAULT);
        lidPaint.setAntiAlias(true);

        selectedPaint = new Paint();
        selectedPaint.setARGB(120, 190, 235, 238);
        selectedPaint.setTypeface(Typeface.DEFAULT);
        selectedPaint.setAntiAlias(true);

        takenLidIndexes = new ArrayList<>();
        takenTupperwareIndexes = new ArrayList<>();
        takenTupperwareDirectionIndex = new ArrayList<>();
        takenTupperwareZoneOnIndex = new ArrayList<>();

        this.id = id;
    }

    @Override
    public void drawGame(Canvas canvas) {
        if (containerPlaced && containerDraw) {
            canvas.drawRoundRect(new RectF(gameContainer), containerEdgeCurve, containerEdgeCurve, containerPaint);
        }

        if (lidPlaced && lidDraw) {
            canvas.drawRect(gameLid, lidPaint);

            canvas.drawArc(gameLidEdges[0], gameLidEdgeAngles[0],
                    gameLidEdgeAngles[1],false, lidPaint);

            canvas.drawArc(gameLidEdges[1], gameLidEdgeAngles[0],
                    gameLidEdgeAngles[1],false, lidPaint);
        }
    }

    @Override
    public void drawDish(Canvas canvas) {
        if (containerSelected) {
            canvas.drawRect(dishSelectionContainer, selectedPaint);
        } else {
            canvas.drawRect(dishSelectionLid, selectedPaint);
        }

        if (!containerPlaced) {
            canvas.drawRoundRect(new RectF(dishContainer), containerEdgeCurve, containerEdgeCurve, containerPaint);
        }

        if (!lidPlaced) {
            canvas.drawRect(dishLid, lidPaint);

            canvas.drawArc(dishLidEdges[edgeDirection][0], dishLidEdgeAngles[edgeDirection][0],
                    dishLidEdgeAngles[edgeDirection][1], false, lidPaint);

            canvas.drawArc(dishLidEdges[edgeDirection][1], dishLidEdgeAngles[edgeDirection][0],
                    dishLidEdgeAngles[edgeDirection][1], false, lidPaint);
        }


    }

    @Override
    public void setGame(int[] values) {
        if (containerSelected) {
            gameContainer =  new Rect(values[0] - containerOverflow, values[1] - (int)(containerOverflow*1.25), values[2] + containerOverflow, values[3] + containerOverflow);
            containerPlaced = true;
            setContainerSelected(false);
        } else {

            Rect bounds =  new Rect(values[0], values[1], values[2], values[3]);
            gameLidSelection = bounds;


            int centerX = bounds.right - (bounds.width() / 2);
            int centerY = bounds.bottom - (bounds.height() / 2);

            if (horizontal) {
                gameLid = new Rect(bounds.left + (thickness), centerY - (thickness/2), bounds.right - (thickness), centerY + (thickness / 2));
            } else {
                gameLid = new Rect(centerX - (thickness / 2), bounds.top + (thickness), centerX + (thickness / 2), bounds.bottom - (thickness));
            }

            gameLidEdgeAngles = dishLidEdgeAngles[edgeDirection];

            switch(edgeDirection) {
                case 0: // left
                    gameLidEdges = new RectF[] {new RectF(gameLid.left - lidSidesHeight, gameLid.bottom - lidSidesWidth,
                            gameLid.left + thickness, gameLid.bottom),
                            new RectF(gameLid.left - lidSidesHeight, gameLid.top,
                                    gameLid.left + thickness, gameLid.top + lidSidesWidth)};
                    break;
                case 1: // top
                    gameLidEdges = new RectF[] {new RectF(gameLid.left, gameLid.top - lidSidesHeight,
                            gameLid.left + lidSidesWidth, gameLid.top + thickness),
                            new RectF(gameLid.right - lidSidesWidth, gameLid.top - lidSidesHeight,
                                    gameLid.right, gameLid.top + thickness)};
                    break;
                case 2: // right
                    gameLidEdges = new RectF[] {new RectF(gameLid.right - thickness, gameLid.bottom - lidSidesWidth,
                            gameLid.right + lidSidesHeight, gameLid.bottom),
                            new RectF(gameLid.right - thickness, gameLid.top,
                                    gameLid.right + lidSidesHeight, gameLid.top + lidSidesWidth)};
                    break;
                case 3: // bottom
                    gameLidEdges = new RectF[] {new RectF(gameLid.left, gameLid.bottom - thickness,
                            gameLid.left + lidSidesWidth, gameLid.bottom + lidSidesHeight),
                            new RectF(gameLid.right - lidSidesWidth, gameLid.bottom - thickness,
                                    gameLid.right, gameLid.bottom + lidSidesHeight)};
                    break;
                default:
                    break;

            }

            lidPlaced = true;
            setContainerSelected(true);
        }
    }

    @Override
    public void setDish(int[] values) {
        thickness = values[4];
        containerOverflow = thickness*2;
        selectionSize = thickness;

        dishContainerVert = new Rect(values[0] - containerOverflow, values[1] - (int)(containerOverflow*1.25), values[2] + containerOverflow, values[3] + containerOverflow);

        int vertContainerCenterX = dishContainerVert.centerX();
        int vertContainerCenterY = dishContainerVert.centerY();

        dishContainerHorz = new Rect((vertContainerCenterX - (dishContainerVert.height() / 2)),
                vertContainerCenterY - (dishContainerVert.width() / 2),
                vertContainerCenterX + (dishContainerVert.height() / 2),
                vertContainerCenterY + (dishContainerVert.width() / 2));

        final int vertLidMargin = dishContainerVert.width() / 2;
        dishLidVert = new Rect(dishContainerVert.left - vertLidMargin - (thickness / 2),
                dishContainerVert.centerY() - (dishContainerVert.height() / 2) + (thickness),
                dishContainerVert.left - vertLidMargin + (thickness / 2),
                dishContainerVert.centerY() + (dishContainerVert.height() / 2) - (thickness));

        int lidCenterX = dishLidVert.centerX();
        int lidCenterY = dishLidVert.centerY();

        dishLidHorz = new Rect(  lidCenterX - (dishLidVert.height() / 2),
                lidCenterY - (dishLidVert.width() / 2),
                lidCenterX + (dishLidVert.height() / 2),
                lidCenterY + (dishLidVert.width() / 2));


        lidSidesWidth = dishLidHorz.width() / 10;
        lidSidesHeight = thickness/2;

        RectF[] lidEdgesDown = new RectF[]{new RectF(dishLidHorz.left, dishLidHorz.bottom - thickness,
                dishLidHorz.left + lidSidesWidth, dishLidHorz.bottom + lidSidesHeight),
                new RectF(dishLidHorz.right - lidSidesWidth, dishLidHorz.bottom - thickness,
                        dishLidHorz.right, dishLidHorz.bottom + lidSidesHeight)};
        int[] downArcs = new int[] {0, 180};

        RectF[] lidEdgesRight = new RectF[]{new RectF(dishLidVert.right - thickness, dishLidVert.bottom - lidSidesWidth,
                dishLidVert.right + lidSidesHeight, dishLidVert.bottom),
                new RectF(dishLidVert.right - thickness, dishLidVert.top,
                        dishLidVert.right + lidSidesHeight, dishLidVert.top + lidSidesWidth)};
        int[] rightArcs = new int[] {270, 180};

        RectF[] lidEdgesUp = new RectF[]{new RectF(dishLidHorz.left, dishLidHorz.top - lidSidesHeight,
                dishLidHorz.left + lidSidesWidth, dishLidHorz.top + thickness),
                new RectF(dishLidHorz.right - lidSidesWidth, dishLidHorz.top - lidSidesHeight,
                        dishLidHorz.right, dishLidHorz.top + thickness)};
        int[] upArcs = new int[] {180, 180};

        RectF[] lidEdgesLeft = new RectF[]{new RectF(dishLidVert.left - lidSidesHeight, dishLidVert.bottom - lidSidesWidth,
                dishLidVert.left + thickness, dishLidVert.bottom),
                new RectF(dishLidVert.left - lidSidesHeight, dishLidVert.top,
                        dishLidVert.left + thickness, dishLidVert.top + lidSidesWidth)};
        int[] leftArcs = new int[] {90, 180};

        dishLidEdges = new RectF[][] {lidEdgesLeft, lidEdgesUp, lidEdgesRight, lidEdgesDown};
        dishLidEdgeAngles = new int[][] {leftArcs, upArcs, rightArcs, downArcs};


        if (!horizontal) {
            dishContainer = dishContainerVert;
            dishLid = dishLidVert;
        } else {
            dishContainer = dishContainerHorz;
            dishLid = dishLidHorz;
        }

        setSelectionContainers();
    }

    @Override
    public void rotate() {
        if (horizontal) {
            dishContainer = dishContainerVert;
            dishLid = dishLidVert;

            edgeDirection = (edgeDirection == 1) ? 0 : 2;

            horizontal = false;
        } else {
            dishContainer = dishContainerHorz;
            dishLid = dishLidHorz;

            edgeDirection = (edgeDirection == 0) ? 3 : 1;

            horizontal = true;
        }

        setSelectionContainers();
    }



    private void setSelectionContainers() {
        dishSelectionContainer = getSelectionRect(dishContainer, selectionSize);
        dishSelectionLid = getSelectionRect(dishLid, selectionSize*3);
    }

    public boolean onePiecePlaced() {
        return (containerPlaced && !lidPlaced) || (!containerPlaced && lidPlaced);
    }

    private Rect getSelectionRect(Rect r, int margin) {
        return new Rect(r.left - margin, r.top - margin, r.right + margin, r.bottom + margin);
    }

    public void setContainerSelected(boolean isContainer) {
        if (isContainer && !containerPlaced) {
            containerSelected = true;
            spacesWide = spacesWideContainer;
            name = containerName;
        } else if (!isContainer && !lidPlaced) {
            containerSelected = false;
            spacesWide = 1;
            name = lidName;
        }
    }

    public boolean isContainerSelected() {
        return containerSelected;
    }

    @Override
    public boolean contains(int x, int y) {
        if (gameContainer != null) {
            if (gameContainer.contains(x, y)) {
                containerHolding = true;
                return true;
            }
        }

        if (gameLidSelection != null) {
            if (gameLidSelection.contains(x, y)) {
                containerHolding = false;
                return true;
            }
        }

        return false;
    }

    @Override
    public void remove(List<TouchZone> plateZones, List<TouchZone> cupZones, List<UtensilZone> utensilZones) {
        remove(plateZones);
    }

    public void remove(List<TouchZone> tupperwareZones) {

        if (containerHolding) {
            for (int n : takenTupperwareIndexes) {
                tupperwareZones.get(n).removeTupperwareID(id);
                tupperwareZones.get(n).setTupperwareTakenZoneOff();
            }

            for (int n : takenTupperwareDirectionIndex) {
                tupperwareZones.get(n).removeDirectionalTupperwareID(id);
                tupperwareZones.get(n).setTupperwareZoneDirectionOff();
            }

            for (int n : takenTupperwareZoneOnIndex) {
                tupperwareZones.get(n).removeZoneOnTupperwareID(id);
                tupperwareZones.get(n).setTupperwareZoneOn(false, id);
            }

        } else {
            for (int n : takenLidIndexes) {
                TouchZone zone = tupperwareZones.get(n);

                zone.setTaken(false);

                if (horizontal) {
                    zone.setZoneHorizontal(false);
                } else {
                    zone.setZoneVertical(false);
                }
            }
        }
    }

    public void removeContainer() {
        containerPlaced = true;
        containerDraw = false;
        gameContainer = null;

        if (removedOnce) {
            removedTwice = true;
        }

        removedOnce = true;
    }

    public void removeLid() {
        lidPlaced = true;
        lidDraw = false;
        gameLidSelection = null;

        if (removedOnce) {
            removedTwice = true;
        }

        removedOnce = true;
    }

    public void singleContainerPiece() {
        lidDraw = false;
        lidPlaced = true;
        containerDraw = true;
        containerPlaced = false;
        containerHolding = true;
        isSingleContainer = true;

        setContainerSelected(true);
    }

    public void singleLidPiece() {
        containerDraw = false;
        containerPlaced = true;
        lidDraw = true;
        lidPlaced = false;
        containerHolding = false;
        isSingleLid = true;

        setContainerSelected(false);
    }

    public boolean canRemove() {
        return (removedTwice);
    }

    public void addToTakenTupperwareZoneOnIndex(int index) {
        if (!takenTupperwareZoneOnIndex.contains(index)) {
            takenTupperwareZoneOnIndex.add(index);
        }
    }

    public void addToDirectionalTupperwareZoneOnIndex(int index) {
        if (!takenTupperwareDirectionIndex.contains(index)) {
            takenTupperwareDirectionIndex.add(index);
        }
    }

    public void addToTakenTupperwareIndex(int index) {
        if (!takenTupperwareIndexes.contains(index)) {
            takenTupperwareIndexes.add(index);
        }
    }
}
