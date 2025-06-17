package ccis.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.apache.poi.xwpf.usermodel.*;

import java.awt.Desktop;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class IdentificationBesoinController {

    // Champs entreprise
    @FXML private TextField nom_entreprise, اسمالشركة, adresse, العنوان, telephone1,  email1;
    // Personne à contacter
    @FXML private TextField nom_prenom, اسمالكامل, fonction, الوظيفة, telephone2,  email2;
    // Prospection
    @FXML private CheckBox actionoui, actionnon;
    @FXML private TextField action;
    @FXML private CheckBox pd, psf, pel, rc, pc, autre;
    @FXML private TextField autrep;
    // Motivations
    @FXML private CheckBox m1, m2, m3, m4, m5;
    @FXML private TextField m5p;
    @FXML private TextField marche;
    @FXML private TextArea comentaire;

    @FXML
    public void generate() {
        Map<String, String> replacements = new HashMap<>();
        // Infos entreprise
        replacements.put("{nom_entreprise}", getValue(nom_entreprise));
        replacements.put("{اسمالشركة}", getValue(اسمالشركة));
        replacements.put("{adresse}", getValue(adresse));
        replacements.put("{العنوان}", getValue(العنوان));
        replacements.put("{telephone1}", getValue(telephone1));
        replacements.put("{email1}", getValue(email1));
        // Personne à contacter
        replacements.put("{nom_prenom}", getValue(nom_prenom));
        replacements.put("{اسمالكامل}", getValue(اسمالكامل));
        replacements.put("{fonction}", getValue(fonction));
        replacements.put("{الوظيفة}", getValue(الوظيفة));
        replacements.put("{telephone2}", getValue(telephone2));
        replacements.put("{email2}", getValue(email2));
        // Actions de prospection
        replacements.put("{actionoui}", actionoui.isSelected() ? "☑" : "☐");
        replacements.put("{actionnon}", actionnon.isSelected() ? "☑" : "☐");
        replacements.put("{action}", getValue(action));
        replacements.put("{pd}", pd.isSelected() ? "☑" : "☐");
        replacements.put("{psf}", psf.isSelected() ? "☑" : "☐");
        replacements.put("{pel}", pel.isSelected() ? "☑" : "☐");
        replacements.put("{rc}", rc.isSelected() ? "☑" : "☐");
        replacements.put("{pc}", pc.isSelected() ? "☑" : "☐");
        replacements.put("{autre}", autre.isSelected() ? "☑" : "☐");
        replacements.put("{autrep}", getValue(autrep));
        // Motivations
        replacements.put("{m1}", m1.isSelected() ? "☑" : "☐");
        replacements.put("{m2}", m2.isSelected() ? "☑" : "☐");
        replacements.put("{m3}", m3.isSelected() ? "☑" : "☐");
        replacements.put("{m4}", m4.isSelected() ? "☑" : "☐");
        replacements.put("{m5}", m5.isSelected() ? "☑" : "☐");
        replacements.put("{m5p}", getValue(m5p));
        replacements.put("{marche}", getValue(marche));
        replacements.put("{comentaire}", getValue(comentaire));

        // Génération du Word
        try (InputStream is = getClass().getResourceAsStream("/templates/template_identification_besoins.docx");
             XWPFDocument doc = new XWPFDocument(is)) {

            for (XWPFParagraph p : doc.getParagraphs()) {
                replaceInParagraph(p, replacements);
            }
            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            replaceInParagraph(p, replacements);
                        }
                    }
                }
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le rapport");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Document Word (*.docx)", "*.docx")
            );
            fileChooser.setInitialFileName("IdentificationBesoin.docx");
            fileChooser.setInitialDirectory(new File("C:/fichiers application ccis/prospection"));
            File file = fileChooser.showSaveDialog(nom_entreprise.getScene().getWindow());

            if (file != null) {
                try (FileOutputStream out = new FileOutputStream(file)) {
                    doc.write(out);
                }
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getValue(TextInputControl field) {
        return field != null && field.getText() != null ? field.getText() : "";
    }

    private void replaceInParagraph(XWPFParagraph paragraph, Map<String, String> replacements) {
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null) {
                for (Map.Entry<String, String> entry : replacements.entrySet()) {
                    if (text.contains(entry.getKey())) {
                        text = text.replace(entry.getKey(), entry.getValue());
                    }
                }
                run.setText(text, 0);
            }
        }
    }
}