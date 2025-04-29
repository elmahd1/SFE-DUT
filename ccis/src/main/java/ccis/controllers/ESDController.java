package ccis.controllers;

import ccis.dao.DemarcheAdministratifDao;
import ccis.models.DemarcheAdministratif;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

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
    private void handleDelete() {
        DemarcheAdministratif selectedDemarche = tableView.getSelectionModel().getSelectedItem();
        if (selectedDemarche != null) {
            dao.deleteDemarche(selectedDemarche.getId());
            demarchesList.remove(selectedDemarche);
        } else {
            showAlert("Aucune sélection", "Veuillez sélectionner une démarche à supprimer.");
        }
    }

    @FXML
    private void handleSave() {
        // Suppose que tu as un formulaire pour saisir une démarche
        DemarcheAdministratif newDemarche = new DemarcheAdministratif();
        // Remplir newDemarche avec les données du formulaire

        dao.insertDemarche(newDemarche);
        loadDemarches(); // Rafraîchir la table
    }

    @FXML
    private void handleUpdate() {
        DemarcheAdministratif selectedDemarche = tableView.getSelectionModel().getSelectedItem();
        if (selectedDemarche != null) {
            // Modifier selectedDemarche avec les données du formulaire
            dao.updateDemarche(selectedDemarche);
            loadDemarches(); // Rafraîchir la table
        } else {
            showAlert("Aucune sélection", "Veuillez sélectionner une démarche à modifier.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
