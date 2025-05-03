package ccis.controllers;

import ccis.dao.DemarcheAdministratifDao;
import ccis.models.DemarcheAdministratif;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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
    @FXML private TableColumn<DemarcheAdministratif, Integer> colMontant;
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
        colNomPrenom.setCellValueFactory(new PropertyValueFactory<>("nomPrenom"));
        colAccepteEnvoiCcis.setCellValueFactory(new PropertyValueFactory<>("accepteEnvoi"));
        colSiteWeb.setCellValueFactory(new PropertyValueFactory<>("siteWeb"));
        colNomPrenomConseillerCcis.setCellValueFactory(new PropertyValueFactory<>("nomPrenomCCIS"));
        colHeureDelivrance.setCellValueFactory(new PropertyValueFactory<>("heureDelivrance"));
    }
    private void loadDemarches() {
        List<DemarcheAdministratif> demarches = dao.getAllDemarches();
        demarchesList.setAll(demarches);
        tableView.setItems(demarchesList);
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
        "Date Contact", "Heure Contact", "Type Demande", "Type", "Objet Visite", "Montant",
        "Nom et Prénom", "Fixe", "GSM", "Email", "Accepte Envoi CCIS", "Site Web",
        "Adresse Siege", "Ville/Commune", "Dénomination", "Nom Représentant", "Forme Juridique",
        "Date Depot", "Heure Depot", "Secteur Activité", "Activité", "Nom Conseiller CCIS",
        "Qualité Conseiller CCIS", "Etat Dossier", "Suite Commande", "Observation",
        "Date Délivrance", "Heure Délivrance"
    };

    for (int i = 0; i < headers.length; i++) {
        headerRow.createCell(i).setCellValue(headers[i]);
    }

    // Ajouter les données
  List<DemarcheAdministratif> demarches = dao.getAllDemarches();
  int rowNum = 1;
  for (DemarcheAdministratif d : demarches) {
      Row row = sheet.createRow(rowNum++);
      row.createCell(0).setCellValue(d.getDateContact());
      row.createCell(1).setCellValue(d.getHeureContact());
      row.createCell(2).setCellValue(d.getTypeDemande());
      row.createCell(3).setCellValue(d.getStatut());
      row.createCell(4).setCellValue(d.getObjetVisite());
      row.createCell(5).setCellValue(d.getMontant());
      row.createCell(6).setCellValue(d.getNomPrenom());
      row.createCell(7).setCellValue(d.getFixe());
      row.createCell(8).setCellValue(d.getGsm());
      row.createCell(9).setCellValue(d.getEmail());
      row.createCell(10).setCellValue(d.getAccepteEnvoi());
      row.createCell(11).setCellValue(d.getSiteWeb());
      row.createCell(12).setCellValue(d.getAdresse());
      row.createCell(13).setCellValue(d.getVille());
      row.createCell(14).setCellValue(d.getDenomination());
      row.createCell(15).setCellValue(d.getNomRepLegal());
      row.createCell(16).setCellValue(d.getFormeJuridique());
      row.createCell(17).setCellValue(d.getDateDepot());
      row.createCell(18).setCellValue(d.getHeureDepot());
      row.createCell(19).setCellValue(d.getSecteurActivite());
      row.createCell(20).setCellValue(d.getActivite());
      row.createCell(21).setCellValue(d.getNomPrenomCCIS());
      row.createCell(22).setCellValue(d.getQualiteCCIS());
      row.createCell(23).setCellValue(d.getEtatDossier());
      row.createCell(24).setCellValue(d.getSuiteDemande());
      row.createCell(25).setCellValue(d.getObservation());
      row.createCell(26).setCellValue(d.getDateDelivrance());
      row.createCell(27).setCellValue(d.getHeureDelivrance());
  }

    File exportDir=new File("C:\\ccis documents\\demarche administratif");
    if (!exportDir.exists()) {
        exportDir.mkdirs();
    }
    String filePath = "C:\\ccis documents\\demarche administratif\\demarche_administrati.xlsx";
   
    // Sauvegarder le fichier Excel
    try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
        workbook.write(fileOut);
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

}
