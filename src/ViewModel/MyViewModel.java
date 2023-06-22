package ViewModel;

import Model.IModel;
import Model.MovementDirection;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

/**
 * ViewModel class in MVVM Architecture
 */
public class MyViewModel extends Observable implements Observer {

    //model to observe
    private IModel model;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this); //observe the model for changes
    }

    @Override
    public void update(Observable o, Object arg) {
        //notify observers
        setChanged();
        notifyObservers(arg);
    }

    /**
     * Sending rows and cols to model for generating the maze
     *
     * @param rows Rows of the maze
     * @param cols Columns of the maze
     */
    public void generateMaze(int rows, int cols) {
        model.generateMaze(rows, cols);
    }

    /**
     * Sending correct enum to model for moving the player
     *
     * @param keyEvent key pressed
     */
    public void movePlayer(KeyEvent keyEvent) {
        MovementDirection direction;
        switch (keyEvent.getCode()) {
            case UP -> direction = MovementDirection.UP;
            case DOWN -> direction = MovementDirection.DOWN;
            case LEFT -> direction = MovementDirection.LEFT;
            case RIGHT -> direction = MovementDirection.RIGHT;
            case NUMPAD1 -> direction = MovementDirection.DIADownLeft;
            case NUMPAD2 -> direction = MovementDirection.DOWN;
            case NUMPAD3 -> direction = MovementDirection.DIADownRight;
            case NUMPAD4 -> direction = MovementDirection.LEFT;
            case NUMPAD6 -> direction = MovementDirection.RIGHT;
            case NUMPAD7 -> direction = MovementDirection.DIAUpLeft;
            case NUMPAD8 -> direction = MovementDirection.UP;
            case NUMPAD9 -> direction = MovementDirection.DIAUpRight;
            default -> {
                return;
            }
        }
        model.updatePlayerLocation(direction);
    }

    /**
     * Sending command to the model to solve the maze
     */
    public void solveMaze() {
        model.solveMaze();
    }

    /**
     * Sending command to the model to save the maze to given path
     *
     * @param chosen path for saving
     */
    public void saveMaze(String chosen) {
        model.saveMaze(chosen);
    }

    /**
     * Sending command to the model to load the maze from given path
     *
     * @param chosen path for loading
     */
    public void loadMaze(String chosen) {
        model.loadMaze(chosen);
    }


    /**
     * Getters
     */
    public Position getGoalPosition() {
        return model.getGoalPosition();
    }

    public int[][] getMaze() {
        return model.getMaze();
    }

    public int getPlayerRow() {
        return model.getPlayerRow();
    }

    public int getPlayerCol() {
        return model.getPlayerCol();
    }

    public Solution getSolution() {
        return model.getSolution();
    }
}
