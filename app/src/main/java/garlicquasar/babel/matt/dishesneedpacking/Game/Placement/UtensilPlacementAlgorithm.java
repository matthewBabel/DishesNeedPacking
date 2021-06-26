package garlicquasar.babel.matt.dishesneedpacking.Game.Placement;

import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Utensil;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.Map;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.UtensilZone;
import android.graphics.Point;

public class UtensilPlacementAlgorithm extends PlacementAlgorithm{


    public boolean utensilPlacementAlgorithm(int mouseX, int mouseY) {
        setLocalVars();

        for (UtensilZone z : rackLayouts[Map.curFloor].utensilTouchZones) {
            if (z.getZone().contains(mouseX, mouseY)) {
                if (!z.full && (z.zoneType == "none" || z.zoneType == currentDish.name)) { // utensil placed

                    if (z.zoneType == "none") {
                        z.zoneType = currentDish.name;
                    }

                    Point p = z.getAvailablePoint();
                    currentDish.setGame(new int[]{p.x, p.y});
                    ((Utensil) currentDish).takenUtensilZoneIndex = rackLayouts[Map.curFloor].utensilTouchZones.indexOf(z);

                    dishes[Map.curFloor].add(currentDish);
                    currentDish = puzzleHandler.getDish();

                    setGlobalVars();
                    return true;
                } else {
                    setTakenZone(z.getZone());

                    setGlobalVars();
                    return false;
                }
            }
        }

        return false;
    }
}
