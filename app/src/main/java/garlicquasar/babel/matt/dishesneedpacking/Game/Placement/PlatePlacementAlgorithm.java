package garlicquasar.babel.matt.dishesneedpacking.Game.Placement;

import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Plate;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.Map;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.TouchZone;
import android.graphics.Rect;

import java.util.List;

public class PlatePlacementAlgorithm extends PlacementAlgorithm {


    public boolean platePlacementAlgorithm(int mouseX, int mouseY) {
        setLocalVars();

        boolean placed = false;

        List<TouchZone> zones = rackLayouts[Map.curFloor].plateTouchZones;

        for (TouchZone z : zones) {
            if (z.getZone().contains(mouseX, mouseY)) {

                if (!z.isTakenAtAll() && currentDish instanceof Plate) {
                    Rect curRect = z.getZone();

                    int horzMargin = 5;
                    int thickness = ((Plate) currentDish).thickness;
                    int vertMargin = 5;

                    Plate plate = (Plate) currentDish;
                    int spaces = plate.spaces;

                    if (spaces == 1) {
                        if(singleSpacedPlacement(z, curRect, horzMargin, thickness, vertMargin, plate, spaces)) {
                            placed = true;
                        }
                        break;
                    } else if (spaces == 2) {
                        if(doubleSpacedPlacement(zones, z, curRect, horzMargin, thickness, vertMargin, plate, spaces)) {
                            placed = true;
                        }
                        break;
                    } else if (spaces == 3) {
                        if (tripleSpacedPlacement(zones, z, curRect, horzMargin, thickness, vertMargin, plate, spaces)) {
                            placed = true;
                        }
                        break;
                    } else if (spaces == 4) {
                        if (fourSpacedPlacement(zones, z, curRect, horzMargin, thickness, vertMargin, plate, spaces)) {
                            placed = true;
                        }
                    }

                } else { // not a plate
                    setTakenZone(z.getZone());
                }
                break;
            }
        }

        setGlobalVars();
        return placed;
    }

    private boolean singleSpacedPlacement(TouchZone z, Rect curRect, int horzMargin, int thickness, int vertMargin, Plate plate, int spaces) {
        if (isPlacementAvailable(1, z, currentDish.horizontal, null)) {
            handlePlacement(spaces, z, plate, curRect, horzMargin, vertMargin, thickness, null, null);
            return true;
        } else {
            setTakenZone(z.getZone());
            return false;
        }
    }

    private boolean doubleSpacedPlacement(List<TouchZone> zones, TouchZone z, Rect curRect, int horzMargin, int thickness, int vertMargin, Plate plate, int spaces) {
        if (plate.horizontal) {

            TouchZone[][] allPossibleNeighbors = new TouchZone[][]{new TouchZone[]{(z.getRightNeighbor() == -1) ? null : zones.get(z.getRightNeighbor())},
                    new TouchZone[]{(z.getLeftNeighbor() == -1) ? null : zones.get(z.getLeftNeighbor())}};

            int[][] allZoneUsages = new int[][] {{-1, -1, 0, -1}, {0, -1, -1, -1}};

            return iteratePossibleSelections(z, curRect, horzMargin, thickness, vertMargin, plate, spaces, allPossibleNeighbors, allZoneUsages);
        } else {

            TouchZone[][] allPossibleNeighbors = new TouchZone[][]{new TouchZone[]{(z.getBottomNeighbor() == -1) ? null : zones.get(z.getBottomNeighbor())},
                    new TouchZone[]{(z.getTopNeighbor() == -1) ? null : zones.get(z.getTopNeighbor())}};

            int[][] allZoneUsages = new int[][] {{-1, -1, -1, 0}, {-1, 0, -1, -1}};

            return iteratePossibleSelections(z, curRect, horzMargin, thickness, vertMargin, plate, spaces, allPossibleNeighbors, allZoneUsages);
        }
    }

    private boolean tripleSpacedPlacement(List<TouchZone> zones, TouchZone z, Rect curRect, int horzMargin, int thickness, int vertMargin, Plate plate, int spaces) {

        if (plate.horizontal) {

            TouchZone[][] allPossibleNeighbors = new TouchZone[][]{new TouchZone[]{(z.getLeftNeighbor() == -1) ? null : zones.get(z.getLeftNeighbor()), (z.getRightNeighbor() == -1) ? null : zones.get(z.getRightNeighbor())},
                    new TouchZone[]{(z.getRightNeighbor() == -1) ? null : zones.get(z.getRightNeighbor()), (z.getRightNeighbor() != -1 && zones.get(z.getRightNeighbor()).getRightNeighbor() != -1) ? zones.get(zones.get(z.getRightNeighbor()).getRightNeighbor()) : null},
                    new TouchZone[]{(z.getLeftNeighbor() == -1) ? null : zones.get(z.getLeftNeighbor()), (z.getLeftNeighbor() != -1 && zones.get(z.getLeftNeighbor()).getLeftNeighbor() != -1) ? zones.get(zones.get(z.getLeftNeighbor()).getLeftNeighbor()) : null}};

            int[][] allZoneUsages = new int[][] {{0, -1, 1, -1}, {-1, -1, 1, -1}, {1, -1, -1, -1}};

            return iteratePossibleSelections(z, curRect, horzMargin, thickness, vertMargin, plate, spaces, allPossibleNeighbors, allZoneUsages);
        } else {

            TouchZone[][] allPossibleNeighbors = new TouchZone[][]{new TouchZone[]{(z.getTopNeighbor() == -1) ? null : zones.get(z.getTopNeighbor()), (z.getBottomNeighbor() == -1) ? null : zones.get(z.getBottomNeighbor())},
                    new TouchZone[]{(z.getBottomNeighbor() == -1) ? null : zones.get(z.getBottomNeighbor()), (z.getBottomNeighbor() != -1 && zones.get(z.getBottomNeighbor()).getBottomNeighbor() != -1) ? zones.get(zones.get(z.getBottomNeighbor()).getBottomNeighbor()) : null},
                    new TouchZone[]{(z.getTopNeighbor() == -1) ? null : zones.get(z.getTopNeighbor()), (z.getTopNeighbor() != -1 && zones.get(z.getTopNeighbor()).getTopNeighbor() != -1) ? zones.get(zones.get(z.getTopNeighbor()).getTopNeighbor()) : null}};

            int[][] allZoneUsages = new int[][] {{-1, 0, -1, 1}, {-1, -1, -1, 1}, {-1, 1, -1, -1}};

            return iteratePossibleSelections(z, curRect, horzMargin, thickness, vertMargin, plate, spaces, allPossibleNeighbors, allZoneUsages);
        }
    }

    private boolean fourSpacedPlacement(List<TouchZone> zones, TouchZone z, Rect curRect, int horzMargin, int thickness, int vertMargin, Plate plate, int spaces) {

        if (plate.horizontal) {

            // will need to add here, left left right, left right right, left left left, right right right
            TouchZone[][] allPossibleNeighbors = new TouchZone[][]{new TouchZone[]{
                    (z.getLeftNeighbor() == -1) ? null : zones.get(z.getLeftNeighbor()), // left
                    (z.getLeftNeighbor() != -1 && zones.get(z.getLeftNeighbor()).getLeftNeighbor() != -1) ? zones.get(zones.get(z.getLeftNeighbor()).getLeftNeighbor()) : null, // left left
                    (z.getRightNeighbor() == -1) ? null : zones.get(z.getRightNeighbor())}, // right
                    new TouchZone[]{
                    (z.getLeftNeighbor() == -1) ? null : zones.get(z.getLeftNeighbor()), // left
                    (z.getRightNeighbor() == -1) ? null : zones.get(z.getRightNeighbor()), // right
                    (z.getRightNeighbor() != -1 && zones.get(z.getRightNeighbor()).getRightNeighbor() != -1) ? zones.get(zones.get(z.getRightNeighbor()).getRightNeighbor()) : null}, // right right
                    new TouchZone[]{
                    (z.getLeftNeighbor() == -1) ? null : zones.get(z.getLeftNeighbor()), // left
                    (z.getLeftNeighbor() != -1 && zones.get(z.getLeftNeighbor()).getLeftNeighbor() != -1) ? zones.get(zones.get(z.getLeftNeighbor()).getLeftNeighbor()) : null, // left left
                    (z.getLeftNeighbor() != -1 && zones.get(z.getLeftNeighbor()).getLeftNeighbor() != -1 && zones.get(zones.get(z.getLeftNeighbor()).getLeftNeighbor()).getLeftNeighbor() != -1) ? zones.get(zones.get(zones.get(z.getLeftNeighbor()).getLeftNeighbor()).getLeftNeighbor()) : null}, // left left left
                    new TouchZone[]{
                    (z.getRightNeighbor() == -1) ? null : zones.get(z.getRightNeighbor()), // right
                    (z.getRightNeighbor() != -1 && zones.get(z.getRightNeighbor()).getRightNeighbor() != -1) ? zones.get(zones.get(z.getRightNeighbor()).getRightNeighbor()) : null, // right right
                    (z.getRightNeighbor() != -1 && zones.get(z.getRightNeighbor()).getRightNeighbor() != -1 && zones.get(zones.get(z.getRightNeighbor()).getRightNeighbor()).getRightNeighbor() != -1) ? zones.get(zones.get(zones.get(z.getRightNeighbor()).getRightNeighbor()).getRightNeighbor()) : null}}; // right right right

            int[][] allZoneUsages = new int[][] {{1, -1, 2, -1}, {0, -1, 2, -1}, {2, -1, -1, -1}, {-1, -1, 2, -1}};

            return iteratePossibleSelections(z, curRect, horzMargin, thickness, vertMargin, plate, spaces, allPossibleNeighbors, allZoneUsages);
        } else {
            // top bottom bottom, top top bottom, bottom bottom bottom, top top top
            TouchZone[][] allPossibleNeighbors = new TouchZone[][]{new TouchZone[]{
                    (z.getBottomNeighbor() == -1) ? null : zones.get(z.getBottomNeighbor()), // bottom
                    (z.getBottomNeighbor() != -1 && zones.get(z.getBottomNeighbor()).getBottomNeighbor() != -1) ? zones.get(zones.get(z.getBottomNeighbor()).getBottomNeighbor()) : null, // bottom bottom
                    (z.getTopNeighbor() == -1) ? null : zones.get(z.getTopNeighbor())}, // top
                    new TouchZone[]{
                            (z.getBottomNeighbor() == -1) ? null : zones.get(z.getBottomNeighbor()), // bottom
                            (z.getTopNeighbor() == -1) ? null : zones.get(z.getTopNeighbor()), // top
                            (z.getTopNeighbor() != -1 && zones.get(z.getTopNeighbor()).getTopNeighbor() != -1) ? zones.get(zones.get(z.getTopNeighbor()).getTopNeighbor()) : null}, // top top
                    new TouchZone[]{
                            (z.getBottomNeighbor() == -1) ? null : zones.get(z.getBottomNeighbor()), // bottom
                            (z.getBottomNeighbor() != -1 && zones.get(z.getBottomNeighbor()).getBottomNeighbor() != -1) ? zones.get(zones.get(z.getBottomNeighbor()).getBottomNeighbor()) : null, // bottom bottom
                            (z.getBottomNeighbor() != -1 && zones.get(z.getBottomNeighbor()).getBottomNeighbor() != -1 && zones.get(zones.get(z.getBottomNeighbor()).getBottomNeighbor()).getBottomNeighbor() != -1) ? zones.get(zones.get(zones.get(z.getBottomNeighbor()).getBottomNeighbor()).getBottomNeighbor()) : null}, // bottom bottom bottom
                    new TouchZone[]{
                            (z.getTopNeighbor() == -1) ? null : zones.get(z.getTopNeighbor()), // top
                            (z.getTopNeighbor() != -1 && zones.get(z.getTopNeighbor()).getTopNeighbor() != -1) ? zones.get(zones.get(z.getTopNeighbor()).getTopNeighbor()) : null, // top top
                            (z.getTopNeighbor() != -1 && zones.get(z.getTopNeighbor()).getTopNeighbor() != -1 && zones.get(zones.get(z.getTopNeighbor()).getTopNeighbor()).getTopNeighbor() != -1) ? zones.get(zones.get(zones.get(z.getTopNeighbor()).getTopNeighbor()).getTopNeighbor()) : null}}; // top top top

            int[][] allZoneUsages = new int[][] {{-1, 2, -1, 1}, {-1, 2, -1, 0}, {-1, -1, -1, 2}, {-1, 2, -1, -1}};

            return iteratePossibleSelections(z, curRect, horzMargin, thickness, vertMargin, plate, spaces, allPossibleNeighbors, allZoneUsages);
        }
    }

    private boolean iteratePossibleSelections(TouchZone z, Rect curRect, int horzMargin, int thickness, int vertMargin, Plate plate, int spaces, TouchZone[][] allPossibleNeighbors, int[][] allZoneUsages) {

        int count = 0;
        for (TouchZone[] n : allPossibleNeighbors) {

            boolean passed = true;
            for (TouchZone zone : n) {
                if (zone == null) {
                    passed = false;
                    break;
                } else if (zone.isTakenAtAllForDirection(plate.horizontal)) {
                    passed = false;
                    break;
                }
            }

            if (passed) {

                if (isPlacementAvailable(spaces, z, currentDish.horizontal, n)) {
                    handlePlacement(spaces, z, plate, curRect, horzMargin, vertMargin, thickness, n, allZoneUsages[count]);
                    return true;
                }
            }

            count++;
        }

        setTakenZone(z.getZone());
        return false;
    }


    private boolean isPlacementAvailable(int spaces, TouchZone z, boolean horizontal, TouchZone[] zones) {
        switch (spaces) {
            case 1:
                return z.isCorrectDirection(horizontal);
            case 2:
            case 3:
            case 4:
                return checkSpaces(z, horizontal, zones);
            default:
                return true;
        }
    }

    private boolean checkSpaces(TouchZone z, boolean horizontal, TouchZone[] zones) {
        boolean passed = true;
        if (z.isCorrectDirection(horizontal)) {
            for (TouchZone neighbor : zones) {
                if (!neighbor.isCorrectDirection(horizontal)) {
                    passed = false;
                    break;
                }
            }
        } else {
            passed = false;
        }

        return passed;
    }

    private void handlePlacement(int spaces, TouchZone z, Plate plate, Rect curRect, int horzMargin, int vertMargin, int thickness, TouchZone[] zones, int[] zoneUsage) {
        switch (spaces) {
            case 1:
                dishes[Map.curFloor].add(setPlateOnMap(curRect, horzMargin, vertMargin, thickness, plate));
                setTakenAndDirectionZone(plate, z);
                currentDish = puzzleHandler.getDish();
                break;
            case 2:
            case 3:
            case 4:
                Rect fullRect = genPlateRect(zones, zoneUsage, curRect);
                setPlate(fullRect, horzMargin, vertMargin, thickness, plate, zones);

                setTakenAndDirectionZone(plate, z);

                for (TouchZone neighbor : zones) {
                    setTakenAndDirectionZone(plate, neighbor);
                }
                break;
            default:
                break;
        }
    }

    // zoneUsage = left top right bottom if -1 use curRect, is not use the number as an index of zones
    private Rect genPlateRect(TouchZone[] zones, int[] zoneUsage, Rect curRect) {
        return new Rect((zoneUsage[0] == -1) ? curRect.left : zones[zoneUsage[0]].getZone().left,
                (zoneUsage[1] == -1) ? curRect.top : zones[zoneUsage[1]].getZone().top,
                (zoneUsage[2] == -1) ? curRect.right : zones[zoneUsage[2]].getZone().right,
                (zoneUsage[3] == -1) ? curRect.bottom : zones[zoneUsage[3]].getZone().bottom);
    }

    private void setPlate(Rect fullRect, int horzMargin, int vertMargin, int thickness, Plate plate, TouchZone[] zones) {
        dishes[Map.curFloor].add(setPlateOnMap(fullRect, horzMargin, vertMargin, thickness, plate));

        for (TouchZone z : zones) {
            z.addPlateID(plate.getID());
            z.setTaken(true);
        }
        currentDish = puzzleHandler.getDish();
    }

    private Plate setPlateOnMap(Rect rect, int horzMargin, int vertMargin, int thickness, Plate plate) {
        thickness /= 2;

        if (plate.horizontal) {
            plate.setGame(new int[]{rect.left + horzMargin,
                    rect.top + (((rect.bottom - rect.top) / 2) - thickness),
                    rect.right - horzMargin,
                    rect.top + (((rect.bottom - rect.top) / 2) + thickness)});
        } else {
            plate.setGame(new int[]{rect.left + (((rect.right - rect.left) / 2) - thickness),
                    rect.top + vertMargin,
                    rect.left + (((rect.right - rect.left) / 2) + thickness),
                    rect.bottom - vertMargin});
        }

        plate.setGameRectSelectionBound(rect);

        return plate;
    }
}
