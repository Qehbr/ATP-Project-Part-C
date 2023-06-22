package View;

import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Helper class for displaying the maze
 */
public class MazeDisplayer extends Canvas {
    private int[][] maze; //maze to display
    private Position goalPosition; //goal position to display
    protected Solution solution; //solution of the maze
    //player position
    private int playerRow = 0, playerCol = 0;

    //images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameSolution = new SimpleStringProperty();
    StringProperty imageFileNameGoal = new SimpleStringProperty();
    StringProperty imageFileNamePass = new SimpleStringProperty();


    /**
     * Updating class variables and calling draw function
     *
     * @param maze         maze to draw
     * @param goalPosition goal position to draw
     */
    public void drawMaze(int[][] maze, Position goalPosition) {
        this.goalPosition = goalPosition;
        this.maze = maze;
        draw();
    }

    /**
     * Drawing the maze
     */
    private void draw() {
        if (maze != null) {

            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.length;
            int cols = maze[0].length;
            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();

            //clear the canvas
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

            //draw walls, goal, player
            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            drawMazeGoal(graphicsContext, cellHeight, cellWidth);
            drawPlayer(graphicsContext, cellHeight, cellWidth);

            //if solution exists draw solution
            if (solution != null)
                drawSolution(graphicsContext, cellHeight, cellWidth);


        }
    }

    /**
     * Function for drawing the solution
     *
     * @param graphicsContext where to draw
     * @param cellHeight      height of each cell
     * @param cellWidth       width of each cell
     */
    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        graphicsContext.setFill(Color.LIGHTBLUE);

        Image solutionImage = null;
        try {
            solutionImage = new Image(new FileInputStream(getImageFileNameSolution()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no solution image file");
        }
        //get solution
        ArrayList<AState> solution_states = solution.getSolutionPath();
        //remove last solution step (goal) we do not want to overdraw the solution
        solution_states.remove(solution_states.size() - 1);
        //draw the solution
        for (AState state : solution_states) {
            double x = state.getPosition()[1] * cellWidth;
            double y = state.getPosition()[0] * cellHeight;
            if (solutionImage == null)
                graphicsContext.fillRect(x, y, cellWidth, cellHeight);
            else
                graphicsContext.drawImage(solutionImage, x, y, cellWidth, cellHeight);

        }
    }

    /**
     * Function for drawing the maze
     *
     * @param graphicsContext where to draw
     * @param cellHeight      height of each cell
     * @param cellWidth       width of each cell
     * @param rows            rows of the maze
     * @param cols            cols of the maze
     */
    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.BLACK);

        Image wallImage = null;
        try {
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }

        Image passImage = null;
        try {
            passImage = new Image(new FileInputStream(getImageFileNamePass()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no pass image file");
        }

        //iterate throug maze
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double x = j * cellWidth;
                double y = i * cellHeight;
                // if its wall draw wall
                if (maze[i][j] == 1) {
                    if (wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);

                }
                //if its path draw path
                else {
                    if (passImage != null)
                        graphicsContext.drawImage(passImage, x, y, cellWidth, cellHeight);

                }
            }
        }
    }

    /**
     * Function for drawing the player
     *
     * @param graphicsContext where to draw
     * @param cellHeight      height of each cell
     * @param cellWidth       width of each cell
     */
    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if (playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    /**
     * Function for drawing the goal
     *
     * @param graphicsContext where to draw
     * @param cellHeight      height of each cell
     * @param cellWidth       width of each cell
     */
    private void drawMazeGoal(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        graphicsContext.setFill(Color.HOTPINK);

        Image goalImage = null;
        try {
            goalImage = new Image(new FileInputStream(getImageFileNameGoal()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no goal image file");
        }

        double x = goalPosition.getColumnIndex() * cellWidth;
        double y = goalPosition.getRowIndex() * cellHeight;
        if (goalImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(goalImage, x, y, cellWidth, cellHeight);
    }

    /**
     * Getters and setters
     */
    public String getImageFileNameGoal() {
        return imageFileNameGoal.get();
    }

    public StringProperty imageFileNameGoalProperty() {
        return imageFileNameGoal;
    }

    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.imageFileNameGoal.set(imageFileNameGoal);
    }


    public String getImageFileNameSolution() {
        return imageFileNameSolution.get();
    }

    public StringProperty imageFileNameSolutionProperty() {
        return imageFileNameSolution;
    }

    public void setImageFileNameSolution(String imageFileNameSolution) {
        this.imageFileNameSolution.set(imageFileNameSolution);
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
        draw();
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String imageFileNameWallProperty() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public String imageFileNamePlayerProperty() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public String getImageFileNamePass() {
        return imageFileNamePass.get();
    }

    public StringProperty imageFileNamePassProperty() {
        return imageFileNamePass;
    }

    public void setImageFileNamePass(String imageFileNamePass) {
        this.imageFileNamePass.set(imageFileNamePass);
    }


}
