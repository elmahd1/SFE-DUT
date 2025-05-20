package ccis.controllers;

import ccis.dao.EspaceEntrepriseDAO;
import ccis.models.EspaceEntreprise;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EAEController {

    @FXML private TableView<EspaceEntreprise> tableView;

    @FXML private TableColumn<EspaceEntreprise, String> denominationCol;
    @FXML private TableColumn<EspaceEntreprise, String> codeICECol;
    @FXML private TableColumn<EspaceEntreprise, String> typeCol;
    @FXML private TableColumn<EspaceEntreprise, String> formeJuridiqueCol;
    @FXML private TableColumn<EspaceEntreprise, String> tailleCol;
    @FXML private TableColumn<EspaceEntreprise, String> secteurActiviteCol;
    @FXML private TableColumn<EspaceEntreprise, String> activiteCol;
    @FXML private TableColumn<EspaceEntreprise, String> GSMCol;
    @FXML private TableColumn<EspaceEntreprise, String> FixeCol;
    @FXML private TableColumn<EspaceEntreprise, String> AdresseCol;
    @FXML private TableColumn<EspaceEntreprise, String> villeCol;
    @FXML private TableColumn<EspaceEntreprise, String> interlocuteurCol;
    @FXML private TableColumn<EspaceEntreprise, String> emailCol;
    @FXML private TableColumn<EspaceEntreprise, String> siteWebCol;

    private final EspaceEntrepriseDAO dao = new EspaceEntrepriseDAO();
    private final ObservableList<EspaceEntreprise> demarchesList = FXCollections.observableArrayList();

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
        codeICECol.setCellValueFactory(new PropertyValueFactory<>("codeICE"));
        tailleCol.setCellValueFactory(new PropertyValueFactory<>("tailleEntreprise")); 
}
private void loadDemarches() {
    List<EspaceEntreprise> demarches = dao.getAll();

    // Utiliser un Set pour éliminer les doublons
    Set<EspaceEntreprise> uniqueDemarches = new HashSet<>(demarches);

    // Ajouter les données uniques à la liste observable
    demarchesList.setAll(uniqueDemarches);
    tableView.setItems(demarchesList);
    FXCollections.reverse(demarchesList);
}

    @FXML
    private void handleDelete() {
        EspaceEntreprise selectedDemarche = tableView.getSelectionModel().getSelectedItem();
        if (selectedDemarche != null) {
            dao.delete(selectedDemarche.getId());
            demarchesList.remove(selectedDemarche);
        } else {
            showAlert("Aucune sélection", "Veuillez sélectionner une ligne à supprimer.");
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
        Sheet sheet = workbook.createSheet("Espace Entreprise");
    
        // Créer la ligne d'en-têtes
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Dénomination");
        headerRow.createCell(1).setCellValue("Code ICE");
        headerRow.createCell(2).setCellValue("Type");
        headerRow.createCell(3).setCellValue("Forme Juridique");
        headerRow.createCell(4).setCellValue("Secteur Activité");
        headerRow.createCell(5).setCellValue("Activité");
        headerRow.createCell(6).setCellValue("GSM");
        headerRow.createCell(7).setCellValue("Fixe");
        headerRow.createCell(8).setCellValue("Adresse");
        headerRow.createCell(9).setCellValue("Ville");
        headerRow.createCell(10).setCellValue("Interlocuteur");
        headerRow.createCell(11).setCellValue("Email");
        headerRow.createCell(12).setCellValue("Site Web");
    
        // Récupérer les données et éliminer les doublons
        List<EspaceEntreprise> demarches = dao.getAll();
        Set<EspaceEntreprise> uniqueDemarches = new HashSet<>(demarches);
    
        // Ajouter les données uniques
        int rowNum = 1;
        for (EspaceEntreprise demarche : uniqueDemarches) {
            Row row = sheet.createRow(rowNum++);
    
            row.createCell(0).setCellValue(demarche.getDenomination());
            row.createCell(1).setCellValue(demarche.getCodeICE());
            row.createCell(2).setCellValue(demarche.getStatut());
            row.createCell(3).setCellValue(demarche.getFormeJuridique());
            row.createCell(4).setCellValue(demarche.getSecteurActivite());
            row.createCell(5).setCellValue(demarche.getActivite());
            row.createCell(6).setCellValue(demarche.getGsm());
            row.createCell(7).setCellValue(demarche.getFixe());
            row.createCell(8).setCellValue(demarche.getAdresse());
            row.createCell(9).setCellValue(demarche.getVille());
            row.createCell(10).setCellValue(demarche.getNomPrenom());
            row.createCell(11).setCellValue(demarche.getEmail());
            row.createCell(12).setCellValue(demarche.getSiteWeb());
        }
    
               FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier généré");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName("Extrait annuaire Espace Entreprise.xlsx");
        File output = fileChooser.showSaveDialog(null);
        if (output == null) {
            // User cancelled the save dialog
            return;
        }
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
