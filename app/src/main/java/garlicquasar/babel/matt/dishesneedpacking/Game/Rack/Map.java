package garlicquasar.babel.matt.dishesneedpacking.Game.Rack;

import android.graphics.Rect;

/**
 * Holds static variables to do with the game map.
 */
public class Map {
    public static Rect takenZone = null;
    public static int takenRuns = 0;
    public static Rect GAME_RECT;
    public static Rect DISH_RECT;
    public static Rect ROTATE_RECT;
    public static Rect BACK_RECT;
    public static Rect DISH_TEXT_BOUNDS;
    public static Rect FLOOR_BUTTON_BOUNDS;
    public static Rect PICK_UP_BUTTON_BOUNDS;
    public static boolean pickUpToggleOn = false;
    public static Rect DISH_COUNTER_BOUNDS;
    public static Rect RUN_BUTTON_BOUNDS;
    public static Rect NEXT_BUTTON_BOUNDS;
    public static Rect CONFIRM_BOUNDS;
    public static Rect CONFIRM_TEXT_BOUNDS;
    public static Rect CONFIRM_YES_BUTTON;
    public static Rect CONFIRM_NO_BUTTON;


    public static int nextBtnCircleCenterX;
    public static int nextBtnCircleCenterY;
    public static int nextBtnCircleRadius;
    public static int maxFloors;
    public static int curFloor = 0;
    public static Rect[] floorBtns = new Rect[0];

    public static int[] columnsPerFloor;
    public static int MAXWIDTH;
    public static int MAXHEIGHT;
}
