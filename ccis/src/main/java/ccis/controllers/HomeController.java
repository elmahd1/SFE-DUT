package ccis.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import ccis.dao.*;


public class HomeController {
    @FXML private Label demarcheCount;
    @FXML private Label entrepriseCount;
    @FXML private Label prospectionCount;

    private final DemarcheAdministratifDao demarcheDAO = new DemarcheAdministratifDao();
    private final EspaceEntrepriseDAO entrepriseDAO = new EspaceEntrepriseDAO();
    private final ProspectionDAO prospectionDAO = new ProspectionDAO();

    @FXML
    public void initialize() {
        refreshCounts();
        java.nio.file.Path baseDir = java.nio.file.Paths.get("C:/fichiers application ccis");
        if (!java.nio.file.Files.exists(baseDir)) {
            try {
                java.nio.file.Files.createDirectories(baseDir.resolve("demarche administrative"));
                java.nio.file.Files.createDirectories(baseDir.resolve("espace entreprise"));
                java.nio.file.Files.createDirectories(baseDir.resolve("prospection"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshCounts() {
        long demarches = demarcheDAO.count();  // Get count from DemarcheAdministratifDao
        long entreprises = entrepriseDAO.count();  // Get count from EspaceEntrepriseDAO
        long prospections = prospectionDAO.count();  // Get count from ProspectionDAO

        demarcheCount.setText(String.valueOf(demarches));  // Set count in the label
        entrepriseCount.setText(String.valueOf(entreprises));  // Set count in the label
        prospectionCount.setText(String.valueOf(prospections));  // Set count in the label
    }

 
}
