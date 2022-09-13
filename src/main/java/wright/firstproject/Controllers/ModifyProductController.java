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

import static wright.firstproject.Controllers.MainController.selectedProduct;
/** Class used to manage the modify product screen.
 * LOGICAL ERROR I had an issue where the adding and removing of parts from the associated parts table would affect the total parts list. I was able to create a clone of the parts list
 * by initializing on declaration to the value of the parts list.
 FUTURE ENHANCEMENT The associated parts list can get tricky when knowing what to display in each table. A lot of duplicate parts are stored and the way to combat this,
 is to constantly remove duplicates from both the parts and associated parts list. I would hope to reduce the amount of for loops used to do this.*/
public class ModifyProductController implements Initializable {
    @FXML
    private TextField partSearchBar;
    @FXML
    private TableColumn partIdCol, partNameCol, partInvCol, partPriceCol;
    @FXML
    private TableColumn assPartIdCol, assPartNameCol, assPartInvCol, assPartPriceCol;
    @FXML
    private TableView allPartsTable, associatedPartsTable;
    @FXML
    private TextField prodIdField, prodNameField, prodStockField, prodPriceField, prodMaxField, prodMinField;
    private ObservableList<Part> allParts = FXCollections.observableArrayList(Inventory.getAllParts());
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList(selectedProduct.getAssociatedParts());
    private Part selectedPart;
    private boolean formSubmittable = true;
    /** method to add an associated part to a product. */
    // when adding and removing parts there is now a check to see if the part already exists in the
    // target table and will remove the duplicate.
    public void addAssociatedPart() {
        selectedPart = (Part) allPartsTable.getSelectionModel().getSelectedItem();
        for (int i = 0; i< associatedParts.size(); i++){
            if (selectedPart.getId() == associatedParts.get(i).getId()){
                associatedParts.remove(selectedPart);
                i = associatedParts.size();
            }
        }
        allParts.remove(selectedPart);
        associatedParts.add(selectedPart);
    }
    /** method to remove an associated part from a product. */
    public void removeAssociatedPart(ActionEvent actionEvent) {
        selectedPart = (Part) associatedPartsTable.getSelectionModel().getSelectedItem();
        for (int i = 0; i< allParts.size(); i++){
            if (selectedPart.getId() == allParts.get(i).getId()){
                allParts.remove(selectedPart);
                i = allParts.size();
            }
        }
        allParts.add(selectedPart);
        associatedParts.remove(selectedPart);
    }
    /** method to save a product after being modified */
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

            Product addProduct = new Product(addProdId, addProdName, addProdCost, addProdInv, addProdMin, addProdMax, associatedParts);
            if (formSubmittable) {
                Inventory.updateProduct(addProduct);
                toMain(actionEvent);
            }
            formSubmittable = true;
        } catch (Exception error){
            Inventory.warnDialog("Error adding product", "Double check your input criteria");
        }
    }
    /** changes screen back to the main screen */
    public void toMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/wright/firstproject/main.fxml")));
        Stage stage = (Stage)((javafx.scene.Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main");
        stage.setScene(scene);
        scene.getStylesheets().add(MainApplication.class.getResource("bootstrap3.css").toExternalForm());
    }
    /** initializes the page with the selected product's data. */
    // initializing the selected product data
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        associatedPartsTable.setItems(associatedParts);
        allPartsTable.setItems(allParts);

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        assPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodIdField.setText(String.valueOf(selectedProduct.getId()));
        prodNameField.setText(selectedProduct.getName());
        prodPriceField.setText(String.valueOf(selectedProduct.getPrice()));
        prodStockField.setText(String.valueOf(selectedProduct.getStock()));
        prodMinField.setText(String.valueOf(selectedProduct.getMin()));
        prodMaxField.setText(String.valueOf(selectedProduct.getMax()));

        partSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            partSearch(newValue);
        });
    }

    public void partSearch(String searchTerm) {
        String loweredSearch = searchTerm.toLowerCase();
        allPartsTable.setItems(Inventory.lookupPart(loweredSearch));
    }
}
