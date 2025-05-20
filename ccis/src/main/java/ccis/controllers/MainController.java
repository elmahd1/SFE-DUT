package ccis.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;


public class MainController {
@FXML
private AnchorPane contentArea;

public static AnchorPane sharedContentArea;

@FXML
public void initialize() {
    try {
        AnchorPane homePage = FXMLLoader.load(getClass().getResource("/views/Home.fxml"));
        contentArea.getChildren().setAll(homePage);
    } catch (IOException e) {
        e.printStackTrace();
    }
    sharedContentArea = contentArea;
}

}
