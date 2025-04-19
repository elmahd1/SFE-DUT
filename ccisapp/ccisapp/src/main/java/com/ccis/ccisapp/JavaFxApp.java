package com.ccis.ccisapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.ccis.ccisapp.controllers.MainLayoutController;

public class JavaFxApp extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() throws Exception {
        springContext = new SpringApplicationBuilder(CcisappApplicationSpring.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ccis/ccisapp/MainLayout.fxml"));
        loader.setController(new MainLayoutController());
        loader.setControllerFactory(springContext::getBean); // Let Spring inject controllers

        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/CCIS-logo-Copy-_2_.png")));
   
        primaryStage.setTitle("CCIS App");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }
}
