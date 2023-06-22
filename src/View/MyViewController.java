package View;

import ViewModel.MyViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.ResourceBundle;

public class MyViewController implements IView, Initializable, Observer {
    @FXML
    public MyViewModel myViewModel; //View Model to observe

    public MazeDisplayer mazeDisplayer; // Helper class to display the maze

    //GUI fields and labels
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public Button generateMaze;
    public Button solveMaze;
    public Label playerRow;
    public Label playerCol;
    private Media background;
    private Media victory;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayer2;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
        BooleanBinding booleanBind = Bindings.or(textField_mazeColumns.textProperty().isEmpty(),textField_mazeRows.textProperty().isEmpty());
        generateMaze.disableProperty().bind(booleanBind);

        background = new Media(new File("resources/music/background.mp3").toURI().toString());
        victory = new Media(new File("resources/music/victory.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(background);
        mediaPlayer2 = new MediaPlayer(victory);


    }

    @Override
    public void generateMaze(ActionEvent actionEvent) {
        //get rows and cols from GUI
        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        //send it to view model
        myViewModel.generateMaze(rows, cols);
        solveMaze.setDisable(false);
        mediaPlayer.play();


    }

    @Override
    public void solveMaze(ActionEvent actionEvent) {
        //show alert for solving maze
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        myViewModel.solveMaze();
        alert.setTitle("Maze solution");
        alert.setHeaderText("The solution is marked with red crosses");
        alert.setContentText("Maze Solved");
        alert.show();
        solveMaze.setDisable(true);
    }

    /**
     * Function to get key pressed from the user and send it to ViewModel
     *
     * @param keyEvent key pressed by the user
     */
    public void keyPressed(KeyEvent keyEvent) {
        myViewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    /**
     * Function to get mouse clicked from the user and send it to ViewModel
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
        mediaPlayer2.stop();
    }

    @Override
    public void     playerWon() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Victory!");
        alert.setHeaderText("You have reached the goal!");
        alert.setContentText("You won!");
        alert.show();
        myViewModel.solveMaze();
        solveMaze.setDisable(true);
        mediaPlayer2.play();

    }

    @Override
    public void saveFile(ActionEvent actionEvent) {
        //make pop up with saving file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Maze");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fileChooser.setInitialDirectory(new File("./resources"));
        File chosen = fileChooser.showSaveDialog(null);
        if (chosen != null) {
            myViewModel.saveMaze(chosen.getPath());
            solveMaze.setDisable(false);
        }
    }

    @Override
    public void openFile(ActionEvent actionEvent) {
        //make pop up with loading file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Maze");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fileChooser.setInitialDirectory(new File("./resources"));
        File chosen = fileChooser.showOpenDialog(null);
        if (chosen != null) {
            myViewModel.loadMaze(chosen.getPath());
            solveMaze.setDisable(false);
        }
    }



    @Override
    public void exit(ActionEvent actionEvent) {
        System.exit(1);
    }

    public void helpText(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How to Play");
        alert.setHeaderText("You are stuck in a maze. \nYour goal is to reach the chest. \n" +
                "Use the numpad keys or arrow keys to move. \nYou can only move to empty spots. \nGood Luck!");
        alert.setContentText("Made by: 340915149, 206001976");
        alert.show();
    }

    public void settingsWindow(ActionEvent actionEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Properties.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root,600,400);

            stage.setTitle("Properties");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        }
        catch (IOException e) {
            return;
        }

    }

    @Override
    public void aboutText(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("The maze generation Algorithm we used was Prims algorithm \n" +
                "The maze solving algorithm we used are, like in the name:\n \tDepth First Search AKA DFS \n" +
                "\tBreadth First Search AKA BFS\n" +
                "\tBest First Search, the same as BFS but with diagonal movement");
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
