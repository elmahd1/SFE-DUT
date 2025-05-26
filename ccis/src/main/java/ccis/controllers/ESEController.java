package ccis.controllers;

import ccis.dao.EspaceEntrepriseDAO;
import ccis.models.EspaceEntreprise;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Sheet;

public class ESEController {

    @FXML private TableView<EspaceEntreprise> tableView;

    @FXML private TableColumn<EspaceEntreprise, String> colFormeJuridique;
    @FXML private TableColumn<EspaceEntreprise, String> colDateDepot;
    @FXML private TableColumn<EspaceEntreprise, String> colHeureDepot;
    @FXML private TableColumn<EspaceEntreprise, String> colSecteurActivite;
    @FXML private TableColumn<EspaceEntreprise, String> colActivite;
    @FXML private TableColumn<EspaceEntreprise, String> colNomRepresentantLegal;
    @FXML private TableColumn<EspaceEntreprise, String> colQualiteConseillerCcis;
    @FXML private TableColumn<EspaceEntreprise, String> colDateDepart;
    @FXML private TableColumn<EspaceEntreprise, String> colObjetVisite;
    @FXML private TableColumn<EspaceEntreprise, String> colHeureContact;
    @FXML private TableColumn<EspaceEntreprise, String> colDateContact;
    @FXML private TableColumn<EspaceEntreprise, String> colDenomination; 
    @FXML private TableColumn<EspaceEntreprise, Integer> colFixe;
    @FXML private TableColumn<EspaceEntreprise, Integer> colGsm;
    @FXML private TableColumn<EspaceEntreprise, String> colSiegeSocialeAdresse;
    @FXML private TableColumn<EspaceEntreprise, String> colVilleCommunite;
    @FXML private TableColumn<EspaceEntreprise, String> colEmail;
    @FXML private TableColumn<EspaceEntreprise, String> colNomPrenom;
    @FXML private TableColumn<EspaceEntreprise, String> colAccepteEnvoiCcis;
    @FXML private TableColumn<EspaceEntreprise, String> colSiteWeb;
    @FXML private TableColumn<EspaceEntreprise, String> colNomPrenomConseillerCcis;
    @FXML private TableColumn<EspaceEntreprise, String> colHeureDepart;
    @FXML private TableColumn<EspaceEntreprise, String> colStatus;
    @FXML private TableColumn<EspaceEntreprise, String> colCodeICE;
    @FXML private TableColumn<EspaceEntreprise, Void> colActions;


    private final EspaceEntrepriseDAO dao = new EspaceEntrepriseDAO();
    private final ObservableList<EspaceEntreprise> demarchesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        configureColumns();
        // Charger les données depuis la base
        loadEspaces();
      // Adding delete button in the "Actions" column
      setupActionColumn();

    }

    private void setupActionColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");
    
            {
                deleteButton.setOnAction(event -> {
                    EspaceEntreprise selected = getTableView().getItems().get(getIndex());
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
        colSecteurActivite.setCellValueFactory(new PropertyValueFactory<>("secteurActivite"));
        colActivite.setCellValueFactory(new PropertyValueFactory<>("activite"));
        colNomRepresentantLegal.setCellValueFactory(new PropertyValueFactory<>("nomRepLegal"));
        colDateDepart.setCellValueFactory(new PropertyValueFactory<>("dateDepart"));
        colObjetVisite.setCellValueFactory(new PropertyValueFactory<>("objetVisite"));
        colHeureContact.setCellValueFactory(new PropertyValueFactory<>("heureContact"));
        colDateContact.setCellValueFactory(new PropertyValueFactory<>("dateContact"));
        colDenomination.setCellValueFactory(new PropertyValueFactory<>("denomination"));
        colFixe.setCellValueFactory(new PropertyValueFactory<>("fixe"));
        colGsm.setCellValueFactory(new PropertyValueFactory<>("gsm"));
        colSiegeSocialeAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colVilleCommunite.setCellValueFactory(new PropertyValueFactory<>("ville"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNomPrenom.setCellValueFactory(new PropertyValueFactory<>("nomPrenom"));
        colAccepteEnvoiCcis.setCellValueFactory(new PropertyValueFactory<>("accepteEnvoi"));
        colSiteWeb.setCellValueFactory(new PropertyValueFactory<>("siteWeb"));
        colNomPrenomConseillerCcis.setCellValueFactory(new PropertyValueFactory<>("nomPrenomCCIS"));
        colHeureDepart.setCellValueFactory(new PropertyValueFactory<>("heureDepart"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statut"));
colCodeICE.setCellValueFactory(new PropertyValueFactory<>("codeICE"));
colQualiteConseillerCcis.setCellValueFactory(new PropertyValueFactory<>("qualiteCCIS"));
        
    }
    private void loadEspaces() {
        List<EspaceEntreprise> espaces = dao.getAll();
        demarchesList.setAll(espaces);
        tableView.setItems(demarchesList);
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
    private void handleDelete(EspaceEntreprise selectedEspace) {
        if (selectedEspace != null) {
            dao.delete(selectedEspace.getId());
            demarchesList.remove(selectedEspace);
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
    Sheet sheet = workbook.createSheet("Espace Entreprise");

    // Créer la ligne d'en-têtes
    Row headerRow = sheet.createRow(0);
    String[] headers = {
        "Date Contact", "Heure Contact", "Type Demande", "Type", "Objet Visite", 
        "Nom et Prénom",  "Fixe", "GSM", "Email", "Accepte Envoi CCIS", "Site Web",
        "Adresse Siege", "Ville/Commune", "Dénomination", "Code ICE", "Nom Représentant", "Forme Juridique",
         "Secteur Activité", "Activité", "Nom Conseiller CCIS",
        "Qualité Conseiller CCIS", 
         "Date Départ", "Heure Départ"
    };

    for (int i = 0; i < headers.length; i++) {
        headerRow.createCell(i).setCellValue(headers[i]);
    }

    // Ajouter les données
  List<EspaceEntreprise> demarches = dao.getAll();
  int rowNum = 1;
  for (EspaceEntreprise d : demarches) {
      Row row = sheet.createRow(rowNum++);
      row.createCell(0).setCellValue(d.getDateContact());
      row.createCell(1).setCellValue(d.getHeureContact());
        row.createCell(2).setCellValue(d.getObjetVisite());
      row.createCell(3).setCellValue(d.getStatut());
      row.createCell(4).setCellValue(d.getObjetVisite());
      row.createCell(5).setCellValue(d.getNomPrenom());
        row.createCell(6).setCellValue(d.getFixe());
        row.createCell(7).setCellValue(d.getGsm());
        row.createCell(8).setCellValue(d.getEmail());
        row.createCell(9).setCellValue(d.getAccepteEnvoi());
        row.createCell(10).setCellValue(d.getSiteWeb());
        row.createCell(11).setCellValue(d.getAdresse());
        row.createCell(12).setCellValue(d.getVille());
        row.createCell(13).setCellValue(d.getDenomination());
        row.createCell(14).setCellValue(d.getCodeICE());
        row.createCell(15).setCellValue(d.getNomRepLegal());
        row.createCell(16).setCellValue(d.getFormeJuridique());
        row.createCell(17).setCellValue(d.getSecteurActivite());
        row.createCell(18).setCellValue(d.getActivite());
        row.createCell(19).setCellValue(d.getNomPrenomCCIS());
        row.createCell(20).setCellValue(d.getQualiteCCIS());
        row.createCell(21).setCellValue(d.getDateDepart()); 
        row.createCell(22).setCellValue(d.getHeureDepart());
  }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier généré");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName("Etat Suivi Espace Entreprise.xlsx");
        fileChooser.setInitialDirectory(new File("C:/fichiers application ccis/espace entreprise"));
        File output = fileChooser.showSaveDialog(null);
        if (output == null) {
            // User cancelled the save dialog
            return;
        }
    // Sauvegarder le fichier Excel
    try (FileOutputStream fileOut = new FileOutputStream(output)) {
        workbook.write(fileOut);
        if (java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop.getDesktop().open(output);
        }
        else {
            showAlert("Fichier enregistré", "Le fichier a été enregistré avec succès : " + output.getAbsolutePath());
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
        ObservableList<EspaceEntreprise> filteredList = FXCollections.observableArrayList();

        for (EspaceEntreprise d : demarchesList) {
            if (
                (d.getNomPrenom() != null && d.getNomPrenom().toLowerCase().contains(keyword)) ||
                (d.getEmail() != null && d.getEmail().toLowerCase().contains(keyword)) ||
                (d.getObjetVisite() != null && d.getObjetVisite().toLowerCase().contains(keyword)) ||
                (d.getVille() != null && d.getVille().toLowerCase().contains(keyword)) ||
                (d.getStatut() != null && d.getStatut().toLowerCase().contains(keyword))
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
        Map<String, BiConsumer<EspaceEntreprise, String>> fieldMapping = new HashMap<>();
        fieldMapping.put("Dénomination", (d, v) -> d.setDenomination(v));
        fieldMapping.put("Forme Juridique", (d, v) -> d.setFormeJuridique(v));
        fieldMapping.put("Secteur Activité", (d, v) -> d.setSecteurActivite(v));
        fieldMapping.put("Activité", (d, v) -> d.setActivite(v));
        fieldMapping.put("Téléphone Fixe", (d, v) -> d.setFixe(v));
        fieldMapping.put("Téléphone GSM", (d, v) -> d.setGsm(v));
        fieldMapping.put("Adresse", (d, v) -> d.setAdresse(v));
        fieldMapping.put("Ville", (d, v) -> d.setVille(v));
        fieldMapping.put("Email", (d, v) -> d.setEmail(v));
        fieldMapping.put("Date de contact", (d, v) -> d.setDateContact(v));
        fieldMapping.put("Heure de contact", (d, v) -> d.setHeureContact(v));
        fieldMapping.put("Objet de la visite", (d, v) -> d.setObjetVisite(v));
        fieldMapping.put("Nom et Prénom", (d, v) -> d.setNomPrenom(v));
        fieldMapping.put("Accepte Envoi CCIS", (d, v) -> d.setAccepteEnvoi(v));
        fieldMapping.put("Site Web", (d, v) -> d.setSiteWeb(v));
        fieldMapping.put("Nom du Représentant Légal", (d, v) -> d.setNomRepLegal(v));
       fieldMapping.put("Nom et Prénom Conseiller CCIS", (d, v) -> d.setNomPrenomCCIS(v));
        fieldMapping.put("Qualité Conseiller CCIS", (d, v) -> d.setQualiteCCIS(v));
        fieldMapping.put("Date de Départ", (d, v) -> d.setDateDepart(v));
        fieldMapping.put("Heure de Départ", (d, v) -> d.setHeureDepart(v));
        fieldMapping.put("Code ICE", (d, v) -> d.setCodeICE(v));
        fieldMapping.put("Taille Entreprise", (d, v) -> d.setTailleEntreprise(v));
        fieldMapping.put("Statut", (d, v) -> d.setStatut(v));

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            EspaceEntreprise d = new EspaceEntreprise();

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

            new EspaceEntrepriseDAO().create(d);
        }

        System.out.println("Importation terminée avec succès.");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("L'importation a été effectuée avec succès!");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

private String getCellAsString(Cell cell) {
    if (cell == null) return "";
    switch (cell.getCellType()) {
        case STRING: return cell.getStringCellValue();
        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            } else {
                return String.valueOf(cell.getNumericCellValue());
            }
        case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
        case FORMULA:
            try {
                return cell.getStringCellValue(); // or cell.getNumericCellValue() depending
            } catch (IllegalStateException e) {
                return String.valueOf(cell.getNumericCellValue());
            }
        default: return "";
    }
}
}
