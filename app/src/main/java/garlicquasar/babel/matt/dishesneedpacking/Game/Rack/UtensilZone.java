package garlicquasar.babel.matt.dishesneedpacking.Game.Rack;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.Arrays;
import java.util.Random;

/**
 * A Touch Zone specifically for a utensil.
 */
public class UtensilZone extends TouchZone {

    private Point[] utensilPoints;
    private boolean[] takenPoints;
    public boolean full = false;
    public static final int maxUtensils = 5;
    public String zoneType = "none";

    public UtensilZone(Rect r, int[] neighbors) {
        super(r, neighbors, true,  true);
        generateUtensilPoints();
        initTakenPoints();
    }

    private void generateUtensilPoints() {
        utensilPoints = new Point[maxUtensils];

        Rect r = this.getZone();
        int width = r.width();
        int height = r.height();

        // this is not dynamic with maxUtensils
        utensilPoints[0] = new Point(r.left + (width/4), r.top + (height/4));
        utensilPoints[1] = new Point(r.right - (width/4), r.bottom - (height/4));
        utensilPoints[2] = new Point(r.left + (width/4), r.bottom - (height/4));
        utensilPoints[3] = new Point(r.right - (width/4), r.top + (height/4));
        utensilPoints[4] = new Point(r.left + (width/2), r.top + (height/2));
    }

    private void initTakenPoints() {
        takenPoints = new boolean[maxUtensils];
        Arrays.fill(takenPoints, false);
    }

    /**
     * randomly returns an available point
     * @return Point for a utensil to be drawn on
     */
    public Point getAvailablePoint() {
        final int random = new Random().nextInt(maxUtensils); // [0, 1000]

        if (takenPoints[random]) {
            int count = 0;
            for (boolean taken : takenPoints) {
                if (!taken) {
                    takenPoints[count] = true;
                    checkIfFull();
                    return utensilPoints[count];
                } else {
                    count++;
                }
            }
        } else {
            takenPoints[random] = true;
            checkIfFull();
            return utensilPoints[random];
        }

        // unreachable
        return utensilPoints[0];
    }

    /**
     * sets full to true if all points have been taken.
     */
    private void checkIfFull() {
        full = true;
        for (boolean taken : takenPoints) {
            if (!taken) {
                full = false;
            }
        }
    }

    private void checkIfEmpty() {
        boolean empty = true;
        for (boolean taken : takenPoints) {
            if (taken) {
                empty = false;
                break;
            }
        }

        if (empty) {
             zoneType = "none";
        }
    }

    public void removeRandomUtensil() {
        int takenCount = 0;

        for (boolean taken : takenPoints) {
            if (taken) {
                takenCount++;
            }
        }

        final int random = new Random().nextInt(takenCount); // 1000 = [0, 999]

        takenCount = 0;
        int index = 0;
        for (boolean taken : takenPoints) {
            if (taken) {
                if (takenCount == random) {
                    break;
                } else {
                    takenCount++;
                }
            }

            index++;
        }

        takenPoints[index] = false;
        full = false;
        checkIfEmpty();
    }
}
