package garlicquasar.babel.matt.dishesneedpacking.Game.Placement;

import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Tupperware;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.Map;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.TouchZone;
import garlicquasar.babel.matt.dishesneedpacking.Game.View.DishWasherPackerView;
import android.graphics.Rect;

import java.util.List;

public class TupperwarePlacementAlgorithm extends PlacementAlgorithm {



    public static boolean dishScreenPress = false;
    public static boolean selectionPress = false;

    /**
     * Algorithm for tupperware placement.
     *
     * @param mouseX
     * @param mouseY
     */
    public boolean tupperwarePlacementAlgorithm(int mouseX, int mouseY) {
        setLocalVars();

        Tupperware tupperware = (Tupperware) currentDish;

        if(Map.DISH_RECT.contains(mouseX, mouseY)) {
            dishScreenPress = true;
            if (tupperware.dishSelectionLid.contains(mouseX, mouseY)) { // checking for selection
                if (!tupperware.onePiecePlaced() && tupperware.isContainerSelected()) {
                    selectionPress = true;
                }
                tupperware.setContainerSelected(false);
                return false;
            } else if (tupperware.dishSelectionContainer.contains(mouseX, mouseY)) { // checking for selection
                if (!tupperware.onePiecePlaced() && !tupperware.isContainerSelected()) {
                    selectionPress = true;
                }
                tupperware.setContainerSelected(true);
                return false;
            }
        } else {

            List<TouchZone> allZones = rackLayouts[Map.curFloor].plateTouchZones;

            for (TouchZone z : allZones) {
                if (z.getZone().contains(mouseX, mouseY)) { // found touched zone

                    if (!z.isTakenAtAll()) {

                        boolean horz = tupperware.horizontal;
                        int actualWidth = tupperware.spacesWide;
                        int actualLength = tupperware.spacesLong;


                        //size - index
                        int[][] left = new int[][]{{0}, {0, 1}, {1, 0, 2}, {1, 2, 0, 3}, {2, 1, 3, 0, 4}, {2, 3, 1, 4, 0, 5}};
                        int[][] right = new int[][]{{0}, {1, 0}, {1, 2, 0}, {2, 1, 3, 0}, {2, 3, 1, 4, 0}, {3, 2, 4, 1, 5, 0}};
                        int[][] up = new int[][]{{0}, {0, 1}, {1, 0, 2}, {1, 2, 0, 3}, {2, 1, 3, 0, 4}, {2, 3, 1, 4, 0, 5}};
                        int[][] down = new int[][]{{0}, {1, 0}, {1, 2, 0}, {2, 1, 3, 0}, {2, 1, 3, 0, 4}, {3, 2, 4, 1, 5, 0}};


                        if (horz) {
                            int lengthSpaces = actualLength - 1;
                            int widthSpaces = actualWidth - 1;

                            for (int i = 0; i < actualWidth; i++) {
                                for (int j = 0; j < actualLength; j++) {
                                    if (tryPlacement(tupperware, allZones, z, left[lengthSpaces][j], right[lengthSpaces][j], up[widthSpaces][i], down[widthSpaces][i]))
                                        return true;
                                }
                            }
                        } else {
                            int lengthSpaces = actualLength - 1;
                            int widthSpaces = actualWidth - 1;

                            for (int i = 0; i < actualLength; i++) {
                                for (int j = 0; j < actualWidth; j++) {
                                    if (tryPlacement(tupperware, allZones, z, left[widthSpaces][j], right[widthSpaces][j], up[lengthSpaces][i], down[lengthSpaces][i]))
                                        return true;
                                }
                            }
                        }
                    }

                    setTakenZone(z.getZone());
                    return false;
                }
            }
        }

        return false;
    }

    private boolean tryPlacement(Tupperware tupperware, List<TouchZone> allZones, TouchZone z, int spacesLeft, int spacesRight, int spacesUp, int spacesDown) {
        if (isTouchZoneAreaOpen(allZones.toArray(new TouchZone[0]), z, spacesRight, spacesLeft, spacesDown, spacesUp)) {

            if (tupperware.onePiecePlaced() && !tupperware.reset) {
                setTupperwareOnMap(allZones, z, spacesRight, spacesLeft, spacesDown, spacesUp, tupperware);
                currentDish = puzzleHandler.getDish();
            } else if (tupperware.onePiecePlaced() && tupperware.reset) {
                dishes[Map.curFloor].add(setTupperwareOnMap(allZones, z, spacesRight, spacesLeft, spacesDown, spacesUp, tupperware));
                currentDish = puzzleHandler.getDish();
            } else {
                dishes[Map.curFloor].add(setTupperwareOnMap(allZones, z, spacesRight, spacesLeft, spacesDown, spacesUp, tupperware));
            }

            setGlobalVars();
            return true;
        }
        return false;
    }

    /**
     * Checks if multiple zones are open, the current zone, and the zones (n) spaces Right, (n) spaces Left, ect.
     *
     * @param zones       all zones
     * @param curZone     current zone, pressed by user
     * @param spacesRight number of zones to the right of current zone to check
     * @param spacesLeft  number of zones to the left of current zone to check
     * @param spacesDown  number of zones to bottom of current zone to check
     * @param spacesUp    number of zones to the top of current zone to check
     * @return true if zone are is open
     */
    private boolean isTouchZoneAreaOpen(TouchZone[] zones, TouchZone curZone, int spacesRight, int spacesLeft, int spacesDown, int spacesUp) {


        boolean container = ((Tupperware) currentDish).isContainerSelected();

        if (container && !curZone.isOpenForTupperware()) {
            return false;
        } else if (container && !isTupperwareSpotOpen(zones, curZone)) {
            return false;
        } else if (!container && !curZone.isCorrectDirection(currentDish.horizontal)) {
            return false;
        }

        if (!checkVertZones(zones, curZone, spacesDown, spacesUp, container)) {
            return false;
        }

        if (!checkZonesDirectionally(3, spacesRight, curZone, container, zones, spacesDown, spacesUp, false)) {
            return false;
        }

        if (!checkZonesDirectionally(1, spacesLeft, curZone, container, zones, spacesDown, spacesUp, false)) {
            return false;
        }

        return true;
    }

    private boolean checkZonesDirectionally(int direction, int spaces, TouchZone curZone, boolean container, TouchZone[] zones, int spacesDown, int spacesUp, boolean vert) {
        for (int i = 0; i < spaces; i++) {

            TouchZone nextZone = null;

            switch (direction) {
                case 1:
                    if (curZone.getLeftNeighbor() == -1) {
                        return false;
                    }

                    nextZone = zones[curZone.getLeftNeighbor()];
                    break;
                case 2:
                    if (curZone.getTopNeighbor() == -1) {
                        return false;
                    }

                    nextZone = zones[curZone.getTopNeighbor()];
                    break;
                case 3:
                    if (curZone.getRightNeighbor() == -1) {
                        return false;
                    }

                    nextZone = zones[curZone.getRightNeighbor()];
                    break;
                case 4:
                    if (curZone.getBottomNeighbor() == -1) {
                        return false;
                    }

                    nextZone = zones[curZone.getBottomNeighbor()];
                    break;

            }

            if (container && !nextZone.isOpenForTupperware()) {
                return false;
            } else if (container && !isTupperwareSpotOpen(zones, nextZone)) {
                return false;
            } else if (!container && !nextZone.isCorrectDirection(currentDish.horizontal)) {
                return false;
            } else {
                curZone = nextZone;

                if (!vert) {
                    if (!checkVertZones(zones, curZone, spacesDown, spacesUp, container)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Does the vertical zone checking for a given zone.
     *
     * @param zones
     * @param curZone
     * @param spacesDown
     * @param spacesUp
     * @param container  true if tupperware container is being placed
     * @return
     */
    private boolean checkVertZones(TouchZone[] zones, TouchZone curZone, int spacesDown, int spacesUp, boolean container) {

        TouchZone workingZone = curZone;

        if (!checkZonesDirectionally(4, spacesDown, workingZone, container, zones, spacesUp, spacesDown, true)) {
            return false;
        }

        if (!checkZonesDirectionally(2, spacesUp, workingZone, container, zones, spacesUp, spacesDown, true)) {
            return false;
        }

        return true;
    }

    /**
     * Checks if zone is free directionally.
     * Meaning horizontal plates/lids don't go into tupperware overflow on the left and right,
     * and vertical plates/lids don't go into the tupperware overflow on the top and bottom.
     *
     * @param zones
     * @param curZone
     * @return true if free directionally
     */
    private boolean isTupperwareSpotOpen(TouchZone[] zones, TouchZone curZone) {
        if (curZone.getLeftNeighbor() != -1 && (zones[curZone.getLeftNeighbor()].isZoneHorz() || !zones[curZone.getLeftNeighbor()].tupperwareOverflowIsAllowed())) {
            return false;
        }

        if (curZone.getTopNeighbor() != -1 && (zones[curZone.getTopNeighbor()].isZoneVert() || !zones[curZone.getTopNeighbor()].tupperwareOverflowIsAllowed())) {
            return false;
        }


        if (curZone.getRightNeighbor() != -1 && (zones[curZone.getRightNeighbor()].isZoneHorz() || !zones[curZone.getRightNeighbor()].tupperwareOverflowIsAllowed())) {
            return false;
        }


        if (curZone.getBottomNeighbor() != -1 && (zones[curZone.getBottomNeighbor()].isZoneVert() || !zones[curZone.getBottomNeighbor()].tupperwareOverflowIsAllowed())) {
            return false;
        }

        // checking corners to see if tupperware overflow is allowed, doing this so utensil spots aren't at the corners of tupperware
        if ((curZone.getLeftNeighbor() != -1 && zones[curZone.getLeftNeighbor()].getTopNeighbor() != -1) && (!zones[zones[curZone.getLeftNeighbor()].getTopNeighbor()].tupperwareOverflowIsAllowed())) {
            return false;
        }

        if ((curZone.getTopNeighbor() != -1 && zones[curZone.getTopNeighbor()].getRightNeighbor() != -1) && ((!zones[zones[curZone.getTopNeighbor()].getRightNeighbor()].tupperwareOverflowIsAllowed()))) {
            return false;
        }

        if ((curZone.getRightNeighbor() != -1 && zones[curZone.getRightNeighbor()].getBottomNeighbor() != -1) && ((!zones[zones[curZone.getRightNeighbor()].getBottomNeighbor()].tupperwareOverflowIsAllowed()))) {
            return false;
        }

        if ((curZone.getBottomNeighbor() != -1 && zones[curZone.getBottomNeighbor()].getLeftNeighbor() != -1) && ((!zones[zones[curZone.getBottomNeighbor()].getLeftNeighbor()].tupperwareOverflowIsAllowed()))) {
            return false;
        }

        return true;
    }

    /**
     * Sets the tupperware on the map, calculating the correct Rect object parameters.
     *
     * @param zonesList
     * @param curZone
     * @param spacesRight
     * @param spacesLeft
     * @param spacesDown
     * @param spacesUp
     * @param tupper
     * @return tupperware after setting game
     */
    private Tupperware setTupperwareOnMap(List<TouchZone> zonesList, TouchZone curZone, int spacesRight, int spacesLeft, int spacesDown, int spacesUp, Tupperware tupper) {
        Rect rightRect;
        Rect leftRect;
        Rect downRect;
        Rect upRect;

        TouchZone[] zones = zonesList.toArray(new TouchZone[0]);

        boolean container = tupper.isContainerSelected();

        if (container) {
            curZone.addTupperwareID(tupper.id);
            curZone.setTupperwarePlacementZoneOn();
            tupper.addToTakenTupperwareIndex(zonesList.indexOf(curZone));
            setSurroundingZonesTupperware(zonesList, curZone);
        } else {
            setTakenAndDirectionZone(tupper, curZone);
        }

        leftRect = getFurthestDirectionalRect(zonesList, curZone, spacesLeft, tupper, zones, container, 1, false, spacesDown, spacesUp);
        rightRect = getFurthestDirectionalRect(zonesList, curZone, spacesRight, tupper, zones, container, 3, false, spacesDown, spacesUp);
        upRect = getFurthestDirectionalRect(zonesList, curZone, spacesUp, tupper, zones, container, 2, true, spacesDown, spacesUp);
        downRect = getFurthestDirectionalRect(zonesList, curZone, spacesDown, tupper, zones, container, 4, true, spacesDown, spacesUp);

        tupper.setGame(new int[]{leftRect.left, upRect.top, rightRect.right, downRect.bottom});
        return tupper;
    }

    private Rect getFurthestDirectionalRect(List<TouchZone> zonesList, TouchZone curZone, int spaces, Tupperware tupper, TouchZone[] zones, boolean container, int direction, boolean vert, int spacesDown, int spacesUp) {
        TouchZone workingZone;
        Rect rect;
        workingZone = curZone;
        for (int i = 0; i < spaces; i++) {
            switch (direction) {
                case 1:
                    workingZone = zones[workingZone.getLeftNeighbor()];
                    break;
                case 2:
                    workingZone = zones[workingZone.getTopNeighbor()];
                    break;
                case 3:
                    workingZone = zones[workingZone.getRightNeighbor()];
                    break;
                case 4:
                    workingZone = zones[workingZone.getBottomNeighbor()];
                    break;
            }


            if (container) {
                workingZone.addTupperwareID(tupper.id);
                workingZone.setTupperwarePlacementZoneOn();
                tupper.addToTakenTupperwareIndex(zonesList.indexOf(workingZone));
                setSurroundingZonesTupperware(zonesList, workingZone);
            } else {
                setTakenAndDirectionZone(tupper, workingZone);
            }

            if (!vert) {
                setVertZones(zonesList, spacesDown, spacesUp, tupper, zones, container, workingZone);
            }

        }
        rect = workingZone.getZone();
        return rect;
    }

    private void setVertZones(List<TouchZone> zonesList, int spacesDown, int spacesUp, Tupperware tupper, TouchZone[] zones, boolean container, TouchZone workingZone) {
        TouchZone vertZone = workingZone;
        for (int j = 0; j < spacesDown; j++) {
            vertZone = zones[vertZone.getBottomNeighbor()];

            if (container) {
                vertZone.addTupperwareID(tupper.id);
                vertZone.setTupperwarePlacementZoneOn();
                tupper.addToTakenTupperwareIndex(zonesList.indexOf(vertZone));
                setSurroundingZonesTupperware(zonesList, vertZone);
            } else {
                setTakenAndDirectionZone(tupper, vertZone);
            }
        }

        vertZone = workingZone;
        for (int j = 0; j < spacesUp; j++) {
            vertZone = zones[vertZone.getTopNeighbor()];

            if (container) {
                vertZone.addTupperwareID(tupper.id);
                vertZone.setTupperwarePlacementZoneOn();
                tupper.addToTakenTupperwareIndex(zonesList.indexOf(vertZone));
                setSurroundingZonesTupperware(zonesList, vertZone);
            } else {
                setTakenAndDirectionZone(tupper, vertZone);
            }
        }
    }

    /**
     * sets surrounding zones of a tupperware so that another tupperware can't be placed where overflow is,
     * and directional pieces can't be placed where overflow is.
     *
     * @param zonesList
     * @param curZone
     */
    private void setSurroundingZonesTupperware(List<TouchZone> zonesList, TouchZone curZone) {
        Tupperware dish = (Tupperware) DishWasherPackerView.currentDish;

        TouchZone[] zones = zonesList.toArray(new TouchZone[0]);

        curZone.addZoneOnTupperwareID(dish.id);
        dish.takenTupperwareZoneOnIndex.add(zonesList.indexOf(curZone));
        setVerticalZonesTupperware(zones, zonesList, dish, curZone);
        setHorizontalZonesTupperware(zones, zonesList, dish, curZone);

        setSurroundingDirectionalZonesTupperware(1, curZone, zones, zonesList, dish);

        setSurroundingDirectionalZonesTupperware(2, curZone, zones, zonesList, dish);

        setSurroundingDirectionalZonesTupperware(3, curZone, zones, zonesList, dish);

        setSurroundingDirectionalZonesTupperware(4, curZone, zones, zonesList, dish);
    }

    private void setSurroundingDirectionalZonesTupperware(int direction, TouchZone curZone, TouchZone[] zones, List<TouchZone> zonesList, Tupperware dish) {
        switch (direction) {
            case 1:
                if (curZone.getLeftNeighbor() != -1) {
                    TouchZone leftZone = zones[curZone.getLeftNeighbor()];
                    leftZone.addDirectionalTupperwareID(dish.id);
                    leftZone.setTupperwareZoneDirection(true);
                    dish.addToDirectionalTupperwareZoneOnIndex(zonesList.indexOf(leftZone));

                    setVerticalZonesTupperware(zones, zonesList, dish, leftZone);
                }
                break;
            case 2:
                if (curZone.getTopNeighbor() != -1) {
                    TouchZone topZone = zones[curZone.getTopNeighbor()];
                    topZone.addDirectionalTupperwareID(dish.id);
                    topZone.setTupperwareZoneDirection(false);
                    dish.addToDirectionalTupperwareZoneOnIndex(zonesList.indexOf(topZone));

                    setHorizontalZonesTupperware(zones, zonesList, dish, topZone);
                }
                break;
            case 3:
                if (curZone.getRightNeighbor() != -1) {
                    TouchZone rightZone = zones[curZone.getRightNeighbor()];
                    rightZone.addDirectionalTupperwareID(dish.id);
                    rightZone.setTupperwareZoneDirection(true);
                    dish.addToDirectionalTupperwareZoneOnIndex(zonesList.indexOf(rightZone));

                    setVerticalZonesTupperware(zones, zonesList, dish, rightZone);
                }
                break;
            case 4:
                if (curZone.getBottomNeighbor() != -1) {
                    TouchZone bottomZone = zones[curZone.getBottomNeighbor()];
                    bottomZone.addDirectionalTupperwareID(dish.id);
                    bottomZone.setTupperwareZoneDirection(false);
                    dish.addToDirectionalTupperwareZoneOnIndex(zonesList.indexOf(bottomZone));

                    setHorizontalZonesTupperware(zones, zonesList, dish, bottomZone);
                }
                break;
        }

    }

    private void setVerticalZonesTupperware(TouchZone[] zones, List<TouchZone> zonesList, Tupperware dish, TouchZone curZone) {
        if (curZone.getTopNeighbor() != -1) {
            zones[curZone.getTopNeighbor()].addZoneOnTupperwareID(dish.id);
            zones[curZone.getTopNeighbor()].setTupperwareZoneOn(true, dish.id);
            dish.addToTakenTupperwareZoneOnIndex(zonesList.indexOf(zones[curZone.getTopNeighbor()]));
        }

        if (curZone.getBottomNeighbor() != -1) {
            zones[curZone.getBottomNeighbor()].addZoneOnTupperwareID(dish.id);
            zones[curZone.getBottomNeighbor()].setTupperwareZoneOn(true, dish.id);
            dish.addToTakenTupperwareZoneOnIndex(zonesList.indexOf(zones[curZone.getBottomNeighbor()]));
        }
    }

    private void setHorizontalZonesTupperware(TouchZone[] zones, List<TouchZone> zonesList, Tupperware dish, TouchZone curZone) {
        if (curZone.getLeftNeighbor() != -1) {
            zones[curZone.getLeftNeighbor()].addZoneOnTupperwareID(dish.id);
            zones[curZone.getLeftNeighbor()].setTupperwareZoneOn(true, dish.id);
            dish.addToTakenTupperwareZoneOnIndex(zonesList.indexOf(zones[curZone.getLeftNeighbor()]));
        }

        if (curZone.getRightNeighbor() != -1) {
            zones[curZone.getRightNeighbor()].addZoneOnTupperwareID(dish.id);
            zones[curZone.getRightNeighbor()].setTupperwareZoneOn(true, dish.id);
            dish.addToTakenTupperwareZoneOnIndex(zonesList.indexOf(zones[curZone.getRightNeighbor()]));
        }
    }
}
