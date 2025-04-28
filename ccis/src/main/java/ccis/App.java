package ccis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Main.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 600);

            // Set application icon if it exists
            var iconStream = getClass().getResourceAsStream("/images/CCIS-logo-Copy-_2_.png");
            if (iconStream != null) {
                stage.getIcons().add(new javafx.scene.image.Image(iconStream));
            }

            stage.setTitle("CCIS");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // You can add a pop-up error alert here if you want
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
// mvn compile exec:java
