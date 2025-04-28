package com.ccis.ccisapp;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CcisappApplicationSpring extends Application {
    private ConfigurableApplicationContext springContext;

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(CcisappApplicationSpring.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Main.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Scene scene = new Scene(loader.load(), 1000, 600);
            
            // Set application icon if exists
            var iconStream = getClass().getResourceAsStream("/images/CCIS-logo-Copy-_2_.png");
            if (iconStream != null) {
                stage.getIcons().add(new javafx.scene.image.Image(iconStream));
            }
            
            stage.setTitle("CCIS");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error appropriately (show alert, log, etc.)
            stop(); // Ensure application shuts down cleanly
        }
    }
    
    @Override
    public void stop() throws Exception {
        if (springContext != null) {
            springContext.close();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}