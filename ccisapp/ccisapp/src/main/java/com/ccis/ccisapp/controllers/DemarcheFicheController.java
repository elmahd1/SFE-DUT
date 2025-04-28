package com.ccis.ccisapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.CheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ccis.ccisapp.models.DemarcheAdministratif;
import com.ccis.ccisapp.repositories.DemarcheAdministratifRepo;

import org.springframework.stereotype.Component;

@Component
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
    @FXML private Label autreFormeJuridiqueError;
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
    @FXML private TextField autreFormeJuridique;
    @FXML private DatePicker dateDepot;
    @FXML private TextField heureDepot;
    @FXML private ComboBox<String> secteurActivite;
    @FXML private TextField activite;
    @FXML private TextField nomPrenomConseillerCCIS;
    @FXML private TextField qualiteConseillerCCIS;
    @FXML private ComboBox<String> etatDossier;
    @FXML private ComboBox<String> suiteDemande;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane scrollContent;
 @FXML
    public void initialize() {
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
ObservableList<String> objetVisteList = FXCollections.observableArrayList(
    "Carte professionnelle",
    "Attestation professionnelle",
    "Visa des factures",
    "Visa de certificats sanitaires/phytosanitaires",
    "Visa des documents commerciaux",
    "Certificat d’origine",
    "Recommandation pour Visa Affaires"
        );
        objetVisite.setItems(objetVisteList);
        // Autres initialisations si nécessaire
    }

@Autowired
private DemarcheAdministratifRepo repo;
    // Méthode de soumission du formulaire
    @FXML
    public void handleSubmit(ActionEvent event) {
        boolean isValid = true;
    
        // Clear previous errors
        clearAllErrors();
        
        // Date Contact validation
        if (dateContact.getValue() == null) {
            showError(dateContact,dateContactError ,"Veuillez sélectionner une date de contact");
            isValid = false;
        }
        
        // Heure Contact validation (HH:mm format)
        if (!heureContact.getText().matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            showError(heureContact,heureContactError, "Format d'heure invalide (HH:mm)");
            isValid = false;
        }
        
        // Type Demande validation
        if (typeDemande.getValue() == null || typeDemande.getValue().isEmpty()) {
            showError(typeDemande,typeDemandeError ,"Veuillez sélectionner un type de demande");
            isValid = false;
        }
        
        // Status validation
        if (status.getValue() == null || status.getValue().isEmpty()) {
            showError(status,statusError ,"Veuillez sélectionner un statut");
            isValid = false;
        }
        
        // Email validation
        if (!email.getText().isEmpty() && !email.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showError(email,emailError ,"Format d'email invalide");
            isValid = false;
        }
        
        // Telephone validation (at least one phone number required)
        if ( telephoneGSM.getText().isEmpty()) {
            showError(telephoneGSM, telephoneGSMError,"Au moins un numéro de téléphone est requis");
            isValid = false;
        }
        
        // Numeric field validation (montant)
        if (!montant.getText().isEmpty() && !montant.getText().matches("^\\d+([.,]\\d+)?$")) {
            showError(montant,montantError, "Le montant doit être numérique");
            isValid = false;
        }
        
        // Required text fields
        if (nomPrenom.getText().trim().isEmpty()) {
            showError(nomPrenom,nomPrenomError, "Ce champ est obligatoire");
            isValid = false;
        }
        
        if (ville.getText().trim().isEmpty()) {
            showError(ville, villeError,"Ce champ est obligatoire");
            isValid = false;
        }
        
        if (denomination.getText().trim().isEmpty()) {
            showError(denomination, denominationError,"Ce champ est obligatoire");
            isValid = false;
        }
        
        // Forme Juridique validation
        if (formeJuridique.getValue() == null || formeJuridique.getValue().isEmpty()) {
            showError(formeJuridique, formeJuridiqueError,"Veuillez sélectionner une forme juridique");
            isValid = false;
        }
        
        // If all validations pass
        if (isValid) {
            System.out.println("Formulaire valide - Traitement des données...");
            // Process the form data here
        
    
        DemarcheAdministratif demarche = new DemarcheAdministratif();
    
       // Récupération des champs
        demarche.setDateContact(dateContact.getValue() != null ? Timestamp.valueOf(dateContact.getValue().atStartOfDay()) : null);
        demarche.setTypeDemande(typeDemande.getValue());
        demarche.setStatut(status.getValue());
        demarche.setAccepteEnvoi(accepteEnvoiCCIS.isSelected()?"oui":"non" );
        demarche.setNomPrenom(nomPrenom.getText());
        demarche.setFixe(telephoneFix.getText());
        demarche.setGsm(telephoneGSM.getText());
        demarche.setEmail(email.getText());
        demarche.setSiteWeb(siteweb.getText());
        demarche.setAdresse(adresse.getText());
        demarche.setVille(ville.getText());
        demarche.setDenomination(denomination.getText());
        demarche.setNomRepLegal(nomRepresentantLegal.getText());
        demarche.setFormeJuridique(formeJuridique.getValue());
        demarche.setDateDepot(dateDepot.getValue() != null ? Timestamp.valueOf(dateDepot.getValue().atStartOfDay()) : null);
        demarche.setSecteurActivite(secteurActivite.getValue());
        demarche.setActivite(activite.getText());
        demarche.setNomPrenomCCIS(nomPrenomConseillerCCIS.getText());
        demarche.setQualiteCCIS(qualiteConseillerCCIS.getText());
        demarche.setEtatDossier(etatDossier.getValue());
        demarche.setSuiteDemande(suiteDemande.getValue());
    
        // Sauvegarde via le repo
        
        repo.save(demarche);
    
        System.out.println("Formulaire soumis avec succès.");
        } else {
            System.out.println("Veuillez corriger les erreurs dans le formulaire");
        }

    }





// Method to show errors for any control
private void showError(Control control, Label errorLabel, String message) {
    control.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
    errorLabel.setText(message);
    errorLabel.setVisible(true);
    
    // Set tooltip for immediate feedback
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
    // Clear all other fields similarly...
}

 
    public void scrollToBottom() {
        // Run later to make sure layout is calculated
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }
}
