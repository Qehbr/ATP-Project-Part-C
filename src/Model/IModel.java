package Model;

import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

import java.util.Observer;

/**
 * Interface for Model in MVVM Architecture
 */
public interface IModel {
    /**
     * Function for maze generation
     *
     * @param rows Number of maze rows
     * @param cols Number of maze columns
     */
    void generateMaze(int rows, int cols);

    /**
     * Updates player location if possible
     *
     * @param direction Enum UP,DOWN,LEFT,RIGHT
     */
    void updatePlayerLocation(MovementDirection direction);

    /**
     * !Classes implementing IModel should be observable!
     * Assigns observer to IModel
     *
     * @param o Observer
     */
    void assignObserver(Observer o);

    /**
     * Solving generated maze
     */
    void solveMaze();


    /**
     * Save generated maze by chosen path
     *
     * @param chosen path to save the maze
     */
    void saveMaze(String chosen);

    /**
     * Load maze from chosen path
     *
     * @param chosen path to load maze from
     */
    void loadMaze(String chosen);


    /**
     * Getters
     */
    int getPlayerRow();

    int getPlayerCol();

    int[][] getMaze();

    Position getGoalPosition();

    Solution getSolution();


}
