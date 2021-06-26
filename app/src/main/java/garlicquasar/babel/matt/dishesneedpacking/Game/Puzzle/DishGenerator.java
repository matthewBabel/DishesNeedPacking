package garlicquasar.babel.matt.dishesneedpacking.Game.Puzzle;

import garlicquasar.babel.matt.dishesneedpacking.Game.DataHolders.MarginHolder;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Cup;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.DinnerPlate;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Dishware;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Fork;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Knife;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.MixingBowl;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.NormalPlate;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Plate;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Saucer;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.ServicePlate;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Spoon;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Tupperware;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Utensil;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.Map;
import garlicquasar.babel.matt.dishesneedpacking.Game.View.DishWasherPackerView;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

/**
 * Generates each dish.
 */
public class DishGenerator {

    private int singlePlateSize;
    private int singleCupSize;
    private int singleMixingBowlSize;
    private boolean horz = false;

    private Bitmap spoonImg;
    private Bitmap knifeImg;
    private Bitmap forkImg;
    private Bitmap cupImg;
    private Bitmap mixingBowlImg;

    private int[][] imgDimensions;

    public int dishID = 0;
    private double cupSizeDivisor = 1.25;


    public DishGenerator(int plateScale, Bitmap spoon, Bitmap knife, Bitmap fork, Bitmap cup, Bitmap bowl) {
        singlePlateSize = plateScale;
        singleCupSize = (int) (plateScale / cupSizeDivisor);
        singleMixingBowlSize = plateScale * 2;

        spoonImg = spoon;
        knifeImg = knife;
        forkImg = fork;
        cupImg = cup;
        mixingBowlImg = bowl;

        imgDimensions = new int[4][2];

        imgDimensions[0] = new int[]{spoonImg.getWidth(), spoonImg.getHeight()};
        imgDimensions[1] = new int[]{knifeImg.getWidth(), knifeImg.getHeight()};
        imgDimensions[2] = new int[]{forkImg.getWidth(), forkImg.getHeight()};
        imgDimensions[3] = new int[]{cupImg.getWidth(), cupImg.getHeight()};
    }

    public void resetForNextDish() {
        DishWasherPackerView.showTaken = false;
        DishWasherPackerView.placement.setLocalTakenRect(null);
        dishID++;
    }

    public Dishware getNewSaucer() {
        return getNewSaucer(true, 0);
    }

    private Dishware getNewSaucer(boolean randomAlign, int direction) {
        int sideMargin = (Map.DISH_RECT.right / 2) - (singlePlateSize / 2);
        int thickness = singlePlateSize / 11;


        Saucer plate;
        if (randomAlign) {
            plate = new Saucer((new Random().nextInt(2) == 0) ? true : false, thickness, dishID);
        } else {
            plate = new Saucer(horz, thickness, dishID);
            plate.direction = direction;
        }


        thickness /= 2;
        plate.setDish(new int[]{sideMargin, (Map.DISH_RECT.bottom / 2) - thickness, (Map.DISH_RECT.right) - sideMargin, (Map.DISH_RECT.bottom / 2) + thickness,
                thickness * 2});


        return plate;
    }

    public Dishware getNewNormalPlate() {
        return getNewNormalPlate(true, 0);
    }

    private Dishware getNewNormalPlate(boolean randomAlign, int direction) {

        int sideMargin = (Map.DISH_RECT.right / 2) - ((singlePlateSize * 2) / 2);
        int thickness = singlePlateSize / 10;


        NormalPlate plate;
        if (randomAlign) {
            plate = new NormalPlate((new Random().nextInt(2) == 0) ? true : false, thickness, dishID);
        } else {
            plate = new NormalPlate(horz, thickness, dishID);
            plate.direction = direction;
        }

        thickness /= 2;
        plate.setDish(new int[]{sideMargin, (Map.DISH_RECT.bottom / 2) - thickness, (Map.DISH_RECT.right) - sideMargin, (Map.DISH_RECT.bottom / 2) + thickness,
                thickness * 2});

        return plate;
    }

    public Dishware getNewDinnerPlate() {
        return getNewDinnerPlate(true, 0);
    }

    private Dishware getNewDinnerPlate(boolean randomAlign, int direction) {

        int sideMargin = (Map.DISH_RECT.right / 2) - ((singlePlateSize * 3) / 2);
        int thickness = singlePlateSize / 8;

        DinnerPlate plate;
        if (randomAlign) {
            plate = new DinnerPlate((new Random().nextInt(2) == 0) ? true : false, thickness, dishID);
        } else {
            plate = new DinnerPlate(horz, thickness, dishID);
            plate.direction = direction;
        }

        thickness /= 2;
        plate.setDish(new int[]{sideMargin, (Map.DISH_RECT.bottom / 2) - thickness, (Map.DISH_RECT.right) - sideMargin, (Map.DISH_RECT.bottom / 2) + thickness,
                thickness * 2});

        return plate;
    }

    public Dishware getNewServicePlate() {
        return getNewServicePlate(true, 0);
    }

    private Dishware getNewServicePlate(boolean randomAlign, int direction) {

        int sideMargin = (Map.DISH_RECT.right / 2) - ((singlePlateSize * 4) / 2);
        int thickness = singlePlateSize / 6;

        ServicePlate plate;
        if (randomAlign) {
            plate = new ServicePlate((new Random().nextInt(2) == 0) ? true : false, thickness, dishID);
        } else {
            plate = new ServicePlate(horz, thickness, dishID);
            plate.direction = direction;
        }

        thickness /= 2;
        plate.setDish(new int[]{sideMargin, (Map.DISH_RECT.bottom / 2) - thickness, (Map.DISH_RECT.right) - sideMargin, (Map.DISH_RECT.bottom / 2) + thickness,
                thickness * 2});

        return plate;
    }

    public Cup getNewCup() {
        return getNewCup(new Random().nextInt(4));
    }

    private Cup getNewCup(int direction) {

        int height = singleCupSize;
        int width = singleCupSize * (cupImg.getWidth() / cupImg.getHeight());

        Cup cup = new Cup( "Cup (1 x 1)", cupImg, direction, dishID);
        cup.setDish(new int[]{Map.DISH_RECT.left, Map.DISH_RECT.top, Map.DISH_RECT.right, Map.DISH_RECT.bottom, width, height});

        return cup;
    }

    public MixingBowl getNewMixingBowl() {
        return getNewMixingBowl(new Random().nextInt(4));
    }

    private MixingBowl getNewMixingBowl(int direction) {

        int width = singleMixingBowlSize;
        int height = singleMixingBowlSize * (mixingBowlImg.getHeight() / mixingBowlImg.getWidth());

        MixingBowl bowl = new MixingBowl( "Mixing Bowl (3 x 3)", mixingBowlImg, direction, dishID);
        bowl.setDish(new int[]{Map.DISH_RECT.left, Map.DISH_RECT.top, Map.DISH_RECT.right, Map.DISH_RECT.bottom, width, height});

        return bowl;
    }

    public Utensil getNewSpoon() {
        Spoon utensil = new Spoon("Spoon", spoonImg);

        int spoonHeight = singlePlateSize / 2;
        int spoonWidth = spoonHeight / (imgDimensions[0][1] / imgDimensions[0][0]);

        Rect bounds = Map.DISH_RECT;

        utensil.setDish(new int[]{bounds.left + (bounds.width() / 2) - (spoonWidth / 2),
                bounds.top + (bounds.height() / 2) - (spoonHeight / 2),
                bounds.left + (bounds.width() / 2) + (spoonWidth / 2),
                bounds.top + (bounds.height() / 2) + (spoonHeight / 2)});

        return utensil;
    }

    public Utensil getNewKnife() {
        Knife utensil = new Knife("Knife", knifeImg);

        int knifeHeight = singlePlateSize / 2;
        int knifeWidth = knifeHeight / (imgDimensions[1][1] / imgDimensions[1][0]);

        Rect bounds = Map.DISH_RECT;

        utensil.setDish(new int[]{bounds.left + (bounds.width() / 2) - (knifeWidth / 2),
                bounds.top + (bounds.height() / 2) - (knifeHeight / 2),
                bounds.left + (bounds.width() / 2) + (knifeWidth / 2),
                bounds.top + (bounds.height() / 2) + (knifeHeight / 2)});

        return utensil;
    }

    public Utensil getNewFork() {
        Fork utensil = new Fork("Fork", forkImg);

        int forkHeight = singlePlateSize / 2;
        int forkWidth = forkHeight / (imgDimensions[2][1] / imgDimensions[2][0]);

        Rect bounds = Map.DISH_RECT;
        utensil.setDish(new int[]{bounds.left + (bounds.width() / 2) - (forkWidth / 2),
                bounds.top + (bounds.height() / 2) - (forkHeight / 2),
                bounds.left + (bounds.width() / 2) + (forkWidth / 2),
                bounds.top + (bounds.height() / 2) + (forkHeight / 2)});

        return utensil;
    }

    public Dishware getNewTupperware(int id, int width, int height) {
        return getNewTupperware(true, false, false, true, false, false, false, width, height, 0, id);
    }

    private Dishware getNewTupperware(boolean randomAlign, boolean containerPlaced, boolean lidPlaced, boolean containerSelected, boolean containerHolding, boolean singleContainer, boolean singleLid, int spacesWideContainer, int spacesLong, int edge, int id) {

        int widthBlocks = spacesWideContainer;
        int heightBlocks = spacesLong;

        Tupperware tupper;
        if (randomAlign) {
            tupper = new Tupperware("Plastic Container (" + widthBlocks + " x " + heightBlocks + ")", new Random().nextInt(2) == 0 ? true : false, new int[]{widthBlocks, heightBlocks}, id);

            tupper.edgeDirection = (tupper.horizontal) ? 3 : 2;
        } else {
            tupper = new Tupperware("Plastic Container (" + widthBlocks + " x " + heightBlocks + ")", horz, new int[]{widthBlocks, heightBlocks}, id);

            tupper.edgeDirection = edge;
        }

        tupper.containerPlaced = containerPlaced;
        tupper.lidPlaced = lidPlaced;
        tupper.containerHolding = containerHolding;
        tupper.setContainerSelected(containerSelected);

        if (containerPlaced) {
            tupper.setContainerSelected(false);
            tupper.containerDraw = false;
        }

        if (lidPlaced) {
            tupper.setContainerSelected(true);
            tupper.lidDraw = false;
        }

        if (singleContainer) {
            tupper.isSingleContainer = true;
        }

        if (singleLid) {
            tupper.isSingleLid = true;
        }

        tupper.reset = (containerPlaced || lidPlaced);


        int width = singlePlateSize * widthBlocks;
        int height = singlePlateSize * heightBlocks;
        int thickness = singlePlateSize / 10;

        Rect bounds = Map.DISH_RECT;

        tupper.setDish(new int[]{bounds.left + ((bounds.width() / 2) + (bounds.width() / 6)) - (width / 2), bounds.bottom - (bounds.height() / 2) - (height / 2),
                bounds.left + ((bounds.width() / 2) + (bounds.width() / 6)) + (width / 2), bounds.bottom - (bounds.height() / 2) + (height / 2),
                thickness});

        return tupper;
    }

    /**
     * Whenever a floor is changed the scale could change, so a new dish is returned with the new scale.
     * For tupperware I pass certain variables to make it appear like the same dish,
     * even though a new dish is being returned.
     * Different sized floors aren't being used in the current build but keeping this here to allow for it in future updates.
     * @param curDish
     * @return same dish as current dish but with the new scale
     */
    public Dishware resetScale(Dishware curDish) {
        MarginHolder.resetMargins();
        singlePlateSize = DishWasherPackerView.rackLayouts[Map.curFloor].zoneSize;
        singleCupSize = (int) (singlePlateSize / cupSizeDivisor);
        singleMixingBowlSize = singlePlateSize * 2;


        horz = curDish.horizontal;
        if (curDish instanceof Saucer) {
            return getNewSaucer(false, ((Plate) curDish).direction);
        } else if (curDish instanceof NormalPlate) {
            return getNewNormalPlate(false, ((Plate) curDish).direction);
        } else if (curDish instanceof DinnerPlate) {
            return getNewDinnerPlate(false, ((Plate) curDish).direction);
        } else if (curDish instanceof ServicePlate) {
            return getNewServicePlate(false, ((Plate) curDish).direction);
        } else if (curDish instanceof Cup) {
            return getNewCup(((Cup) curDish).getDirection());
        } else if (curDish instanceof MixingBowl) {
            return getNewMixingBowl(((MixingBowl) curDish).getDirection());
        } else if (curDish instanceof Spoon) {
            return getNewSpoon();
        } else if (curDish instanceof Knife) {
            return getNewKnife();
        } else if (curDish instanceof  Fork) {
            return getNewFork();
        } else if (curDish instanceof Tupperware) {
            return getNewTupperware(false, ((Tupperware) curDish).containerPlaced, ((Tupperware) curDish).lidPlaced, ((Tupperware) curDish).isContainerSelected(), ((Tupperware) curDish).containerHolding,
                    ((Tupperware) curDish).isSingleContainer, ((Tupperware) curDish).isSingleLid,
                    ((Tupperware) curDish).spacesWideContainer, ((Tupperware) curDish).spacesLong, ((Tupperware) curDish).edgeDirection, ((Tupperware) curDish).id);
        } else {
            return getNewSaucer();
        }
    }
}
