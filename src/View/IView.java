package View;

import javafx.event.ActionEvent;

public interface IView {

    /**
     * Called when generate maze button clicked
     *
     * @param actionEvent button clicked
     */
    void generateMaze(ActionEvent actionEvent);

    /**
     * Called when solve maze button clicked
     *
     * @param actionEvent button clicked
     */
    void solveMaze(ActionEvent actionEvent);

    /**
     * Called when maze is solved
     */
    void mazeSolved();

    /**
     * Called when player has moved
     */
    void playerMoved();

    /**
     * Called when maze was generated/updated
     */
    void mazeGenerated();

    /**
     * Called when player has won
     */
    void playerWon();

    /**
     * Called when save maze button was clicked
     */
    void saveFile(ActionEvent actionEvent);

    /**
     * Called when open maze button was clicked
     */
    void openFile(ActionEvent actionEvent);

    /**
     * Called when exit button was clicked
     */
    void exit(ActionEvent actionEvent);

    /**
     * Called when about button was clicked
     */
    void aboutText(ActionEvent actionEvent);
}
