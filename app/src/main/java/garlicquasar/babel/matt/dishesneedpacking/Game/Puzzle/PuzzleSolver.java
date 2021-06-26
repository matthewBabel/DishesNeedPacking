package garlicquasar.babel.matt.dishesneedpacking.Game.Puzzle;

import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Dishware;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.UtensilZone;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class PuzzleSolver {

    int plateSpotsOpen;
    int cupSpotsOpen;
    int maxUtensilSlots;
    int bowlSpotsTakenByTupperware = 0;

    int area;
    int edges;

    int tupperware;
    int mixingBowl;
    double cupPercentage;

    Integer[] cupSpotsPerFloor;
    Integer[] bowlSpotsPerFloor;
    Integer[] bowlSpotsPerRow;
    Integer[] columnsPerFloor;
    Integer[] rowsPerFloor;
    Integer[] bowlCountPerRack;

    ProngBooleans[][][] prongs;

    DishGenerator generator;

    boolean cupCanBePlaced = true;


    public PuzzleSolver(DishGenerator dishGenerator, int plate, int cup, int utensil, int area, int edges, Integer[] cupSpotsPerFloor,  Integer[] bowlSpotsPerFloor, Integer[] bowlSpotsPerRow, Integer[] columnsPerFloor, Integer[] rowsPerFloor, int tupperwareAmt, int mixingBowlAmt, double cupPercentage, int plateSubtractions) {
        generator = dishGenerator;
        plateSpotsOpen = plate - plateSubtractions;
        cupSpotsOpen = cup;
        maxUtensilSlots = utensil;
        this.area = area;
        this.edges = edges;
        this.tupperware = tupperwareAmt;
        this.mixingBowl = mixingBowlAmt;
        this.cupPercentage = cupPercentage;
        this.cupSpotsPerFloor = cupSpotsPerFloor;
        this.bowlSpotsPerFloor = bowlSpotsPerFloor;
        this.bowlSpotsPerRow = bowlSpotsPerRow;
        this.columnsPerFloor = columnsPerFloor;
        this.rowsPerFloor = rowsPerFloor;

        bowlCountPerRack = new Integer[bowlSpotsPerFloor.length];
        Arrays.fill(bowlCountPerRack, 0);

        Arrays.sort(cupSpotsPerFloor, Collections.reverseOrder());
        Arrays.sort(columnsPerFloor, Collections.reverseOrder());

        // init prongs array so that every prong on each floor is set to false, meaning not taken
        prongs = new ProngBooleans[rowsPerFloor.length][rowsPerFloor[0] - 1][columnsPerFloor[0] - 1];

        for (int i = 0; i < rowsPerFloor.length; i++) {
            for (int j = 0; j < rowsPerFloor[i]-1; j++) {
                for (int k = 0; k < columnsPerFloor[i]-1; k++) {
                    prongs[i][j][k] = new ProngBooleans();
                }
            }
        }
    }

    class ProngBooleans {
         public boolean takenByTupperware = false;
         public boolean takenByCupOrBowl = false;
         public boolean takenAtAll = false;
    }

    public ArrayList<Dishware> solve() {
        ArrayList<Dishware> dishes = new ArrayList<>();

        addUtensils(dishes);
        addTupperware(dishes, tupperware);
        addMixingBowls(dishes, mixingBowl);
        addCups(dishes, cupPercentage);
        addPlates(dishes);

        return dishes;
    }

    private void addUtensils(ArrayList<Dishware> dishes) {
        for (int n = 0; n < maxUtensilSlots; n++) {

            // 3 types of utensils
            int utensilType = new Random().nextInt(3);

            switch (utensilType) {
                case 0:
                    for (int i = 0; i < UtensilZone.maxUtensils; i++) {
                        dishes.add(generator.getNewKnife());
                    }
                    break;
                case 1:
                    for (int i = 0; i < UtensilZone.maxUtensils; i++) {
                        dishes.add(generator.getNewFork());
                    }
                    break;
                case 2:
                    for (int i = 0; i < UtensilZone.maxUtensils; i++) {
                        dishes.add(generator.getNewSpoon());
                    }
                    break;
            }
        }
    }

    private void addTupperware(ArrayList<Dishware> dishes, int amount) {
        int maxTupperwareSpaces = area - edges;

        int maxHeight = 4;

        if (columnsPerFloor[columnsPerFloor.length-1] < 6) {
            maxHeight = columnsPerFloor[columnsPerFloor.length-1] - 1;
        }

        for (int i = 0; i < amount; i++) {
            if (maxTupperwareSpaces < getTupperwareZonesTakenByTupperware(1, 1)) {
                break;
            }

            int height;
            int width;
            do {
                height = new Random().nextInt(maxHeight) + 1; //1 - maxHeight
                width = new Random().nextInt(height) + 1; //1 - height - so width is never bigger than height
            } while(maxTupperwareSpaces < getTupperwareZonesTakenByTupperware(width, height));


            if (setTakenProngsByTupperware(width+1, height+1) == false) {
                i--;
                maxHeight--;
                if (maxHeight <= 0) {
                    return;
                }
            } else { // set taken prongs passes, now tupperware is officially added

                // new tupperware
                maxTupperwareSpaces -= getTupperwareZonesTakenByTupperware(width, height);
                plateSpotsOpen -= getPlateZonesTakenByTupperware(width, height);
                cupSpotsOpen -= getCupZonesTakenByTupperware(width, height);
                bowlSpotsTakenByTupperware += getBowlsTakenByTupperware(width, height);
                generator.dishID++;
                dishes.add(generator.getNewTupperware(generator.dishID, width, height));

                // I'm not checking every case here
                if (maxTupperwareSpaces < getTupperwareZonesTakenByTupperware(maxHeight, maxHeight)) {
                    maxHeight--;
                }
            }
        }
    }

    private int getPlateZonesTakenByTupperware(int width, int height) {
        return (width * height) + height;
    }

    private int getCupZonesTakenByTupperware(int width, int height) {
        return (width+1) * (height+1);
    }

    private int getTupperwareZonesTakenByTupperware(int width, int height) {
        return (getPlateZonesTakenByTupperware(width, height) + ((width+1) + (height+1) -1));
    }

    private int getBowlsTakenByTupperware(int width, int height) {
        if (width == 1 && height == 1) {
            return 1;
        }

        if (width == 1 && height == 2) {
            return 2;
        }

        if (width == 1 && height == 3) {
            return 2;
        }

        if (width == 1 && height == 4) {
            return 3;
        }

        if (width == 2 && height == 2) {
            return 2;
        }

        if (width == 2 && height == 3) {
            return 4;
        }

        if (width == 2 && height == 4) {
            return 4;
        }

        if (width == 3 && height == 3) {
            return 4;
        }

        if (width == 3 && height == 4) {
            return 4;
        }

        if (width == 4 && height == 4) {
            return 6;
        }

        return 0;


        // 1x1 - 1 - 1
        // 1x2 - 2 - 2
        // 1x3 - 2 - 3
        // 1x4 - 3 - 4
        // 2x2 - 2 - 4
        // 2x3 - 4 - 6
        // 2x4 - 4 - 8
        // 3x3 - 4 - 9
        // 3x4 - 4 - 12
        // 4x4 - 6 - 16
    }

    private void addMixingBowls(ArrayList<Dishware> dishes, int amt) {
        int maxBowls = 0;
        for(int i =0; i < bowlSpotsPerFloor.length; i++) {
            maxBowls += bowlSpotsPerFloor[i];
        }

        maxBowls -= bowlSpotsTakenByTupperware;

        // check if there are 4 spaces on one floor
        int count = 0;
        int bowlsCountOnRack = 0;
        int index = 0;

        for(int i = 0; i < amt; i++) {
            int plateDecrement = 0;
            int cupDecrement = 4;


            if (bowlsCountOnRack == 0) {
                plateDecrement = 9;
            } else if (bowlsCountOnRack < bowlSpotsPerFloor[index]) {
                if (bowlsCountOnRack < bowlSpotsPerRow[index]) {
                    plateDecrement = 6;
                } else {
                    if (bowlsCountOnRack % bowlSpotsPerRow[index] == 0) {
                        plateDecrement = 6;
                    } else {
                        plateDecrement = 4;
                    }
                }
            } else {
                plateDecrement = 9;
            }



            if (bowlsCountOnRack >= bowlSpotsPerFloor[index]) {
                index++;
                if (index > bowlSpotsPerFloor.length-1) {
                    break;
                }
                bowlsCountOnRack = 0;
            }


            if (cupSpotsOpen >= cupDecrement && plateSpotsOpen >= plateDecrement && maxBowls > 0 && setTakenProngs(2, 2)) { // enough space, also setting taken prongs here
                cupSpotsOpen -= cupDecrement;
                cupSpotsPerFloor[index] -= cupDecrement;
                plateSpotsOpen -= plateDecrement;
                maxBowls -= 1;
                bowlsCountOnRack++;
                bowlCountPerRack[index] = bowlsCountOnRack;
                dishes.add(generator.getNewMixingBowl());
                count++;
            } else {
                break;
            }
        }

        Log.d(null, "Bowls: " + count);
    }

    private void addCups(ArrayList<Dishware> dishes, double percentage) {

        if (percentage == 0) {
            return;
        }

        int cups =  (int) Math.ceil(cupSpotsOpen * percentage);

        for (int i = 0; i < cups; i++ ) {


            int plateDecrement = findFutureBestCupPlacement();

            if (cupCanBePlaced) {
                plateSpotsOpen -= plateDecrement;
                dishes.add(generator.getNewCup());
            } else {
                cupCanBePlaced = true;
                return;
            }
        }
    }

    private void addPlates(ArrayList<Dishware> dishes) {
        int zonesOpen = plateSpotsOpen;

        while(zonesOpen > 4) {

            // 4 types of plates
            int plateType = new Random().nextInt(8); // 0 - 7

            switch (plateType) {
                case 0:
                    dishes.add(generator.getNewSaucer());
                    zonesOpen -= 1;
                    break;
                case 1:
                case 2:
                case 3:
                    dishes.add(generator.getNewNormalPlate());
                    zonesOpen -= 2;
                    break;
                case 4:
                case 5:
                case 6:
                    dishes.add(generator.getNewDinnerPlate());
                    zonesOpen -= 3;
                    break;
                case 7:
                    dishes.add(generator.getNewServicePlate());
                    zonesOpen -= 4;
                    break;
            }
        }

        switch(zonesOpen) {
            case 4:
                dishes.add(generator.getNewServicePlate());
                break;
            case 3:
                dishes.add(generator.getNewDinnerPlate());
                break;
            case 2:
                dishes.add(generator.getNewNormalPlate());
                break;
            case 1:
                dishes.add(generator.getNewSaucer());
                break;
            default:
                break;
        }
    }

    // returns false if not able to be placed
    private boolean setTakenProngs(int width, int height) {
        int floor = 0;
        int row = 0;
        int col = 0;
        int maxRow = rowsPerFloor[floor] - 2;
        int maxCol = columnsPerFloor[floor] - 2;

        boolean found = false;
        while(!found) {
            if (prongs[floor][row][col].takenAtAll == false) { // prong not taken
                if (((col + width-1) <= maxCol && prongs[floor][row][col+width-1].takenAtAll == false) &&
                    ((row + height-1) <= maxRow && prongs[floor][row+height-1][col+width-1].takenAtAll == false)) { //found an open spot
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            prongs[floor][row+i][col+j].takenAtAll = true;
                            prongs[floor][row+i][col+j].takenByCupOrBowl = true;
                        }
                    }
                    found = true;
                }
            }

            if (!found) {
                col++;
                if (col > maxCol) {
                    col = 0;
                    row++;

                    if (row > maxRow) {
                        row = 0;
                        floor++;

                        if (floor+1 > rowsPerFloor.length) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean setTakenProngsByTupperware(int width, int height) {
        int floor = 0;
        int maxRow = rowsPerFloor[floor] - 2;
        int maxCol = columnsPerFloor[floor] - 2;
        int row = maxRow;
        int col = maxCol;

        boolean found = false;
        while(!found) {
            if (prongs[floor][row][col].takenAtAll == false) { // prong not taken
                if (((col - width+1) > 0 && prongs[floor][row][col-width+1].takenAtAll == false) &&
                        ((row - height+1) > 0 && prongs[floor][row-height+1][col-width+1].takenAtAll == false)) { //found an open spot
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            prongs[floor][row-i][col-j].takenAtAll = true;
                            prongs[floor][row-i][col-j].takenByTupperware = true;
                        }
                    }
                    found = true;
                }
            }

            if (!found) {
                col--;
                if (col < 0) {
                    col = maxCol;
                    row--;

                    if (row < 0) {
                        row = maxRow;
                        floor++;

                        if (floor+1 > rowsPerFloor.length) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }


    private int getPlateTakenZonesAtProng(int floor, int prongRow, int prongCol) {
        int maxRow = rowsPerFloor[floor]-2;
        int maxCol = columnsPerFloor[floor]-2;

        boolean topLeftTaken = false;
        boolean topRightTaken = false;
        boolean bottomLeftTaken = false;
        boolean bottomRightTaken = false;

        // top left
        if (prongRow > 0 && prongCol > 0) {
            if (prongs[floor][prongRow-1][prongCol-1].takenByCupOrBowl == true) {
                topLeftTaken = true;
            }
        }

        // top
        if (prongRow > 0) {
            if (prongs[floor][prongRow-1][prongCol].takenByCupOrBowl == true) {
                topLeftTaken = true;
                topRightTaken = true;
            }
        }

        // top right
        if (prongRow > 0 && prongCol < maxCol) {
            if (prongs[floor][prongRow-1][prongCol+1].takenByCupOrBowl == true) {
                topRightTaken = true;
            }
        }

        // left
        if (prongCol > 0) {
            if (prongs[floor][prongRow][prongCol-1].takenByCupOrBowl == true) {
                topLeftTaken = true;
                bottomLeftTaken = true;
            }
        }

        // right
        if (prongCol < maxCol) {
            if (prongs[floor][prongRow][prongCol+1].takenByCupOrBowl == true) {
                topRightTaken = true;
                bottomRightTaken = true;
            }
        }

        // bottom left
        if (prongRow < maxRow && prongCol > 0) {
            if (prongs[floor][prongRow+1][prongCol-1].takenByCupOrBowl == true) {
                bottomLeftTaken = true;
            }
        }

        // bottom
        if (prongRow < maxRow) {
            if (prongs[floor][prongRow+1][prongCol].takenByCupOrBowl == true) {
                bottomLeftTaken = true;
                bottomRightTaken = true;
            }
        }

        // bottom right
        if (prongRow < maxRow && prongCol < maxCol) {
            if (prongs[floor][prongRow+1][prongCol+1].takenByCupOrBowl == true) {
                bottomRightTaken = true;
            }
        }


        int openZones = 0;
        if (!topLeftTaken) {
            openZones++;
        }

        if (!topRightTaken) {
            openZones++;
        }

        if (!bottomLeftTaken) {
            openZones++;
        }

        if (!bottomRightTaken) {
            openZones++;
        }

        return openZones;
    }

    private int findFutureBestCupPlacement() {
        int maxFloor = columnsPerFloor.length;
        int maxRow = rowsPerFloor[0]-2;
        int maxCol = columnsPerFloor[0]-2;
        ArrayList<Integer> savedTiedFloor = new ArrayList<>();
        ArrayList<Integer> savedTiedRow = new ArrayList<>();
        ArrayList<Integer> savedTiedCol = new ArrayList<>();


        int bestPlacementScore = 5;

        for(int floor = maxFloor-1; floor > -1; floor--) {
            for (int row = 0; row <= maxRow; row++) {
                for (int col = 0; col <= maxCol; col++) {
                    if (prongs[floor][row][col].takenAtAll == false) {
                        int score = getPlateTakenZonesAtProng(floor, row, col);
                        if (score < bestPlacementScore) {
                            bestPlacementScore = score;
                            savedTiedFloor.clear();
                            savedTiedRow.clear();
                            savedTiedCol.clear();
                            savedTiedFloor.add(floor);
                            savedTiedRow.add(row);
                            savedTiedCol.add(col);
                        } else if (score == bestPlacementScore) {
                            savedTiedFloor.add(floor);
                            savedTiedRow.add(row);
                            savedTiedCol.add(col);
                        }
                    }
                }
            }
        }

        if (bestPlacementScore == 5) {
            cupCanBePlaced = false;
            return 0;
        } else {
            cupCanBePlaced = true;

            if (savedTiedFloor.size() == 1) {
                prongs[savedTiedFloor.get(0)][savedTiedRow.get(0)][savedTiedCol.get(0)].takenAtAll = true;
                prongs[savedTiedFloor.get(0)][savedTiedRow.get(0)][savedTiedCol.get(0)].takenByCupOrBowl = true;
                return bestPlacementScore;
            } else {
                int bestFutureScore = bestPlacementScore+5;
                int savedFloor = savedTiedFloor.get(0);
                int savedRow = savedTiedRow.get(0);
                int savedCol = savedTiedCol.get(0);


                for(int i = 0; i < savedTiedFloor.size(); i++) {
                    prongs[savedTiedFloor.get(i)][savedTiedRow.get(i)][savedTiedCol.get(i)].takenAtAll = true;
                    prongs[savedTiedFloor.get(i)][savedTiedRow.get(i)][savedTiedCol.get(i)].takenByCupOrBowl = true;

                    int futureScore = findBestCupPlacement();
                    if (futureScore + bestPlacementScore < bestFutureScore) {
                        bestFutureScore = futureScore + bestPlacementScore;
                        savedFloor = savedTiedFloor.get(i);
                        savedRow = savedTiedRow.get(i);
                        savedCol = savedTiedCol.get(i);
                    }

                    prongs[savedTiedFloor.get(i)][savedTiedRow.get(i)][savedTiedCol.get(i)].takenAtAll = false;
                    prongs[savedTiedFloor.get(i)][savedTiedRow.get(i)][savedTiedCol.get(i)].takenByCupOrBowl = false;
                }

                prongs[savedFloor][savedRow][savedCol].takenAtAll = true;
                prongs[savedFloor][savedRow][savedCol].takenByCupOrBowl = true;
                return bestPlacementScore;
            }
        }
    }

    private int findBestCupPlacement() {
        int maxFloor = columnsPerFloor.length;
        int maxRow = rowsPerFloor[0]-2;
        int maxCol = columnsPerFloor[0]-2;

        int bestPlacementScore = 5;

        for(int floor = 0; floor < maxFloor; floor++) {
            for (int row = 0; row <= maxRow; row++) {
                for (int col = 0; col <= maxCol; col++) {
                    if (prongs[floor][row][col].takenAtAll == false) {
                        int score = getPlateTakenZonesAtProng(floor, row, col);
                        if (score < bestPlacementScore) {
                            bestPlacementScore = score;
                        }
                    }
                }
            }
        }

        return bestPlacementScore;
    }
}
