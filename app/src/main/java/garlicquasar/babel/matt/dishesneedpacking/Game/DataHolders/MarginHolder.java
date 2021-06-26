package garlicquasar.babel.matt.dishesneedpacking.Game.DataHolders;

import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.Map;
import garlicquasar.babel.matt.dishesneedpacking.Game.View.DishWasherPackerView;

public class MarginHolder {
    private static final int plateSideMarginFactor = 6;
    public static int plateSideMargin;
    public static final int MARGINALL = 20;
    public static final int EXTRABOTTOM = 50;
    public static final int PICKUPBUTTON = 15;
    public static final int FLOORBTNLEFT = 20;
    public static final int ROTATEMARGIN = 15;

    public static void resetMargins() {
        plateSideMargin = DishWasherPackerView.rackLayouts[Map.curFloor].zoneSize/plateSideMarginFactor;
    }
}
