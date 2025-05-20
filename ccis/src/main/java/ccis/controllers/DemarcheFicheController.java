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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import ccis.models.DemarcheAdministratif;
import org.apache.poi.ss.usermodel.Sheet;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
 //   @FXML private ScrollPane scrollPane;
    @FXML private VBox scrollContent;
    @FXML private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // Create an instance of DemarcheAdministratifDAO
    private DemarcheAdministratifDao demarcheDAO = new DemarcheAdministratifDao();
    @FXML
    private VBox objetVisiteContainer;
    
    private final String[] options = {
        "Attestation professionnelle",
        "Carte professionnelle",
        "Visa des documents commerciaux",
        "Visa des factures",
        "Certificat d’origine"
    };
    
    private final Map<String, Map<String, Integer>> objectAmounts = Map.of(
        "personne physique", Map.of(
            "Attestation professionnelle", 50,
            "Carte professionnelle", 50,
            "Visa des documents commerciaux", 200,
            "Visa des factures", 150,
            "Certificat d’origine", 300
        ),
        "personne morale", Map.of(
            "Attestation professionnelle", 100,
            "Carte professionnelle", 150,
            "Visa des documents commerciaux", 200,
            "Visa des factures", 150,
            "Certificat d’origine", 300
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
    qualiteConseillerCCIS.setText("Chef de département services aux ressortissants");
    nomPrenom.textProperty().addListener((observable, oldValue, newValue) -> {
        denomination.setText(newValue); // Update denomination
        nomRepresentantLegal.setText(newValue); // Update nomRepresentantLegal
    });   
    
    dateContact.valueProperty().addListener((observable, oldValue, newValue) -> {
        dateDepot.setValue(newValue); // Update dateDepot
        dateDelivrance.setValue(newValue); // Update dateDelivrance
    });   
    
    // Type Demande ComboBox
        ObservableList<String> typeDemandeOptions = FXCollections.observableArrayList(
            "Demande d’information /renseignement à propos d’un document administratif", 
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
        
       // scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        for (String option : options) {
            HBox row = new HBox(10); // Horizontal layout for each row
            CheckBox checkBox = new CheckBox(option);
            TextField amountField = new TextField();
            amountField.setPromptText("Montant");
            amountField.setDisable(true); // Initially disabled
    
            // Enable/disable the amount field based on checkbox selection
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                amountField.setDisable(!newValue);
                if (newValue) {
                    // Update the amountField based on the selected status
                    String selectedStatus = status.getValue();
                    if (selectedStatus != null) {
                        Map<String, Integer> amounts = objectAmounts.get(selectedStatus.toLowerCase());
                        if (amounts != null && amounts.containsKey(option)) {
                            amountField.setText(String.valueOf(amounts.get(option)));
                        }
                    }
                } else {
                    amountField.clear(); // Clear the field when disabled
                }
            });
    
            row.getChildren().addAll(checkBox, amountField);
            objetVisiteContainer.getChildren().add(row);
        }
    
        // Add a listener to the status ComboBox to update the amounts dynamically
        status.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                for (int i = 0; i < objetVisiteContainer.getChildren().size(); i++) {
                    HBox row = (HBox) objetVisiteContainer.getChildren().get(i);
                    CheckBox checkBox = (CheckBox) row.getChildren().get(0);
                    TextField amountField = (TextField) row.getChildren().get(1);

                    if (checkBox.isSelected()) {
                        Map<String, Integer> amounts = objectAmounts.get(newValue.toLowerCase());
                        if (amounts != null && amounts.containsKey(checkBox.getText())) {
                            amountField.setText(String.valueOf(amounts.get(checkBox.getText())));
                        } else {
                            amountField.clear();
                        }
                    }
                }
            }
        });
        accepteEnvoiCCIS.setSelected(true); // Set default value to true
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

// Collect selected "Objet de Visite" and their "Montant"
Map<String, Double> selectedObjetVisite = new HashMap<>();
StringBuilder selectedObjetVisite2 = new StringBuilder();

for (int i = 0; i < objetVisiteContainer.getChildren().size(); i++) {
    HBox row = (HBox) objetVisiteContainer.getChildren().get(i);
    CheckBox checkBox = (CheckBox) row.getChildren().get(0);
    TextField amountField = (TextField) row.getChildren().get(1);

    if (checkBox.isSelected()) {
        // Add the checkbox text to the concatenated string
        if (selectedObjetVisite2.length() > 0) {
            selectedObjetVisite2.append(", "); // Add a comma separator
        }
        selectedObjetVisite2.append(checkBox.getText());

        // Add the amount to the map
        try {
            Double amount = Double.parseDouble(amountField.getText());
            selectedObjetVisite.put(checkBox.getText(), amount);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount for " + checkBox.getText() + ": " + amountField.getText());
        }
    }
}

// Set the concatenated "Objet de Visite" string in the DemarcheAdministratif object
demarche.setObjetVisite(selectedObjetVisite2.toString());

// Calculate and set the total "Montant"
Double totalMontant = selectedObjetVisite.values()
    .stream()
    .map(Double::doubleValue)  // Convert Double to Double
    .reduce(0.0, Double::sum);

// Set the concatenated "Objet de Visite" string in the DemarcheAdministratif object
demarche.setObjetVisite(selectedObjetVisite2.toString());
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
            objetVisiteContainer.getChildren().clear();
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
        clearError(nomPrenom, nomPrenomError);
        clearError(telephoneGSM, telephoneGSMError);
        clearError(email, emailError);
        clearError(siteweb, sitewebError);
        clearError(adresse, adresseError);
        clearError(ville, villeError);
        clearError(denomination, denominationError);
        clearError(nomRepresentantLegal, nomRepresentantLegalError);
        clearError(formeJuridique, formeJuridiqueError);
        clearError(typeDemande, typeDemandeError);
        clearError(status, statusError);
    }

    // public void scrollToBottom() {
    //     Platform.runLater(() -> scrollPane.setVvalue(1.0));
    // }
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
        fieldMapping.put("Type de demmande", (d, v) -> d.setTypeDemande(v));
        fieldMapping.put("Forme juridique", (d, v) -> d.setFormeJuridique(v));
        fieldMapping.put("Secteur Activité", (d, v) -> d.setSecteurActivite(v));
        fieldMapping.put("Activité", (d, v) -> d.setActivite(v));
        fieldMapping.put("Type", (d, v) -> d.setStatut(v));
        fieldMapping.put("FIXE", (d, v) -> d.setFixe(v));
        fieldMapping.put("GSM 1", (d, v) -> d.setGsm(v));
        fieldMapping.put("Siège Sociale / Adresses", (d, v) -> d.setAdresse(v));
        fieldMapping.put("Ville / Communité", (d, v) -> d.setVille(v));
        fieldMapping.put("Interloculeur", (d, v) -> d.setInterlocuteur(v));
        fieldMapping.put("Email 1", (d, v) -> d.setEmail(v));
        fieldMapping.put("Date de contact", (d, v) -> d.setDateContact(v));
        fieldMapping.put("Heure de contact", (d, v) -> d.setHeureContact(v));
        fieldMapping.put("Objet de la visite", (d, v) -> d.setObjetVisite(v));
        fieldMapping.put("Montant", (d, v) -> d.setMontant(Double.parseDouble(v)));
        fieldMapping.put("Nom et Prénom", (d, v) -> d.setNomPrenom(v));
        fieldMapping.put("Accepte Envoi CCIS", (d, v) -> d.setAccepteEnvoi(v));
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

    } catch (Exception e) {
        e.printStackTrace();
    }
}
private String getCellAsString(Cell cell) {
    if (cell == null) return "";
    switch (cell.getCellType()) {
        case STRING: 
            return cell.getStringCellValue();
        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                // Format dates properly
                java.util.Date date = cell.getDateCellValue();
                return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
            } else {
                // Check if this is the Montant column
                int columnIndex = cell.getColumnIndex();
                String columnHeader = cell.getSheet().getRow(0).getCell(columnIndex).getStringCellValue();
                if ("Montant".equals(columnHeader)) {
                    // Use DecimalFormat for consistent 2 decimal places
                    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                    return df.format(cell.getNumericCellValue());
                }
                // For other numeric values, return as is
                return String.valueOf((int) cell.getNumericCellValue());
            }
        case BOOLEAN: 
            return String.valueOf(cell.getBooleanCellValue());
        case FORMULA:
            try {
                if (DateUtil.isCellDateFormatted(cell)) {
                    java.util.Date date = cell.getDateCellValue();
                    return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
                }
                // Check if formula result is in Montant column
                int columnIndex = cell.getColumnIndex();
                String columnHeader = cell.getSheet().getRow(0).getCell(columnIndex).getStringCellValue();
                if ("Montant".equals(columnHeader)) {
                    java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                    return df.format(cell.getNumericCellValue());
                }
                return String.valueOf((int) cell.getNumericCellValue());
            } catch (IllegalStateException e) {
                return cell.getStringCellValue();
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
public void handlePrint() {
    try {
        // Create a temporary file for the processed Word document
        File outputDocx = new File("generated_document_demarche.docx");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF généré");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Fiche Demarche Administratif.pdf");
        File outputPdf = fileChooser.showSaveDialog(null);
        if (outputPdf == null) {
            // User cancelled the save dialog
            return;
        }

        // Load the template Word document
        String templatePath = "/templates/template_word_demarche.docx";
        InputStream templateStream = getClass().getResourceAsStream(templatePath);
        
        if (templateStream == null) {
            showErrorAlert("Erreur", "Le fichier modèle Word est introuvable : " + templatePath);
            return;
        }
        
        // Create a new Word document from the template
        XWPFDocument doc = new XWPFDocument(templateStream);

        // Replace all placeholders in the document
        Map<String, String> replacements = createReplacementMap();
        replaceText(doc, replacements);
        
        // Save the modified document
        try (FileOutputStream out = new FileOutputStream(outputDocx)) {
            doc.write(out);
        }
       outputDocx.deleteOnExit(); // Delete the file on exit
        
        // Convert the Word document to PDF using a better library
        convertDocxToPdf(outputDocx, outputPdf);
        
      
        
        // Open the generated PDF file
         if (Desktop.isDesktopSupported()) {
             Desktop.getDesktop().open(outputPdf);
         }else {
             // Show success message
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
    // Check if Python is installed
    ProcessBuilder checkPython = new ProcessBuilder("python", "--version");
    Process checkProcess = checkPython.start();
    int checkExit = checkProcess.waitFor();
    if (checkExit != 0) {
        showErrorAlert("Erreur", "Python n'est pas installé sur cet ordinateur. Veuillez installer Python pour activer la génération de PDF.");
        return;
    }
        // Get the path to the Python script
        InputStream scriptStream = getClass().getResourceAsStream("/scripts/convert.py");
        if (scriptStream == null) {
            throw new IOException("Cannot find Python script in resources");
        }

        // Create a temporary file for the Python script
        File tempScript = File.createTempFile("convert", ".py");
        tempScript.deleteOnExit();

        // Copy the script content to the temporary file
        try (FileOutputStream fos = new FileOutputStream(tempScript)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = scriptStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        
        // Build the command
        ProcessBuilder pb = new ProcessBuilder(
            "python",
            tempScript.getAbsolutePath(),
            wordFile.getAbsolutePath(),
            pdfFile.getAbsolutePath()
        );
        
        // Redirect error stream to output stream
        pb.redirectErrorStream(true);
        
        // Start the process
        Process process = pb.start();
        
        // Read the output
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            // Wait for the process to complete
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
    
    // Date and time fields
    replacements.put("{date_contact}", dateContact.getValue() != null ? dateContact.getValue().format(dateFormatter) : "");
    replacements.put("{heure_contact}", heureContact.getText() != null ? heureContact.getText() : "");
    replacements.put("{date_depot}", dateDepot.getValue() != null ? dateDepot.getValue().format(dateFormatter) : "");
    replacements.put("{heure_depot}", heureDepot.getText() != null ? heureDepot.getText() : "");
    replacements.put("{date_delivrance}", dateDelivrance.getValue() != null ? dateDelivrance.getValue().format(dateFormatter) : "");
    replacements.put("{heure_delivrance}", heureDelivrance.getText() != null ? heureDelivrance.getText() : "");
    
    // Document type checkboxes     
    replacements.put("{doc1}", "Demande d’information /renseignement à propos d’un document administratif".equals(typeDemande.getValue()) ? "☑" : "☐");
    replacements.put("{doc2}", "Demande de document administratif".equals(typeDemande.getValue()) ? "☑" : "☐");
    
    // Requested document type checkboxes
    boolean attestationSelected = false;
    boolean carteSelected = false;
    boolean visaFacturesSelected = false;
    boolean visaCertificatsSelected = false;
    boolean visaDocumentsSelected = false;
    boolean certificatOrigineSelected = false;
    boolean recommandationVisaSelected = false;
    
    // Check the objetVisiteContainer for selected checkboxes
    for (int i = 0; i < objetVisiteContainer.getChildren().size(); i++) {
        HBox row = (HBox) objetVisiteContainer.getChildren().get(i);
        CheckBox checkBox = (CheckBox) row.getChildren().get(0);
        
        if (checkBox.isSelected()) {
            switch (checkBox.getText()) {
                case "Attestation professionnelle":
                    attestationSelected = true;
                    break;
                case "Carte professionnelle":
                    carteSelected = true;
                    break;
                case "Visa des factures":
                    visaFacturesSelected = true;
                    break;
                case "Visa des documents commerciaux":
                    visaDocumentsSelected = true;
                    break;
                case "Certificat d'origine":
                    certificatOrigineSelected = true;
                    break;
                // Add other cases as needed
            }
        }
    }
    
    replacements.put("{objet1}", carteSelected ? "☑" : "☐");
    replacements.put("{objet2}", attestationSelected ? "☑" : "☐");
    replacements.put("{objet3}", visaFacturesSelected ? "☑" : "☐");
    replacements.put("{objet4}", visaCertificatsSelected ? "☑" : "☐");
    replacements.put("{objet5}", visaDocumentsSelected ? "☑" : "☐");
    replacements.put("{objet6}", certificatOrigineSelected ? "☑" : "☐");
    replacements.put("{objet7}", recommandationVisaSelected ? "☑" : "☐");

    // Person details
    replacements.put("{nom_prenom}", nomPrenom.getText() != null ? nomPrenom.getText() : "");
    replacements.put("{tel_fixe}", telephoneFix.getText() != null ? telephoneFix.getText() : "");
    replacements.put("{tel_gsm}", telephoneGSM.getText() != null ? telephoneGSM.getText() : "");
    replacements.put("{email}", email.getText() != null ? email.getText() : "");
    replacements.put("{adresse}", adresse.getText() != null ? adresse.getText() : "");
    replacements.put("{ville}", ville.getText() != null ? ville.getText() : "");
    replacements.put("{accepte_envois}", accepteEnvoiCCIS.isSelected() ? "☑" : "☐");
    
    // Company details
    replacements.put("{denomination}", denomination.getText() != null ? denomination.getText() : "");
    replacements.put("{nom_representant_legal}", nomRepresentantLegal.getText() != null ? nomRepresentantLegal.getText() : "");
    
    // Form juridique checkboxes
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
    
    // Sector checkboxes and activity
    String selectedSector = secteurActivite.getValue();
    replacements.put("{secteur1}", "Industrie".equals(selectedSector) ? "☑" : "☐");
    replacements.put("{secteur2}", "Commerce".equals(selectedSector) ? "☑" : "☐");
    replacements.put("{secteur3}", "Services".equals(selectedSector) ? "☑" : "☐");
    
    replacements.put("{activite1}", "Industrie".equals(selectedSector) ? activite.getText() : "");
    replacements.put("{activite2}", "Commerce".equals(selectedSector) ? activite.getText() : "");
    replacements.put("{activite3}", "Services".equals(selectedSector) ? activite.getText() : "");
    
    // Admin section
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