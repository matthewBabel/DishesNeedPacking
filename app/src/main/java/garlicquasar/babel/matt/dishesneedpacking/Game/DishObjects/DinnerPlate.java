package garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects;

/**
 * A plate object that takes up 3 spaces.
 */
public class DinnerPlate extends Plate{
    public DinnerPlate(boolean isHorizontal, int thickness, int id) {
        super(isHorizontal, 3, "Dinner Plate (1 x 3)", thickness, new int[] {255, 189, 138, 244}, id);
    }
}
