package com.ccis.ccisapp.controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import java.io.IOException;
import javafx.scene.Node;

public class MainLayoutController {

    @FXML private BorderPane root;
    @FXML private Pane contentArea;

//     public void initialize() throws IOException {
//         // Load a default view at startup
//         loadView("MainLayout.fxml");
//         FXMLLoader sideLoader = new FXMLLoader(getClass().getResource("SideMenu.fxml"));
// Node side = sideLoader.load();
// SideMenuController controller = sideLoader.getController();
// controller.setMainController(this);

//     }

//     public void loadView(String fxmlFile) {
//         try {
//             FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
//             Parent view = loader.load();
//             contentArea.getChildren().setAll(view);
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
}
