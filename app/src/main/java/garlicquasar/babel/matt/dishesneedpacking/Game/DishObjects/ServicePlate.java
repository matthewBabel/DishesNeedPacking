package garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects;

/**
 * A plate object that takes up 4 spaces.
 */
public class ServicePlate extends Plate {
    public ServicePlate(boolean isHorizontal, int thickness, int id) {
        super(isHorizontal, 4, "Service Plate (1 x 4)", thickness, new int[] {255, 217, 179, 255}, id);
    }
}
