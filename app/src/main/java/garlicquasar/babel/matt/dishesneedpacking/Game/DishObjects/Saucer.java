package garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects;


/**
 * A plate object that takes up 1 space.
 */
public class Saucer extends Plate {
    public Saucer(boolean isHorizontal, int thickness, int id) {
        super(isHorizontal, 1, "Saucer (1 x 1)", thickness, new int[] {255, 124, 22, 233}, id);
    }
}
