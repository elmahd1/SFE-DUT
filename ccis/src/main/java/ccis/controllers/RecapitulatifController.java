package ccis.controllers;

import ccis.dao.ProspectionDAO;
import ccis.models.Prospection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class RecapitulatifController {

    @FXML private TableView<Prospection> tableView;


    @FXML private TableColumn<Prospection, String> nomSocieteCol;
    @FXML private TableColumn<Prospection, String> telephoneCol;
    @FXML private TableColumn<Prospection, String> contactCol;
    @FXML private TableColumn<Prospection, String> resultatCol;
    @FXML private TableColumn<Prospection, String> commentaireCol;
    @FXML private TableColumn<Prospection, String> suiteADonnerCol;
    @FXML private TableColumn<Prospection, String> objectifCol;
    @FXML private TableColumn<Prospection, Void> actionsCol;


    private final ProspectionDAO dao = new ProspectionDAO();
    private final ObservableList<Prospection> demarchesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        configureColumns();
        // Charger les données depuis la base
        loadDemarches();

        setupActionColumn();
    }

    private void setupActionColumn() {
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");
    
            {
                deleteButton.setOnAction(event -> {
                    Prospection selected = getTableView().getItems().get(getIndex());
                    handleDelete(selected);
                });
            }
    
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
    }
        @FXML
    private void handleDelete(Prospection selectedProspection) {
        if (selectedProspection != null) {
            dao.deleteProspection(selectedProspection.getId());
            demarchesList.remove(selectedProspection);
        } else {
            showAlert("Erreur", "Impossible de supprimer : aucune sélection.");
        }
    }
 private void configureColumns() {
        // Configure each column to display the appropriate property
        nomSocieteCol.setCellValueFactory(new PropertyValueFactory<>("nomETP"));
        telephoneCol.setCellValueFactory(new PropertyValueFactory<>("telephoneETP"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("nomPrenom"));
        objectifCol.setCellValueFactory(new PropertyValueFactory<>("typeProspection"));
resultatCol.setCellValueFactory(new PropertyValueFactory<>("resultat"));
resultatCol.setCellFactory(TextFieldTableCell.forTableColumn());
resultatCol.setOnEditCommit(event -> {
    Prospection p = event.getRowValue();
    p.setResultat(event.getNewValue());
    dao.updateProspection(p); // Save to DB if needed
});

commentaireCol.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
commentaireCol.setCellFactory(TextFieldTableCell.forTableColumn());
commentaireCol.setOnEditCommit(event -> {
    Prospection p = event.getRowValue();
    p.setCommentaire(event.getNewValue());
    dao.updateProspection(p);
});

suiteADonnerCol.setCellValueFactory(new PropertyValueFactory<>("suiteADonner"));
suiteADonnerCol.setCellFactory(TextFieldTableCell.forTableColumn());
suiteADonnerCol.setOnEditCommit(event -> {
    Prospection p = event.getRowValue();
    p.setSuiteADonner(event.getNewValue());
    dao.updateProspection(p);
});
    }

private void loadDemarches() {
    List<Prospection> demarches = dao.getAllProspections();

    // Utiliser un Set pour éliminer les doublons
    Set<Prospection> uniqueDemarches = new HashSet<>(demarches);

    // Ajouter les données uniques à la liste observable
    demarchesList.setAll(uniqueDemarches);
    tableView.setItems(demarchesList);
    FXCollections.reverse(demarchesList);
}



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void exportRA() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Espace Entreprise");
    
        // Créer la ligne d'en-têtes
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Nom ETP");
        headerRow.createCell(1).setCellValue("Téléphone");
        headerRow.createCell(2).setCellValue("Contact");
        headerRow.createCell(3).setCellValue("Type de Prospection");
        headerRow.createCell(4).setCellValue("Résultat");
        headerRow.createCell(5).setCellValue("Commentaire");
        headerRow.createCell(6).setCellValue("Suite à donner");


        // Récupérer les données et éliminer les doublons
        List<Prospection> demarches = dao.getAllProspections();
        Set<Prospection> uniqueDemarches = new HashSet<>(demarches);

        // Ajouter les données uniques
        int rowNum = 1;
        for (Prospection demarche : uniqueDemarches) {
            Row row = sheet.createRow(rowNum++);
    
            row.createCell(0).setCellValue(demarche.getNomETP());
            row.createCell(1).setCellValue(demarche.getTelephoneETP());
            row.createCell(2).setCellValue(demarche.getNomPrenom());
            row.createCell(3).setCellValue(demarche.getTypeProspection());
            row.createCell(4).setCellValue(demarche.getResultat());
            row.createCell(5).setCellValue(demarche.getCommentaire());
            row.createCell(6).setCellValue(demarche.getSuiteADonner());
        }
    
FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialDirectory(new File("C:/fichiers application ccis/prospection"));
        fileChooser.setInitialFileName("Recapitulatif activitès.xlsx");
        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            // User cancelled the save dialog
            return;
        }

        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
          
       if(Desktop.isDesktopSupported()){
        Desktop.getDesktop().open(file);
       }else {
          showAlert("Exportation réussie", "Les données ont été exportées avec succès.");
       }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur d'exportation", "Une erreur s'est produite lors de l'exportation des données.");
        }
    }
    
    @FXML
    private void printRA(){
        try {
        // Create a temporary file for the processed Word document
        File outputDocx = new File("generated_document_prospection_recapitulatif.docx");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF généré");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialDirectory(new File("C:/fichiers application ccis/prospection"));
        fileChooser.setInitialFileName("Recapitulatif activitès.pdf");
        File outputPdf = fileChooser.showSaveDialog(null);
        if (outputPdf == null) {
            // User cancelled the save dialog
            return;
        }

        // Load the template Word document
        String templatePath = "/templates/template_recapitulatif_activ.docx";
        InputStream templateStream = getClass().getResourceAsStream(templatePath);
        
        if (templateStream == null) {
            showErrorAlert("Erreur", "Le fichier modèle Word est introuvable : " + templatePath);
            return;
        }
        
        // Create a new Word document from the template
        XWPFDocument doc = new XWPFDocument(templateStream);

                Map<String, String> replacements = createReplacementMap();
        replaceText(doc, replacements);
   List<Prospection> prospections = dao.getAllProspections();
fillTableWithProspections(doc, prospections);
        
        // Save the modified document
        try (FileOutputStream out = new FileOutputStream(outputDocx)) {
            doc.write(out);
        }
       outputDocx.deleteOnExit(); // Delete the file on exit
        
        // Convert the Word document to PDF using a better library
        convertDocxToPdf(outputDocx, outputPdf);
        
     
        
        // Open the generated PDF file
         if (Desktop.isDesktopSupported()) {
             Desktop.getDesktop().print(outputPdf);
         }else {
                showAlert("Exportation réussie", "Le document a été généré avec succès." + outputPdf.getAbsolutePath());
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

    replacements.put("{jour}", String.valueOf(java.time.LocalDate.now().getDayOfMonth()));
    replacements.put("{mois}", String.valueOf(java.time.LocalDate.now().getMonthValue()));
    replacements.put("{annee}", String.valueOf(java.time.LocalDate.now().getYear()));

    return replacements;
}
private void fillTableWithProspections(XWPFDocument doc, List<Prospection> prospections) {
    try {
        // Pour le diagnostic
        System.out.println("Document has " + doc.getTables().size() + " tables");
        if (doc.getTables().isEmpty()) {
            System.err.println("No tables found in the document!");
            return;
        }

        XWPFTable table = doc.getTables().get(doc.getTables().size() - 1); // Dernier tableau
        System.out.println("Selected table has " + table.getRows().size() + " rows");

        int headerRowIndex = table.getRows().size() - 1;

        // Garder uniquement les lignes d'en-tête
        while (table.getRows().size() > headerRowIndex + 1) {
            table.removeRow(headerRowIndex + 1);
        }

        // Ajouter une ligne pour chaque prospection
        for (Prospection p : prospections) {
            XWPFTableRow newRow = table.createRow();

            // S'assurer qu'il y a assez de cellules (7 ici, à adapter selon votre modèle)
            while (newRow.getTableCells().size() < 7) {
                newRow.createCell();
            }

            // Remplir toutes les colonnes avec les valeurs existantes
            fillCell(newRow, 0, p.getNomETP());
            fillCell(newRow, 1, p.getTelephoneETP());
            fillCell(newRow, 2, p.getNomPrenom());
            fillCell(newRow, 3, p.getTypeProspection());
            fillCell(newRow, 4, p.getResultat());        // Résultat (éditable)
            fillCell(newRow, 5, p.getCommentaire());     // Commentaire (éditable)
            fillCell(newRow, 6, p.getSuiteADonner());    // Suite à donner (éditable)
        }

        System.out.println("After modification, table has " + table.getRows().size() + " rows");

    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Error filling table: " + e.getMessage());
    }
}

// Helper method to safely fill a cell
private void fillCell(XWPFTableRow row, int cellIndex, String text) {
    if (cellIndex >= row.getTableCells().size()) {
        return; // Cell index out of bounds
    }
    
    XWPFTableCell cell = row.getCell(cellIndex);
    
    // Create a paragraph if none exists
    if (cell.getParagraphs().isEmpty()) {
        cell.addParagraph();
    }
    
    XWPFParagraph para = cell.getParagraphs().get(0);
    
    // Clear existing text
    for (int i = para.getRuns().size() - 1; i >= 0; i--) {
        para.removeRun(i);
    }
    
    // Add new run with the text
    XWPFRun run = para.createRun();
    run.setText(text != null ? text : "");
}

}
