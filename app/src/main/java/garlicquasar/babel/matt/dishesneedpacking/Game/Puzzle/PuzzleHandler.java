package garlicquasar.babel.matt.dishesneedpacking.Game.Puzzle;

import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Dishware;
import garlicquasar.babel.matt.dishesneedpacking.Game.View.DishWasherPackerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PuzzleHandler {

    private ArrayList<Dishware> dishesToPlace;
    private DishGenerator generator;

    public int dishCount = 0;

    public PuzzleHandler(DishGenerator dishGenerator, int maxPlates, int maxCups, int maxUtensils, int maxArea, int maxEdges, Integer[] cupsPerFloor, Integer[] bowlsPerFloor, Integer[] bowlsPerRow, Integer[] columnsPerFloor, Integer[] rowsPerFloor, int tupperwareAmt, int mixingBowlAmt, double cupPercentage, int plateSubtractions, int runs) {
        generator = dishGenerator;
        generateDishesToBePlaced(maxPlates, maxCups, maxUtensils, maxArea, maxEdges, cupsPerFloor, bowlsPerFloor, bowlsPerRow, columnsPerFloor, rowsPerFloor, tupperwareAmt, mixingBowlAmt, cupPercentage, plateSubtractions, runs);
    }

    private void generateDishesToBePlaced(int plates, int cups, int utensils, int area, int edges, Integer[] cupsPerFloor, Integer[] bowlsPerFloor, Integer[] bowlsPerRow, Integer[] columnsPerFloor, Integer[] rowsPerFloor, int tupperwareAmt, int mixingBowlAmt, double cupPercentage, int plateSubtractions, int runs) {


        PuzzleSolver solver = null;
        int[] plateSubtractionsRunSplit = new int[runs];

        // spread the plate subtraction spots evenly throughout the runs
        if (runs > 1) {
            switch(runs) {
                case 2:
                    if (plateSubtractions == 0) {
                        plateSubtractionsRunSplit = new int[]{0, 0};
                    } else if (plateSubtractions == 1) {
                        plateSubtractionsRunSplit = new int[]{0, 1};
                    } else if (plateSubtractions == 2) {
                        plateSubtractionsRunSplit = new int[]{1, 1};
                    } else if (plateSubtractions == 3) {
                        plateSubtractionsRunSplit = new int[]{1, 2};
                    } else if (plateSubtractions == 4) {
                        plateSubtractionsRunSplit = new int[]{2, 2};
                    } else if (plateSubtractions == 5) {
                        plateSubtractionsRunSplit = new int[]{2, 3};
                    }
                    break;
                case 3:
                    if (plateSubtractions == 0) {
                        plateSubtractionsRunSplit = new int[]{0, 0, 0};
                    } else if (plateSubtractions == 1) {
                        plateSubtractionsRunSplit = new int[]{0, 0, 1};
                    } else if (plateSubtractions == 2) {
                        plateSubtractionsRunSplit = new int[]{0, 1, 1};
                    } else if (plateSubtractions == 3) {
                        plateSubtractionsRunSplit = new int[]{1, 1, 1};
                    } else if (plateSubtractions == 4) {
                        plateSubtractionsRunSplit = new int[]{1, 1, 2};
                    } else if (plateSubtractions == 5) {
                        plateSubtractionsRunSplit = new int[]{1, 2, 2};
                    }
                    break;
                default:
                    break;
            }

            solver = new PuzzleSolver(generator, plates, cups, utensils, area, edges, cupsPerFloor, bowlsPerFloor, bowlsPerRow, columnsPerFloor, rowsPerFloor, tupperwareAmt, mixingBowlAmt, cupPercentage, plateSubtractionsRunSplit[0]);
        } else {
            solver = new PuzzleSolver(generator, plates, cups, utensils, area, edges, cupsPerFloor, bowlsPerFloor, bowlsPerRow, columnsPerFloor, rowsPerFloor, tupperwareAmt, mixingBowlAmt, cupPercentage, plateSubtractions);
        }
        dishesToPlace = solver.solve();
        shuffleDishes(dishesToPlace);

        if (runs > 1) {
            for (int i = 1; i < runs; i++) {
                solver = new PuzzleSolver(generator, plates, cups, utensils, area, edges, cupsPerFloor, bowlsPerFloor, bowlsPerRow, columnsPerFloor, rowsPerFloor, tupperwareAmt, mixingBowlAmt, cupPercentage, plateSubtractionsRunSplit[i]);
                ArrayList<Dishware> dishList = solver.solve();
                shuffleDishes(dishList);
                dishesToPlace.addAll(dishList);
            }
        }

        setDishCount();
        DishWasherPackerView.gameData.totalDishes = dishCount;
    }

    public void setDishCount() {
        dishCount = dishesToPlace.size();
    }

    private void shuffleDishes(ArrayList<Dishware> dishList) {
        Collections.shuffle(dishList);
    }

    public void randomlyInsertDish(Dishware dish) {
        if (dishesToPlace.size() == 0) {
            DishWasherPackerView.puzzleFull = false;
            dishesToPlace.add(0, dish);
        } else {
            int random = new Random().nextInt(dishesToPlace.size() + 1);
            dishesToPlace.add(random, dish);
        }
        setDishCount();
    }

    public Dishware getDish() {
        if (hasPiece()) {
            generator.resetForNextDish();
            Dishware nextDish = dishesToPlace.remove(0);
            setDishCount();
            return generator.resetScale(nextDish);
        }
        return null;
    }

    private boolean hasPiece() {
        if (dishesToPlace.size() == 0) { // no more pieces, game over
            DishWasherPackerView.puzzleFull = true;
            return false;
        } else {
            return true;
        }
    }
}
