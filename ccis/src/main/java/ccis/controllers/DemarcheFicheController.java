package ccis.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.application.Platform;
import ccis.models.DemarcheAdministratif;
import org.apache.poi.ss.usermodel.Sheet;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import ccis.dao.DemarcheAdministratifDao;

public class DemarcheFicheController {

    @FXML private DatePicker dateContact;
    @FXML private Label dateContactError;
    @FXML private TextField heureContact;
    @FXML private Label heureContactError;
    @FXML private ComboBox<String> typeDemande;
    @FXML private Label typeDemandeError;
    @FXML private ComboBox<String> status;
    @FXML private Label statusError;
    @FXML private ComboBox<String> objetVisite;
    @FXML private Label objetVisiteError;
    @FXML private Label montantError;
    @FXML private Label nomPrenomError;
    @FXML private Label telephoneFixError;
    @FXML private Label telephoneGSMError;
    @FXML private Label emailError;
    @FXML private Label sitewebError;
    @FXML private Label adresseError;
    @FXML private Label villeError;
    @FXML private Label denominationError;
    @FXML private Label nomRepresentantLegalError;
    @FXML private Label formeJuridiqueError;
    @FXML private Label dateDepotError;
    @FXML private Label heureDepotError;
    @FXML private Label secteurActiviteError;
    @FXML private Label activiteError;
    @FXML private Label nomPrenomConseillerCCISError;
    @FXML private Label qualiteConseillerCCISError;
    @FXML private Label etatDossierError;
    @FXML private Label suiteDemandeError;
    @FXML private Label erreurLabel;
    @FXML private TextField montant;
    @FXML private CheckBox accepteEnvoiCCIS;
    @FXML private TextField nomPrenom;
    @FXML private TextField telephoneFix;
    @FXML private TextField telephoneGSM;
    @FXML private TextField email;
    @FXML private TextField siteweb;
    @FXML private TextField adresse;
    @FXML private TextField ville;
    @FXML private TextField denomination;
    @FXML private TextField nomRepresentantLegal;
    @FXML private ComboBox<String> formeJuridique;
    @FXML private DatePicker dateDepot;
    @FXML private TextField heureDepot;
    @FXML private ComboBox<String> secteurActivite;
    @FXML private TextField activite;
    @FXML private TextField nomPrenomConseillerCCIS;
    @FXML private TextField qualiteConseillerCCIS;
    @FXML private ComboBox<String> etatDossier;
    @FXML private ComboBox<String> suiteDemande;
    @FXML private TextField interlocuteur; // Add this new field
    @FXML private Label interlocuteurError; // Add this new field
    @FXML private TextField observation; // Add this new field
    @FXML private Label observationError; // Add this new field
    @FXML private DatePicker dateDelivrance; // Add this new field
    @FXML private Label dateDelivranceError; // Add this new field
    @FXML private TextField heureDelivrance; // Add this new field
    @FXML private Label heureDelivranceError;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox scrollContent;
    @FXML private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // Create an instance of DemarcheAdministratifDAO
    private DemarcheAdministratifDao demarcheDAO = new DemarcheAdministratifDao();

    @FXML
    public void initialize() {
        
    nomPrenomConseillerCCIS.setText("Rachid BNINHA");
    qualiteConseillerCCIS.setText("Chef de département services aux ressortissants");
        // Type Demande ComboBox
        ObservableList<String> typeDemandeOptions = FXCollections.observableArrayList(
            "Demande d’information", 
            "Demande de document"
        );
        typeDemande.setItems(typeDemandeOptions);

        // Status ComboBox
        ObservableList<String> statusOptions = FXCollections.observableArrayList(
            "Personne Physique", 
            "Personne Morale"
        );
        status.setItems(statusOptions);

        // Forme Juridique ComboBox
        ObservableList<String> formeJuridiqueOptions = FXCollections.observableArrayList(
            "SARL", 
            "SA", 
            "Auto-entrepreneur", 
            "Autre"
        );
        formeJuridique.setItems(formeJuridiqueOptions);
        formeJuridique.setEditable(true);

        // Secteur Activité ComboBox
        ObservableList<String> secteurActiviteOptions = FXCollections.observableArrayList(
            "Industrie", 
            "Commerce", 
            "Services"
        );
        secteurActivite.setItems(secteurActiviteOptions);

        // Etat Dossier ComboBox
        ObservableList<String> etatDossierOptions = FXCollections.observableArrayList(
            "Complet et conforme", 
            "Incomplet", 
            "Non conforme"
        );
        etatDossier.setItems(etatDossierOptions);

        // Suite Demande ComboBox
        ObservableList<String> suiteDemandeOptions = FXCollections.observableArrayList(
            "Acceptée", 
            "Rejetée"
        );
        suiteDemande.setItems(suiteDemandeOptions);
        
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        ObservableList<String> objetVisiteList = FXCollections.observableArrayList(
            "Carte professionnelle",
            "Attestation professionnelle",
            "Visa des factures",
            "Visa de certificats sanitaires/phytosanitaires",
            "Visa des documents commerciaux",
            "Certificat d’origine",
            "Recommandation pour Visa Affaires"
        );
        objetVisite.setItems(objetVisiteList);
    }

    @FXML
    public void handleSubmit(ActionEvent event) {
        boolean isValid = true;
        clearAllErrors();
        
        // Add validation logic for all required fields here
        // Example:
        if (dateContact.getValue() == null) {
            showError(dateContact, dateContactError, "La date de contact est requise");
            isValid = false;
        }
        
        if (heureContact.getText().isEmpty()) {
            showError(heureContact, heureContactError, "L'heure de contact est requise");
            isValid = false;
        }
        if (typeDemande.getValue() == null) {
            showError(typeDemande, typeDemandeError, "Le type de demande est requis");
            isValid = false;
        }
        if (status.getValue() == null) {
            showError(status, statusError, "Le statut est requis");
            isValid = false;
        }
        if (objetVisite.getValue() == null) {
            showError(objetVisite, objetVisiteError, "L'objet de la visite est requis");
            isValid = false;
        }
        if (nomPrenom.getText().isEmpty()) {
            showError(nomPrenom, nomPrenomError, "Le nom et prénom sont requis");
            isValid = false;
        }
        if (telephoneGSM.getText().isEmpty()) {
            showError(telephoneGSM, telephoneGSMError, "Le téléphone GSM est requis");
            isValid = false;
        }
        if (email.getText().isEmpty()) {
            showError(email, emailError, "L'email est requis");
            isValid = false;
        }
        if (adresse.getText().isEmpty()) {
            showError(adresse, adresseError, "L'adresse est requise");
            isValid = false;
        }
        if (ville.getText().isEmpty()) {
            showError(ville, villeError, "La ville est requise");
            isValid = false;
        }
        if (denomination.getText().isEmpty()) {
            showError(denomination, denominationError, "La dénomination est requise");
            isValid = false;
        }
        if (nomRepresentantLegal.getText().isEmpty()) {
            showError(nomRepresentantLegal, nomRepresentantLegalError, "Le nom du représentant légal est requis");
            isValid = false;
        }
        if (formeJuridique.getValue() == null) {
            showError(formeJuridique, formeJuridiqueError, "La forme juridique est requise");
            isValid = false;
        }
        if (dateDepot.getValue() == null) {
            showError(dateDepot, dateDepotError, "La date de dépôt est requise");
            isValid = false;
        }
        if (heureDepot.getText().isEmpty()) {
            showError(heureDepot, heureDepotError, "L'heure de dépôt est requise");
            isValid = false;
        }
        if (secteurActivite.getValue() == null) {
            showError(secteurActivite, secteurActiviteError, "Le secteur d'activité est requis");
            isValid = false;
        }
        if (activite.getText().isEmpty()) {
            showError(activite, activiteError, "L'activité est requise");
            isValid = false;
        }

       
if (isValid) {
    try {
        System.out.println("Tentative d'enregistrement...");
        DemarcheAdministratif demarche = new DemarcheAdministratif();
        
        // Required fields validation
        if (dateContact.getValue() == null) {
            throw new IllegalArgumentException("La date de contact est obligatoire");
        }
        demarche.setDateContact(dateContact.getValue().format(dateFormatter));
        
        // Time fields with validation
        validateTimeFormat(heureContact.getText(), "Heure de contact");
        demarche.setHeureContact(heureContact.getText());
        
        // Other required fields
        if (typeDemande.getValue() == null) {
            throw new IllegalArgumentException("Le type de demande est obligatoire");
        }
        demarche.setTypeDemande(typeDemande.getValue());
        
        // Handle numeric field with proper validation
        try {
            demarche.setMontant(montant.getText().isEmpty() ? 0 : 
                Float.parseFloat(montant.getText()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Montant invalide");
        }
        
        // Required text fields
        if (nomPrenom.getText() == null || nomPrenom.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom et prénom sont obligatoires");
        }
        demarche.setNomPrenom(nomPrenom.getText().trim());
        
        // Email validation
        if (!email.getText().isEmpty() && !isValidEmail(email.getText())) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
        demarche.setEmail(email.getText());
        
        // Set other fields with null checks
        demarche.setStatut(status.getValue() != null ? status.getValue() : "");
        demarche.setObjetVisite(objetVisite.getValue() != null ? objetVisite.getValue() : "");
        demarche.setFixe(telephoneFix.getText() != null ? telephoneFix.getText() : "");
        demarche.setGsm(telephoneGSM.getText() != null ? telephoneGSM.getText() : "");
        demarche.setSiteWeb(siteweb.getText() != null ? siteweb.getText() : "");
        demarche.setAdresse(adresse.getText() != null ? adresse.getText() : "");
        demarche.setVille(ville.getText() != null ? ville.getText() : "");
        demarche.setDenomination(denomination.getText() != null ? denomination.getText() : "");
        demarche.setNomRepLegal(nomRepresentantLegal.getText() != null ? nomRepresentantLegal.getText() : "");
        demarche.setFormeJuridique(formeJuridique.getValue() != null ? formeJuridique.getValue() : "");
        
        // Date fields with null checks
        demarche.setDateDepot(dateDepot.getValue() != null ? dateDepot.getValue().format(dateFormatter) : null);
        validateTimeFormat(heureDepot.getText(), "Heure de dépôt");
        demarche.setHeureDepot(heureDepot.getText());
        
        // Other fields
        demarche.setSecteurActivite(secteurActivite.getValue() != null ? secteurActivite.getValue() : "");
        demarche.setActivite(activite.getText() != null ? activite.getText() : "");
        demarche.setNomPrenomCCIS(nomPrenomConseillerCCIS.getText() != null ? nomPrenomConseillerCCIS.getText() : "");
        demarche.setQualiteCCIS(qualiteConseillerCCIS.getText() != null ? qualiteConseillerCCIS.getText() : "");
        demarche.setEtatDossier(etatDossier.getValue() != null ? etatDossier.getValue() : "");
        demarche.setSuiteDemande(suiteDemande.getValue() != null ? suiteDemande.getValue() : "");
        demarche.setObservation(observation.getText() != null ? observation.getText() : "");
        demarche.setDateDelivrance(dateDelivrance.getValue() != null ? dateDelivrance.getValue().format(dateFormatter) : null);
        validateTimeFormat(heureDelivrance.getText(), "Heure de délivrance");
        demarche.setHeureDelivrance(heureDelivrance.getText());
        
        demarche.setAccepteEnvoi(accepteEnvoiCCIS.isSelected() ? "Oui" : "Non");

        System.out.println("Données à enregistrer: " + demarche.toString());
        demarcheDAO.insertDemarche(demarche);
        System.out.println("Enregistrement réussi");
        
          // Show success message
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Succès");
          alert.setHeaderText(null);
          alert.setContentText("La démarche a été enregistrée avec succès!");
          alert.showAndWait();
          

          

          
    } catch (IllegalArgumentException e) {
        showErrorAlert("Erreur de validation", e.getMessage());
    } catch (Exception e) {
        showErrorAlert("Erreur", "Une erreur est survenue lors de l'enregistrement: " + e.getMessage());
        e.printStackTrace();
    }
}
        }

@FXML
        private void clearForm() {
            dateContact.setValue(null);
            heureContact.clear();
            typeDemande.getSelectionModel().clearSelection();
            status.getSelectionModel().clearSelection();
            objetVisite.getSelectionModel().clearSelection();
            montant.clear(); 
            accepteEnvoiCCIS.setSelected(false);
            nomPrenom.clear();
            telephoneFix.clear();
            telephoneGSM.clear();
            email.clear();
            siteweb.clear();
            adresse.clear();
            ville.clear();
            denomination.clear();
            nomRepresentantLegal.clear();
            formeJuridique.getSelectionModel().clearSelection();
            dateDepot.setValue(null);
            heureDepot.clear();
            secteurActivite.getSelectionModel().clearSelection();
            activite.clear();
            observation.clear();
            dateDelivrance.setValue(null);
            heureDelivrance.clear();
        }
    // Method to show errors for any control
    private void showError(Control control, Label errorLabel, String message) {
        control.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        control.setTooltip(new Tooltip(message));
    }

    // Method to clear errors
    private void clearError(Control control, Label errorLabel) {
        control.setStyle("");
        errorLabel.setVisible(false);
        control.setTooltip(null);
    }

    // Method to clear all errors
    private void clearAllErrors() {
        clearError(dateContact, dateContactError);
        clearError(heureContact, heureContactError);
        clearError(typeDemande, typeDemandeError);
        clearError(status, statusError);
        clearError(objetVisite, objetVisiteError);
        clearError(montant, montantError);
        clearError(nomPrenom, nomPrenomError);
        clearError(telephoneGSM, telephoneGSMError);
        clearError(email, emailError);
        clearError(siteweb, sitewebError);
        clearError(adresse, adresseError);
        clearError(ville, villeError);
        clearError(denomination, denominationError);
        clearError(nomRepresentantLegal, nomRepresentantLegalError);
        clearError(formeJuridique, formeJuridiqueError);


        clearError(montant, montantError);
        clearError(typeDemande, typeDemandeError);
        clearError(status, statusError);
        clearError(objetVisite, objetVisiteError);
    }

    public void scrollToBottom() {
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }
    // Helper methods
private void validateTimeFormat(String time, String fieldName) {
    if (time != null && !time.isEmpty() && !time.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
        throw new IllegalArgumentException(fieldName + " doit être au format HH:MM");
    }
}

private boolean isValidEmail(String email) {
    return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
}

private void showErrorAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}
@FXML
private void handleImport(ActionEvent event) {

}
}