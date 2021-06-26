package garlicquasar.babel.matt.dishesneedpacking.Game.Rack;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * A rectangle representing a zone that can be pressed. Holds various parameters to determine if a
 * dish is allowed to be placed in the zone.
 */
public class TouchZone {
    private Rect zone;
    private boolean taken = false;
    private boolean takenByCup = false;
    private boolean takenByBowl = false;
    private boolean takenByBowlCorner = false;
    private boolean takenByBowlCenter = false;
    private boolean utensilZone = false;
    private TupperwareZone tupperwareZone = new TupperwareZone();
    private boolean horzZone = false;
    private boolean vertZone = false;
    private boolean allowTupperOverflow = true;
    private boolean layoutEdge;

    // for tracking
    private List<Integer> tupperwareIDs;
    private List<Integer> tupperwareDirectionalIDs;
    private List<Integer> tupperwareZoneOnIDs;
    private List<Integer> cupIDs;
    private List<Integer> bowlIDs;
    private List<Integer> plateIDs;

    private int[] neighborIndexes;

    /**
     * values only for tupperware zone,
     * if zone is on then a tupperware is here or being overflowed into this zone;
     * if it is overflowed than certain pieces can still be placed but at the correct direction.
     *
     * if zone holds horizontal is true than zone can hold horizontal plates and tupperware lids
     *
     * if directional is false than zone holds any direction plate and tupperware lid
     */
    private class TupperwareZone {
        boolean zoneOn = false;
        boolean zoneHoldsHorz = false;
        boolean directional = false;
    }

    public TouchZone(Rect r, int[] neighbors, boolean allowTupper, boolean edge) {
        zone = r;
        neighborIndexes = neighbors;
        tupperwareIDs = new ArrayList<>();
        tupperwareDirectionalIDs = new ArrayList<>();
        tupperwareZoneOnIDs = new ArrayList<>();
        cupIDs = new ArrayList<>();
        bowlIDs = new ArrayList<>();
        plateIDs = new ArrayList<>();

        if (!allowTupper) {
            tupperwareIDs.add(-1);
            setTupperwareZoneOn(true, -1);
        }

        layoutEdge = edge;
    }

    public void addTupperwareID(int id) {
        if (!tupperwareIDs.contains(id)) {
            tupperwareIDs.add(id);
        }
    }

    public void removeTupperwareID(int id) {
        for (int i = 0; i < tupperwareIDs.size(); i++) {
            if (tupperwareIDs.get(i) == id) {
                tupperwareIDs.remove(i);
            }
        }
    }

    public void addDirectionalTupperwareID(int id) {
        if (!tupperwareDirectionalIDs.contains(id)) {
            tupperwareDirectionalIDs.add(id);
        }
    }

    public void removeDirectionalTupperwareID(int id) {
        for (int i = 0; i < tupperwareDirectionalIDs.size(); i++) {
            if (tupperwareDirectionalIDs.get(i) == id) {
                tupperwareDirectionalIDs.remove(i);
            }
        }
    }

    public void addZoneOnTupperwareID(int id) {
        if (!tupperwareZoneOnIDs.contains(id)) {
            tupperwareZoneOnIDs.add(id);
        }
    }

    public void removeZoneOnTupperwareID(int id) {
        for (int i = 0; i < tupperwareZoneOnIDs.size(); i++) {
            if (tupperwareZoneOnIDs.get(i) == id) {
                tupperwareZoneOnIDs.remove(i);
            }
        }
    }

    public void addCupID(int id) {

        if (!cupIDs.contains(id)) {
            cupIDs.add(id);
        }
    }

    public void removeCupID(int id) {
        for (int i = 0; i < cupIDs.size(); i++) {
            if (cupIDs.get(i) == id) {
                cupIDs.remove(i);
            }
        }
    }

    public void addBowlID(int id) {
        if (!bowlIDs.contains(id)) {
            bowlIDs.add(id);
        }
    }

    public void removeBowlID(int id) {
        for (int i = 0; i < bowlIDs.size(); i++) {
            if (bowlIDs.get(i) == id) {
                bowlIDs.remove(i);
            }
        }
    }

    public void addPlateID(int id) {
        if (!plateIDs.contains(id)) {
            plateIDs.add(id);
        }
    }

    public void removePlateID(int id) {
        for (int i = 0; i < plateIDs.size(); i++) {
            if (plateIDs.get(i) == id) {
                plateIDs.remove(i);
            }
        }
    }

    public void setTaken(boolean isTaken) {
        if (!isTaken && plateIDs.isEmpty() && bowlIDs.isEmpty()) {
            taken = false;
        } else if (isTaken) {
            taken = true;
        }
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTakenByCup(boolean isTaken) {
        if (!isTaken && cupIDs.isEmpty()) {
            takenByCup = false;
        } else if (isTaken) {
            takenByCup = true;
        }
    }

    public void setTakenByBowl(boolean isTaken) {
        if (!isTaken && bowlIDs.isEmpty()) {
            takenByBowl = false;
        } else if (isTaken) {
            takenByBowl = true;
        }
    }

    public void setTakenByBowlCorner(boolean isTaken) {
        if (!isTaken && bowlIDs.isEmpty()) {
            takenByBowlCorner = false;
        } else if (isTaken) {
            takenByBowlCorner = true;
        }
    }

    public void setTakenByBowlCenter(boolean isTaken) {
        if (!isTaken && bowlIDs.isEmpty()) {
            takenByBowlCenter = false;
        } else if (isTaken) {
            takenByBowlCenter = true;
        }
    }

    public boolean isOpenForBowlEdge() {
        if (takenByBowlCenter) {
            return false;
        } else if (takenByBowl) {
            return true;
        } else {
            return !taken;
        }
    }

    public boolean isBowlCenter() {
        return takenByBowlCenter;
    }

    public boolean tupperwareOverflowIsAllowed() {
        return allowTupperOverflow;
    }

    public void setTupperwareOverflowIsAllowed(boolean isAllowed) {
        allowTupperOverflow = isAllowed;
    }

    public void setUtensilZone(boolean zone) {
        utensilZone = zone;
    }

    public void setTupperwareZoneOn(boolean on, int id) {
        if (on) {
            tupperwareZone.zoneOn = true;
        } else {
            if (tupperwareZoneOnIDs.contains(id)) {
                tupperwareZoneOnIDs.remove(tupperwareDirectionalIDs.indexOf(id));
            }

            if (tupperwareZoneOnIDs.isEmpty()) {
                tupperwareZone.zoneOn = false;
            }
        }
    }

    /**
     * set tupperware zone on and only allow the dishes with the opposite direction given.
     *
     * @param horz direction given
     */
    public void setTupperwareZoneDirection(boolean horz) {
        tupperwareZone.zoneOn = true;
        tupperwareZone.zoneHoldsHorz = !horz;
        tupperwareZone.directional = true;
    }

    public void setTupperwareZoneDirectionOff() {
        if (tupperwareDirectionalIDs.isEmpty()) {
            tupperwareZone.directional = false;
        }
    }

    /**
     * used when tupperware is covering the whole space,
     * taken is set to true so no pieces can be placed.
     */
    public void setTupperwarePlacementZoneOn() {
        tupperwareZone.zoneOn = true;
        tupperwareZone.zoneHoldsHorz = true;
        taken = true;
    }

    public void setTupperwareTakenZoneOff() {
        if (tupperwareIDs.isEmpty()) {
            taken = false;
        }
    }

    public boolean isTakenAtAll() {
        return taken || takenByCup || utensilZone;
    }

    public boolean isTakenAtAllForDirection(boolean horz) {
        return !isCorrectDirection(horz);
    }

    public boolean isOpenForTupperware() {
        return !isTakenAtAll() && !tupperwareZone.zoneOn && !layoutEdge && !takenByBowlCorner && !takenByBowl;
    }

    public boolean isTakenByTupperware() {
        return (taken && tupperwareZone.directional);
    }

    public Rect getZone() {
        return zone;
    }

    public int getLeftNeighbor() {
        return neighborIndexes[0];
    }

    public int getTopNeighbor() {
        return neighborIndexes[1];
    }

    public int getRightNeighbor() {
        return neighborIndexes[2];
    }

    public int getBottomNeighbor() {
        return neighborIndexes[3];
    }

    public void setLeftNeighbor(int n) {
        neighborIndexes[0] = n;
    }

    public void setTopNeighbor(int n) {
        neighborIndexes[1] = n;
    }

    public void setRightNeighbor(int n) {
        neighborIndexes[2] = n;
    }

    public void setBottomNeighbor(int n) {
        neighborIndexes[3] = n;
    }

    public int[] getNeighbors() {
        return neighborIndexes;
    }

    public void setZoneHorizontal(boolean horz) {
        horzZone = horz;
    }

    public void setZoneVertical(boolean vert) {
        vertZone = vert;
    }

    public boolean isZoneHorz() {
        return horzZone;
    }

    public boolean isZoneVert() {
        return vertZone;
    }

    /**
     * returns true if the direction given is allowed in this zone.
     *
     * if taken at all return false
     *
     * if tupperware zone is false or tupperware zone direction is false return true
     *
     * if horizontal and tupperware holds horizontal are equal return true.
     *
     * @param horz
     * @return
     */
    public boolean isCorrectDirection(boolean horz) {
        if (isTakenAtAll() || takenByBowl || takenByBowlCorner) {
            return false;
        }


        if (!tupperwareZone.zoneOn || !tupperwareZone.directional) {
            return true;
        }

        return horz == tupperwareZone.zoneHoldsHorz;
    }
}
