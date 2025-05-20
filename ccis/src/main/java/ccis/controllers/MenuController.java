package ccis.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import java.io.IOException;


public class MenuController {

    @FXML private StackPane contentArea;


    private void loadPage(String fxmlPath) {
        try {
            AnchorPane newContent = FXMLLoader.load(getClass().getResource(fxmlPath));
            MainController.sharedContentArea.getChildren().setAll(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML private void home() { loadPage("/views/Home.fxml"); }
    @FXML private void fa()     { loadPage("/views/FicheDemarche.fxml"); }
    @FXML private void esd()    { loadPage("/views/EtatSuiviDemarche.fxml"); }
    @FXML private void ead()    { loadPage("/views/ExtraitAnnuaireDemarche.fxml"); }
    @FXML private void gd()     { loadPage("/views/GrapheDemarche.fxml"); }

    @FXML private void fe()     { loadPage("/views/FicheEspace.fxml"); }
    @FXML private void ese()    { loadPage("/views/EtatSuiviEspace.fxml"); }
    @FXML private void eae()    { loadPage("/views/ExtraitAnnuaireEspace.fxml"); }
    @FXML private void ge()     { loadPage("/views/GrapheEspace.fxml"); }

    @FXML private void fp()     { loadPage("/views/FicheProspection.fxml"); }
    @FXML private void ra()     { loadPage("/views/Recapitulatif.fxml"); }
}
