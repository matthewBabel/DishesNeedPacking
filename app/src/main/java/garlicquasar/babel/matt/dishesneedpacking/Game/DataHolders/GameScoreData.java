package garlicquasar.babel.matt.dishesneedpacking.Game.DataHolders;

import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.Map;

import java.text.DecimalFormat;

public class GameScoreData {
    public static int totalDishes;
    public static int totalSpaces;
    public static String puzzleDimensions;
    public static String dishesPerSecond;

    public static long startTime;
    public static long endTime;
    public static long elapsedTime;
    public static String readableTime;

    public static int RUNSNEEDED = 1;
    public static int runs = 0;
    public static int takenZones = 0;
    public static int dishesPickedUp = 0;
    public static int nextDishes = 0;

    public static int score;
    public static Integer[] colsPerFloor;
    public static int[] rowsPerFloor;

    private static long milliseconds;
    private static long minutes;
    private static long seconds;

    public static int level;

    public static void calculateScore() {
        double totalSeconds = milliseconds / 1000;
        double pieces = totalDishes;
        double piecesPerSec = pieces / totalSeconds;

        DecimalFormat df = new DecimalFormat("0.000");
        dishesPerSecond = df.format(totalDishes / totalSeconds);

        score = (int)(piecesPerSec * 10000);

        // - 50% for each extra run
        if (runs > RUNSNEEDED) {
            int count = 0;
            while (count < (runs - RUNSNEEDED)) {
                count++;
                score /= 2;
            }
        }

        // - 06% for each dish held
        for(int i = 0; i < dishesPickedUp; i++) {
            score -= (score * .06);
        }

        // - 03% for each nextDish click
        for(int i = 0; i < nextDishes; i++) {
            score -= (score * .03);
        }

        // - 01% for each takenZone click
        for(int i = 0; i < takenZones; i++) {
            score -= (score * .01);
        }


    }

    public static void calculateReadableTime(long nanoSeconds) {
        milliseconds = nanoSeconds / 1000000;

        minutes = (milliseconds / 1000) / 60;
        seconds = (milliseconds / 1000) % 60;

        readableTime = minutes + ":" + ((seconds < 10) ? "0" + seconds: seconds);
    }

    public static void calculateReadableArea() {
       int first = colsPerFloor[0];
       boolean same = true;

       for(int i =0; i < colsPerFloor.length; i++) {
           if (colsPerFloor[i] != first) {
               same = false;
               break;
           }
       }

       if (same) {
           puzzleDimensions = "(" + colsPerFloor[0] + " x " + rowsPerFloor[0] + " x " + Map.maxFloors + ")";
       } else {
           puzzleDimensions = "Total Spaces was " + totalSpaces;
       }
    }

    public static void resetAll() {
        runs = 0;
        takenZones = 0;
        dishesPickedUp = 0;
        level = 0;
        nextDishes = 0;
    }
}
