package ccis.controllers;

import ccis.dao.DemarcheAdministratifDao;
import ccis.models.DemarcheAdministratif;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

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
    @FXML private TableColumn<DemarcheAdministratif, String> actionCol;

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
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeDemande"));
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
