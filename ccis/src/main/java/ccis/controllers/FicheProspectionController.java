package ccis.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Desktop;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import ccis.dao.ProspectionDAO;
import ccis.models.Prospection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

public class FicheProspectionController {

    @FXML private TextField nomETPField;
    @FXML private TextField adresseField;
    @FXML private TextField telephoneETPField;
    @FXML private TextField emailETPField;
    @FXML private TextField nomPrenomField;
    @FXML private TextField fonctionField;
    @FXML private TextField telephoneField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> typeProspectionField;
    @FXML private TextField secteurActiviteField;
    @FXML private TextField psaProspecterField;
    @FXML private TextField marcheCibleField;
    @FXML private TextField periodeProspectionField;
    @FXML private TextField particulariteField;
    @FXML private DatePicker dateField;
    @FXML private TextField nomETPArabicField;
@FXML private TextField adresseArabicField;
@FXML private TextField nomPrenomArabicField;
@FXML private TextField fonctionArabicField;

// Error labels
@FXML private Label nomETPError;
@FXML private Label nomETPArabicError;
@FXML private Label adresseError;
@FXML private Label adresseArabicError;
@FXML private Label telephoneETPError;
@FXML private Label emailETPError;
@FXML private Label nomPrenomError;
@FXML private Label nomPrenomArabicError;
@FXML private Label fonctionError;
@FXML private Label fonctionArabicError;
@FXML private Label telephoneError;
@FXML private Label emailError;
@FXML private Label typeProspectionError;
@FXML private Label secteurActiviteError;
@FXML private Label psaProspecterError;
@FXML private Label marcheCibleError;
@FXML private Label periodeProspectionError;
@FXML private Label particulariteError;
@FXML private Label dateError;

    private final ProspectionDAO dao = new ProspectionDAO();

@FXML
    private void initialize() {
        typeProspectionField.getItems().addAll("Prospection nationale استكشاف تجاري وطني", "Prospection internationale استكشاف تجاري دولي");
    dateField.setValue(java.time.LocalDate.now());
    
    }

    @FXML
    private void handleSave() {
      
        if ( validateFields() == false) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez corriger les erreurs dans le formulaire.");
            alert.showAndWait();
            return;
        }
        else {
        Prospection p = new Prospection();
        p.setNomETP(nomETPField.getText());
        p.setNomETPArabic(nomETPArabicField.getText());
        p.setAdresseArabic(adresseArabicField.getText());
        p.setAdresse(adresseField.getText());
        p.setTelephoneETP(telephoneETPField.getText());
        p.setEmailETP(emailETPField.getText());
        p.setNomPrenom(nomPrenomField.getText());
        p.setNomPrenomArabic(nomPrenomArabicField.getText());
        p.setFonctionArabic(fonctionArabicField.getText());
        p.setFonction(fonctionField.getText());
        p.setTelephone(telephoneField.getText());
        p.setEmail(emailField.getText());
        p.setTypeProspection(typeProspectionField.getValue());
        p.setSecteurActivite(secteurActiviteField.getText());
        p.setPSAProspecter(psaProspecterField.getText());
        p.setMarcheCible(marcheCibleField.getText());
        p.setPeriodeProspection(periodeProspectionField.getText());
        p.setParticularite(particulariteField.getText());
        p.setDate(dateField.getValue() != null ? dateField.getValue().toString() : null);

        dao.insertProspection(p);

        // Optionally, show a confirmation dialog or clear the form
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Enregistré avec succès !");
        alert.showAndWait();}
    }

    @FXML
    private void handleCancel() {
        // Clear all fields
        nomETPField.clear();
        adresseField.clear();
        telephoneETPField.clear();
        emailETPField.clear();
        nomPrenomField.clear();
        fonctionField.clear();
        telephoneField.clear();
        emailField.clear();
        typeProspectionField.setValue(null);
        secteurActiviteField.clear();
        psaProspecterField.clear();
        marcheCibleField.clear();
        periodeProspectionField.clear();
        particulariteField.clear();
        dateField.setValue(null);
    }
    @FXML
    private void handlePrint() {
 try {
        // Create a temporary file for the processed Word document
        File outputDocx = new File("Fiche Prospection.docx");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF généré");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Fiche Prospection.pdf");
        File outputPdf = fileChooser.showSaveDialog(null);
        if (outputPdf == null) {
            // User cancelled the save dialog
            return;
        }

        // Load the template Word document
        String templatePath = "/templates/template_word_prospection.docx";
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
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
private Map<String, String> createReplacementMap() {
    Map<String, String> replacements = new HashMap<>();
    
    replacements.put("{nom_entreprise}", nomETPField.getText());
    replacements.put("{adresse}", adresseField.getText());
    replacements.put("{telephone1}", telephoneETPField.getText());
    replacements.put("{email1}", emailETPField.getText());
    replacements.put("{nom_prenom}", nomPrenomField.getText());
    replacements.put("{fonction}", fonctionField.getText());
    replacements.put("{telephone2}", telephoneField.getText());
    replacements.put("{email2}", emailField.getText());
    String selectedType = typeProspectionField.getValue();
    replacements.put("{type1}", "Prospection nationale استكشاف تجاري وطني".equals(selectedType) ? "☑" : "☐");
    replacements.put("{type2}", "Prospection internationale استكشاف تجاري دولي".equals(selectedType) ? "☑" : "☐");
    replacements.put("{secteur_activite}", secteurActiviteField.getText());
    replacements.put("{psa_prospecter}", psaProspecterField.getText());
    replacements.put("{marche_cible}", marcheCibleField.getText());
    replacements.put("{periode}", periodeProspectionField.getText());
    replacements.put("{particularite}", particulariteField.getText());
    replacements.put("{date}", dateField.getValue() != null ? dateField.getValue().toString() : "");
    replacements.put("{اسمالشركة}", nomETPArabicField.getText());
    replacements.put("{العنوان}", adresseArabicField.getText());
    replacements.put("{اسمالكامل}", nomPrenomArabicField.getText());
    replacements.put("{الوظيفة}", fonctionArabicField.getText());

    return replacements;
}

 
@FXML
    private void handleExportRecap() {
    }
    private Boolean validateFields() {
       Boolean isValid = true;

        if (nomETPField.getText().isEmpty() && nomETPArabicField.getText().isEmpty()) {
            nomETPError.setText("Nom de l'ETP est requis");
            nomETPArabicError.setText("اسم المؤسسة مطلوب");
            isValid = false;
        } else {
            nomETPError.setText("");
            nomETPArabicError.setText("");
        }

        if (adresseField.getText().isEmpty() && adresseArabicField.getText().isEmpty()) {
            adresseError.setText("Adresse est requise");
            adresseArabicError.setText("العنوان مطلوب");
            isValid = false;
        } else {
            adresseError.setText("");
            adresseArabicError.setText("");
        }
        if (telephoneETPField.getText().isEmpty()) {
            telephoneETPError.setText("Téléphone est requis");
            isValid = false;
        } else {
            telephoneETPError.setText("");
        }
        if (emailETPField.getText().isEmpty()) {
            emailETPError.setText("Email est requis");
            isValid = false;
        } else {
            emailETPError.setText("");
        }
        if (!validateEmail(emailETPField.getText())) {
            emailETPError.setText("Email invalide");
            isValid = false;
        } else {
            emailETPError.setText("");
        }
        if (nomPrenomField.getText().isEmpty() && nomPrenomArabicField.getText().isEmpty()) {
            nomPrenomError.setText("Nom et prénom sont requis");
            nomPrenomArabicError.setText("الاسم واللقب مطلوبان");
            isValid = false;
        } else {
            nomPrenomError.setText("");
            nomPrenomArabicError.setText("");
        }
        if (fonctionField.getText().isEmpty() && fonctionArabicField.getText().isEmpty()) {
            fonctionError.setText("Fonction est requise");
            fonctionArabicError.setText("الوظيفة مطلوبة");
            isValid = false;
        } else {
            fonctionError.setText("");
            fonctionArabicError.setText("");
        }
        if (telephoneField.getText().isEmpty()) {
            telephoneError.setText("Téléphone est requis");
            isValid = false;
        } else {
            telephoneError.setText("");
        }
        if (emailField.getText().isEmpty()) {
            emailError.setText("Email est requis");
            isValid = false;
        } else {
            emailError.setText("");
        }
        if (!validateEmail(emailField.getText())) {
            emailError.setText("Email invalide");
            isValid = false;
        } else {
            emailError.setText("");
        }
        if (typeProspectionField.getValue() == null) {
            typeProspectionError.setText("Type de prospection est requis");
            isValid = false;
        } else {
            typeProspectionError.setText("");
        }
        if (secteurActiviteField.getText().isEmpty()) {
            secteurActiviteError.setText("Secteur d'activité est requis");
            isValid = false;
        } else {
            secteurActiviteError.setText("");
        }
        if (psaProspecterField.getText().isEmpty()) {
            psaProspecterError.setText("PSA à prospecter est requis");
            isValid = false;
        } else {
            psaProspecterError.setText("");
        }
        if (marcheCibleField.getText().isEmpty()) {
            marcheCibleError.setText("Marché cible est requis");
            isValid = false;
        } else {
            marcheCibleError.setText("");
        }

        if (dateField.getValue() == null) {
            dateError.setText("Date est requise");
            isValid = false;
        } else {
            dateError.setText("");
        } 
        
        if (periodeProspectionField.getText().isEmpty()) {
            periodeProspectionError.setText("فترة الاستكشاف مطلوبة");
            isValid = false;
        } else {
            periodeProspectionError.setText("");
        }
        if (particulariteField.getText().isEmpty()) {
            particulariteError.setText("الخصوصية مطلوبة");
            isValid = false;
        } else {
            particulariteError.setText("");
        }
        if(dateField.getValue()== null) {
            dateError.setText("Date est requise");
            isValid = false;
        } else {
            dateError.setText("");
        }
        return isValid;

    }
    private Boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
}