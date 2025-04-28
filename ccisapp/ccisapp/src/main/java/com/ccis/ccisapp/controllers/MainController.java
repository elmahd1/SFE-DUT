package com.ccis.ccisapp.controllers;
import javafx.scene.Node;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import org.springframework.stereotype.Component;

@Component
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
