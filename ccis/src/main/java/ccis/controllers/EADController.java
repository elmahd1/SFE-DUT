package ccis.controllers;

import ccis.dao.DemarcheAdministratifDao;
import ccis.models.DemarcheAdministratif;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Callback;

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
    @FXML private TableColumn<DemarcheAdministratif, Void> actionCol;

    private final DemarcheAdministratifDao dao = new DemarcheAdministratifDao();
    private final ObservableList<DemarcheAdministratif> demarchesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configureColumns();
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
        
        // Configure action column with delete button
        actionCol.setCellFactory(new Callback<TableColumn<DemarcheAdministratif, Void>, TableCell<DemarcheAdministratif, Void>>() {
            @Override
            public TableCell<DemarcheAdministratif, Void> call(TableColumn<DemarcheAdministratif, Void> param) {
                return new TableCell<DemarcheAdministratif, Void>() {
                    private final Button deleteButton = new Button("Supprimer");
                    
                    {
                        deleteButton.getStyleClass().add("delete-button");
                        deleteButton.setOnAction(event -> {
                            DemarcheAdministratif item = getTableView().getItems().get(getIndex());
                            handleDeleteFromTable(item);
                        });
                    }
                    
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        });
    }

    private void loadDemarches() {
        List<DemarcheAdministratif> demarches = dao.getAllDemarches();
        
        // Remove duplicates based on unique key
        List<DemarcheAdministratif> uniqueDemarches = removeDuplicates(demarches);
        
        // Load filtered data into table
        demarchesList.setAll(uniqueDemarches);
        tableView.setItems(demarchesList);
    }

    private List<DemarcheAdministratif> removeDuplicates(List<DemarcheAdministratif> demarches) {
        Set<String> uniqueKeys = new HashSet<>();
        List<DemarcheAdministratif> filteredDemarches = new ArrayList<>();

        for (DemarcheAdministratif demarche : demarches) {
            // Create unique key based on specific fields
            String uniqueKey = createUniqueKey(demarche);
            
            // Add only if key doesn't already exist
            if (uniqueKeys.add(uniqueKey)) {
                filteredDemarches.add(demarche);
            }
        }
        
        return filteredDemarches;
    }

    private String createUniqueKey(DemarcheAdministratif demarche) {
        return (demarche.getDenomination() != null ? demarche.getDenomination() : "") + "|" +
               (demarche.getFormeJuridique() != null ? demarche.getFormeJuridique() : "") + "|" +
               (demarche.getSecteurActivite() != null ? demarche.getSecteurActivite() : "") + "|" +
               (demarche.getActivite() != null ? demarche.getActivite() : "") + "|" +
               (demarche.getVille() != null ? demarche.getVille() : "");
    }

    private void handleDeleteFromTable(DemarcheAdministratif demarche) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmer la suppression");
        confirmAlert.setHeaderText("Supprimer de la table");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette ligne de la table d'affichage ?");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                demarchesList.remove(demarche);
                showAlert("Suppression réussie", "La ligne a été supprimée de la table d'affichage.");
            }
        });
    }

    @FXML
    private void handleRefresh() {
        loadDemarches();
        showAlert("Actualisation", "Les données ont été actualisées depuis la base de données.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void exportEA() {
        if (demarchesList.isEmpty()) {
            showAlert("Aucune donnée", "Il n'y a aucune donnée à exporter.");
            return;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Démarche Administratif");
    
        // Create header row
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
    
        // Export only the data currently displayed in the table (without duplicates)
        int rowNum = 1;
        for (DemarcheAdministratif demarche : demarchesList) {
            Row row = sheet.createRow(rowNum++);
    
            row.createCell(0).setCellValue(demarche.getDenomination() != null ? demarche.getDenomination() : "");
            row.createCell(1).setCellValue(demarche.getStatut() != null ? demarche.getStatut() : "");
            row.createCell(2).setCellValue(demarche.getFormeJuridique() != null ? demarche.getFormeJuridique() : "");
            row.createCell(3).setCellValue(demarche.getSecteurActivite() != null ? demarche.getSecteurActivite() : "");
            row.createCell(4).setCellValue(demarche.getActivite() != null ? demarche.getActivite() : "");
            row.createCell(5).setCellValue(demarche.getGsm() != null ? demarche.getGsm() : "");
            row.createCell(6).setCellValue(demarche.getFixe() != null ? demarche.getFixe() : "");
            row.createCell(7).setCellValue(demarche.getAdresse() != null ? demarche.getAdresse() : "");
            row.createCell(8).setCellValue(demarche.getVille() != null ? demarche.getVille() : "");
            row.createCell(9).setCellValue(demarche.getNomPrenom() != null ? demarche.getNomPrenom() : "");
            row.createCell(10).setCellValue(demarche.getEmail() != null ? demarche.getEmail() : "");
            row.createCell(11).setCellValue(demarche.getSiteWeb() != null ? demarche.getSiteWeb() : "");
        }
    
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier généré");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialDirectory(new File("C:/fichiers application ccis/demarche administratif"));
        fileChooser.setInitialFileName("Extrait annuaire Demarche Administratif.xlsx");
        File output = fileChooser.showSaveDialog(null);
        
        if (output == null) {
            return;
        }

        try (FileOutputStream fileOut = new FileOutputStream(output)) {
            workbook.write(fileOut);
           
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(output);
            } else {
                showAlert("Exportation réussie", "Les données ont été exportées avec succès : " + output.getAbsolutePath());
            }
        
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur d'exportation", "Une erreur s'est produite lors de l'exportation des données.");
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}