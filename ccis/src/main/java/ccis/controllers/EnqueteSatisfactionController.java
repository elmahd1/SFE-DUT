package ccis.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.apache.poi.xwpf.usermodel.*;

import java.awt.Desktop;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class EnqueteSatisfactionController {
    // Inputs pour chaque case (ligne 1 à 8, colonnes 2 à 4)
    @FXML private TextField input12, input13, input14 ,nom;
    @FXML private TextField input22, input23, input24;
    @FXML private TextField input32, input33, input34;
    @FXML private TextField input42, input43, input44;
    @FXML private TextField input52, input53, input54;
    @FXML private TextField input62, input63, input64;
    @FXML private TextField input72, input73, input74;
    @FXML private TextField input82, input83, input84;
    @FXML private TextField input92, input93, input94;
    @FXML private TextField input102, input103, input104;
    @FXML private TextField input112, input113, input114;
 

    @FXML
    public void generer() {
      Map<String, String> replacements = new HashMap<>();
    // Champ nom
    replacements.put("{nom}", nom != null ? nom.getText() : "");

    // Champs de satisfaction
    for (int i = 1; i <= 11; i++) {
        for (int j = 2; j <= 4; j++) {
            String fxid = "input" + i + j;
            String placeholder = "{" + i + j + "}";
            String value = "";
            try {
                TextField tf = (TextField) this.getClass().getDeclaredField(fxid).get(this);
                value = tf.getText();
            } catch (Exception ignored) {}
            replacements.put(placeholder, value);
        }
    }
      

        // Charger le template Word
        try (InputStream is = getClass().getResourceAsStream("/templates/template_enquete_satisfaction.docx");
             XWPFDocument doc = new XWPFDocument(is)) {

            // Remplacer dans tous les paragraphes
            for (XWPFParagraph p : doc.getParagraphs()) {
                replaceInParagraph(p, replacements);
            }
            // Remplacer dans les tableaux
            for (XWPFTable table : doc.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            replaceInParagraph(p, replacements);
                        }
                    }
                }
            }

            // Choisir où sauvegarder
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le rapport");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Document Word (*.docx)", "*.docx")
            );
            fileChooser.setInitialFileName("EnqueteSatisfaction.docx");
            fileChooser.setInitialDirectory(new File("C:/fichiers application ccis/prospection"));
            File file = fileChooser.showSaveDialog(input12.getScene().getWindow());

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