package garlicquasar.babel.matt.dishesneedpacking.Game.DataHolders;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;

import garlicquasar.babel.matt.dishesneedpacking.Game.View.DishWasherPackerView;
import garlicquasar.babel.matt.dishesneedpacking.R;

public class PaintHolder {

    // paints
    public Paint borderPaint = new Paint();
    public Paint prongPaint = new Paint();
    public Paint dishBorderPaint = new Paint();
    public Paint backgroundPaint = new Paint();
    public Paint transparentBackgroundPaint = new Paint();
    public Paint takenPaint = new Paint();
    public Paint gameBackgroundPaint = new Paint();
    public Paint invalidPaint = new Paint();
    public Paint utensilPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public Paint pickUpButtonTogglePaint = new Paint();
    public Paint solidBlackOutlinePaint = new Paint();
    public Paint exitConfirmBoxPaint = new Paint();
    public Paint testingBorderPaint = new Paint();
    public Paint testingBackgroundPaint = new Paint();

    // text paints
    public Paint dishTextPaint = new Paint();
    public Paint floorTextPaint = new Paint();
    public Paint victoryTextPaint = new Paint();
    public Paint savedTextPaint = new Paint();
    public Paint dishCounterTextPaint = new Paint();
    public Paint dishCounterRedTextPaint = new Paint();
    public Paint runBtnTextPaint = new Paint();
    public Paint pickUpBtnTextPaint = new Paint();
    public Paint exitTextPaint = new Paint();
    public Paint yesnoPaint = new Paint();

    public PaintHolder() {
        borderPaint.setColor(Color.rgb(116, 116, 144));
        borderPaint.setTypeface(Typeface.DEFAULT);
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);

        prongPaint.setColor(Color.rgb(116, 116, 144));
        prongPaint.setTypeface(Typeface.DEFAULT);
        prongPaint.setAntiAlias(true);

       // backgroundPaint.setColor(ContextCompat.getColor(DishWasherPackerView.context, R.color.background));
        backgroundPaint.setColor(ContextCompat.getColor(DishWasherPackerView.context, R.color.buttonWhite));
        backgroundPaint.setTypeface(Typeface.DEFAULT);
        backgroundPaint.setAntiAlias(true);

        transparentBackgroundPaint.setColor(Color.argb(100, 255, 255 ,255));
        transparentBackgroundPaint.setTypeface(Typeface.DEFAULT);
        transparentBackgroundPaint.setAntiAlias(true);

        gameBackgroundPaint.setColor(Color.rgb(235,248,255));
        gameBackgroundPaint.setTypeface(Typeface.DEFAULT);
        gameBackgroundPaint.setAntiAlias(true);
        gameBackgroundPaint.setStyle(Paint.Style.FILL);

        exitConfirmBoxPaint.setColor(Color.rgb(226,227,225));
        exitConfirmBoxPaint.setTypeface(Typeface.DEFAULT);
        exitConfirmBoxPaint.setAntiAlias(true);
        exitConfirmBoxPaint.setStyle(Paint.Style.FILL);

        dishBorderPaint.setColor(Color.GREEN);
        dishBorderPaint.setTypeface(Typeface.DEFAULT);
        dishBorderPaint.setAntiAlias(true);

        testingBorderPaint.setColor(Color.GREEN);
        testingBorderPaint.setAlpha(100);
        testingBorderPaint.setTypeface(Typeface.DEFAULT);
        testingBorderPaint.setAntiAlias(true);

        testingBackgroundPaint.setColor(Color.WHITE);
        testingBackgroundPaint.setAlpha(100);
        testingBackgroundPaint.setTypeface(Typeface.DEFAULT);
        testingBackgroundPaint.setAntiAlias(true);

        utensilPaint.setColor(Color.BLACK);
        utensilPaint.setStyle(Paint.Style.FILL);
        utensilPaint.setTypeface(Typeface.DEFAULT);
        utensilPaint.setAlpha(20);

        takenPaint.setColor(Color.RED);
        takenPaint.setAlpha(150);
        takenPaint.setTypeface(Typeface.DEFAULT);
        takenPaint.setAntiAlias(true);

        pickUpButtonTogglePaint.setColor(Color.argb(200, 181, 137, 125));
        pickUpButtonTogglePaint.setTypeface(Typeface.DEFAULT);

        invalidPaint.setColor(Color.argb(100, 163, 153, 150));
        invalidPaint.setTypeface(Typeface.DEFAULT);
        invalidPaint.setAntiAlias(true);

        solidBlackOutlinePaint.setColor(Color.BLACK);
        solidBlackOutlinePaint.setStyle(Paint.Style.STROKE);
        solidBlackOutlinePaint.setTypeface(Typeface.DEFAULT);
        solidBlackOutlinePaint.setAntiAlias(true);


        // TEXT PAINTS
        dishTextPaint.setColor(Color.BLACK);
        dishTextPaint.setTypeface(Typeface.DEFAULT);
        dishTextPaint.setTextAlign(Paint.Align.RIGHT);
        dishTextPaint.setAntiAlias(true);

        floorTextPaint.setColor(Color.BLACK);
        floorTextPaint.setTypeface(Typeface.DEFAULT);
        floorTextPaint.setAntiAlias(true);
        floorTextPaint.setTextAlign(Paint.Align.CENTER);

        victoryTextPaint.setAntiAlias(true);
        victoryTextPaint.setColor(Color.BLACK);
        victoryTextPaint.setStyle(Paint.Style.FILL);
        victoryTextPaint.setTypeface(Typeface.DEFAULT);

        savedTextPaint.setAntiAlias(true);
        savedTextPaint.setColor(Color.BLACK);
        savedTextPaint.setStyle(Paint.Style.FILL);
        savedTextPaint.setTypeface(Typeface.DEFAULT);

        dishCounterTextPaint.setAntiAlias(true);
        dishCounterTextPaint.setColor(Color.BLACK);
        dishCounterTextPaint.setStyle(Paint.Style.FILL);
        dishCounterTextPaint.setTypeface(Typeface.DEFAULT);

        dishCounterRedTextPaint.setAntiAlias(true);
        dishCounterRedTextPaint.setColor(Color.RED);
        dishCounterRedTextPaint.setStyle(Paint.Style.FILL);
        dishCounterRedTextPaint.setTypeface(Typeface.DEFAULT);

        runBtnTextPaint.setAntiAlias(true);
        runBtnTextPaint.setColor(Color.BLACK);
        runBtnTextPaint.setTypeface(Typeface.DEFAULT);
        runBtnTextPaint.setTextAlign(Paint.Align.CENTER);

        pickUpBtnTextPaint.setAntiAlias(true);
        pickUpBtnTextPaint.setColor(Color.BLACK);
        pickUpBtnTextPaint.setTypeface(Typeface.DEFAULT);
        pickUpBtnTextPaint.setTextAlign(Paint.Align.CENTER);

        exitTextPaint.setAntiAlias(true);
        exitTextPaint.setColor(Color.BLACK);
        exitTextPaint.setTypeface(Typeface.DEFAULT);
        exitTextPaint.setTextAlign(Paint.Align.CENTER);

        yesnoPaint.setAntiAlias(true);
        yesnoPaint.setColor(Color.BLACK);
        yesnoPaint.setTypeface(Typeface.DEFAULT);
        yesnoPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setStrokeWidth(int borderStroke, int blackOutlineStroke) {
        borderPaint.setStrokeWidth(borderStroke);
        solidBlackOutlinePaint.setStrokeWidth(blackOutlineStroke);
    }

}
