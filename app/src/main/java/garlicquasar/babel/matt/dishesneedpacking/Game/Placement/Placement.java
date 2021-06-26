package garlicquasar.babel.matt.dishesneedpacking.Game.Placement;

import android.graphics.Rect;

public class Placement {
    private TupperwarePlacementAlgorithm tupperwarePlacement;
    private CupPlacementAlgorithm cupPlacement;
    private PlatePlacementAlgorithm platePlacement;
    private UtensilPlacementAlgorithm utensilPlacement;
    private MixingBowlPlacementAlgorithm mixingBowlPlacement;

    private PlacementAlgorithm allPlacement;

    public Placement() {
        allPlacement = new PlacementAlgorithm();
        tupperwarePlacement = new TupperwarePlacementAlgorithm();
        cupPlacement = new CupPlacementAlgorithm();
        mixingBowlPlacement = new MixingBowlPlacementAlgorithm();
        platePlacement = new PlatePlacementAlgorithm();
        utensilPlacement = new UtensilPlacementAlgorithm();
    }


    public boolean tupperwarePlacementAlgorithm(int x, int y) {
        return tupperwarePlacement.tupperwarePlacementAlgorithm(x, y);
    }

    public boolean utensilPlacementAlgorithm(int x, int y) {
        if(!utensilPlacement.utensilPlacementAlgorithm(x, y)) {
            platePlacementAlgorithm(x, y);
            return false;
        } else {
            return true;
        }
    }

    public boolean platePlacementAlgorithm(int x, int y) {
        return platePlacement.platePlacementAlgorithm(x, y);
    }

    public boolean cupPlacementAlgorithm(int x, int y) {
        return cupPlacement.cupPlacementAlgorithm(x, y);
    }

    public boolean mixingBowlPlacementAlgorithm(int x, int y) {
        return mixingBowlPlacement.mixingBowlPlacementAlgorithm(x, y);
    }

    public void layoutSwitch(int n) {
        allPlacement.layoutSwitch(n);
    }

    public void setLocalTakenRect(Rect r) {
        allPlacement.localTakenRect = r;
    }
}
