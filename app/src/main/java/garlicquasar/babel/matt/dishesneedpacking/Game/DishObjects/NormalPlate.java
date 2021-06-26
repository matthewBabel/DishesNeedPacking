package garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects;

/**
 * A plate object that takes up 2 spaces.
 */
public class NormalPlate extends Plate {
    public NormalPlate(boolean isHorizontal, int thickness, int id) {
        super(isHorizontal, 2, "Plate (1 x 2)", thickness, new int[] {255, 150, 68, 238}, id);
    }
}
