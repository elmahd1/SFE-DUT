package ccis.controllers;

import ccis.dao.DemarcheAdministratifDao;
import ccis.models.DemarcheAdministratif;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.util.function.BiConsumer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ESDController {

    @FXML private TableView<DemarcheAdministratif> tableView;

    @FXML private TableColumn<DemarcheAdministratif, String> colFormeJuridique;
    @FXML private TableColumn<DemarcheAdministratif, String> colDateDepot;
    @FXML private TableColumn<DemarcheAdministratif, String> colHeureDepot;
    @FXML private TableColumn<DemarcheAdministratif, String> colSecteurActivite;
    @FXML private TableColumn<DemarcheAdministratif, String> colActivite;
    @FXML private TableColumn<DemarcheAdministratif, String> colNomRepresentantLegal;
    @FXML private TableColumn<DemarcheAdministratif, String> colQualiteConseillerCcis;
    @FXML private TableColumn<DemarcheAdministratif, String> colEtatDossierFournit;
    @FXML private TableColumn<DemarcheAdministratif, String> colSuiteAccordeeCommande;
    @FXML private TableColumn<DemarcheAdministratif, String> colObservation;
    @FXML private TableColumn<DemarcheAdministratif, String> colDateDelivrance;
    @FXML private TableColumn<DemarcheAdministratif, String> colObjetVisite;
    @FXML private TableColumn<DemarcheAdministratif, String> colHeureContact;
    @FXML private TableColumn<DemarcheAdministratif, String> colDateContact;
    @FXML private TableColumn<DemarcheAdministratif, String> colDenomination; 
    @FXML private TableColumn<DemarcheAdministratif, String> colTypeDemande;
    @FXML private TableColumn<DemarcheAdministratif, String> colType;
    @FXML private TableColumn<DemarcheAdministratif, Integer> colFixe;
    @FXML private TableColumn<DemarcheAdministratif, Integer> colGsm;
    @FXML private TableColumn<DemarcheAdministratif, String> colSiegeSocialeAdresse;
    @FXML private TableColumn<DemarcheAdministratif, String> colVilleCommunite;
    @FXML private TableColumn<DemarcheAdministratif, String> colInterlocuteur;
    @FXML private TableColumn<DemarcheAdministratif, String> colEmail;
    @FXML private TableColumn<DemarcheAdministratif, Double> colMontant;
    @FXML private TableColumn<DemarcheAdministratif, String> colNomPrenom;
    @FXML private TableColumn<DemarcheAdministratif, String> colAccepteEnvoiCcis;
    @FXML private TableColumn<DemarcheAdministratif, String> colSiteWeb;
    @FXML private TableColumn<DemarcheAdministratif, String> colNomPrenomConseillerCcis;
    @FXML private TableColumn<DemarcheAdministratif, Integer> colId;
    @FXML private TableColumn<DemarcheAdministratif, String> colHeureDelivrance;
    @FXML private TableColumn<DemarcheAdministratif, Void> colActions; // Column for delete button


    private final DemarcheAdministratifDao dao = new DemarcheAdministratifDao();
    private final ObservableList<DemarcheAdministratif> demarchesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        configureColumns();
        // Charger les données depuis la base
        loadDemarches();
      // Adding delete button in the "Actions" column
      setupActionColumn();

    }

    private void setupActionColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");
    
            {
                deleteButton.setOnAction(event -> {
                    DemarcheAdministratif selected = getTableView().getItems().get(getIndex());
                    handleDelete(selected);
                });
            }
    
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
    }

 private void configureColumns() {
        // Configure each column to display the appropriate property
        colFormeJuridique.setCellValueFactory(new PropertyValueFactory<>("formeJuridique"));
        colDateDepot.setCellValueFactory(new PropertyValueFactory<>("dateDepot"));
        colHeureDepot.setCellValueFactory(new PropertyValueFactory<>("heureDepot"));
        colSecteurActivite.setCellValueFactory(new PropertyValueFactory<>("secteurActivite"));
        colActivite.setCellValueFactory(new PropertyValueFactory<>("activite"));
        colNomRepresentantLegal.setCellValueFactory(new PropertyValueFactory<>("nomRepLegal"));
        colQualiteConseillerCcis.setCellValueFactory(new PropertyValueFactory<>("qualiteCCIS"));
        colEtatDossierFournit.setCellValueFactory(new PropertyValueFactory<>("etatDossier"));
        colSuiteAccordeeCommande.setCellValueFactory(new PropertyValueFactory<>("suiteDemande"));
        colObservation.setCellValueFactory(new PropertyValueFactory<>("observation"));
        colDateDelivrance.setCellValueFactory(new PropertyValueFactory<>("dateDelivrance"));
        colObjetVisite.setCellValueFactory(new PropertyValueFactory<>("objetVisite"));
        colHeureContact.setCellValueFactory(new PropertyValueFactory<>("heureContact"));
        colDateContact.setCellValueFactory(new PropertyValueFactory<>("dateContact"));
        colDenomination.setCellValueFactory(new PropertyValueFactory<>("denomination"));
        colTypeDemande.setCellValueFactory(new PropertyValueFactory<>("typeDemande"));
        colType.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colFixe.setCellValueFactory(new PropertyValueFactory<>("fixe"));
        colGsm.setCellValueFactory(new PropertyValueFactory<>("gsm"));
        colSiegeSocialeAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colVilleCommunite.setCellValueFactory(new PropertyValueFactory<>("ville"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));

colMontant.setCellFactory(column -> new TableCell<DemarcheAdministratif, Double>() {
    @Override
    protected void updateItem(Double montant, boolean empty) {
        super.updateItem(montant, empty);
        if (empty || montant == null) {
            setText(null);
        } else {
            setText(String.format("%.2f", montant));
        }
    }
});
        colNomPrenom.setCellValueFactory(new PropertyValueFactory<>("nomPrenom"));
        colAccepteEnvoiCcis.setCellValueFactory(new PropertyValueFactory<>("accepteEnvoi"));
        colSiteWeb.setCellValueFactory(new PropertyValueFactory<>("siteWeb"));
        colNomPrenomConseillerCcis.setCellValueFactory(new PropertyValueFactory<>("nomPrenomCCIS"));
        colHeureDelivrance.setCellValueFactory(new PropertyValueFactory<>("heureDelivrance"));
    }
    private void loadDemarches() {
        List<DemarcheAdministratif> demarches = dao.getAllDemarches();
        demarchesList.setAll(demarches);
        tableView.setItems(demarchesList); // Reverse the order of the list removed
        demarchesList.sort((d1, d2) -> {
            String date1 = d1.getDateContact();
            String date2 = d2.getDateContact();
            if (date1 == null && date2 == null) return 0;
            if (date1 == null) return 1;
            if (date2 == null) return -1;
            return date2.compareTo(date1); // Descending order (most recent first)
        });
    }

    @FXML
    private void handleDelete(DemarcheAdministratif selectedDemarche) {
        if (selectedDemarche != null) {
            dao.deleteDemarche(selectedDemarche.getId());
            demarchesList.remove(selectedDemarche);
        } else {
            showAlert("Erreur", "Impossible de supprimer : aucune sélection.");
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void exportD(){
          Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Démarches Administratives");

    // Créer la ligne d'en-têtes
    Row headerRow = sheet.createRow(0);

String[] headers = {
    "Dénomination",
    "Type de demmande",
    "Forme juridique",
    "Secteur Activité",
    "Activité",
    "Type",
    "FIXE",
    "GSM",
    "Siège Sociale / Adresses",
    "Ville / Communité",
    "Email",
    "Date de contact",
    "Heure de contact",
    "Objet de la visite",
    "Montant",
    "Nom et Prénom",
    "Accepte Envoi CCIS",
    "Site Web",
    "Nom du représentant légal",
    "Date de dépôt",
    "Heure de dépôt",
    "Nom et Prénom du conseiller CCIS",
    "Qualité du conseiller CCIS",
    "Etat du dossier fourni",
    "Suite accordée à la commande",
    "0bservation",
    "Date de délivrance",
    "Heure de délivrance"
};

for (int i = 0; i < headers.length; i++) {
    headerRow.createCell(i).setCellValue(headers[i]);
}

// Ajouter les données
List<DemarcheAdministratif> demarches = dao.getAllDemarches();
int rowNum = 1;
for (DemarcheAdministratif d : demarches) {
    Row row = sheet.createRow(rowNum++);
    row.createCell(0).setCellValue(d.getDenomination());
    row.createCell(1).setCellValue(d.getTypeDemande());
    row.createCell(2).setCellValue(d.getFormeJuridique());
    row.createCell(3).setCellValue(d.getSecteurActivite());
    row.createCell(4).setCellValue(d.getActivite());
    row.createCell(5).setCellValue(d.getStatut());
    row.createCell(6).setCellValue(d.getFixe() != null ? d.getFixe() : "");
    row.createCell(7).setCellValue(d.getGsm() != null ? d.getGsm() : "");
    row.createCell(8).setCellValue(d.getAdresse());
    row.createCell(9).setCellValue(d.getVille());
    row.createCell(10).setCellValue(d.getEmail());
    row.createCell(11).setCellValue(d.getDateContact());
    row.createCell(12).setCellValue(d.getHeureContact());
    row.createCell(13).setCellValue(d.getObjetVisite());
    row.createCell(14).setCellValue(d.getMontant() != null ? d.getMontant() : 0.0);
    row.createCell(15).setCellValue(d.getNomPrenom());
    row.createCell(16).setCellValue(d.getAccepteEnvoi());
    row.createCell(17).setCellValue(d.getSiteWeb());
    row.createCell(18).setCellValue(d.getNomRepLegal());
    row.createCell(19).setCellValue(d.getDateDepot());
    row.createCell(20).setCellValue(d.getHeureDepot());
    row.createCell(21).setCellValue(d.getNomPrenomCCIS());
    row.createCell(22).setCellValue(d.getQualiteCCIS());
    row.createCell(23).setCellValue(d.getEtatDossier());
    row.createCell(24).setCellValue(d.getSuiteDemande());
    row.createCell(25).setCellValue(d.getObservation());
    row.createCell(26).setCellValue(d.getDateDelivrance());
    row.createCell(27).setCellValue(d.getHeureDelivrance());
}

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier généré");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialDirectory(new File("C:/fichiers application ccis/demarche administratif"));
        fileChooser.setInitialFileName("Etat Suivi Demarche Administratif.xlsx");
        File output = fileChooser.showSaveDialog(null);
        if (output == null) {
            // User cancelled the save dialog
            return;
        }
    // Sauvegarder le fichier Excel
    try (FileOutputStream fileOut = new FileOutputStream(output)) {
        workbook.write(fileOut);
       
    if(Desktop.isDesktopSupported()){
        Desktop.getDesktop().open(output);
    }else {
         showAlert("Exportation réussie", "Les données ont été exportées avec succès : " + output.getAbsolutePath());
    }
    
    } catch (IOException e) {
        e.printStackTrace();
        showAlert("Erreur d'exportation", "Une erreur s'est produite lors de l'exportation des données.");
    }
    }
    @FXML
private TextField searchField;

@FXML
private void onSearch() {
    String keyword = searchField.getText().toLowerCase();

    if (keyword.isEmpty()) {
        tableView.setItems(demarchesList); // remet tous les éléments
    } else {
        ObservableList<DemarcheAdministratif> filteredList = FXCollections.observableArrayList();

        for (DemarcheAdministratif d : demarchesList) {
            if (
                (d.getNomPrenom() != null && d.getNomPrenom().toLowerCase().contains(keyword)) ||
                (d.getEmail() != null && d.getEmail().toLowerCase().contains(keyword)) ||
                (d.getTypeDemande() != null && d.getTypeDemande().toLowerCase().contains(keyword)) ||
                (d.getObjetVisite() != null && d.getObjetVisite().toLowerCase().contains(keyword)) ||
                (d.getVille() != null && d.getVille().toLowerCase().contains(keyword))
            ) {
                filteredList.add(d);
            }
        }

        tableView.setItems(filteredList);
    }
}

@FXML
private void handleImport(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Sélectionner un fichier Excel");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
    File file = fileChooser.showOpenDialog(null); // tu peux passer ici le Stage si tu l’as

    if (file != null) {
        importFromExcel(file);
    }
}
private void importFromExcel(File file) {
    try (FileInputStream fis = new FileInputStream(file);
         Workbook workbook = new XSSFWorkbook(fis)) {

        Sheet sheet = workbook.getSheetAt(0);
        Row headerRow = sheet.getRow(0);

        if (headerRow == null) {
            System.out.println("Fichier Excel vide ou sans en-tête.");
            return;
        }

        // Map header titles to their column indexes
        Map<String, Integer> columnIndexes = new HashMap<>();
        for (Cell cell : headerRow) {
            columnIndexes.put(cell.getStringCellValue().trim(), cell.getColumnIndex());
        }

        // Define mapping from Excel column name to Java object setter
        Map<String, BiConsumer<DemarcheAdministratif, String>> fieldMapping = new HashMap<>();
        fieldMapping.put("Dénomination", (d, v) -> d.setDenomination(v));
        fieldMapping.put("Type de demmande", (d, v) -> {
            if ("DemandeDoc".equalsIgnoreCase(v.trim())) {
            d.setTypeDemande("Demande de document administratif");
            } else if ("DemandeInfo".equalsIgnoreCase(v.trim())) {
            d.setTypeDemande("Demande d’information /renseignement à propos d’un document administratif");
            } else {
            d.setTypeDemande(v);
            }
        });
        fieldMapping.put("Forme juridique", (d, v) -> d.setFormeJuridique(v));
        fieldMapping.put("Secteur Activité", (d, v) -> d.setSecteurActivite(v));
        fieldMapping.put("Activité", (d, v) -> d.setActivite(v));
        fieldMapping.put("Type", (d, v) -> d.setStatut(v));
        fieldMapping.put("FIXE", (d, v) -> d.setFixe(v));
        fieldMapping.put("GSM 1", (d, v) -> d.setGsm(v));
        fieldMapping.put("GSM", (d, v) -> d.setGsm(v));
        fieldMapping.put("Siège Sociale / Adresses", (d, v) -> d.setAdresse(v));
        fieldMapping.put("Ville / Communité", (d, v) -> d.setVille(v));
        fieldMapping.put("Interlocuteur", (d, v) -> d.setInterlocuteur(v));
        fieldMapping.put("Email 1", (d, v) -> d.setEmail(v));
        fieldMapping.put("Date de contact", (d, v) -> d.setDateContact(v));
        fieldMapping.put("Heure de contact", (d, v) -> d.setHeureContact(v));
        fieldMapping.put("Objet de la visite", (d, v) -> d.setObjetVisite(v));
        fieldMapping.put("Montant", (d, v) -> d.setMontant(Double.parseDouble(v)));
        fieldMapping.put("montant", (d, v) -> d.setMontant(Double.parseDouble(v)));
        fieldMapping.put("Nom et Prénom", (d, v) -> d.setNomPrenom(v));
        fieldMapping.put("Accepte Envoi CCIS", (d, v) -> {
            if ("1".equals(v.trim())) {
            d.setAccepteEnvoi("Oui");
            } else if ("0".equals(v.trim())) {
            d.setAccepteEnvoi("Non");
            } else {
            d.setAccepteEnvoi(v); // fallback to original value
            }
        });
        fieldMapping.put("Site Web", (d, v) -> d.setSiteWeb(v));
        fieldMapping.put("Nom du représentant légal", (d, v) -> d.setNomRepLegal(v));
        fieldMapping.put("Date de dépôt", (d, v) -> d.setDateDepot(v));
        fieldMapping.put("Heure de dépôt", (d, v) -> d.setHeureDepot(v));
        fieldMapping.put("Nom et Prénom du conseiller CCIS", (d, v) -> d.setNomPrenomCCIS(v));
        fieldMapping.put("Qualité du conseiller CCIS", (d, v) -> d.setQualiteCCIS(v));
        fieldMapping.put("Etat du dossier fourni", (d, v) -> d.setEtatDossier(v));
        fieldMapping.put("Suite accordée à la commande", (d, v) -> d.setSuiteDemande(v));
        fieldMapping.put("0bservation", (d, v) -> d.setObservation(v));
        fieldMapping.put("Date de délivrance", (d, v) -> d.setDateDelivrance(v));
        fieldMapping.put("Heure de délivrance", (d, v) -> d.setHeureDelivrance(v));

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            DemarcheAdministratif d = new DemarcheAdministratif();

            for (String header : fieldMapping.keySet()) {
                Integer colIndex = columnIndexes.get(header);
                if (colIndex == null) continue;

                Cell cell = row.getCell(colIndex);
                String value = getCellAsString(cell);

                try {
                    fieldMapping.get(header).accept(d, value);
                } catch (Exception e) {
                    System.out.println("Erreur lors du traitement de la colonne '" + header + "' à la ligne " + (i + 1) + ": " + e.getMessage());
                }
            }

            new DemarcheAdministratifDao().insertDemarche(d);
        }

        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("L'importation a été effectuée avec succès!");
loadDemarches();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
private String getCellAsString(Cell cell) {
    if (cell == null) return "";
    switch (cell.getCellType()) {
        case STRING:
            String value = cell.getStringCellValue();
            // Try to detect Excel serial date format (e.g., "45000" or similar)
            if (value.matches("^4[0-9]{4}$")) {
                try {
                    double excelDate = Double.parseDouble(value);
                    java.util.Date date = DateUtil.getJavaDate(excelDate);
                    return new SimpleDateFormat("yyyy-MM-dd").format(date);
                } catch (Exception e) {
                    // Not a valid Excel date, return as is
                    return value;
                }
            }
            return value;
        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
            } else {
                // Check if this is the Montant column
                int columnIndex = cell.getColumnIndex();
                String columnHeader = cell.getSheet().getRow(0).getCell(columnIndex).getStringCellValue();
                if ("Montant".equalsIgnoreCase(columnHeader)) {
                    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                    return df.format(cell.getNumericCellValue());
                }
                // If value looks like an Excel date serial (e.g., 45000), convert to date
                double num = cell.getNumericCellValue();
                if (num >= 40000 && num < 60000) { // Excel date serial range (approx 2009-2050)
                    java.util.Date date = DateUtil.getJavaDate(num);
                    return new SimpleDateFormat("yyyy-MM-dd").format(date);
                }
                // For other numeric values, return as int if possible
                if (num == (int) num) {
                    return String.valueOf((int) num);
                } else {
                    return String.valueOf(num);
                }
            }
        case BOOLEAN:
            return String.valueOf(cell.getBooleanCellValue());
        case FORMULA:
            try {
                if (DateUtil.isCellDateFormatted(cell)) {
                    java.util.Date date = cell.getDateCellValue();
                    return new SimpleDateFormat("yyyy-MM-dd").format(date);
                }
                int columnIndex = cell.getColumnIndex();
                String columnHeader = cell.getSheet().getRow(0).getCell(columnIndex).getStringCellValue();
                if ("Montant".equalsIgnoreCase(columnHeader)) {
                    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                    return df.format(cell.getNumericCellValue());
                }
                double num = cell.getNumericCellValue();
                if (num >= 40000 && num < 60000) {
                    java.util.Date date = DateUtil.getJavaDate(num);
                    return new SimpleDateFormat("yyyy-MM-dd").format(date);
                }
                if (num == (int) num) {
                    return String.valueOf((int) num);
                } else {
                    return String.valueOf(num);
                }
            } catch (IllegalStateException e) {
                String valueFormula = cell.getStringCellValue();
                if (valueFormula.matches("^4[0-9]{4}$")) {
                    try {
                        double excelDate = Double.parseDouble(valueFormula);
                        java.util.Date date = DateUtil.getJavaDate(excelDate);
                        return new SimpleDateFormat("yyyy-MM-dd").format(date);
                    } catch (Exception ex) {
                        return valueFormula;
                    }
                }
                return valueFormula;
            }
        default:
            return "";
    }
}
        // Fonction d'aide pour les cases à cocher
        String caseACocher(boolean value) {
            return value ? "✓" : "☐";
        }
@FXML
private void ecraser(ActionEvent event) {
    // Demande confirmation à l'utilisateur
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    confirm.setTitle("Confirmation");
    confirm.setHeaderText("Voulez-vous vraiment écraser toutes les démarches existantes ?");
    confirm.setContentText("Cette action supprimera toutes les démarches et importera celles du fichier sélectionné.");
    if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
        return;
    }

    // Supprimer toutes les démarches
    dao.deleteAll();

    // Sélectionner le fichier à importer
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Sélectionner un fichier Excel");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
    File file = fileChooser.showOpenDialog(null);

    if (file != null) {
        importFromExcel(file);
        // Recharger la table
        loadDemarches();
        showAlert("Succès", "Les démarches ont été écrasées et réimportées avec succès !");
    }
}
}
