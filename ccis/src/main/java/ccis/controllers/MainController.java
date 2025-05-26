package ccis.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;


public class MainController {

@FXML
private BorderPane borderPane;

public static BorderPane sharedContentArea;

@FXML
public void initialize() {
    try {
        BorderPane homePage = FXMLLoader.load(getClass().getResource("/views/Home.fxml"));
        borderPane.setCenter(homePage);
    } catch (IOException e) {
        e.printStackTrace();
    }
    sharedContentArea = borderPane;
}

}
