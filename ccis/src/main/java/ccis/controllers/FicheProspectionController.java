package ccis.controllers;

import java.io.*;
import java.util.*;
import java.awt.Desktop;
import org.apache.poi.xwpf.usermodel.*;
import ccis.dao.ProspectionDAO;
import ccis.models.Prospection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

  

    private final ProspectionDAO dao = new ProspectionDAO();

    @FXML
    private void initialize() {
        typeProspectionField.getItems().addAll(
            "Prospection nationale استكشاف تجاري وطني",
            "Prospection internationale استكشاف تجاري دولي"
        );
        dateField.setValue(java.time.LocalDate.now());
    }

    @FXML
    private void handleSave() {
        boolean isValid = true;
        clearAllErrors();

        // Nom ETP
        if (nomETPField.getText().isEmpty() && nomETPArabicField.getText().isEmpty()) {
            showError(nomETPField, "Nom de l'ETP est requis");
            showError(nomETPArabicField, "اسم المؤسسة مطلوب");
            isValid = false;
        }

        // Adresse
        if (adresseField.getText().isEmpty() && adresseArabicField.getText().isEmpty()) {
            showError(adresseField, "Adresse est requise");
            showError(adresseArabicField, "العنوان مطلوب");
            isValid = false;
        }

        // Téléphone ETP
        if (telephoneETPField.getText().isEmpty()) {
            showError(telephoneETPField, "Téléphone est requis");
            isValid = false;
        }

        

        // Nom Prenom
        if (nomPrenomField.getText().isEmpty() && nomPrenomArabicField.getText().isEmpty()) {
            showError(nomPrenomField, "Nom et prénom sont requis");
            showError(nomPrenomArabicField, "الاسم واللقب مطلوبان");
            isValid = false;
        }

        // Fonction
        if (fonctionField.getText().isEmpty() && fonctionArabicField.getText().isEmpty()) {
            showError(fonctionField, "Fonction est requise");
            showError(fonctionArabicField, "الوظيفة مطلوبة");
            isValid = false;
        }

        // Téléphone
        if (telephoneField.getText().isEmpty()) {
            showError(telephoneField, "Téléphone est requis");
            isValid = false;
        }

       

        // Type Prospection
        if (typeProspectionField.getValue() == null) {
            showError(typeProspectionField, "Type de prospection est requis");
            isValid = false;
        }

        // Secteur Activité
        if (secteurActiviteField.getText().isEmpty()) {
            showError(secteurActiviteField, "Secteur d'activité est requis");
            isValid = false;
        }

        // PSA Prospecter
        if (psaProspecterField.getText().isEmpty()) {
            showError(psaProspecterField, "PSA à prospecter est requis");
            isValid = false;
        }

        // Marché Cible
        if (marcheCibleField.getText().isEmpty()) {
            showError(marcheCibleField, "Marché cible est requis");
            isValid = false;
        }

        // Date
        if (dateField.getValue() == null) {
            showError(dateField.getEditor(), "Date est requise");
            isValid = false;
        }

        // Période Prospection
        if (periodeProspectionField.getText().isEmpty()) {
            showError(periodeProspectionField, "Période prospection requise");
            isValid = false;
        }

        // Particularité
        if (particulariteField.getText().isEmpty()) {
            showError(particulariteField, "Particularité requise");
            isValid = false;
        }

        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez corriger les erreurs dans le formulaire.");
            alert.showAndWait();
            return;
        }

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

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Enregistré avec succès !");
        alert.showAndWait();
    }

private void showError(Control control, String message) {
    control.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
    if (control instanceof TextField) {
        TextField tf = (TextField) control;
        tf.promptTextProperty().unbind(); // Unbind before setting
        tf.setPromptText(message);
    } else if (control instanceof ComboBox) {
        ComboBox<?> cb = (ComboBox<?>) control;
        cb.promptTextProperty().unbind();
        cb.setPromptText(message);
    }
 
    
    // Unbind tooltip property if it's bound
    if (control.tooltipProperty().isBound()) {
        control.tooltipProperty().unbind();
    }
    control.setTooltip(new Tooltip(message));
}

private void clearError(Control control) {
    control.setStyle("");
    if (control instanceof TextField) {
        TextField tf = (TextField) control;
        tf.promptTextProperty().unbind();
        tf.setPromptText("");
    } else if (control instanceof ComboBox) {
        ComboBox<?> cb = (ComboBox<?>) control;
        cb.promptTextProperty().unbind();
        cb.setPromptText("");
    }
   
    
    // Safely clear the tooltip
    try {
        // Check if this is a FakeFocusTextField or if tooltip is bound
        boolean isFakeFocusTextField = control.getClass().getSimpleName().equals("FakeFocusTextField");
        
        if (isFakeFocusTextField || (control.tooltipProperty() != null && control.tooltipProperty().isBound())) {
            // For bound tooltips, use Tooltip.uninstall
            Tooltip.uninstall(control, control.getTooltip());
        } else {
            // For other controls, set tooltip to null normally
            control.setTooltip(null);
        }
    } catch (Exception e) {
        // Fall back to not touching the tooltip at all
        System.err.println("Could not clear tooltip: " + e.getMessage());
    }
}

    // Clear all errors at the start of handleSave
    private void clearAllErrors() {
        clearError(nomETPField);
        clearError(nomETPArabicField);
        clearError(adresseField);
        clearError(adresseArabicField);
        clearError(telephoneETPField);
        clearError(emailETPField);
        clearError(nomPrenomField);
        clearError(nomPrenomArabicField);
        clearError(fonctionField);
        clearError(fonctionArabicField);
        clearError(telephoneField);
        clearError(emailField);
        clearError(typeProspectionField);
        clearError(secteurActiviteField);
        clearError(psaProspecterField);
        clearError(marcheCibleField);
        clearError(dateField.getEditor());
        clearError(periodeProspectionField);
        clearError(particulariteField);
    }

    @FXML
    private void handleCancel() {
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
        nomETPArabicField.clear();
        adresseArabicField.clear();
        nomPrenomArabicField.clear();
        fonctionArabicField.clear();
        clearAllErrors();
    }

    @FXML
    private void handlePrint() {
        try {
            File outputDocx = new File("Fiche Prospection.docx");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le PDF généré");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            fileChooser.setInitialFileName("Fiche Prospection.pdf");
            fileChooser.setInitialDirectory(new File("C:/fichiers application ccis/prospection"));
            File outputPdf = fileChooser.showSaveDialog(null);
            if (outputPdf == null) return;

            String templatePath = "/templates/template_word_prospection.docx";
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

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    private void replaceText(XWPFDocument doc, Map<String, String> replacements) {
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            List<XWPFRun> runs = paragraph.getRuns();
            if (runs != null) {
                StringBuilder builder = new StringBuilder();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    if (text != null) builder.append(text);
                }
                String text = builder.toString();
                for (Map.Entry<String, String> entry : replacements.entrySet()) {
                    text = text.replace(entry.getKey(), entry.getValue());
                }
                for (int i = runs.size() - 1; i > 0; i--) {
                    paragraph.removeRun(i);
                }
                if (runs.size() > 0) {
                    XWPFRun run = runs.get(0);
                    run.setText(text, 0);
                }
            }
        }
        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        List<XWPFRun> runs = paragraph.getRuns();
                        if (runs != null) {
                            StringBuilder builder = new StringBuilder();
                            for (XWPFRun run : runs) {
                                String text = run.getText(0);
                                if (text != null) builder.append(text);
                            }
                            String text = builder.toString();
                            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                                text = text.replace(entry.getKey(), entry.getValue());
                            }
                            for (int i = runs.size() - 1; i > 0; i--) {
                                paragraph.removeRun(i);
                            }
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

    private Boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
}