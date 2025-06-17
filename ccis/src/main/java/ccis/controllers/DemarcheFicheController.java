package ccis.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import ccis.models.DemarcheAdministratif;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import ccis.dao.DemarcheAdministratifDao;

public class DemarcheFicheController {

    @FXML private DatePicker dateContact;
    @FXML private TextField heureContact;
    @FXML private ComboBox<String> typeDemande;
    @FXML private ComboBox<String> status;
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
    @FXML private TextField interlocuteur;
    @FXML private TextField observation;
    @FXML private DatePicker dateDelivrance;
    @FXML private TextField heureDelivrance;
    @FXML private CheckBox objet1;
    @FXML private CheckBox objet2;
    @FXML private CheckBox objet3;
    @FXML private CheckBox objet4;
    @FXML private CheckBox objet5;
    @FXML private TextField m1;
    @FXML private TextField m2;
    @FXML private TextField m3;
    @FXML private TextField m4;
    @FXML private TextField m5;

    @FXML private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DemarcheAdministratifDao demarcheDAO = new DemarcheAdministratifDao();

    // Mapping des CheckBox avec leurs montants
    private final Map<CheckBox, String> checkBoxToOptionMap = new HashMap<>();
    private final Map<String, Map<String, Integer>> objectAmounts = Map.of(
        "personne physique", Map.of(
            "Attestation professionnelle", 50,
            "Carte professionnelle", 50,
            "Visa des documents commerciaux", 200,
            "Visa des factures", 150,
            "Certificat d'origine", 300
        ),
        "personne morale", Map.of(
            "Attestation professionnelle", 100,
            "Carte professionnelle", 150,
            "Visa des documents commerciaux", 200,
            "Visa des factures", 150,
            "Certificat d'origine", 300
        )
    );

    @FXML
    public void initialize() {
        setupTimeValidation(heureContact);
        setupTimeValidation(heureDepot);
        setupTimeValidation(heureDelivrance);
        dateContact.setValue(java.time.LocalDate.now());
        etatDossier.setValue("Complet et conforme");
        suiteDemande.setValue("Acceptée");
        dateDelivrance.setValue(java.time.LocalDate.now());
        dateDepot.setValue(java.time.LocalDate.now());
        nomPrenomConseillerCCIS.setText("Rachid BNINHA");
        ville.setText("Essaouira");
        qualiteConseillerCCIS.setText("Chef de département services aux ressortissants");
        
        // Mapping des CheckBox avec leurs options
        checkBoxToOptionMap.put(objet1, "Attestation professionnelle");
        checkBoxToOptionMap.put(objet2, "Carte professionnelle");
        checkBoxToOptionMap.put(objet3, "Visa des documents commerciaux");
        checkBoxToOptionMap.put(objet4, "Visa des factures");
        checkBoxToOptionMap.put(objet5, "Certificat d'origine");

        nomPrenom.textProperty().addListener((observable, oldValue, newValue) -> {
            denomination.setText(newValue);
            nomRepresentantLegal.setText(newValue);
        });

        dateContact.valueProperty().addListener((observable, oldValue, newValue) -> {
            dateDepot.setValue(newValue);
            dateDelivrance.setValue(newValue);
        });

        // Type Demande ComboBox
        ObservableList<String> typeDemandeOptions = FXCollections.observableArrayList(
            "Demande d'information /renseignement à propos d'un document administratif",
            "Demande de document administratif"
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
            "PP (Personne physique)",
            "Auto-entrepreneur",
            "SARL",
            "SA",
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

        accepteEnvoiCCIS.setSelected(true);
    status.valueProperty().addListener((observable, oldValue, newValue) -> {
    updateMontantFields();
});
objet1.selectedProperty().addListener((observable, oldValue, newValue) -> updateMontantFields());
objet2.selectedProperty().addListener((observable, oldValue, newValue) -> updateMontantFields());
objet3.selectedProperty().addListener((observable, oldValue, newValue) -> updateMontantFields());
objet4.selectedProperty().addListener((observable, oldValue, newValue) -> updateMontantFields());
objet5.selectedProperty().addListener((observable, oldValue, newValue) -> updateMontantFields());

    }
private void updateMontantFields() {
    // Clear all montant fields first
    m1.clear();
    m2.clear();
    m3.clear();
    m4.clear();
    m5.clear();
    
    String selectedStatus = status.getValue();
    if (selectedStatus == null) {
        return;
    }
    
    // Normalize status for map lookup
    String statusKey = selectedStatus.toLowerCase().contains("physique") ? "personne physique" : "personne morale";
    Map<String, Integer> amounts = objectAmounts.get(statusKey);
    
    if (amounts != null) {
        // Update montant fields based on selected checkboxes
        if (objet1.isSelected()) { // Attestation professionnelle
            String objetVisite = checkBoxToOptionMap.get(objet1);
            Integer montant = amounts.get(objetVisite);
            if (montant != null) {
                m1.setText(montant.toString());
            }
        }
        
        if (objet2.isSelected()) { // Carte professionnelle
            String objetVisite = checkBoxToOptionMap.get(objet2);
            Integer montant = amounts.get(objetVisite);
            if (montant != null) {
                m2.setText(montant.toString());
            }
        }
        
        if (objet3.isSelected()) { // Visa des documents commerciaux
            String objetVisite = checkBoxToOptionMap.get(objet3);
            Integer montant = amounts.get(objetVisite);
            if (montant != null) {
                m3.setText(montant.toString());
            }
        }
        
        if (objet4.isSelected()) { // Visa des factures
            String objetVisite = checkBoxToOptionMap.get(objet4);
            Integer montant = amounts.get(objetVisite);
            if (montant != null) {
                m4.setText(montant.toString());
            }
        }
        
        if (objet5.isSelected()) { // Certificat d'origine
            String objetVisite = checkBoxToOptionMap.get(objet5);
            Integer montant = amounts.get(objetVisite);
            if (montant != null) {
                m5.setText(montant.toString());
            }
        }
    }
}
    @FXML
    public void handleSubmit(ActionEvent event) {
        boolean isValid = true;
        clearAllErrors();

        // Validation des champs obligatoires
        if (dateContact.getValue() == null) {
            showError(dateContact, "La date de contact est requise");
            isValid = false;
        }

        if (heureContact.getText().isEmpty()) {
            showError(heureContact, "L'heure de contact est requise");
            isValid = false;
        }
        if (typeDemande.getValue() == null) {
            showError(typeDemande, "Le type de demande est requis");
            isValid = false;
        }
        if (status.getValue() == null) {
            showError(status, "Le statut est requis");
            isValid = false;
        }

        if (nomPrenom.getText().isEmpty()) {
            showError(nomPrenom, "Le nom et prénom sont requis");
            isValid = false;
        }
        if (telephoneGSM.getText().isEmpty()) {
            showError(telephoneGSM, "Le téléphone GSM est requis");
            isValid = false;
        }
        if (adresse.getText().isEmpty()) {
            showError(adresse, "L'adresse est requise");
            isValid = false;
        }
        if (ville.getText().isEmpty()) {
            showError(ville, "La ville est requise");
            isValid = false;
        }
        if (denomination.getText().isEmpty()) {
            showError(denomination, "La dénomination est requise");
            isValid = false;
        }
        if (nomRepresentantLegal.getText().isEmpty()) {
            showError(nomRepresentantLegal, "Le nom du représentant légal est requis");
            isValid = false;
        }
        if (formeJuridique.getValue() == null) {
            showError(formeJuridique, "La forme juridique est requise");
            isValid = false;
        }
        if (dateDepot.getValue() == null) {
            showError(dateDepot, "La date de dépôt est requise");
            isValid = false;
        }
        if (heureDepot.getText().isEmpty()) {
            showError(heureDepot, "L'heure de dépôt est requise");
            isValid = false;
        }
        if (secteurActivite.getValue() == null) {
            showError(secteurActivite, "Le secteur d'activité est requis");
            isValid = false;
        }
        if (activite.getText().isEmpty()) {
            showError(activite, "L'activité est requise");
            isValid = false;
        }

        // Vérifier qu'au moins un objet de visite est sélectionné
        boolean hasSelectedObject = objet1.isSelected() || objet2.isSelected() || 
                                  objet3.isSelected() || objet4.isSelected() || objet5.isSelected();
        if (!hasSelectedObject) {
            showError(objet1, "Sélectionnez au moins un objet de visite");
            isValid = false;
        }

        if (isValid) {
            try {
                System.out.println("Tentative d'enregistrement...");
                
                // Collecter les objets sélectionnés
                List<CheckBox> selectedCheckBoxes = new ArrayList<>();
                if (objet1.isSelected()) selectedCheckBoxes.add(objet1);
                if (objet2.isSelected()) selectedCheckBoxes.add(objet2);
                if (objet3.isSelected()) selectedCheckBoxes.add(objet3);
                if (objet4.isSelected()) selectedCheckBoxes.add(objet4);
                if (objet5.isSelected()) selectedCheckBoxes.add(objet5);

                // Créer une démarche pour chaque objet sélectionné
                for (CheckBox selectedCheckBox : selectedCheckBoxes) {
                    DemarcheAdministratif demarche = new DemarcheAdministratif();
                    
                    // Remplir les données communes
                    fillCommonData(demarche);
                    
                    // Définir l'objet de visite spécifique
                    String objetVisite = checkBoxToOptionMap.get(selectedCheckBox);
                    demarche.setObjetVisite(objetVisite);
                    
String objetVisite2 = checkBoxToOptionMap.get(selectedCheckBox);
double montant = calculateMontant(objetVisite2);
demarche.setMontant(montant);
                    demarche.setMontant(montant);
                    
                    // Enregistrer cette démarche
                    demarcheDAO.insertDemarche(demarche);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Les fiches de démarche administrative ont été enregistrées avec succès.");
                alert.showAndWait();

            } catch (IllegalArgumentException e) {
                showErrorAlert("Erreur de validation", e.getMessage());
            } catch (Exception e) {
                showErrorAlert("Erreur", "Une erreur est survenue lors de l'enregistrement: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showErrorAlert("Erreur de validation", "Veuillez remplir tous les champs obligatoires.");
        }
    }

    private void fillCommonData(DemarcheAdministratif demarche) {
        // Validation et remplissage des données communes
        if (dateContact.getValue() == null) {
            throw new IllegalArgumentException("La date de contact est obligatoire");
        }
        demarche.setDateContact(dateContact.getValue().format(dateFormatter));
        
        validateTimeFormat(heureContact.getText(), "Heure de contact");
        demarche.setHeureContact(heureContact.getText());
        
        if (typeDemande.getValue() == null) {
            throw new IllegalArgumentException("Le type de demande est obligatoire");
        }
        demarche.setTypeDemande(typeDemande.getValue());
        
        if (nomPrenom.getText() == null || nomPrenom.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom et prénom sont obligatoires");
        }
        demarche.setNomPrenom(nomPrenom.getText().trim());
        
        if (!email.getText().isEmpty() && !isValidEmail(email.getText())) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
        demarche.setEmail(email.getText());
        demarche.setStatut(status.getValue() != null ? status.getValue() : "");
        demarche.setFixe(telephoneFix.getText() != null ? telephoneFix.getText() : "");
        demarche.setGsm(telephoneGSM.getText() != null ? telephoneGSM.getText() : "");
        demarche.setSiteWeb(siteweb.getText() != null ? siteweb.getText() : "");
        demarche.setAdresse(adresse.getText() != null ? adresse.getText() : "");
        demarche.setVille(ville.getText() != null ? ville.getText() : "");
        demarche.setDenomination(denomination.getText() != null ? denomination.getText() : "");
        demarche.setNomRepLegal(nomRepresentantLegal.getText() != null ? nomRepresentantLegal.getText() : "");
        demarche.setFormeJuridique(formeJuridique.getValue() != null ? formeJuridique.getValue() : "");
        demarche.setDateDepot(dateDepot.getValue() != null ? dateDepot.getValue().format(dateFormatter) : null);
        validateTimeFormat(heureDepot.getText(), "Heure de dépôt");
        demarche.setHeureDepot(heureDepot.getText());
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
    }

    private double calculateMontant(String objetVisite) {
        String selectedStatus = status.getValue();
        if (selectedStatus != null) {
            // Normaliser le statut pour correspondre aux clés du Map
            String statusKey = selectedStatus.toLowerCase().contains("physique") ? "personne physique" : "personne morale";
            Map<String, Integer> amounts = objectAmounts.get(statusKey);
            if (amounts != null && amounts.containsKey(objetVisite)) {
                return amounts.get(objetVisite).doubleValue();
            }
        }
        return 0.0;
    }

    @FXML
    private void clearForm() {
        dateContact.setValue(null);
        heureContact.clear();
        typeDemande.getSelectionModel().clearSelection();
        status.getSelectionModel().clearSelection();
        objet1.setSelected(false);
        objet2.setSelected(false);
        objet3.setSelected(false);
        objet4.setSelected(false);
        objet5.setSelected(false);
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

    private void showError(Control control, String message) {
        control.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        if (control instanceof TextField) {
            TextField tf = (TextField) control;
            try { tf.promptTextProperty().unbind(); } catch (Exception ignored) {}
            tf.setPromptText(message);
        } else if (control instanceof ComboBox) {
            ComboBox<?> cb = (ComboBox<?>) control;
            try { cb.promptTextProperty().unbind(); } catch (Exception ignored) {}
            cb.setPromptText(message);
        }
        control.setTooltip(new Tooltip(message));
    }

    private void clearError(Control control) {
        control.setStyle("");
        if (control instanceof TextField) {
            TextField tf = (TextField) control;
            try { tf.promptTextProperty().unbind(); } catch (Exception ignored) {}
            tf.setPromptText("");
        } else if (control instanceof ComboBox) {
            ComboBox<?> cb = (ComboBox<?>) control;
            try { cb.promptTextProperty().unbind(); } catch (Exception ignored) {}
            cb.setPromptText("");
        }
        control.setTooltip(null);
    }

    private void clearAllErrors() {
        clearError(dateContact);
        clearError(heureContact);
        clearError(typeDemande);
        clearError(status);
        clearError(nomPrenom);
        clearError(telephoneGSM);
        clearError(email);
        clearError(siteweb);
        clearError(adresse);
        clearError(ville);
        clearError(denomination);
        clearError(nomRepresentantLegal);
        clearError(formeJuridique);
        clearError(dateDepot);
        clearError(heureDepot);
        clearError(secteurActivite);
        clearError(activite);
    }

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
    public void handlePrint() {
        try {
            File outputDocx = new File("generated_document_demarche.docx");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le PDF généré");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            fileChooser.setInitialDirectory(new File("C:/fichiers application ccis/demarche administratif"));
            fileChooser.setInitialFileName("Fiche Demarche Administratif.pdf");
            File outputPdf = fileChooser.showSaveDialog(null);
            if (outputPdf == null) {
                return;
            }

            String templatePath = "/templates/template_word_demarche.docx";
            InputStream templateStream = getClass().getResourceAsStream(templatePath);
            
            if (templateStream == null) {
                showErrorAlert("Erreur", "Le fichier modèle Word est introuvable : " + templatePath);
                return;
            }
            
            XWPFDocument doc = new XWPFDocument(templateStream);
            Map<String, String> replacements = createReplacementMap();
            replaceText(doc, replacements);
            
            try (FileOutputStream out = new FileOutputStream(outputDocx)) {
                doc.write(out);
            }
            outputDocx.deleteOnExit();
            
            convertDocxToPdf(outputDocx, outputPdf);
            
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(outputPdf);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Le document PDF a été généré avec succès : " + outputPdf.getAbsolutePath());
                alert.showAndWait(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Erreur", "Une erreur est survenue lors de la génération du document : " + e.getMessage());
        }
    }

    private void convertDocxToPdf(File wordFile, File pdfFile) {
        try {
            ProcessBuilder checkPython = new ProcessBuilder("python", "--version");
            Process checkProcess = checkPython.start();
            int checkExit = checkProcess.waitFor();
            if (checkExit != 0) {
                showErrorAlert("Erreur", "Python n'est pas installé sur cet ordinateur. Veuillez installer Python pour activer la génération de PDF.");
                return;
            }

            InputStream scriptStream = getClass().getResourceAsStream("/scripts/convert.py");
            if (scriptStream == null) {
                throw new IOException("Cannot find Python script in resources");
            }

            File tempScript = File.createTempFile("convert", ".py");
            tempScript.deleteOnExit();

            try (FileOutputStream fos = new FileOutputStream(tempScript)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = scriptStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            
            ProcessBuilder pb = new ProcessBuilder(
                "python",
                tempScript.getAbsolutePath(),
                wordFile.getAbsolutePath(),
                pdfFile.getAbsolutePath()
            );
            
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                StringBuilder output = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                
                int exitCode = process.waitFor();
                
                if (exitCode != 0 || !pdfFile.exists()) {
                    throw new IOException("PDF conversion failed: " + output.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Erreur", "La conversion en PDF a échoué : " + e.getMessage());
        }
    }

    private Map<String, String> createReplacementMap() {
        Map<String, String> replacements = new HashMap<>();
        
        replacements.put("{date_contact}", dateContact.getValue() != null ? dateContact.getValue().format(dateFormatter) : "");
        replacements.put("{heure_contact}", heureContact.getText() != null ? heureContact.getText() : "");
        replacements.put("{date_depot}", dateDepot.getValue() != null ? dateDepot.getValue().format(dateFormatter) : "");
        replacements.put("{heure_depot}", heureDepot.getText() != null ? heureDepot.getText() : "");
        replacements.put("{date_delivrance}", dateDelivrance.getValue() != null ? dateDelivrance.getValue().format(dateFormatter) : "");
        replacements.put("{heure_delivrance}", heureDelivrance.getText() != null ? heureDelivrance.getText() : "");
        
        replacements.put("{doc1}", "Demande d'information /renseignement à propos d'un document administratif".equals(typeDemande.getValue()) ? "☑" : "☐");
        replacements.put("{doc2}", "Demande de document administratif".equals(typeDemande.getValue()) ? "☑" : "☐");
        
        replacements.put("{objet1}", objet2.isSelected() ? "☑" : "☐"); // Carte professionnelle
        replacements.put("{objet2}", objet1.isSelected() ? "☑" : "☐"); // Attestation professionnelle
        replacements.put("{objet3}", objet4.isSelected() ? "☑" : "☐"); // Visa des factures
        replacements.put("{objet5}", objet3.isSelected() ? "☑" : "☐"); // Visa des documents commerciaux
        replacements.put("{objet6}", objet5.isSelected() ? "☑" : "☐"); // Certificat d'origine

        replacements.put("{nom_prenom}", nomPrenom.getText() != null ? nomPrenom.getText() : "");
        replacements.put("{tel_fixe}", telephoneFix.getText() != null ? telephoneFix.getText() : "");
        replacements.put("{tel_gsm}", telephoneGSM.getText() != null ? telephoneGSM.getText() : "");
        replacements.put("{email}", email.getText() != null ? email.getText() : "");
        replacements.put("{adresse}", adresse.getText() != null ? adresse.getText() : "");
        replacements.put("{ville}", ville.getText() != null ? ville.getText() : "");
        replacements.put("{accepte_envois}", accepteEnvoiCCIS.isSelected() ? "☑" : "☐");
        
        replacements.put("{denomination}", denomination.getText() != null ? denomination.getText() : "");
        replacements.put("{nom_representant_legal}", nomRepresentantLegal.getText() != null ? nomRepresentantLegal.getText() : "");
        
        String selectedFormeJuridique = formeJuridique.getValue();
        replacements.put("{forme1}", "PP (Personne physique)".equals(selectedFormeJuridique) ? "☑" : "☐");
        replacements.put("{forme2}", "Auto-entrepreneur".equals(selectedFormeJuridique) ? "☑" : "☐");
        replacements.put("{forme3}", "SARL".equals(selectedFormeJuridique) ? "☑" : "☐");
        replacements.put("{forme4}", "SA".equals(selectedFormeJuridique) ? "☑" : "☐");
        replacements.put("{autre_forme_juridique}", selectedFormeJuridique != null && 
                                                !selectedFormeJuridique.equals("SARL") && 
                                                !selectedFormeJuridique.equals("SA") && 
                                                !selectedFormeJuridique.equals("Auto-entrepreneur") && 
                                                !selectedFormeJuridique.equals("PP (Personne physique)") ? 
                                                selectedFormeJuridique : "");
        
        String selectedSector = secteurActivite.getValue();
        replacements.put("{secteur1}", "Industrie".equals(selectedSector) ? "☑" : "☐");
        replacements.put("{secteur2}", "Commerce".equals(selectedSector) ? "☑" : "☐");
        replacements.put("{secteur3}", "Services".equals(selectedSector) ? "☑" : "☐");
        replacements.put("{activite1}", "Industrie".equals(selectedSector) ? activite.getText() : "");
        replacements.put("{activite2}", "Commerce".equals(selectedSector) ? activite.getText() : "");
        replacements.put("{activite3}", "Services".equals(selectedSector) ? activite.getText() : "");

    replacements.put("{etat1}", "Complet et conforme".equals(etatDossier.getValue()) ? "☑" : "☐");
    replacements.put("{etat2}", "Incomplet".equals(etatDossier.getValue()) ? "☑" : "☐");
    replacements.put("{etat3}", "Non conforme".equals(etatDossier.getValue()) ? "☑" : "☐");

    replacements.put("{suite1}", "Acceptée".equals(suiteDemande.getValue()) ? "☑" : "☐");
    replacements.put("{suite2}", "Rejetée".equals(suiteDemande.getValue()) ? "☑" : "☐");
    
    replacements.put("{observation}", observation.getText() != null ? observation.getText() : "");
    replacements.put("{conseilleur_ccis}", nomPrenomConseillerCCIS.getText() != null ? nomPrenomConseillerCCIS.getText() : "");
    replacements.put("{qualite2}", qualiteConseillerCCIS.getText() != null ? qualiteConseillerCCIS.getText() : "");
    
    return replacements;
}

private void replaceText(XWPFDocument doc, Map<String, String> replacements) {
    // Replace text in paragraphs
    for (XWPFParagraph paragraph : doc.getParagraphs()) {
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs != null) {
            // Combine all runs text
            StringBuilder builder = new StringBuilder();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                if (text != null) {
                    builder.append(text);
                }
            }
            
            // Get the full text
            String text = builder.toString();
            
            // Apply all replacements
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                text = text.replace(entry.getKey(), entry.getValue());
            }
            
            // Remove all runs except first
            for (int i = runs.size() - 1; i > 0; i--) {
                paragraph.removeRun(i);
            }
            
            // Set the new text in the first run
            if (runs.size() > 0) {
                XWPFRun run = runs.get(0);
                run.setText(text, 0);
            }
        }
    }
    
    // Replace text in tables
    for (XWPFTable table : doc.getTables()) {
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    List<XWPFRun> runs = paragraph.getRuns();
                    if (runs != null) {
                        // Combine all runs text
                        StringBuilder builder = new StringBuilder();
                        for (XWPFRun run : runs) {
                            String text = run.getText(0);
                            if (text != null) {
                                builder.append(text);
                            }
                        }
                        
                        // Get the full text
                        String text = builder.toString();
                        
                        // Apply all replacements
                        for (Map.Entry<String, String> entry : replacements.entrySet()) {
                            text = text.replace(entry.getKey(), entry.getValue());
                        }
                        
                        // Remove all runs except first
                        for (int i = runs.size() - 1; i > 0; i--) {
                            paragraph.removeRun(i);
                        }
                        
                        // Set the new text in the first run
                        if (runs.size() > 0) {
                            XWPFRun run = runs.get(0);
                            run.setText(text, 0);
                        }
                    }
                }
            }
        }
    }
}
private void setupTimeValidation(TextField timeField) {
    // Add listener for text changes
    timeField.textProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue == null || newValue.isEmpty()) {
            return;
        }

        // Remove any non-digit characters except ':'
        String cleaned = newValue.replaceAll("[^\\d:]", "");
        
        if (cleaned.length() > 5) {
            timeField.setText(oldValue);
            return;
        }

        // Format as HH:mm
        if (cleaned.matches("\\d{1,2}:\\d{0,2}")) {
            // Valid format, check ranges
            String[] parts = cleaned.split(":");
            try {
                int hours = Integer.parseInt(parts[0]);
                int minutes = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;

                if (hours > 23) {
                    timeField.setText(oldValue);
                    return;
                }
                if (minutes > 59) {
                    timeField.setText(oldValue);
                    return;
                }
            } catch (NumberFormatException e) {
                timeField.setText(oldValue);
                return;
            }
        } else if (cleaned.matches("\\d{1,4}")) {
            // Convert to HH:mm format
            if (cleaned.length() >= 3) {
                String hours = cleaned.substring(0, 2);
                String minutes = cleaned.substring(2);
                if (Integer.parseInt(hours) <= 23 && 
                    (minutes.isEmpty() || Integer.parseInt(minutes) <= 59)) {
                    cleaned = hours + ":" + minutes;
                } else {
                    timeField.setText(oldValue);
                    return;
                }
            }
        } else if (!cleaned.isEmpty()) {
            timeField.setText(oldValue);
            return;
        }

        timeField.setText(cleaned);
    });
}


}