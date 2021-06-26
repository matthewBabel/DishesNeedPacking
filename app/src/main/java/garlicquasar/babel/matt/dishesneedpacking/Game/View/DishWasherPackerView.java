package garlicquasar.babel.matt.dishesneedpacking.Game.View;

import garlicquasar.babel.matt.dishesneedpacking.Acitivities.MainActivity;
import garlicquasar.babel.matt.dishesneedpacking.Audio.MusicServiceHandler;
import garlicquasar.babel.matt.dishesneedpacking.Audio.SoundEffectPlayer;
import garlicquasar.babel.matt.dishesneedpacking.Fragments.GameDisplayFragment;
import garlicquasar.babel.matt.dishesneedpacking.Game.DataHolders.BitmapHolder;
import garlicquasar.babel.matt.dishesneedpacking.Game.DataHolders.ColorHolder;
import garlicquasar.babel.matt.dishesneedpacking.Game.DataHolders.GameScoreData;
import garlicquasar.babel.matt.dishesneedpacking.Game.DataHolders.MarginHolder;
import garlicquasar.babel.matt.dishesneedpacking.Game.DataHolders.PaintHolder;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Dishware;
import garlicquasar.babel.matt.dishesneedpacking.Game.Input.GestureHandler;
import garlicquasar.babel.matt.dishesneedpacking.Game.Placement.Placement;
import garlicquasar.babel.matt.dishesneedpacking.Game.Puzzle.DishGenerator;
import garlicquasar.babel.matt.dishesneedpacking.Game.Puzzle.PuzzleHandler;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.Map;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.RackLayout;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * View where the Dish Washer Packer game is drawn.
 */
public class DishWasherPackerView extends View {

    // game objects
    public static RackLayout[] rackLayouts; // different rack layers
    public static DishGenerator generator;
    public static List<Dishware>[] dishes; // each rack has a different array of dishes
    public static Dishware currentDish;
    public static Placement placement;
    public static PuzzleHandler puzzleHandler;
    public static boolean showTaken = false;

    // data holders
    public static GameScoreData gameData;
    public static PaintHolder paints;
    public static BitmapHolder bitmaps;
    public static ColorHolder colors;

    // static booleans
    public static boolean gameOver = false;
    public static boolean moveToScoreScreen = false;
    public static boolean puzzleFull = false;
    public static boolean exitConfirmBoxOn = false;
    public static boolean washConfirmBoxOn = false;

    public static boolean fromGameInputScreen;

    // input
    private GestureDetector touchDetector;

    // Rect's used to size text
    private Rect victoryTextRect;
    private Rect dishCounterRect;

    private static int[] utensilParams;
    public static Context context;

    /**
     * Init Paints, Maps, and game objects.
     *
     * @param context
     */
    public DishWasherPackerView(Context context, int columns, int floors, int tupperware, int mixingBowl, double cupPercentage,
                                int utensilCorner, int utensilDirection, int utensilAmount, int level, int plateSubtractions, int runs, boolean fromGameInput) {
        super(context);
        this.context = context;
        fromGameInputScreen = fromGameInput;

        //reset static
        resetStatic();

        // init data holders
        initDataHolders(level, runs);

        utensilParams = new int[]{utensilCorner, utensilDirection, utensilAmount};

        // Map variables
        final int MAXWIDTH = getResources().getDisplayMetrics().widthPixels - MarginHolder.MARGINALL;
        final int MAXHEIGHT = getResources().getDisplayMetrics().heightPixels - MarginHolder.MARGINALL - MarginHolder.EXTRABOTTOM;
        initMap(columns, floors, MAXWIDTH, MAXHEIGHT);

        //init puzzle variables
        rackLayouts = new RackLayout[Map.maxFloors];
        dishes = new List[Map.maxFloors];
        initPuzzle(columns, tupperware, mixingBowl, cupPercentage, utensilCorner, utensilDirection, utensilAmount, plateSubtractions, runs, MAXWIDTH);

        //init rest of variables
        placement = new Placement();
        currentDish = puzzleHandler.getDish();
        touchDetector = new GestureDetector(this.getContext(), new GestureHandler());
        this.setOnTouchListener(touchListener);

        // do dynamic font sizes
        setFontSizes();
        makeFontAdjustmentRects();

        //set background color and time
        setBackgroundColor(colors.getMainBackgroundColor());
        gameData.startTime = System.nanoTime();
    }

    private void initDataHolders(int level, int runs) {
        paints = new PaintHolder();
        bitmaps = new BitmapHolder(this.getContext());
        colors = new ColorHolder();
        gameData = new GameScoreData();
        gameData.resetAll(); // reset static
        GameScoreData.level = level;
        GameScoreData.RUNSNEEDED = runs;
    }

    private void resetStatic() {
        gameOver = false;
        moveToScoreScreen = false;
        showTaken = false;
        gameOver = false;
        moveToScoreScreen = false;
        puzzleFull = false;
        exitConfirmBoxOn = false;
        washConfirmBoxOn = false;
        Map.pickUpToggleOn = false;
        Map.takenRuns = 0;
    }

    private void initPuzzle(int columns, int tupperware, int mixingBowls, double cupPercentage, int utensilCorner, int utensilDirection, int utensilAmount, int plateSubtractions, int runs, int MAXWIDTH) {
        // per floor loop, gathering needed data for puzzle solver
        int maxPlateZones = 0;
        int maxCupZones = 0;
        int maxUtensilZones = 0;
        int maxArea = 0;
        int maxEdges = 0;

        Integer[] cupsPerFloor = new Integer[Map.maxFloors];
        Integer[] columnsPerFloor = new Integer[Map.maxFloors];
        Integer[] rowsPerFloor = new Integer[Map.maxFloors];
        Integer[] bowlsPerFloor = new Integer[Map.maxFloors];
        Integer[] bowlsPerRowPerFloor = new Integer[Map.maxFloors];


        for (int i = 0; i < Map.maxFloors; i++) {

            int floorColumn = columns; // in case I want diff columns per floor
            if (i == 0) {
                rackLayouts[i] = new RackLayout(floorColumn, MAXWIDTH, new int[]{utensilCorner, utensilDirection, utensilAmount});
            } else {
                rackLayouts[i] = new RackLayout(floorColumn, MAXWIDTH, new int[]{0});
            }

            Map.columnsPerFloor[i] = floorColumn;

            rackLayouts[i].generateLayout();
            dishes[i] = new ArrayList<>();

            maxPlateZones += rackLayouts[i].getMaxPlate();
            maxCupZones += rackLayouts[i].getMaxCups();
            maxUtensilZones += rackLayouts[i].getMaxUtensils();

            maxArea += rackLayouts[i].getArea();
            maxEdges += rackLayouts[i].getEdges();

            cupsPerFloor[i] = rackLayouts[i].getMaxCups();
            columnsPerFloor[i] = rackLayouts[i].getColumns();
            rowsPerFloor[i] = rackLayouts[i].getRows();
            bowlsPerFloor[i] = rackLayouts[i].getMaxBowls();
            bowlsPerRowPerFloor[i] = rackLayouts[i].getMaxBowlsPerRow();
        }

        GameScoreData.totalSpaces = maxArea;
        GameScoreData.colsPerFloor = columnsPerFloor;
        GameScoreData.rowsPerFloor =  convertIntegerArrayToIntArray(rowsPerFloor);
        GameScoreData.calculateReadableArea();

        if (Map.maxFloors > 1) {
            generateFloorButtons(Map.maxFloors, Map.FLOOR_BUTTON_BOUNDS);
        }


        MarginHolder.resetMargins();
        generator = new DishGenerator(rackLayouts[Map.curFloor].zoneSize,
                bitmaps.spoonImg, bitmaps.knifeImg, bitmaps.forkImg, bitmaps.cupImg, bitmaps.bowlImg);
        puzzleHandler = new PuzzleHandler(generator, maxPlateZones, maxCupZones, maxUtensilZones, maxArea, maxEdges, cupsPerFloor, bowlsPerFloor, bowlsPerRowPerFloor, columnsPerFloor, rowsPerFloor, tupperware, mixingBowls, cupPercentage, plateSubtractions, runs);
    }

    private void makeFontAdjustmentRects() {
        victoryTextRect = new Rect(Map.GAME_RECT);
        victoryTextRect.offset(-victoryTextRect.width() / 2, 0);

        dishCounterRect = new Rect(Map.DISH_COUNTER_BOUNDS);
        dishCounterRect.offset(-dishCounterRect.width() / 2, 0);
    }

    private void setFontSizes() {
        setTextSizeForWidth(paints.victoryTextPaint, Map.GAME_RECT.width(), "VICTORY");
        setTextSizeForWidth(paints.dishCounterTextPaint, Map.DISH_COUNTER_BOUNDS.width(), "Dishes: " + puzzleHandler.dishCount);
        setTextSizeForWidth(paints.dishCounterRedTextPaint, Map.DISH_COUNTER_BOUNDS.width(), "Dishes: " + puzzleHandler.dishCount);
        setTextSizeForWidth(paints.dishTextPaint, Map.DISH_TEXT_BOUNDS.width() - (Map.DISH_TEXT_BOUNDS.width() / 4), "SPOON");
        setTextSizeForWidth(paints.runBtnTextPaint, Map.RUN_BUTTON_BOUNDS.width() - (Map.RUN_BUTTON_BOUNDS.width() / 8), "WASH");
        setTextSizeForWidth(paints.floorTextPaint, Map.FLOOR_BUTTON_BOUNDS.width() - (Map.FLOOR_BUTTON_BOUNDS.width() / 4), "Rack 1");
        setTextSizeForWidth(paints.pickUpBtnTextPaint, Map.PICK_UP_BUTTON_BOUNDS.width() - (Map.PICK_UP_BUTTON_BOUNDS.width() / 4), "PICK UP");
        setTextSizeForWidth(paints.exitTextPaint, Map.CONFIRM_TEXT_BOUNDS.width() + (Map.CONFIRM_TEXT_BOUNDS.width() / 4), "Do You Want To Wash Dishes?");
        setTextSizeForWidth(paints.yesnoPaint, Map.CONFIRM_YES_BUTTON.width() - (Map.CONFIRM_YES_BUTTON.width() / 4), "YES");

        if (Map.floorBtns.length > 1) {
            adjustFloorFontHeight("Rack 1", paints.floorTextPaint, Map.floorBtns[0].height());
        }
    }

    private void initMap(int columns, int floors, int MAXWIDTH, int MAXHEIGHT) {
        final int GAMESCREENWIDTH = MAXWIDTH;
        final int DISHSCREENWIDTH = MAXWIDTH;
        final int DISHSCREENHEIGHT = calculateDishScreenHeight(columns, DISHSCREENWIDTH, MAXHEIGHT, MarginHolder.MARGINALL);
        final int stroke = (int)Math.ceil(DISHSCREENHEIGHT / 75);
        paints.setStrokeWidth(stroke, stroke/2);

        final int GAMESCREENHEIGHT = MAXHEIGHT;

        final Rect GAMESCREENRECT = new Rect(MarginHolder.MARGINALL, DISHSCREENHEIGHT + MarginHolder.MARGINALL, GAMESCREENWIDTH, GAMESCREENHEIGHT);
        final Rect DISHSCREENRECT = new Rect(MarginHolder.MARGINALL, MarginHolder.MARGINALL, DISHSCREENWIDTH, DISHSCREENHEIGHT);

        final Rect BACKCONFIRMBOUNDS = new Rect(DISHSCREENRECT.left + (DISHSCREENRECT.width()/4), DISHSCREENRECT.top + (DISHSCREENRECT.height()/4),
                                            DISHSCREENRECT.right - (DISHSCREENRECT.width()/4), DISHSCREENRECT.bottom - (DISHSCREENRECT.height()/4));

        final Rect BACKCONFIRMTEXTBOX = new Rect(BACKCONFIRMBOUNDS.left + (BACKCONFIRMBOUNDS.width()/8), BACKCONFIRMBOUNDS.top + (BACKCONFIRMBOUNDS.height()/16),
                                                 BACKCONFIRMBOUNDS.right - (BACKCONFIRMBOUNDS.width()/8), BACKCONFIRMBOUNDS.top + (BACKCONFIRMBOUNDS.height()/2));

        final Rect BACKCONFIRMYESBUTTON = new Rect(BACKCONFIRMBOUNDS.left + (BACKCONFIRMBOUNDS.width()/16), BACKCONFIRMBOUNDS.top + (BACKCONFIRMBOUNDS.height()/2),
                                                  BACKCONFIRMBOUNDS.left + (BACKCONFIRMBOUNDS.width()/2) - (BACKCONFIRMBOUNDS.width()/16), BACKCONFIRMBOUNDS.bottom - (BACKCONFIRMBOUNDS.height()/16));

        final Rect BACKCONFIRMNOBUTTON = new Rect(BACKCONFIRMBOUNDS.left + (BACKCONFIRMBOUNDS.width()/2) + (BACKCONFIRMBOUNDS.width()/16), BACKCONFIRMBOUNDS.top + (BACKCONFIRMBOUNDS.height()/2),
                                                 BACKCONFIRMBOUNDS.right  - (BACKCONFIRMBOUNDS.width()/16), BACKCONFIRMBOUNDS.bottom - (BACKCONFIRMBOUNDS.height()/16));

        int backBtnWidth = (DISHSCREENRECT.left + MarginHolder.ROTATEMARGIN + (DISHSCREENHEIGHT / 6)) -  (DISHSCREENRECT.left + MarginHolder.ROTATEMARGIN);
        final Rect BACKBTNBOUNDS = new Rect(DISHSCREENRECT.left + MarginHolder.ROTATEMARGIN, DISHSCREENRECT.top + MarginHolder.ROTATEMARGIN,
                (DISHSCREENRECT.left + MarginHolder.ROTATEMARGIN + (DISHSCREENHEIGHT / 6)),
                (DISHSCREENRECT.top + MarginHolder.ROTATEMARGIN) + backBtnWidth);

        int rotateBtnWidth = (DISHSCREENRECT.left + (DISHSCREENWIDTH / 8)) - (DISHSCREENRECT.left + MarginHolder.ROTATEMARGIN);
        final Rect ROTATERECT = new Rect(DISHSCREENRECT.left + MarginHolder.ROTATEMARGIN, (DISHSCREENRECT.bottom - MarginHolder.ROTATEMARGIN) - (rotateBtnWidth * (bitmaps.rotateImg.getHeight() / bitmaps.rotateImg.getWidth())),
                DISHSCREENRECT.left +(DISHSCREENWIDTH / 8), DISHSCREENRECT.bottom - MarginHolder.ROTATEMARGIN);

        final Rect FLOORBOUNDS = new Rect(MarginHolder.MARGINALL + MarginHolder.FLOORBTNLEFT, BACKBTNBOUNDS.bottom,
                ROTATERECT.right + (ROTATERECT.width()/2), ROTATERECT.top);


        int dishCounterWidth = DISHSCREENWIDTH / 4;
        int dishCounterHeight = DISHSCREENHEIGHT / 10;
        final Rect DISHCOUNTERBOUNDS = new Rect(DISHSCREENRECT.centerX() - (dishCounterWidth / 2), DISHSCREENRECT.top + ((int) (MarginHolder.MARGINALL * 2)),
                DISHSCREENRECT.centerX() + (dishCounterWidth / 2), DISHSCREENRECT.top + (int) (MarginHolder.MARGINALL * 2) + dishCounterHeight);


        int pickUpTop = ((DISHSCREENRECT.top + DISHSCREENHEIGHT / 15) + MarginHolder.PICKUPBUTTON);
        int pickUpBottom = pickUpTop + (DISHSCREENHEIGHT / 8);
        int pickUpButtonWidth = (int)(ROTATERECT.width()*1.5);
        final Rect PICKUPBUTTON = new Rect(DISHSCREENRECT.right - MarginHolder.PICKUPBUTTON - pickUpButtonWidth, pickUpTop,
                DISHSCREENRECT.right - MarginHolder.PICKUPBUTTON, pickUpBottom);

        int nextBtnWidth = (PICKUPBUTTON.centerX() + (PICKUPBUTTON.width() / 3)) - (PICKUPBUTTON.centerX());
        final Rect DISHTEXTBOUNDS = new Rect(DISHSCREENRECT.right - (DISHSCREENRECT.width() / 2) + (DISHSCREENRECT.width() / 8), DISHSCREENRECT.bottom - (DISHSCREENHEIGHT / 5),
                PICKUPBUTTON.centerX() - nextBtnWidth, DISHSCREENRECT.bottom);


        final Rect NEXTBUTTON = new Rect(PICKUPBUTTON.centerX(),
                DISHTEXTBOUNDS.bottom-(DISHTEXTBOUNDS.height()/3) - (nextBtnWidth * (bitmaps.nextImg.getHeight() / bitmaps.nextImg.getWidth())),
                PICKUPBUTTON.centerX() + (PICKUPBUTTON.width() / 3),
                DISHTEXTBOUNDS.bottom-(DISHTEXTBOUNDS.height()/3));


        int runBtnWidth = (int) (PICKUPBUTTON.width() * 0.66);
        final Rect RUNBUTTON = new Rect(PICKUPBUTTON.centerX() - (runBtnWidth / 2), PICKUPBUTTON.bottom + MarginHolder.MARGINALL,
                PICKUPBUTTON.centerX() + (runBtnWidth / 2), PICKUPBUTTON.bottom + MarginHolder.MARGINALL + PICKUPBUTTON.height());

        // Map variables
        Map.GAME_RECT = GAMESCREENRECT;
        Map.DISH_RECT = DISHSCREENRECT;
        Map.ROTATE_RECT = ROTATERECT;
        Map.BACK_RECT = BACKBTNBOUNDS;
        Map.DISH_TEXT_BOUNDS = DISHTEXTBOUNDS;
        Map.FLOOR_BUTTON_BOUNDS = FLOORBOUNDS;
        Map.PICK_UP_BUTTON_BOUNDS = PICKUPBUTTON;
        Map.NEXT_BUTTON_BOUNDS = NEXTBUTTON;
        Map.nextBtnCircleCenterX = NEXTBUTTON.centerX();
        Map.nextBtnCircleCenterY = NEXTBUTTON.centerY();
        Map.nextBtnCircleRadius = NEXTBUTTON.width();
        Map.DISH_COUNTER_BOUNDS = DISHCOUNTERBOUNDS;
        Map.RUN_BUTTON_BOUNDS = RUNBUTTON;
        Map.CONFIRM_BOUNDS = BACKCONFIRMBOUNDS;
        Map.CONFIRM_TEXT_BOUNDS = BACKCONFIRMTEXTBOX;
        Map.CONFIRM_YES_BUTTON = BACKCONFIRMYESBUTTON;
        Map.CONFIRM_NO_BUTTON = BACKCONFIRMNOBUTTON;


        Map.maxFloors = floors;
        Map.curFloor = 0;
        Map.MAXWIDTH = MAXWIDTH;
        Map.MAXHEIGHT = MAXHEIGHT;
        Map.columnsPerFloor = new int[floors];
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return touchDetector.onTouchEvent(event);
        }
    };

    private int calculateDishScreenHeight(int columns, int maxWidth, int maxHeight, int margin) {
        int columnWidth = (maxWidth - margin) / columns;
        int maxPieceSize = (columnWidth * 4);
        int maxDishScreenHeight = maxHeight - (int) (columnWidth * 2.5);
        int dishScreenHeight = (maxHeight / 4);

        while (maxPieceSize > (dishScreenHeight + margin) && maxDishScreenHeight >= dishScreenHeight + 5) {
            dishScreenHeight += 5;
        }

        int adjustedDishScreenHeight = maxHeight;
        while (adjustedDishScreenHeight > dishScreenHeight + margin) {
            adjustedDishScreenHeight -= columnWidth;
        }

        return adjustedDishScreenHeight - margin;
    }


    /**
     * Generate the floor buttons needed according to the number of floors.
     *
     * @param num    number of floors
     * @param bounds the boundaries that all floor buttons need to be placed within
     */
    private void generateFloorButtons(int num, Rect bounds) {
        Rect[] floorBtns = new Rect[num];

        int btnMargin = bounds.height() / 15;

        for (int i = 0; i < num; i++) {
            Rect r = new Rect(bounds.left,
                    bounds.top + ((bounds.height() / num) * i),
                    bounds.right,
                    bounds.bottom - (((bounds.height() / num) * (num - (i + 1)))) - btnMargin);
            floorBtns[i] = r;
        }


        Map.floorBtns = floorBtns;
    }


    /**
     * Game loop call, draws game screen and game objects currently being displayed.
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        // Drawing Borders
        Rect borderRect = Map.GAME_RECT;
        canvas.drawRect(Map.DISH_RECT, paints.borderPaint);
        canvas.drawRect(getBackgroundRect(Map.DISH_RECT), paints.backgroundPaint);
        canvas.drawRect(borderRect, paints.borderPaint);
        canvas.drawRect(getBackgroundRect(borderRect), paints.gameBackgroundPaint);

        /******************** start dish screen ********************/
        canvas.save();
        canvas.clipRect(Map.DISH_RECT);

        if (currentDish != null) {
            currentDish.drawDish(canvas);


            canvas.drawText(currentDish.name, Map.DISH_TEXT_BOUNDS.right,
                    Map.DISH_TEXT_BOUNDS.bottom - (Map.DISH_TEXT_BOUNDS.height() / 4), paints.dishTextPaint);
        }

        canvas.drawBitmap(bitmaps.backImg, null, Map.BACK_RECT, paints.backgroundPaint);
        canvas.drawBitmap(bitmaps.rotateImg, null, Map.ROTATE_RECT, paints.backgroundPaint);

        canvas.drawCircle(Map.nextBtnCircleCenterX, Map.nextBtnCircleCenterY, Map.nextBtnCircleRadius, paints.solidBlackOutlinePaint);
        canvas.drawBitmap(bitmaps.nextImg, null, Map.NEXT_BUTTON_BOUNDS, paints.backgroundPaint);

        // floor buttons
        int count = 0;
        if (Map.maxFloors > 1) {
            for (Rect r : Map.floorBtns) {
                canvas.drawRect(r, paints.borderPaint);
                if (Map.curFloor != (Map.maxFloors - (count + 1))) {
                    canvas.drawRect(getBackgroundRect(r), paints.transparentBackgroundPaint);
                } else {
                    canvas.drawRect(getBackgroundRect(r), paints.invalidPaint);
                }

                drawCenteredText("Rack " + (Map.maxFloors - count), r, canvas, paints.floorTextPaint);
                count++;
            }
        }

        if (Map.pickUpToggleOn) {
            drawButton(canvas, Map.PICK_UP_BUTTON_BOUNDS, paints.borderPaint, paints.pickUpButtonTogglePaint, paints.pickUpBtnTextPaint, "PICK UP");
        } else {
            drawButton(canvas, Map.PICK_UP_BUTTON_BOUNDS, paints.borderPaint, paints.transparentBackgroundPaint, paints.pickUpBtnTextPaint, "PICK UP");
        }
        drawButton(canvas, Map.RUN_BUTTON_BOUNDS, paints.borderPaint, paints.transparentBackgroundPaint, paints.runBtnTextPaint, "WASH");


        int additionalDishes = 0;
        additionalDishes = (currentDish != null) ? additionalDishes + 1 : additionalDishes;

        drawCenteredText("Dishes: " + (puzzleHandler.dishCount + additionalDishes), dishCounterRect, canvas,
                (puzzleHandler.dishCount + additionalDishes == 0) ? paints.dishCounterRedTextPaint : paints.dishCounterTextPaint);

        if (exitConfirmBoxOn) {
            canvas.drawRoundRect(new RectF(Map.CONFIRM_BOUNDS), 8, 8, paints.exitConfirmBoxPaint);
            drawCenteredText("Do You Want To Exit Game?", Map.CONFIRM_TEXT_BOUNDS, canvas, paints.exitTextPaint);
            drawButton(canvas, Map.CONFIRM_YES_BUTTON, paints.borderPaint, paints.transparentBackgroundPaint, paints.yesnoPaint, "YES");
            drawButton(canvas, Map.CONFIRM_NO_BUTTON, paints.borderPaint, paints.transparentBackgroundPaint, paints.yesnoPaint, "NO");
        }

        if (washConfirmBoxOn) {
            canvas.drawRoundRect(new RectF(Map.CONFIRM_BOUNDS), 8, 8, paints.exitConfirmBoxPaint);
            drawCenteredText("Do You Want To Wash Dishes?", Map.CONFIRM_TEXT_BOUNDS, canvas, paints.exitTextPaint);
            drawButton(canvas, Map.CONFIRM_YES_BUTTON, paints.borderPaint, paints.transparentBackgroundPaint, paints.yesnoPaint, "YES");
            drawButton(canvas, Map.CONFIRM_NO_BUTTON, paints.borderPaint, paints.transparentBackgroundPaint, paints.yesnoPaint, "NO");
        }

        canvas.restore();

        /******************* end dish screen ************************/


        /******************* start game screen ************************/
        rackLayouts[Map.curFloor].drawGrid(canvas, paints.prongPaint, paints.utensilPaint);

        if (showTaken && Map.takenZone != null) {
            canvas.drawRect(Map.takenZone, paints.takenPaint);
        }

        for (Dishware dish : dishes[Map.curFloor]) {
            dish.drawGame(canvas);
        }

        if (gameOver) {
            drawCenteredText("VICTORY", victoryTextRect, canvas, paints.victoryTextPaint);
        }

        /******************* end game screen ************************/

        // testing
        /*
        for (TouchZone z : rackLayouts[Map.curFloor].plateTouchZones) {
            if (z.isDirectionalZone()) {
                canvas.drawRect(z.getZone(), paints.testingBorderPaint);
                canvas.drawRect(getBackgroundRect(z.getZone()), paints.testingBackgroundPaint);
            }
        }*/
    }

    private void drawButton(Canvas canvas, Rect bounds, Paint borderPaint, Paint backgroundPaint, Paint fontPaint, String text) {
        canvas.drawRoundRect(new RectF(bounds), 8, 8, borderPaint);
        canvas.drawRoundRect(new RectF(getBackgroundRect(bounds)), 8, 8, backgroundPaint);
        drawCenteredText(text, bounds, canvas, fontPaint);
    }

    /**
     * return a rectangle slightly smaller than the rectangle provided.
     *
     * @param r rectangle provided
     * @return a rectangle smaller than r
     */
    private Rect getBackgroundRect(Rect r) {
        final int SIZE = 4;
        return new Rect(r.left + SIZE, r.top + SIZE, r.right - SIZE, r.bottom - SIZE);
    }

    /**
     * Draw text centered in the bounds rectangle.
     *
     * @param str    the text
     * @param bounds rectangle to be centered in
     * @param canvas
     */
    private void drawCenteredText(String str, Rect bounds, Canvas canvas, Paint paint) {

        int x = bounds.left + (bounds.width() / 2);
        int y = (int) (bounds.top + (bounds.height() / 2) - ((paint.descent() + paint.ascent()) / 2));

        canvas.drawText(str, x, y, paint);
    }


    /**
     * Sets the text size for a Paint object so a given string of text will be a
     * given width.
     *
     * @param paint        the Paint to set the text size for
     * @param desiredWidth the desired width
     * @param text         the text that should be that width
     */
    private static void setTextSizeForWidth(Paint paint, float desiredWidth,
                                            String text) {

        // Pick a reasonably large value for the test. Larger values produce
        // more accurate results, but may cause problems with hardware
        // acceleration. But there are workarounds for that, too; refer to
        // http://stackoverflow.com/questions/6253528/font-size-too-large-to-fit-in-cache
        final float testTextSize = 48f;

        // Get the bounds of the text, using our testTextSize.
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = (float) Math.floor(testTextSize * desiredWidth / bounds.width());

        // Set the paint for that size.
        paint.setTextSize(desiredTextSize);
    }

    private void adjustFloorFontHeight(String text, Paint font, int height) {
        Rect textBounds = new Rect();
        font.getTextBounds(text, 0, text.length(), textBounds);

        int fontHeight = textBounds.height();
        int margin = height / 4;
        while (fontHeight > height - margin) {
            font.setTextSize(font.getTextSize() - 1);
            font.getTextBounds(text, 0, text.length(), textBounds);
            fontHeight = textBounds.height();
        }
    }

    public static void runVictory() {
        gameOver = true;
        gameData.endTime = System.nanoTime();
        gameData.elapsedTime = gameData.endTime - gameData.startTime;

        gameData.calculateReadableTime(gameData.elapsedTime);
        gameData.calculateScore();

        Timer t = new Timer();
        t.schedule(new Victory(), 3500);

        SoundEffectPlayer.playVictory(DishWasherPackerView.context);
    }

    static class Victory extends TimerTask {
        @Override
        public void run() {
            moveToScoreScreen = true;
        }
    }

    public boolean isGameOver() {
        return moveToScoreScreen;
    }

    public static void clearPuzzle() {
        for (int i = 0; i < Map.maxFloors; i++) {

            if (i == 0) {
                rackLayouts[i] = new RackLayout(Map.columnsPerFloor[i], Map.MAXWIDTH, new int[]{utensilParams[0], utensilParams[1], utensilParams[2]});
            } else {
                rackLayouts[i] = new RackLayout(Map.columnsPerFloor[i], Map.MAXWIDTH, new int[]{0});
            }

            rackLayouts[i].generateLayout();
            dishes[i] = new ArrayList<>();

            if (currentDish != null) {
                currentDish = generator.resetScale(currentDish);
            }
        }
    }

    public static void back() {
        MusicServiceHandler.stopGameAudio();
        MusicServiceHandler.startMenuAudio();

        GameDisplayFragment.stopTimer();
        MainActivity.backAFragment("Game Display");
    }

    public int[] convertIntegerArrayToIntArray(Integer[] arr) {
        int[] newArr = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {
            newArr[i] = arr[i];
        }

        return newArr;
    }
}
