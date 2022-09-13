package wright.firstproject.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import wright.firstproject.MainApplication;
import wright.firstproject.Models.Part;
import wright.firstproject.Models.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/** This is the class that controls the add product page.
 RUNTIME ERROR The associated parts table used to affect the actual parts list.
 this was resolved by creating a clone of the parts list on initialization of this page.
 FUTURE ENHANCEMENT I would like to add listeners to the text field and add real time validation instead of validating on button submit.*/
public class AddProductController implements Initializable {
    public TextField partSearchBar;
    @FXML
    private TableColumn partIdCol, partNameCol, partInvCol, partPriceCol;
    @FXML
    private TableColumn assPartIdCol, assPartNameCol, assPartInvCol, assPartPriceCol;
    @FXML
    private TableView allPartsTable, associatedPartsTable;
    @FXML
    private TextField prodIdField, prodNameField, prodStockField, prodPriceField, prodMaxField, prodMinField;
    private ObservableList<Part> allParts = FXCollections.observableArrayList(Inventory.getAllParts());
    private ObservableList<Part> prodAssociatedParts = FXCollections.observableArrayList();
    private Part selectedPart;
    private boolean formSubmittable = true;
    /** method that adds parts to the associated parts table. */
    public void addAssociatedPart() {
        boolean existsToggle = false;
        selectedPart = (Part) allPartsTable.getSelectionModel().getSelectedItem();
        allParts.remove(selectedPart);
        for (Part part: prodAssociatedParts){
            if (selectedPart.getId() == part.getId()){
                existsToggle = true;
            }
        }
        if (!existsToggle) {
            prodAssociatedParts.add(selectedPart);
        }
    }
    /** method that removes an associated part from the associated parts table */
    public void removeAssociatedPart() {
        selectedPart = (Part) associatedPartsTable.getSelectionModel().getSelectedItem();
        allParts.add(selectedPart);
        prodAssociatedParts.remove(selectedPart);
    }
    /** method to save the product and do basic form validation */
    public void onSave(ActionEvent actionEvent) {

        if (prodNameField.getText().isEmpty() || prodNameField.getText().matches(".*\\d.*")){
            prodNameField.setStyle("-fx-border-color: red;");
            formSubmittable = false;
        }
        if (prodStockField.getText().length() == 0 || !prodStockField.getText().matches(".*\\d.*")){
            prodStockField.setStyle("-fx-border-color: red;");
            formSubmittable = false;
        }
        if (prodPriceField.getText().length() == 0 || !prodPriceField.getText().matches(".*\\d.*")){
            prodPriceField.setStyle("-fx-border-color: red;");
            formSubmittable = false;
        }
        if (prodMinField.getText().length() == 0 || !prodMinField.getText().matches(".*\\d.*")){
            prodMinField.setStyle("-fx-border-color: red;");
            formSubmittable = false;
        }
        if (prodMaxField.getText().length() == 0 || !prodMaxField.getText().matches(".*\\d.*")){
            prodMaxField.setStyle("-fx-border-color: red;");
            formSubmittable = false;
        }
        if (Integer.parseInt(prodMinField.getText()) > Integer.parseInt(prodMaxField.getText())) {
            Inventory.warnDialog("Product Error", "min cannot be greater than max");
            formSubmittable = false;
        }
        if (Integer.parseInt(prodStockField.getText()) > Integer.parseInt(prodMaxField.getText()) || Integer.parseInt(prodStockField.getText()) < Integer.parseInt(prodMinField.getText())) {
            Inventory.warnDialog("Quantity Error", "Inventory must fall within the min and max");
            formSubmittable = false;
        }
        // adding new part fields to a new part and adding them to the parts list
        try{
            int addProdId = Integer.parseInt(prodIdField.getText());
            String addProdName = prodNameField.getText().trim();
            int addProdInv = Integer.parseInt(prodStockField.getText().trim());
            double addProdCost = Double.parseDouble(prodPriceField.getText().trim());
            int addProdMin= Integer.parseInt(prodMinField.getText().trim());
            int addProdMax = Integer.parseInt(prodMaxField.getText().trim());

            Product addProduct = new Product(addProdId, addProdName, addProdCost, addProdInv, addProdMin, addProdMax, prodAssociatedParts);
            if (formSubmittable) {
                Inventory.addProduct(addProduct);

                    for (Part part : prodAssociatedParts){
                        Inventory.associatedPartsList.add(part);
                    }

                toMain(actionEvent);
            }
            formSubmittable = true;
        } catch (Exception error){
            Inventory.warnDialog("Error adding product", "Double check your input criteria");
        }
    }
    /** method to change the scene back to the main screen */
    public void toMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/wright/firstproject/main.fxml")));
        Stage stage = (Stage)((javafx.scene.Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main");
        stage.setScene(scene);
        scene.getStylesheets().add(MainApplication.class.getResource("bootstrap3.css").toExternalForm());
    }
    /** initializes the tables and gets a random Id for the productId field */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        associatedPartsTable.setItems(prodAssociatedParts);
        allPartsTable.setItems(allParts);

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        assPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodIdField.setText(String.valueOf(Inventory.getRandomProductId()));

        partSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            partSearch(newValue);
        });
    }

    public void partSearch(String searchTerm) {
        String loweredSearch = searchTerm.toLowerCase();
        allPartsTable.setItems(Inventory.lookupPart(loweredSearch));
    }
}
