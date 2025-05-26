package ccis.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MenuController {

  

private void loadPage(String fxmlPath) {
    try {
        Parent newContent = FXMLLoader.load(getClass().getResource(fxmlPath));
        MainController.sharedContentArea.setCenter(newContent); // Set as center of BorderPane
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML private void home() { loadPage("/views/Home.fxml"); }
    @FXML private void fa()   { loadPage("/views/FicheDemarche.fxml"); }
    @FXML private void esd()  { loadPage("/views/EtatSuiviDemarche.fxml"); }
    @FXML private void ead()  { loadPage("/views/ExtraitAnnuaireDemarche.fxml"); }
    @FXML private void gd()   { loadPage("/views/GrapheDemarche.fxml"); }
    @FXML private void rd()   { loadPage("/views/RapportDemarche.fxml"); }

    @FXML private void fe()   { loadPage("/views/FicheEspace.fxml"); }
    @FXML private void ese()  { loadPage("/views/EtatSuiviEspace.fxml"); }
    @FXML private void eae()  { loadPage("/views/ExtraitAnnuaireEspace.fxml"); }
    @FXML private void ge()   { loadPage("/views/GrapheEspace.fxml"); }
    @FXML private void re()    { loadPage("/views/RapportEspace.fxml"); }

    @FXML private void fp()   { loadPage("/views/FicheProspection.fxml"); }
    @FXML private void ra()   { loadPage("/views/recapitulatif.fxml"); }
}