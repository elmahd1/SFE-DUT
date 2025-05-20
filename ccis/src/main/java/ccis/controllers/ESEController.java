package ccis.controllers;

import ccis.dao.EspaceEntrepriseDAO;
import ccis.models.EspaceEntreprise;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

}
