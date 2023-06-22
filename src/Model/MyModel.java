package Model;


import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    private boolean isWon; //if maze was won already
    private int rows, cols; //rows and cols of maze

    //coordinates of the player
    private int playerRow;
    private int playerCol;

    //helper class containing all maze logic
    private MazeGenerator generator;


    public MyModel() {
        generator = new MazeGenerator();
    }

    @Override
    public void generateMaze(int rows, int cols) {
        //generate new maze and update variables of the class
        generator.generateMaze(rows, cols);
        this.rows = generator.getMaze().getMazeMap().length;
        this.cols = generator.getMaze().getMazeMap()[0].length;
        this.isWon = false;
        //notify observers to change GUI
        setChanged();
        notifyObservers("maze generated");
        //move player to the start of the maze
        movePlayer(generator.getMaze().getStartPosition().getRowIndex(), generator.getMaze().getStartPosition().getColumnIndex());
    }


    @Override
    public void updatePlayerLocation(MovementDirection direction) {
        //check if moving is possible
        switch (direction) {
            case UP -> {
                if (playerRow > 0 && generator.getMaze().getMazeMap()[playerRow - 1][playerCol] != 1)
                    movePlayer(playerRow - 1, playerCol);
            }
            case DOWN -> {
                if (playerRow < rows - 1 && generator.getMaze().getMazeMap()[playerRow + 1][playerCol] != 1)
                    movePlayer(playerRow + 1, playerCol);
            }
            case LEFT -> {
                if (playerCol > 0 && generator.getMaze().getMazeMap()[playerRow][playerCol - 1] != 1)
                    movePlayer(playerRow, playerCol - 1);
            }
            case RIGHT -> {
                if (playerCol < cols - 1 && generator.getMaze().getMazeMap()[playerRow][playerCol + 1] != 1)
                    movePlayer(playerRow, playerCol + 1);
            }
        }

    }

    /**
     * Helper function to move the player, called from updatePlayerLocation function, which firstly checks if it is
     * possible to move the player
     *
     * @param row row of the player
     * @param col column of the player
     */
    private void movePlayer(int row, int col) {
        //update player position
        this.playerRow = row;
        this.playerCol = col;
        //if player came to goal
        if (playerRow == generator.getMaze().getGoalPosition().getRowIndex() && playerCol == generator.getMaze().getGoalPosition().getColumnIndex() && isWon == false) {
            isWon = true;
            //notify observers to change GUI
            setChanged();
            notifyObservers("player won");
        }
        //notify observers to change GUI
        setChanged();
        notifyObservers("player moved");
    }


    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze() {
        //solve maze from position of the player
        generator.solveMaze(new Position(getPlayerRow(), getPlayerCol(), "START"));
        //notify observers to change GUI
        setChanged();
        notifyObservers("maze solved");
    }

    @Override
    public void saveMaze(String chosen) {
        generator.saveMaze(chosen, getPlayerRow(), getPlayerCol());
    }

    @Override
    public void loadMaze(String chosen) {
        //load maze and update class variables
        generator.loadMaze(chosen);
        this.isWon = false;
        this.rows = generator.getMaze().getMazeMap().length;
        this.cols = generator.getMaze().getMazeMap()[0].length;
        //notify observers to change GUI
        setChanged();
        notifyObservers("maze generated");
        //move player to the start of the maze
        movePlayer(generator.getMaze().getStartPosition().getRowIndex(), generator.getMaze().getStartPosition().getColumnIndex());
    }

    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    @Override
    public Solution getSolution() {
        return generator.getSolution();
    }

    @Override
    public int[][] getMaze() {
        return generator.getMaze().getMazeMap();
    }

    @Override
    public Position getGoalPosition() {
        return generator.getMaze().getGoalPosition();
    }
}
