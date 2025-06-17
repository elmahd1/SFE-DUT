package ccis.controllers;

import ccis.dao.EspaceEntrepriseDAO;
import ccis.models.EspaceEntreprise;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrapheEspaceController {
    // Circular Charts
    @FXML
    private PieChart chart1;
    @FXML
    private PieChart chart3;

    // Bar Chart
    @FXML
    private BarChart<String, Number> chart2;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    // Tables
    @FXML
    private TableView<ObjetVisiteData> table1;
    @FXML
    private TableColumn<ObjetVisiteData, String> objetColumn;
    @FXML
    private TableColumn<ObjetVisiteData, Integer> totalColumn;
    @FXML
    private TableColumn<ObjetVisiteData, Double> percentageColumn;

    @FXML
    private TableView<ObjetVisiteDetailData> table2;
    @FXML
    private TableColumn<ObjetVisiteDetailData, String> objetVisiteColumn;
    @FXML
    private TableColumn<ObjetVisiteDetailData, Double> delaiMoyenColumn;
    @FXML
    private TableColumn<ObjetVisiteDetailData, Double> pourcentageDelaiColumn;

    @FXML
    private TableView<FormeJuridiqueData> table3;
    @FXML
    private TableColumn<FormeJuridiqueData, String> formeColumn;
    @FXML
    private TableColumn<FormeJuridiqueData, Integer> nombreVisiteColumn;
    @FXML
    private TableColumn<FormeJuridiqueData, Double> percentageFormeColumn;

    // Buttons
    @FXML
    private Button downloadChart1Button;
    @FXML
    private Button copyChart1Button;
    @FXML
    private Button downloadChart2Button;
    @FXML
    private Button copyChart2Button;
    @FXML
    private Button downloadChart3Button;
    @FXML
    private Button copyChart3Button;
    @FXML
    private Button copyTable1Button;
    @FXML
    private Button copyTable2Button;
    @FXML
    private Button copyTable3Button;

    private EspaceEntrepriseDAO dao = new EspaceEntrepriseDAO();

    // Data models for tables
    public static class ObjetVisiteData {
        private final String objet;
        private final int total;
        private final double percentage;

        public ObjetVisiteData(String objet, int total, double percentage) {
            this.objet = objet;
            this.total = total;
            this.percentage = percentage;
        }

        public String getObjet() { return objet; }
        public int getTotal() { return total; }
        public double getPercentage() { return percentage; }
    }

    public static class FormeJuridiqueData {
        private final String forme;
        private final int nombreVisite;
        private final double percentage;

        public FormeJuridiqueData(String forme, int nombreVisite, double percentage) {
            this.forme = forme;
            this.nombreVisite = nombreVisite;
            this.percentage = percentage;
        }

        public String getForme() { return forme; }
        public int getNombreVisite() { return nombreVisite; }
        public double getPercentage() { return percentage; }
    }

    public static class ObjetVisiteDetailData {
        private final String objetVisite;
        private final double delaiMoyen;
        private final double pourcentageDelai;

        public ObjetVisiteDetailData(String objetVisite, double delaiMoyen,  double pourcentageDelai) {
            this.objetVisite = objetVisite;
            this.delaiMoyen = delaiMoyen;
            this.pourcentageDelai = pourcentageDelai;
        }

        public String getObjetVisite() { return objetVisite; }
        public double getDelaiMoyen() { return delaiMoyen; }
        public double getPourcentageDelai() { return pourcentageDelai; }
    }

    @FXML
    public void initialize() {
        // Initialize table columns
        setupTableColumns();
        
        // Load data
        loadData();
       
    }

    private void setupTableColumns() {
        // Table 1 columns
        objetColumn.setCellValueFactory(new PropertyValueFactory<>("objet"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        percentageColumn.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        percentageColumn.setCellFactory(column -> new TableCell<ObjetVisiteData, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f%%", item));
                }
            }
        });

        // Table 2 columns
        objetVisiteColumn.setCellValueFactory(new PropertyValueFactory<>("objetVisite"));
        delaiMoyenColumn.setCellValueFactory(new PropertyValueFactory<>("delaiMoyen"));
        pourcentageDelaiColumn.setCellValueFactory(new PropertyValueFactory<>("pourcentageDelai"));
        

        
        pourcentageDelaiColumn.setCellFactory(column -> new TableCell<ObjetVisiteDetailData, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f%%", item));
                }
            }
        });

        // Table 3 columns
        formeColumn.setCellValueFactory(new PropertyValueFactory<>("forme"));
        nombreVisiteColumn.setCellValueFactory(new PropertyValueFactory<>("nombreVisite"));
        percentageFormeColumn.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        percentageFormeColumn.setCellFactory(column -> new TableCell<FormeJuridiqueData, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f%%", item));
                }
            }
        });
    }
private void loadData() {
        List<EspaceEntreprise> data = dao.getAll();
        int total = data.size();

        // Maps for data aggregation
        Map<String, Integer> objetCounts = new HashMap<>();
        Map<String, Integer> formeCounts = new HashMap<>();
        Map<String, Long> objetDelaiTotal = new HashMap<>();
        Map<String, Integer> objetDelaiCount = new HashMap<>();

        long totalMinutes = 0;

        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

        for (EspaceEntreprise item : data) {
            // Count objetVisite
            String objet = item.getObjetVisite();
            objetCounts.put(objet, objetCounts.getOrDefault(objet, 0) + 1);
String forme ="";
            // Count formeJuridique
            if (!item.getFormeJuridique().equals("SARL") &&
            !item.getFormeJuridique().equals("SA") &&
            !item.getFormeJuridique().equals("PP (Personne physique)")) {
            forme = "Autre";
        } else {
            forme = item.getFormeJuridique();
        }
        
            formeCounts.put(forme, formeCounts.getOrDefault(forme, 0) + 1);

            // Calculate delay and montant
            try {
               
                String t1 = item.getHeureContact();
                String t2 = item.getHeureDepart();
                
                if ( t1 == null ||  t2 == null ||
                   t1.isEmpty() ||  t2.isEmpty()) {
                    long defaultMinutes = 120;
                    totalMinutes += defaultMinutes;
                    objetDelaiTotal.put(objet, objetDelaiTotal.getOrDefault(objet, 0L) + defaultMinutes);
                    objetDelaiCount.put(objet, objetDelaiCount.getOrDefault(objet, 0) + 1);
                    continue;
                }


                LocalTime time1 = LocalTime.parse(t1, timeFormat);
                LocalTime time2 = LocalTime.parse(t2, timeFormat);

                Duration duration = Duration.between(time1, time2);
                long minutes = Math.max(duration.toMinutes(), 0);
                totalMinutes += minutes;
                objetDelaiTotal.put(objet, objetDelaiTotal.getOrDefault(objet, 0L) + minutes);
                objetDelaiCount.put(objet, objetDelaiCount.getOrDefault(objet, 0) + 1);
            } catch (Exception e) {
                long defaultMinutes = 120;
                totalMinutes += defaultMinutes;
                objetDelaiTotal.put(objet, objetDelaiTotal.getOrDefault(objet, 0L) + defaultMinutes);
                objetDelaiCount.put(objet, objetDelaiCount.getOrDefault(objet, 0) + 1);
            }
        }

       ObservableList<ObjetVisiteData> table1Data = FXCollections.observableArrayList();
chart1.getData().clear();
chart1.setTitle("distribution par prestation fournie");

// Afficher d'abord les objets normaux
for (Map.Entry<String, Integer> entry : objetCounts.entrySet()) {
    String key = entry.getKey();
    if ("Autre".equalsIgnoreCase(key) || "Total".equalsIgnoreCase(key)) continue;
    double percent = (entry.getValue() * 100.0) / total;
    chart1.getData().add(new PieChart.Data(key + " (" + String.format("%.1f", percent) + "%):" + entry.getValue(), entry.getValue()));
    table1Data.add(new ObjetVisiteData(key, entry.getValue(), percent));
}
// Puis "Autre" si présent
if (objetCounts.containsKey("Autre")) {
    int value = objetCounts.get("Autre");
    double percent = (value * 100.0) / total;
    chart1.getData().add(new PieChart.Data("Autre (" + String.format("%.1f", percent) + "%):" + value, value));
    table1Data.add(new ObjetVisiteData("Autre", value, percent));
}
// Puis "Total"
table1Data.add(new ObjetVisiteData("Total", total, 100.0));

// Set Table 1 data
table1.setItems(table1Data);


       // Table 3 & PieChart 3: Forme Juridique
ObservableList<FormeJuridiqueData> table3Data = FXCollections.observableArrayList();
chart3.getData().clear();
chart3.setTitle("Répartition par Forme Juridique");

// Afficher d'abord les formes normales
for (Map.Entry<String, Integer> entry : formeCounts.entrySet()) {
    String key = entry.getKey();
    if ("Autre".equalsIgnoreCase(key) || "Total".equalsIgnoreCase(key)) continue;
    double percent = (entry.getValue() * 100.0) / total;
    chart3.getData().add(new PieChart.Data(key + " (" + String.format("%.1f", percent) + "%):" + entry.getValue(), entry.getValue()));
    table3Data.add(new FormeJuridiqueData(key, entry.getValue(), percent));
}
// Puis "Autre" si présent
if (formeCounts.containsKey("Autre")) {
    int value = formeCounts.get("Autre");
    double percent = (value * 100.0) / total;
    chart3.getData().add(new PieChart.Data("Autre (" + String.format("%.1f", percent) + "%):" + value, value));
    table3Data.add(new FormeJuridiqueData("Autre", value, percent));
}
// Puis "Total"
table3Data.add(new FormeJuridiqueData("Total", total, 100.0));

// Set Table 3 data
table3.setItems(table3Data);

        // BarChart 2: Délai moyen en heures by objet
        chart2.getData().clear();
        chart2.setTitle("Délai Moyen par Objet de Visite");

        XYChart.Series<String, Number> delaiSeries = new XYChart.Series<>();
        delaiSeries.setName("Délai Moyen (par minutes)");
        
        // Create data for Table 2
        ObservableList<ObjetVisiteDetailData> table2Data = FXCollections.observableArrayList();
        
        // Add bars for each objet visite with their montant and average delay
        for (String objet : objetCounts.keySet()) {
        
        
            // Délai
            long delaiMinutes = objetDelaiTotal.getOrDefault(objet, 0L);
            int count = objetDelaiCount.getOrDefault(objet, 0);
            double delaiMoyen = count > 0 ? delaiMinutes  / count : 0;
            double pourcentageDelai = (delaiMinutes * 100.0) / (totalMinutes > 0 ? totalMinutes : 1);
            delaiSeries.getData().add(new XYChart.Data<>(objet, delaiMoyen));
            
            // Add to table2
            table2Data.add(new ObjetVisiteDetailData(objet, delaiMoyen, pourcentageDelai));
        }
        
        chart2.getData().addAll(delaiSeries);
        
for (XYChart.Series<String, Number> series : chart2.getData()) {
    for (XYChart.Data<String, Number> data5 : series.getData()) {
        Label label = new Label(data5.getYValue().toString());
        StackPane node = (StackPane) data5.getNode();

        node.getChildren().add(label);

        // Style optionnel pour le label
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        StackPane.setAlignment(label, Pos.TOP_CENTER);
    }
}
// Calcul du délai moyen global (en heures)
long totalDelaiMinutes = 0;
int totalDelaiCount = 0;

for (String objet : objetCounts.keySet()) {
    totalDelaiMinutes += objetDelaiTotal.getOrDefault(objet, 0L);
    totalDelaiCount += objetDelaiCount.getOrDefault(objet, 0);
}

double totalDelaiMoyen = totalDelaiCount > 0 ? totalDelaiMinutes  / totalDelaiCount : 0;

// Ajoute la ligne "Total" avec la moyenne globale et 100%
table2Data.add(new ObjetVisiteDetailData("Delai moyen total", totalDelaiMoyen, 100.0));
        // Set Table 2 data
        table2.setItems(table2Data);
        // Set axes labels
        xAxis.setLabel("Objet de Visite");
        yAxis.setLabel("Valeur");
    }

    public void savePieChartAsPng(PieChart chart, String filename) {
    WritableImage image = chart.snapshot(new SnapshotParameters(), null);
    File file = new File(filename);
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Chart as PNG");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
    fileChooser.setInitialFileName(filename);
    file = fileChooser.showSaveDialog(null);
    if (file != null) {
        filename = file.getAbsolutePath();
    } else {
        System.out.println("Save operation was cancelled.");
        return;
    }
    try {
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        if(Desktop.isDesktopSupported()){
            Desktop.getDesktop().open(file);
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Chart saved to: " + file.getAbsolutePath());
            alert.showAndWait();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
public void savebarChartAsPng(BarChart<String, Number> chart, String filename) {
            WritableImage image = chart.snapshot(new SnapshotParameters(), null);
            File file = new File(filename);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Chart as PNG");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
            fileChooser.setInitialDirectory(new File("C:/fichiers application ccis/espace entreprise"));
            fileChooser.setInitialFileName(filename);

            file = fileChooser.showSaveDialog(null);
            if (file != null) {
                filename = file.getAbsolutePath();
            } else {
                System.out.println("Save operation was cancelled.");
                return;
            }
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                if(Desktop.isDesktopSupported()){
                    Desktop.getDesktop().open(file);}
                    else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Chart saved to: " + file.getAbsolutePath());
                    alert.showAndWait();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
@FXML
private void copyChart1(ActionEvent event) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            WritableImage image = chart1.snapshot(new SnapshotParameters(), null);
            content.putImage(image);
            clipboard.setContent(content);
        }
        @FXML
        private void copyChart2(ActionEvent event) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            WritableImage image = chart2.snapshot(new SnapshotParameters(), null);
            content.putImage(image);
            clipboard.setContent(content);
        }
        
        @FXML
        private void copyChart3(ActionEvent event) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            WritableImage image = chart3.snapshot(new SnapshotParameters(), null);
            content.putImage(image);
            clipboard.setContent(content);
        }


       
@FXML
private void downloadChart1(ActionEvent event) {
    savePieChartAsPng(chart1, "graphe objet de visite.png");
}
@FXML
private void downloadChart2(ActionEvent event) {
    savebarChartAsPng(chart2, "graphe delai et montant moyen par objet de visite.png");
}
@FXML
private void downloadChart3(ActionEvent event) {
    savePieChartAsPng(chart3,       "graphe forme juridique.png");
}
@FXML
private void downloadTable1(ActionEvent event) {
    exportTableToCSV(table1, "table objet de visite.csv");
}
@FXML
private void downloadTable2(ActionEvent event) {
    exportTableToCSV(table2, "table delai et montant moyens par objet de visite.csv");
}
@FXML
private void downloadTable3(ActionEvent event) {
    exportTableToCSV(table3, "table forme juridique.csv");
}
public <T> void exportTableToCSV(TableView<T> table, String filename) {
    File file = new File(filename);
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save Table as CSV");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
    fileChooser.setInitialFileName(filename);
    file = fileChooser.showSaveDialog(null);
    if (file != null) {
        filename = file.getAbsolutePath();
    } else {
        System.out.println("Save operation was cancelled.");
        return;
    }
    try (PrintWriter writer = new PrintWriter(file)) {
        // En-têtes de colonnes
        for (TableColumn<T, ?> column : table.getColumns()) {
            writer.print(column.getText() + ";");
        }
        writer.println();

        // Données des lignes
        for (T item : table.getItems()) {
            for (TableColumn<T, ?> column : table.getColumns()) {
                Object cellData = column.getCellObservableValue(item).getValue();
                writer.print((cellData != null ? cellData.toString() : "") + ";");
            }
            writer.println();
        }

        if(Desktop.isDesktopSupported()){
            Desktop.getDesktop().open(file);}
            else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Table saved to: " + file.getAbsolutePath());
            alert.showAndWait();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


}