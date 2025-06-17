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
import javafx.stage.FileChooser;
import ccis.models.EspaceEntreprise;
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

import ccis.dao.EspaceEntrepriseDAO;

 public class EspaceFicheController {

    @FXML private DatePicker dateContact;
    @FXML private TextField heureContact;
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
    @FXML private ComboBox<String> secteurActivite;
    @FXML private TextField activite;
    @FXML private TextField nomPrenomConseillerCCIS;
    @FXML private TextField qualiteConseillerCCIS;
    @FXML private DatePicker dateDepart;
    @FXML private TextField heureDepart;
    @FXML private ComboBox<String> statut;
    @FXML private TextField codeICE;
    @FXML private ComboBox<String> tailleEntreprise;
    @FXML private CheckBox objet1;
    @FXML private CheckBox objet2;
    @FXML private CheckBox objet3;
    @FXML private CheckBox objet4;
    @FXML private TextField recommandation;


 private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // Create an instance of dao
   EspaceEntrepriseDAO dao = new EspaceEntrepriseDAO();

    @FXML
    public void initialize() {
    setupTimeValidation(heureContact);
    setupTimeValidation(heureDepart);
    nomPrenom.textProperty().addListener((observable, oldValue, newValue) -> {
        denomination.setText(newValue); // Update denomination
        nomRepresentantLegal.setText(newValue); // Update nomRepresentantLegal
    });   
    nomPrenomConseillerCCIS.setText("Rachid BNINHA");
    qualiteConseillerCCIS.setText("Chef de département services aux ressortissants");
    ville.setText("Essaouira");


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

                // Statut ComboBox
                ObservableList<String> statutOptions = FXCollections.observableArrayList(
                    "Entrepreneur", 
                    "Porteur de projet"
                );
                statut.setItems(statutOptions);
                statut.setEditable(true);

        // Secteur Activité ComboBox
        ObservableList<String> secteurActiviteOptions = FXCollections.observableArrayList(
            "Industrie", 
            "Commerce", 
            "Services"
        );
        secteurActivite.setItems(secteurActiviteOptions);

        ObservableList<String> objetVisiteList = FXCollections.observableArrayList(
            "Programmes d'appui / aide aux entreprises",
            "Demarches administratives",
            "Annuaire des entreprises",
            "Repertoire de contact des administrations"
        );
        accepteEnvoiCCIS.setSelected(true);
        dateContact.setValue(java.time.LocalDate.now());
        dateDepart.setValue(java.time.LocalDate.now());
tailleEntreprise.setItems(FXCollections.observableArrayList(
            "Micro",
            "Petite",
            "Moyenne",
            "Grande"
        ));
        dateContact.valueProperty().addListener((observable, oldValue, newValue) -> {
            dateDepart.setValue(newValue); 
        }); 
    }

    @FXML
    public void handleSubmit(ActionEvent event) {
        boolean isValid = true;
        clearAllErrors();
        
        // Add validation logic for all required fields here
        // Example:
        if (dateContact.getValue() == null) {
            showError(dateContact, "La date de contact est requise");
            isValid = false;
        }
        
        if (heureContact.getText().isEmpty()) {
            showError(heureContact, "L'heure de contact est requise");
            isValid = false;
        }
        if (!objet1.isSelected() && !objet2.isSelected() && !objet3.isSelected() && !objet4.isSelected()) {
            showError(objet1, "L'objet de la visite est requis");
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
        if (secteurActivite.getValue() == null) {
            showError(secteurActivite, "Le secteur d'activité est requis");
            isValid = false;
        }
        if (activite.getText().isEmpty()) {
            showError(activite, "L'activité est requise");
            isValid = false;
        }
        if(statut.getValue() == null) {
            showError(statut, "Le statut est requis");
            isValid = false;
        }
        if (tailleEntreprise.getValue() == null || tailleEntreprise.getValue().isEmpty()) {
            showError(tailleEntreprise, "La taille de l'entreprise est requise");
            isValid = false;
        }
        if (dateDepart.getValue() == null) {
            showError(dateDepart, "La date de départ est requise");
            isValid = false;
        }
    // Récupérer les objets de visite cochés
    List<String> objetsVisite = new ArrayList<>();
    if (objet1.isSelected()) objetsVisite.add(objet1.getText());
    if (objet2.isSelected()) objetsVisite.add(objet2.getText());
    if (objet3.isSelected()) objetsVisite.add(objet3.getText());
    if (objet4.isSelected()) objetsVisite.add(objet4.getText());

       
if (isValid) {
    try {

        EspaceEntreprise espace = new EspaceEntreprise();
      for (String objet : objetsVisite) {  
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
        espace.setObjetVisite(objet);
        // Set other fields with null checks
        espace.setFixe(telephoneFix.getText() != null ? telephoneFix.getText() : "");
        espace.setGsm(telephoneGSM.getText() != null ? telephoneGSM.getText() : "");
        espace.setSiteWeb(siteweb.getText() != null ? siteweb.getText() : "");
        espace.setAdresse(adresse.getText() != null ? adresse.getText() : "");
        espace.setVille(ville.getText() != null ? ville.getText() : "");
        espace.setDenomination(denomination.getText() != null ? denomination.getText() : "");
        espace.setNomRepLegal(nomRepresentantLegal.getText() != null ? nomRepresentantLegal.getText() : "");
        espace.setFormeJuridique(formeJuridique.getValue() != null ? formeJuridique.getValue() : "");
        
        // Other fields
        espace.setSecteurActivite(secteurActivite.getValue() != null ? secteurActivite.getValue() : "");
        espace.setActivite(activite.getText() != null ? activite.getText() : "");
        espace.setNomPrenomCCIS(nomPrenomConseillerCCIS.getText() != null ? nomPrenomConseillerCCIS.getText() : "");
        espace.setQualiteCCIS(qualiteConseillerCCIS.getText() != null ? qualiteConseillerCCIS.getText() : "");
        espace.setDateDepart(dateDepart.getValue() != null ? dateDepart.getValue().format(dateFormatter) : null);
        validateTimeFormat(heureDepart.getText(), "Heure de départ");
        espace.setHeureDepart(heureDepart.getText());
        
        espace.setAccepteEnvoi(accepteEnvoiCCIS.isSelected() ? "Oui" : "Non");
        espace.setStatut(null != statut.getValue() ? statut.getValue() : "");
        espace.setCodeICE(null != codeICE.getText() ? codeICE.getText() : "");
        espace.setTailleEntreprise(null != tailleEntreprise.getValue() ? tailleEntreprise.getValue() : "");
espace.setRecommandation(recommandation.getText() != null ? recommandation.getText() : "");
       dao.create(espace);
    }
          // Show success message
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Succès");
          alert.setHeaderText(null);
          alert.setContentText("Les données ont été enregistrées avec succès!");
          alert.showAndWait();

    } catch (IllegalArgumentException e) {
        showErrorAlert("Erreur de validation", e.getMessage());
    } catch (Exception e) {
        showErrorAlert("Erreur", "Une erreur est survenue lors de l'enregistrement: " + e.getMessage());
        e.printStackTrace();
    }
}
else {
    // Show error message
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Erreur de validation");
    alert.setHeaderText(null);
    alert.setContentText("Veuillez remplir tous les champs obligatoires.");
    alert.showAndWait();
}
        }
    
@FXML
        private void clearForm() {
            dateContact.setValue(null);
            heureContact.clear();
            objet1.setSelected(false);
            objet2.setSelected(false);
            objet3.setSelected(false);
            objet4.setSelected(false);
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
            secteurActivite.getSelectionModel().clearSelection();
            activite.clear();
            dateDepart.setValue(null);
            heureDepart.clear();
            statut.getSelectionModel().clearSelection();

        }
    // Method to show errors for any control
private void showError(Control control, String message) {
    control.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
    if (control instanceof TextField) {
        ((TextField) control).setPromptText(message);
    } else if (control instanceof ComboBox) {
        ((ComboBox<?>) control).setPromptText(message);
    }
    // Optionally: errorLabel.setVisible(false);
    control.setTooltip(new Tooltip(message));
}

private void clearError(Control control) {
    control.setStyle("");
    if (control instanceof TextField) {
        ((TextField) control).setPromptText("");
    } else if (control instanceof ComboBox) {
        ((ComboBox<?>) control).setPromptText("");
    }
    // Optionally: errorLabel.setVisible(false);
    control.setTooltip(null);
}

    // Method to clear all errors
    private void clearAllErrors() {
        clearError(dateContact);
        clearError(heureContact);
        clearError(nomPrenom);
        clearError(telephoneGSM);
        clearError(email);
        clearError(adresse);
        clearError(ville);
        clearError(denomination);
        clearError(nomRepresentantLegal);
        clearError(formeJuridique);
        clearError(secteurActivite);
        clearError(activite);
        clearError(statut);
        clearError(tailleEntreprise);
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
public void handlePrint() {
   try {
        // Create a temporary file for the processed Word document
        File outputDocx = new File("generated_document_espace.docx");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF généré");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialDirectory(new File("C:/fichiers application ccis/espace entreprise"));
        
        fileChooser.setInitialFileName("Fiche Espace Entreprise.pdf");
        File outputPdf = fileChooser.showSaveDialog(null);
        if (outputPdf == null) {
            // User cancelled the save dialog
            return;
        }

        // Load the template Word document
        String templatePath = "/templates/template_word_espace_entreprise.docx";
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
        outputDocx.delete(); // Delete the Word document after conversion
       
        
        // Open the generated PDF file
         if (Desktop.isDesktopSupported()) {
             Desktop.getDesktop().open(outputPdf);
         }
         else {
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
 // Fonction d'aide pour les cases à cocher
        String caseACocher(boolean value) {
            return value ? "✓" : "☐";
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
     // Fill the Word template with data
        replacements.put("{heure_contact}", heureContact.getText() != null ? heureContact.getText() : "");
        replacements.put("{date_contact}", dateContact.getValue() != null ? dateContact.getValue().toString() : "");
replacements.put("{objet1}", caseACocher(objet1.isSelected()));
replacements.put("{objet2}", caseACocher(objet2.isSelected()));
replacements.put("{objet3}", caseACocher(objet3.isSelected()));
replacements.put("{objet4}", caseACocher(objet4.isSelected()));
        replacements.put("{nom_prenom}", nomPrenom.getText() != null ? nomPrenom.getText() : "");
        replacements.put("{status1}", caseACocher("Entrepreneur".equals(statut.getValue())));
        replacements.put("{status2}", caseACocher("Porteur de projet".equals(statut.getValue())));
        replacements.put("{autre_status}", statut.getValue() != null && statut.getValue().equals("Autre") ? "Autre" : "");
        replacements.put("{tel_fixe}", telephoneFix.getText() != null ? telephoneFix.getText() : "");
        replacements.put("{tel_gsm}", telephoneGSM.getText() != null ? telephoneGSM.getText() : "");
        replacements.put("{email}", email.getText() != null ? email.getText() : "");
        replacements.put("{accepte_envois}", caseACocher(accepteEnvoiCCIS.isSelected()));
        replacements.put("{adresse}", adresse.getText() != null ? adresse.getText() : "");
        replacements.put("{ville}", ville.getText() != null ? ville.getText() : "");
        replacements.put("{denomination}", denomination.getText() != null ? denomination.getText() : "");
        replacements.put("{ice}", codeICE.getText() != null ? codeICE.getText() : "");
        replacements.put("{nom_representant_legal}", nomRepresentantLegal.getText() != null ? nomRepresentantLegal.getText() : "");
        replacements.put("{site_web}", siteweb.getText() != null ? siteweb.getText() : "");
        replacements.put("{forme1}", caseACocher("PP(Personne physique)".equals(formeJuridique.getValue())));
        replacements.put("{forme2}", caseACocher("Auto-entrepreneur".equals(formeJuridique.getValue())));
        replacements.put("{forme3}", caseACocher("SARL".equals(formeJuridique.getValue())));
        replacements.put("{forme4}", caseACocher("SA".equals(formeJuridique.getValue())));
        replacements.put("{autre_forme_juridique}", "Autre".equals(formeJuridique.getValue()) ? formeJuridique.getEditor().getText() : "");
        replacements.put("{taille1}", caseACocher("Petite".equals(tailleEntreprise.getValue()) || "Micro".equals(tailleEntreprise.getValue())));
        replacements.put("{taille2}", caseACocher("Moyenne".equals(tailleEntreprise.getValue())));
        replacements.put("{taille3}", caseACocher("Grande".equals(tailleEntreprise.getValue())));
        replacements.put("{secteur1}", caseACocher("Industrie".equals(secteurActivite.getValue())));
        replacements.put("{secteur2}", caseACocher("Commerce".equals(secteurActivite.getValue())));
        replacements.put("{secteur3}", caseACocher("Services".equals(secteurActivite.getValue())));
        replacements.put("{activite}", activite.getText() != null ? activite.getText() : "");
        replacements.put("{conseilleur_ccis}", nomPrenomConseillerCCIS.getText() != null ? nomPrenomConseillerCCIS.getText() : "");
        replacements.put("{qualite}", qualiteConseillerCCIS.getText() != null ? qualiteConseillerCCIS.getText() : "");
        replacements.put("{heure_depart}", heureDepart.getText() != null ? heureDepart.getText() : "");

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