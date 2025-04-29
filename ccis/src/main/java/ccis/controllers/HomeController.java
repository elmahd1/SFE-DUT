package ccis.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import ccis.dao.DemarcheAdministratifDao;
import ccis.dao.EspaceEntrepriseDAO;

public class HomeController {
    @FXML private Label demarcheCount;
    @FXML private Label entrepriseCount;

    private final DemarcheAdministratifDao demarcheDAO = new DemarcheAdministratifDao();
    private final EspaceEntrepriseDAO entrepriseDAO = new EspaceEntrepriseDAO();

    @FXML
    public void initialize() {
        refreshCounts();
    }

    private void refreshCounts() {
        long demarches = demarcheDAO.count();  // Get count from DemarcheAdministratifDao
        long entreprises = entrepriseDAO.count();  // Get count from EspaceEntrepriseDAO

        demarcheCount.setText(String.valueOf(demarches));  // Set count in the label
        entrepriseCount.setText(String.valueOf(entreprises));  // Set count in the label
    }
        private void loadPage(String fxmlPath) {
        try {
            AnchorPane newContent = FXMLLoader.load(getClass().getResource(fxmlPath));
            MainController.sharedContentArea.getChildren().setAll(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML private void fa()     { loadPage("/views/EtatSuiviDemarche.fxml"); }
    @FXML private void fe()     { loadPage("/views/EtatSuiviEspace.fxml"); }
}
