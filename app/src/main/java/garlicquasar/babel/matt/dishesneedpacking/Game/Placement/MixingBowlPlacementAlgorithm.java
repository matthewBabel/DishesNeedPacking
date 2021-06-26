package garlicquasar.babel.matt.dishesneedpacking.Game.Placement;

import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.MixingBowl;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.Map;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.TouchZone;
import android.graphics.Rect;

import java.util.List;

public class MixingBowlPlacementAlgorithm extends PlacementAlgorithm {

    public boolean mixingBowlPlacementAlgorithm(int mouseX, int mouseY) {
        setLocalVars();
        boolean placed = false;

        for (TouchZone z : rackLayouts[Map.curFloor].plateTouchZones) {
            if (z.getZone().contains(mouseX, mouseY)) {
                if (!z.isTakenAtAll()) {
                    int[] neighbors = {z.getLeftNeighbor(), z.getTopNeighbor(), z.getRightNeighbor(), z.getBottomNeighbor()};

                    boolean pass = true;
                    List<TouchZone> plateZones = rackLayouts[Map.curFloor].plateTouchZones;
                    for (int n : neighbors) {
                        if (n == -1) {
                            pass = false;
                            break;
                        } else if (!plateZones.get(n).isOpenForBowlEdge()) {
                            pass = false;
                            break;
                        }
                    }


                    if(pass) {
                        boolean topdown = true;
                        for (int n : neighbors) {
                            if(topdown) {
                                if (getTopNeighbor(plateZones, plateZones.get(n)).isTaken() || getTopNeighbor(plateZones, plateZones.get(n)).isTakenByTupperware() || getTopNeighbor(plateZones, plateZones.get(n)).isBowlCenter()) {
                                    pass = false;
                                    break;
                                }

                                if (getBottomNeighbor(plateZones, plateZones.get(n)).isTaken() || getBottomNeighbor(plateZones, plateZones.get(n)).isTakenByTupperware() || getBottomNeighbor(plateZones, plateZones.get(n)).isBowlCenter()) {
                                    pass = false;
                                    break;
                                }

                                topdown = false;
                            } else {
                                if (getLeftNeighbor(plateZones, plateZones.get(n)).isTaken() || getLeftNeighbor(plateZones, plateZones.get(n)).isTakenByTupperware() || getLeftNeighbor(plateZones, plateZones.get(n)).isBowlCenter()) {
                                    pass = false;
                                    break;
                                }

                                if (getRightNeighbor(plateZones, plateZones.get(n)).isTaken() || getRightNeighbor(plateZones, plateZones.get(n)).isTakenByTupperware() || getRightNeighbor(plateZones, plateZones.get(n)).isBowlCenter()) {
                                    pass = false;
                                    break;
                                }

                                topdown = true;
                            }
                        }
                    }

                    if (pass) {
                        int id = ((MixingBowl) currentDish).getId();

                        z.setTaken(true);
                        z.setTakenByCup(true);
                        z.setTakenByBowl(true);
                        z.setTakenByBowlCenter(true);
                        ((MixingBowl) currentDish).takenBowlZoneIndexes.add(plateZones.indexOf(z));
                        z.addBowlID(id);

                        boolean topDown = true;
                        for (int n : neighbors) {
                            plateZones.get(n).setTakenByBowl(true);
                            plateZones.get(n).addBowlID(id);
                            ((MixingBowl) currentDish).takenBowlEdgeZoneIndexes.add(n);

                            if (topDown) { // corners
                                getTopNeighbor(plateZones, plateZones.get(n)).setTakenByBowlCorner(true);
                                ((MixingBowl) currentDish).takenBowlCornerZoneIndexes.add(plateZones.get(n).getTopNeighbor());
                                getTopNeighbor(plateZones, plateZones.get(n)).addBowlID(id);

                                getBottomNeighbor(plateZones, plateZones.get(n)).setTakenByBowlCorner(true);
                                ((MixingBowl) currentDish).takenBowlCornerZoneIndexes.add(plateZones.get(n).getBottomNeighbor());
                                getBottomNeighbor(plateZones, plateZones.get(n)).addBowlID(id);

                                topDown = false;
                            } else {
                                getLeftNeighbor(plateZones, plateZones.get(n)).setTakenByBowlCorner(true);
                                ((MixingBowl) currentDish).takenBowlCornerZoneIndexes.add(plateZones.get(n).getLeftNeighbor());
                                getLeftNeighbor(plateZones, plateZones.get(n)).addBowlID(id);

                                getRightNeighbor(plateZones, plateZones.get(n)).setTakenByBowlCorner(true);
                                ((MixingBowl) currentDish).takenBowlCornerZoneIndexes.add(plateZones.get(n).getRightNeighbor());
                                getRightNeighbor(plateZones, plateZones.get(n)).addBowlID(id);

                                topDown = true;
                            }
                        }

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

    private TouchZone getTopNeighbor(List<TouchZone> touchZones, TouchZone curZone) {
        if (curZone.getTopNeighbor() == -1) {
            return null;
        }

        return touchZones.get(curZone.getTopNeighbor());
    }

    private TouchZone getBottomNeighbor(List<TouchZone> touchZones, TouchZone curZone) {
        if (curZone.getBottomNeighbor() == -1) {
            return null;
        }

        return touchZones.get(curZone.getBottomNeighbor());
    }

    private TouchZone getLeftNeighbor(List<TouchZone> touchZones, TouchZone curZone) {
        if (curZone.getLeftNeighbor() == -1) {
            return null;
        }

        return touchZones.get(curZone.getLeftNeighbor());
    }

    private TouchZone getRightNeighbor(List<TouchZone> touchZones, TouchZone curZone) {
        if (curZone.getRightNeighbor() == -1) {
            return null;
        }

        return touchZones.get(curZone.getRightNeighbor());
    }
}
