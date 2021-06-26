package garlicquasar.babel.matt.dishesneedpacking.Game.Placement;

import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Cup;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.Map;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.TouchZone;
import android.graphics.Rect;

import java.util.List;

public class CupPlacementAlgorithm extends PlacementAlgorithm{

    public boolean cupPlacementAlgorithm(int mouseX, int mouseY) {
        setLocalVars();

        boolean placed = false;
        for (TouchZone z : rackLayouts[Map.curFloor].cupTouchZones) {
            if (z.getZone().contains(mouseX, mouseY)) {
                if (!z.isTaken()) {
                    int[] neighbors = {z.getLeftNeighbor(), z.getTopNeighbor(), z.getRightNeighbor(), z.getBottomNeighbor()};

                    boolean pass = true;
                    List<TouchZone> plateZones = rackLayouts[Map.curFloor].plateTouchZones;
                    for (int n : neighbors) {
                        if (n != -1) {
                            if (plateZones.get(n).isTaken()) {
                                pass = false;
                                break;
                            }
                        }
                    }

                    if (pass) {
                        for (int n : neighbors) {
                            if (n != -1) {
                                plateZones.get(n).addCupID(((Cup) currentDish).getId());
                                plateZones.get(n).setTakenByCup(true);
                            }
                        }


                        z.setTaken(true);
                        ((Cup) currentDish).takenCupZoneIndexes.add(rackLayouts[Map.curFloor].cupTouchZones.indexOf(z));


                        Rect rect = z.getZone();
                        int centerX = rect.centerX();
                        int centerY = rect.centerY();
                        currentDish.setGame(new int[]{centerX, centerY});
                        dishes[Map.curFloor].add(currentDish);
                        currentDish = puzzleHandler.getDish();

                        placed = true;
                    } else {
                        setTakenZone(z.getZone());
                    }
                } else {
                    setTakenZone(z.getZone());
                }
            }
        }

        setGlobalVars();
        return placed;
    }
}
