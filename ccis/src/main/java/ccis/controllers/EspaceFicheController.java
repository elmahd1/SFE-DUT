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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.application.Platform;
import ccis.models.EspaceEntreprise;

import org.apache.poi.ss.usermodel.Sheet;
import java.io.File;
import java.io.FileInputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ccis.dao.EspaceEntrepriseDAO;  // Import the DAO class

 public class EspaceFicheController {

    @FXML private DatePicker dateContact;
    @FXML private Label dateContactError;
    @FXML private TextField heureContact;
    @FXML private Label heureContactError;
    @FXML private ComboBox<String> objetVisite;
    @FXML private Label objetVisiteError;
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
    @FXML private DatePicker dateDepart; // Add this new field
    @FXML private Label dateDepartError; // Update this line
    @FXML private TextField heureDepart; // Add this new field
    @FXML private Label heureDepartError;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox scrollContent;
 private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // Create an instance of dao
   EspaceEntrepriseDAO dao = new EspaceEntrepriseDAO();

    @FXML
    public void initialize() {
        
    nomPrenomConseillerCCIS.setText("Rachid BNINHA");
    qualiteConseillerCCIS.setText("Chef de département services aux ressortissants");


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
        EspaceEntreprise espace = new EspaceEntreprise();
        
        // Required fields validation
        if (dateContact.getValue() == null) {
            throw new IllegalArgumentException("La date de contact est obligatoire");
        }
        espace.setDateContact(dateContact.getValue().format(dateFormatter));
        
        // Time fields with validation
        validateTimeFormat(heureContact.getText(), "Heure de contact");
        espace.setHeureContact(heureContact.getText());
        
        // Required text fields
        if (nomPrenom.getText() == null || nomPrenom.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom et prénom sont obligatoires");
        }
        espace.setNomPrenom(nomPrenom.getText().trim());
        
        // Email validation
        if (!email.getText().isEmpty() && !isValidEmail(email.getText())) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
        espace.setEmail(email.getText());
        
        // Set other fields with null checks
        espace.setObjetVisite(objetVisite.getValue() != null ? objetVisite.getValue() : "");
        espace.setFixe(telephoneFix.getText() != null ? telephoneFix.getText() : "");
        espace.setGsm(telephoneGSM.getText() != null ? telephoneGSM.getText() : "");
        espace.setSiteWeb(siteweb.getText() != null ? siteweb.getText() : "");
        espace.setAdresse(adresse.getText() != null ? adresse.getText() : "");
        espace.setVille(ville.getText() != null ? ville.getText() : "");
        espace.setDenomination(denomination.getText() != null ? denomination.getText() : "");
        espace.setNomRepLegal(nomRepresentantLegal.getText() != null ? nomRepresentantLegal.getText() : "");
        espace.setFormeJuridique(formeJuridique.getValue() != null ? formeJuridique.getValue() : "");
        
        // Date fields with null checks
        espace.setDateDepot(dateDepot.getValue() != null ? dateDepot.getValue().format(dateFormatter) : null);
        validateTimeFormat(heureDepot.getText(), "Heure de dépôt");
        espace.setHeureDepot(heureDepot.getText());
        
        // Other fields
        espace.setSecteurActivite(secteurActivite.getValue() != null ? secteurActivite.getValue() : "");
        espace.setAvtivite(activite.getText() != null ? activite.getText() : "");
        espace.setNomPrenomCCIS(nomPrenomConseillerCCIS.getText() != null ? nomPrenomConseillerCCIS.getText() : "");
        espace.setQualiteCCIS(qualiteConseillerCCIS.getText() != null ? qualiteConseillerCCIS.getText() : "");
        espace.setDateDepart(dateDepart.getValue() != null ? dateDepart.getValue().format(dateFormatter) : null);
        validateTimeFormat(heureDepart.getText(), "Heure de départ");
        espace.setHeureDepart(heureDepart.getText());
        
        espace.setAccepteEnvoi(accepteEnvoiCCIS.isSelected() ? "Oui" : "Non");

        System.out.println("Données à enregistrer: " + espace.toString());
       dao.create(espace);
        System.out.println("Enregistrement réussi");
        
          // Show success message
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Succès");
          alert.setHeaderText(null);
          alert.setContentText("La démarche a été enregistrée avec succès!");
          alert.showAndWait();
          

          clearForm();

          
    } catch (IllegalArgumentException e) {
        showErrorAlert("Erreur de validation", e.getMessage());
    } catch (Exception e) {
        showErrorAlert("Erreur", "Une erreur est survenue lors de l'enregistrement: " + e.getMessage());
        e.printStackTrace();
    }
}
        }
    

        private void clearForm() {
            dateContact.setValue(null);
            heureContact.clear();
            objetVisite.getSelectionModel().clearSelection();
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
            dateDepart.setValue(null);
            heureDepart.clear();

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
        clearError(nomPrenom, nomPrenomError);
        clearError(telephoneGSM, telephoneGSMError);
        clearError(email, emailError);
        clearError(adresse, adresseError);
        clearError(ville, villeError);
        clearError(denomination, denominationError);
        clearError(nomRepresentantLegal, nomRepresentantLegalError);
        clearError(formeJuridique, formeJuridiqueError);
        clearError(dateDepot, dateDepotError);
        clearError(heureDepot, heureDepotError);
        clearError(secteurActivite, secteurActiviteError);
        clearError(activite, activiteError);
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
        fieldMapping.put("Forme juridique", (d, v) -> d.setFormeJuridique(v));
        fieldMapping.put("Secteur Activité", (d, v) -> d.setSecteurActivite(v));
        fieldMapping.put("Activité", (d, v) -> d.setAvtivite(v));
        fieldMapping.put("FIXE", (d, v) -> d.setFixe(v));
        fieldMapping.put("GSM 1", (d, v) -> d.setGsm(v));
        fieldMapping.put("Siège Sociale / Adresses", (d, v) -> d.setAdresse(v));
        fieldMapping.put("Ville / Communité", (d, v) -> d.setVille(v));
        fieldMapping.put("Email 1", (d, v) -> d.setEmail(v));
        fieldMapping.put("Date de contact", (d, v) -> d.setDateContact(v));
        fieldMapping.put("Heure de contact", (d, v) -> d.setHeureContact(v));
        fieldMapping.put("Objet de la visite", (d, v) -> d.setObjetVisite(v));
        fieldMapping.put("Nom et Prénom", (d, v) -> d.setNomPrenom(v));
        fieldMapping.put("Accepte Envoi CCIS", (d, v) -> d.setAccepteEnvoi(v));
        fieldMapping.put("Site Web", (d, v) -> d.setSiteWeb(v));
        fieldMapping.put("Nom du représentant légal", (d, v) -> d.setNomRepLegal(v));
        fieldMapping.put("Date de dépôt", (d, v) -> d.setDateDepot(v));
        fieldMapping.put("Heure de dépôt", (d, v) -> d.setHeureDepot(v));
        fieldMapping.put("Nom et Prénom du conseiller CCIS", (d, v) -> d.setNomPrenomCCIS(v));
        fieldMapping.put("Qualité du conseiller CCIS", (d, v) -> d.setQualiteCCIS(v));
        fieldMapping.put("Date de départ", (d, v) -> d.setDateDepart(v));
        fieldMapping.put("Heure de départ", (d, v) -> d.setHeureDepart(v));

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
