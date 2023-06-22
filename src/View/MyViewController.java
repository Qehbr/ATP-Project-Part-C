package View;

import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements IView, Initializable, Observer {
    public MyViewModel myViewModel; //View Model to observe

    public MazeDisplayer mazeDisplayer; // Helper class to display the maze

    //GUI fields and labels
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public Label playerRow;
    public Label playerCol;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
    }

    @Override
    public void generateMaze(ActionEvent actionEvent) {
        //get rows and cols from GUI
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        //send it to view model
        myViewModel.generateMaze(rows, cols);
    }

    @Override
    public void solveMaze(ActionEvent actionEvent) {
        //show alert for solving maze
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        myViewModel.solveMaze();
        alert.setContentText("Maze Solved");
        alert.show();
    }

    /**
     * Fuction to get key pressed from the user and send it to ViewModel
     *
     * @param keyEvent key pressed by the user
     */
    public void keyPressed(KeyEvent keyEvent) {
        myViewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    /**
     * Fuction to get mouse clicked from the user and send it to ViewModel
     *
     * @param mouseEvent mouse pressed by the user
     */
    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    /**
     * Call proper logic from observation from o
     *
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     *            method.
     */
    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;
        switch (change) {
            case "maze generated" -> mazeGenerated();
            case "player moved" -> playerMoved();
            case "maze solved" -> mazeSolved();
            case "player won" -> playerWon();
            default -> System.out.println("Not implemented change: " + change);
        }
    }

    @Override
    public void mazeSolved() {
        mazeDisplayer.setSolution(myViewModel.getSolution());
    }

    @Override
    public void playerMoved() {
        setPlayerPosition(myViewModel.getPlayerRow(), myViewModel.getPlayerCol());
    }

    @Override
    public void mazeGenerated() {
        mazeDisplayer.solution = null;
        mazeDisplayer.drawMaze(myViewModel.getMaze(), myViewModel.getGoalPosition());
    }

    @Override
    public void     playerWon() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("You won");
        alert.show();
        myViewModel.solveMaze();
    }

    @Override
    public void saveFile(ActionEvent actionEvent) {
        //make pop up with saving file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Maze");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fileChooser.setInitialDirectory(new File("./resources"));
        File chosen = fileChooser.showSaveDialog(null);
        myViewModel.saveMaze(chosen.getPath());
    }

    @Override
    public void openFile(ActionEvent actionEvent) {
        //make pop up with loading file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Maze");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fileChooser.setInitialDirectory(new File("./resources"));
        File chosen = fileChooser.showOpenDialog(null);
        myViewModel.loadMaze(chosen.getPath());
    }

    @Override
    public void exit(ActionEvent actionEvent) {
        System.exit(1);
    }

    @Override
    public void aboutText(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Made by: 340915149, 206001976");
        alert.show();
    }

    /**
     * Getters and setters
     */
    public void setViewModel(MyViewModel myViewModel) {
        this.myViewModel = myViewModel;
        this.myViewModel.addObserver(this);
    }


    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    public void setPlayerPosition(int row, int col) {
        // set new player position
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
    }


}
