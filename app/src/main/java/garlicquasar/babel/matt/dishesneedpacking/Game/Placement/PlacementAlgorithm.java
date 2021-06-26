package garlicquasar.babel.matt.dishesneedpacking.Game.Placement;

import garlicquasar.babel.matt.dishesneedpacking.Game.DataHolders.GameScoreData;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Dishware;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Plate;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Tupperware;
import garlicquasar.babel.matt.dishesneedpacking.Game.Puzzle.DishGenerator;
import garlicquasar.babel.matt.dishesneedpacking.Game.Puzzle.PuzzleHandler;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.Map;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.RackLayout;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.TouchZone;
import garlicquasar.babel.matt.dishesneedpacking.Game.View.DishWasherPackerView;
import android.graphics.Rect;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Holds the algorithms used to determine if a dish can be placed in the chosen TouchZone.
 */
public class PlacementAlgorithm {

    protected RackLayout[] rackLayouts; // different rack layers
    protected DishGenerator generator;
    protected PuzzleHandler puzzleHandler;
    protected List<Dishware>[] dishes; // each rack has a different array of dishes
    protected Dishware currentDish;
    protected Dishware holdingDish;


    public Rect localTakenRect;


    public PlacementAlgorithm() {
        setLocalVars();
    }

    protected void setLocalVars() {
        rackLayouts = DishWasherPackerView.rackLayouts;
        generator = DishWasherPackerView.generator;
        puzzleHandler = DishWasherPackerView.puzzleHandler;
        dishes = DishWasherPackerView.dishes;
        currentDish = DishWasherPackerView.currentDish;
    }

    protected void setGlobalVars() {
        DishWasherPackerView.rackLayouts = rackLayouts;
        DishWasherPackerView.generator = generator;
        DishWasherPackerView.dishes = dishes;
        DishWasherPackerView.currentDish = currentDish;
        DishWasherPackerView.puzzleHandler = puzzleHandler;
    }


    /**
     * Switched floors, reset current dish with a new scale and set Map variables.
     *
     * @param floor floor switched to.
     */
    public void layoutSwitch(int floor) {
        setLocalVars();

        Map.curFloor = floor;

        if (currentDish != null) {
            currentDish = generator.resetScale(currentDish);
        }

        if (holdingDish != null) {
            holdingDish = generator.resetScale(holdingDish);
        }

        Map.takenZone = null;
        Map.takenRuns = 0;

        setGlobalVars();
    }

    protected void setTakenAndDirectionZone(Dishware dish, TouchZone zone) {
        zone.setTaken(true);

        List<TouchZone> zones = rackLayouts[Map.curFloor].plateTouchZones;

        if (dish instanceof Plate) {
            ((Plate) dish).takenPlateZoneIndexes.add(zones.indexOf(zone));
        } else if (dish instanceof Tupperware) {
            ((Tupperware) dish).takenLidIndexes.add(zones.indexOf(zone));
        }

        if (dish.horizontal) {
            zone.setZoneHorizontal(true);
        } else {
            zone.setZoneVertical(true);
        }
    }

    protected void setTakenZone(Rect r) {
        localTakenRect = r;
        runTakenZone();
    }

    public void runTakenZone() {
        Map.takenZone = localTakenRect;
        Map.takenRuns++;
        GameScoreData.takenZones++;

        DishWasherPackerView.showTaken = true;
        Timer t = new Timer();
        t.schedule(new InvalidZoneReset(), 600);
    }


    class InvalidZoneReset extends TimerTask {
        @Override
        public void run() {
            Map.takenRuns--;
            if (Map.takenRuns <= 0) {
                if (localTakenRect == Map.takenZone) {
                    localTakenRect = null;
                }

                Map.takenZone = null;
                DishWasherPackerView.showTaken = false;
            }
        }
    }
}