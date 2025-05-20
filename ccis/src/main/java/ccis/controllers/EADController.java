package ccis.controllers;

import ccis.dao.DemarcheAdministratifDao;
import ccis.models.DemarcheAdministratif;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EADController {

    @FXML private TableView<DemarcheAdministratif> tableView;

    @FXML private TableColumn<DemarcheAdministratif, String> denominationCol;
    @FXML private TableColumn<DemarcheAdministratif, String> typeCol;
    @FXML private TableColumn<DemarcheAdministratif, String> formeJuridiqueCol;
    @FXML private TableColumn<DemarcheAdministratif, String> secteurActiviteCol;
    @FXML private TableColumn<DemarcheAdministratif, String> activiteCol;
    @FXML private TableColumn<DemarcheAdministratif, String> GSMCol;
    @FXML private TableColumn<DemarcheAdministratif, String> FixeCol;
    @FXML private TableColumn<DemarcheAdministratif, String> AdresseCol;
    @FXML private TableColumn<DemarcheAdministratif, String> villeCol;
    @FXML private TableColumn<DemarcheAdministratif, String> interlocuteurCol;
    @FXML private TableColumn<DemarcheAdministratif, String> emailCol;
    @FXML private TableColumn<DemarcheAdministratif, String> siteWebCol;

    private final DemarcheAdministratifDao dao = new DemarcheAdministratifDao();
    private final ObservableList<DemarcheAdministratif> demarchesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        configureColumns();
        // Charger les données depuis la base
        loadDemarches();
    }
 private void configureColumns() {
        // Configure each column to display the appropriate property
        denominationCol.setCellValueFactory(new PropertyValueFactory<>("denomination"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
        formeJuridiqueCol.setCellValueFactory(new PropertyValueFactory<>("formeJuridique"));
        secteurActiviteCol.setCellValueFactory(new PropertyValueFactory<>("secteurActivite"));
        activiteCol.setCellValueFactory(new PropertyValueFactory<>("activite"));
        GSMCol.setCellValueFactory(new PropertyValueFactory<>("gsm"));
        FixeCol.setCellValueFactory(new PropertyValueFactory<>("fixe"));
        AdresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        villeCol.setCellValueFactory(new PropertyValueFactory<>("ville"));
        interlocuteurCol.setCellValueFactory(new PropertyValueFactory<>("nomPrenom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        siteWebCol.setCellValueFactory(new PropertyValueFactory<>("siteWeb")); 
}
private void loadDemarches() {
    List<DemarcheAdministratif> demarches = dao.getAllDemarches();

    // Utiliser un Set pour éliminer les doublons
    Set<String> uniqueKeys = new HashSet<>();
    List<DemarcheAdministratif> filteredDemarches = new ArrayList<>();

    for (DemarcheAdministratif demarche : demarches) {
        // Créer une clé unique basée sur les champs spécifiques
        String uniqueKey = demarche.getDenomination() + "|" +
                           demarche.getFormeJuridique() + "|" +
                           demarche.getSecteurActivite() + "|" +
                           demarche.getActivite() + "|" +
                           demarche.getVille();

        // Ajouter uniquement si la clé n'existe pas déjà
        if (uniqueKeys.add(uniqueKey)) {
            filteredDemarches.add(demarche);
        }
    }

    // Charger les données filtrées dans le tableau
    demarchesList.setAll(filteredDemarches);
    tableView.setItems(demarchesList);
}

    @FXML
    private void handleDelete() {
        DemarcheAdministratif selectedDemarche = tableView.getSelectionModel().getSelectedItem();
        if (selectedDemarche != null) {
            dao.deleteDemarche(selectedDemarche.getId());
            demarchesList.remove(selectedDemarche);
        } else {
            showAlert("Aucune sélection", "Veuillez sélectionner une démarche à supprimer.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void exportEA() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Démarche Administratif");
    
        // Créer la ligne d'en-têtes
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Dénomination");
        headerRow.createCell(1).setCellValue("Type");
        headerRow.createCell(2).setCellValue("Forme Juridique");
        headerRow.createCell(3).setCellValue("Secteur Activité");
        headerRow.createCell(4).setCellValue("Activité");
        headerRow.createCell(5).setCellValue("GSM");
        headerRow.createCell(6).setCellValue("Fixe");
        headerRow.createCell(7).setCellValue("Adresse");
        headerRow.createCell(8).setCellValue("Ville");
        headerRow.createCell(9).setCellValue("Interlocuteur");
        headerRow.createCell(10).setCellValue("Email");
        headerRow.createCell(11).setCellValue("Site Web");
    
        // Récupérer les données depuis la base
        List<DemarcheAdministratif> demarches = dao.getAllDemarches();
    
        // Utiliser un Set pour éliminer les doublons
        Set<String> uniqueKeys = new HashSet<>();
        List<DemarcheAdministratif> filteredDemarches = new ArrayList<>();
    
        for (DemarcheAdministratif demarche : demarches) {
            // Créer une clé unique basée sur les champs spécifiques
            String uniqueKey = demarche.getDenomination() + "|" +
                               demarche.getFormeJuridique() + "|" +
                               demarche.getSecteurActivite() + "|" +
                               demarche.getActivite() + "|" +
                               demarche.getVille();
    
            // Ajouter uniquement si la clé n'existe pas déjà
            if (uniqueKeys.add(uniqueKey)) {
                filteredDemarches.add(demarche);
            }
        }
    
        // Ajouter les données filtrées dans le fichier Excel
        int rowNum = 1;
        for (DemarcheAdministratif demarche : filteredDemarches) {
            Row row = sheet.createRow(rowNum++);
    
            row.createCell(0).setCellValue(demarche.getDenomination());
            row.createCell(1).setCellValue(demarche.getStatut());
            row.createCell(2).setCellValue(demarche.getFormeJuridique());
            row.createCell(3).setCellValue(demarche.getSecteurActivite());
            row.createCell(4).setCellValue(demarche.getActivite());
            row.createCell(5).setCellValue(demarche.getGsm());
            row.createCell(6).setCellValue(demarche.getFixe());
            row.createCell(7).setCellValue(demarche.getAdresse());
            row.createCell(8).setCellValue(demarche.getVille());
            row.createCell(9).setCellValue(demarche.getNomPrenom());
            row.createCell(10).setCellValue(demarche.getEmail());
            row.createCell(11).setCellValue(demarche.getSiteWeb());
        }
    
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier généré");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName("Extrait annuaire Demarche Administratif.xlsx");
        File output= fileChooser.showSaveDialog(null);
        if (output == null) {
            // User cancelled the save dialog
            return;
        }

        // Sauvegarder le fichier Excel
        try (FileOutputStream fileOut = new FileOutputStream(output)) {
            workbook.write(fileOut);
           
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(output);
        }else{
             showAlert("Exportation réussie", "Les données ont été exportées avec succès : " + output.getAbsolutePath());
        }
        
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur d'exportation", "Une erreur s'est produite lors de l'exportation des données.");
        }
    }
}
