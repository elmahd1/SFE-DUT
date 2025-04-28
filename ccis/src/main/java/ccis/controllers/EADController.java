package ccis.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ccis.models.DemarcheAdministratif;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class EADController {
     @FXML
    private TableView<DemarcheAdministratif> demarcheTable;

    // TableColumn declarations (match fx:id from FXML)
    @FXML
    private TableColumn<DemarcheAdministratif, String> dateContactCol;
 
    // Layout components
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ToolBar toolBar;
    @FXML
    private MenuButton columnsMenuButton;

    private Map<String, TableColumn<DemarcheAdministratif, ?>> columnMap = new HashMap<>();
    private Map<String, CheckMenuItem> menuItemMap = new HashMap<>();

    @FXML
    public void initialize() {
        // Setup column mappings
        setupColumn(dateContactCol, "dateContact");
    
        // Initialize with all columns visible
        columnMap.values().forEach(col -> col.setVisible(true));
        
        // TODO: Load data from your repository
        // demarcheTable.setItems(FXCollections.observableArrayList(yourDataList));
    }
    
    private void setupColumn(TableColumn<DemarcheAdministratif, String> column, String propertyName) {
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        columnMap.put(column.getText(), column);
    }
    
    @FXML
    private void toggleColumn(ActionEvent event) {
        CheckMenuItem menuItem = (CheckMenuItem) event.getSource();
        String columnName = menuItem.getText();
        TableColumn<DemarcheAdministratif, ?> column = columnMap.get(columnName);
        
        if (column != null) {
            column.setVisible(menuItem.isSelected());
        }
    }
    
    // Method to refresh table data
    public void refreshTable(List<DemarcheAdministratif> data) {
        demarcheTable.getItems().setAll(data);
    }

}
