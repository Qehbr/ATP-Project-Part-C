package View;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {

    @FXML
    public ComboBox<String> mazeGeneratorComboBox;
    public ComboBox<String> mazeSolveComboBox;
    public TextField threadSizeTextField;
    public Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mazeGeneratorComboBox.getItems().addAll("Algorithm","Random");


        mazeSolveComboBox.getItems().add("Depth First Search");
        mazeSolveComboBox.getItems().add("Breadth First Search");
        mazeSolveComboBox.getItems().add("Best First Search");

    }

    public void submitProperties(ActionEvent actionEvent)
    {
        try {
            InputStream input = new FileInputStream("resources/config.properties");
            Properties properties = new Properties();
            properties.load(input);
            String generator = mazeGeneratorComboBox.getValue();
            String search = mazeSolveComboBox.getValue();
            String threads = threadSizeTextField.getText();
            if (generator != null) {
                switch (generator) {
                    case "Algorithm" -> generator = "MyMazeGenerator";
                    case "Random" -> generator = "SimpleMazeGenerator";
                    default -> generator = properties.getProperty("MazeGeneratingAlgorithm");
                }
            }
            else
                generator = properties.getProperty("MazeGeneratingAlgorithm");
            if (search != null) {
                switch (search) {
                    case "Best First Search" -> search = "BestFirstSearch";
                    case "Depth First Search" -> search = "DepthFirstSearch";
                    case "Breadth First Search" -> search = "Breadth First Search";
                    default -> search = properties.getProperty("MazeSearchingAlgorithm");
                }
            }
            else
                search = properties.getProperty("MazeSearchingAlgorithm");
            if (threads == null || threads.equals(""))
                threads = properties.getProperty("ThreadPoolSize");
            OutputStream output = new FileOutputStream("resources/config.properties");
            properties.setProperty("MazeGeneratingAlgorithm", generator);
            properties.setProperty("MazeSearchingAlgorithm", search);
            properties.setProperty("ThreadPoolSize", threads);
            properties.store(output,null);
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
            input.close();
            output.close();
        }
        catch (IOException e)
        {

        }
    }



}
