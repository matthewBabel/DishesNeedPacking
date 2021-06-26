package garlicquasar.babel.matt.dishesneedpacking.Game.Input;

import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.List;

import garlicquasar.babel.matt.dishesneedpacking.Audio.SoundEffectPlayer;
import garlicquasar.babel.matt.dishesneedpacking.Game.DataHolders.GameScoreData;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Cup;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Dishware;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.MixingBowl;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Plate;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Tupperware;
import garlicquasar.babel.matt.dishesneedpacking.Game.DishObjects.Utensil;
import garlicquasar.babel.matt.dishesneedpacking.Game.Placement.TupperwarePlacementAlgorithm;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.Map;
import garlicquasar.babel.matt.dishesneedpacking.Game.Rack.RackLayout;
import garlicquasar.babel.matt.dishesneedpacking.Game.View.DishWasherPackerView;

/**
 * Handles user touch input.
 */
public class GestureHandler extends GestureDetector.SimpleOnGestureListener {


    @Override
    public boolean onDown(MotionEvent event) {
        // if games is over then consume all inputs
        if (DishWasherPackerView.gameOver) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int mouseX = (int) event.getX();
            int mouseY = (int) event.getY();

            if (DishWasherPackerView.exitConfirmBoxOn) { // if confirm box is showing stop all other touch inputs
                if (Map.CONFIRM_YES_BUTTON.contains(mouseX, mouseY)) {
                    SoundEffectPlayer.playBackButton(DishWasherPackerView.context);
                    DishWasherPackerView.back();
                } else if (Map.CONFIRM_NO_BUTTON.contains(mouseX, mouseY)) {
                    SoundEffectPlayer.playClick(DishWasherPackerView.context);
                    DishWasherPackerView.exitConfirmBoxOn = false;
                }
            } else if (DishWasherPackerView.washConfirmBoxOn) {
                if (Map.CONFIRM_YES_BUTTON.contains(mouseX, mouseY)) {
                    SoundEffectPlayer.playRunButton(DishWasherPackerView.context);
                    DishWasherPackerView.washConfirmBoxOn = false;
                    if (DishWasherPackerView.puzzleFull) {
                        GameScoreData.runs++;
                        DishWasherPackerView.runVictory();
                    } else {
                        GameScoreData.runs++;
                        DishWasherPackerView.clearPuzzle();
                    }
                } else if (Map.CONFIRM_NO_BUTTON.contains(mouseX, mouseY)) {
                    SoundEffectPlayer.playClick(DishWasherPackerView.context);
                    DishWasherPackerView.washConfirmBoxOn = false;
                }
            } else {
                if (Map.ROTATE_RECT.contains(mouseX, mouseY)) { // rotate button
                    if (DishWasherPackerView.currentDish != null) {
                        SoundEffectPlayer.playRotateButton(DishWasherPackerView.context);
                        DishWasherPackerView.currentDish.rotate();
                    }
                } else if (Map.BACK_RECT.contains(mouseX, mouseY)) { // back button
                    SoundEffectPlayer.playClick(DishWasherPackerView.context);
                    DishWasherPackerView.exitConfirmBoxOn = true;

                } else if (Map.PICK_UP_BUTTON_BOUNDS.contains(mouseX, mouseY)) { // pick up button
                    SoundEffectPlayer.playClick(DishWasherPackerView.context);
                    if (!Map.pickUpToggleOn) {
                        Map.pickUpToggleOn = true;
                    } else {
                        Map.pickUpToggleOn = false;
                    }
                } else if (Map.NEXT_BUTTON_BOUNDS.contains(mouseX, mouseY)) { // next button
                    Dishware dish = DishWasherPackerView.puzzleHandler.getDish();

                    if (dish != null) {
                        DishWasherPackerView.puzzleHandler.randomlyInsertDish(DishWasherPackerView.currentDish);
                        DishWasherPackerView.currentDish = dish;
                        GameScoreData.nextDishes++;
                        SoundEffectPlayer.playNextPiece(DishWasherPackerView.context);
                    }
                } else if (Map.maxFloors != 1 && Map.FLOOR_BUTTON_BOUNDS.contains(mouseX, mouseY)) { // floor buttons
                    int count = 0;
                    for (Rect r : Map.floorBtns) {
                        if (r.contains(mouseX, mouseY)) {
                            SoundEffectPlayer.playClick(DishWasherPackerView.context);
                            DishWasherPackerView.placement.layoutSwitch(Map.maxFloors - (count + 1));
                            break;
                        }
                        count++;
                    }
                } else if (Map.RUN_BUTTON_BOUNDS.contains(mouseX, mouseY)) { // run button
                    DishWasherPackerView.washConfirmBoxOn = true;
                } else if (!Map.pickUpToggleOn) {
                    boolean placed = false;

                    if (!Map.DISH_RECT.contains(mouseX, mouseY) || DishWasherPackerView.currentDish instanceof Tupperware) { // only go through algorithms when click is on dish screen if current dish is tupperware
                        if (DishWasherPackerView.currentDish instanceof Cup) { // cup placement
                            placed = DishWasherPackerView.placement.cupPlacementAlgorithm(mouseX, mouseY);
                        } else if (DishWasherPackerView.currentDish instanceof MixingBowl) { // mixing bowl placement
                            placed = DishWasherPackerView.placement.mixingBowlPlacementAlgorithm(mouseX, mouseY);
                        } else if (DishWasherPackerView.currentDish instanceof Plate) { // plate placement
                            placed = DishWasherPackerView.placement.platePlacementAlgorithm(mouseX, mouseY);
                        } else if (DishWasherPackerView.currentDish instanceof Utensil) { // utensil placement
                            placed = DishWasherPackerView.placement.utensilPlacementAlgorithm(mouseX, mouseY);
                        } else if (DishWasherPackerView.currentDish instanceof Tupperware) { // tupperware placement
                            placed = DishWasherPackerView.placement.tupperwarePlacementAlgorithm(mouseX, mouseY);
                        }

                        // checking if tupperware selection has been changed, playing a different sound effect for it if so
                        if (DishWasherPackerView.currentDish instanceof Tupperware && TupperwarePlacementAlgorithm.dishScreenPress) {
                            if (TupperwarePlacementAlgorithm.selectionPress) {
                                SoundEffectPlayer.playNextPiece(DishWasherPackerView.context);
                            }

                            TupperwarePlacementAlgorithm.dishScreenPress = false;
                            TupperwarePlacementAlgorithm.selectionPress = false;
                        } else {
                            if (placed) {
                                SoundEffectPlayer.playPlacePiece(DishWasherPackerView.context);
                            } else {
                                SoundEffectPlayer.playTakenSpot(DishWasherPackerView.context);
                            }
                        }
                    }

                } else { // pick up the next dish pressed
                    if (pickUpDish(mouseX, mouseY)) {
                        SoundEffectPlayer.playPickupPiece(DishWasherPackerView.context);
                        Map.pickUpToggleOn = false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean pickUpDish(int mouseX, int mouseY) {
        List<Dishware> dishes = DishWasherPackerView.dishes[Map.curFloor];

        Dishware toRemove = null;
        boolean foundDish = false;

        for (Dishware dish : dishes) {
            if (dish.contains(mouseX, mouseY)) {
                foundDish = true;
                Map.takenZone = null;

                if (DishWasherPackerView.puzzleFull) {
                    DishWasherPackerView.puzzleFull = false;
                }

                DishWasherPackerView.gameData.dishesPickedUp++;

                RackLayout[] layouts = DishWasherPackerView.rackLayouts;
                RackLayout layout = layouts[Map.curFloor];

                if (dish instanceof Cup) {
                    ((Cup) dish).remove(layout.plateTouchZones, layout.cupTouchZones);
                } else if (dish instanceof MixingBowl) {
                    ((MixingBowl) dish).remove(layout.plateTouchZones);
                } else if (dish instanceof Plate) {
                    ((Plate) dish).remove(layout.plateTouchZones);
                } else if (dish instanceof Utensil) {
                    ((Utensil) dish).remove(layout.utensilTouchZones);
                } else if (dish instanceof Tupperware) {
                    ((Tupperware) dish).remove(layout.plateTouchZones);
                }

                if (!(dish instanceof Tupperware)) {
                    if (DishWasherPackerView.currentDish != null) {
                        DishWasherPackerView.puzzleHandler.randomlyInsertDish(DishWasherPackerView.currentDish);
                    }
                    DishWasherPackerView.currentDish = dish;
                    toRemove = dish;
                } else { // splitting tupperware into two objects
                    if (DishWasherPackerView.currentDish != null) {
                        DishWasherPackerView.puzzleHandler.randomlyInsertDish(DishWasherPackerView.currentDish);
                    }
                    determineTupperwareSplit(((Tupperware) dish), dishes);
                }
                break;
            }

        }

        if (toRemove != null) {
            dishes.remove(toRemove);
        }

        return foundDish;
    }

    private void determineTupperwareSplit(Tupperware tupperware, List<Dishware> dishes) {
        Tupperware newTupperware = (Tupperware) DishWasherPackerView.generator.resetScale(tupperware);

        if (tupperware.containerHolding) {
            tupperware.removeContainer();
            newTupperware.singleContainerPiece();
        } else {
            tupperware.removeLid();
            newTupperware.singleLidPiece();
        }

        if (tupperware.canRemove()) {
            dishes.remove(tupperware);
        }

        DishWasherPackerView.currentDish = newTupperware;
    }
}
