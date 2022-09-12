package wright.firstproject.Controllers;

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

import static wright.firstproject.Controllers.Inventory.getAllParts;
import static wright.firstproject.Controllers.Inventory.getAllProducts;

/** This is the class for the main window or first window shown.
 LOGICAL ERROR To overcome the issue when selecting a part to delete after having searched the object would still display, I made the search term and table reset after deleting an object.
 FUTURE ENHANCEMENT I already added some basic styling to the buttons of this application, I would like to make this project more aesthetically pleasing in the future.*/
public class MainController implements Initializable {
    @FXML
    private TableView partsTable, productsTable;
    @FXML
    private TableColumn partIdCol, partNameCol, partInventoryCol, partPriceCol;
    @FXML
    private TableColumn prodIdCol, prodNameCol, prodInvCol, prodPriceCol;
    public static Part selectedPart;
    public static Product selectedProduct;

    public ObservableList<Part> parts = getAllParts();
    public ObservableList<Product> products = getAllProducts();
    @FXML
    private TextField partSearch, productSearch;

    /** This method sets the tables and columns to our Part and Product models.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsTable.setItems(parts);
        productsTable.setItems(products);

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

//      these two bits of code initialize listeners on the search bars to call the search functions when text is added
        partSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            partSearch(newValue);
        });

        productSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            productSearch(newValue);
        });
    }
    /** Changes the screen to the add part screen */
    public void toAddPart(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/wright/firstproject/add-part.fxml")));
        Stage stage = (Stage)((javafx.scene.Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        scene.getStylesheets().add(MainApplication.class.getResource("bootstrap3.css").toExternalForm());
    }
    /** Changes the screen to the modify part screen */
    public void goToModifyPart(ActionEvent actionEvent) throws IOException{
        try {
            selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/wright/firstproject/modify-part.fxml")));
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            scene.getStylesheets().add(MainApplication.class.getResource("bootstrap3.css").toExternalForm());
        } catch (Exception error){
            Inventory.warnDialog("No Part Selected", "Please select a part to modify");
            System.out.println(error);
        }
    }
    /** gets the part selected in the tableView */
    public static Part getSelectedPart(){
        return selectedPart;
    }
    /** Exits the application on button click */
    public void getOuttaHere() {
        System.exit(0);
    }
    /** changes the screen to the add product screen */
    public void toAddProduct(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/wright/firstproject/add-product.fxml")));
        Stage stage = (Stage)((javafx.scene.Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        scene.getStylesheets().add(MainApplication.class.getResource("bootstrap3.css").toExternalForm());
    }
    /** Changes the screen to the modify product screen */
    public void toModifyProduct(ActionEvent actionEvent) throws IOException{
        try {
            selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/wright/firstproject/modify-product.fxml")));
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            scene.getStylesheets().add(MainApplication.class.getResource("bootstrap3.css").toExternalForm());
        } catch (Exception error){
            Inventory.warnDialog("No Product Selected", "Please select a product to modify");
        }
    }
    /** method to delete a part */
    public void onDeletePart() {
        selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
        if (Inventory.confirmDelete("Confirm?", "Are you sure you want to delete " + selectedPart.getName() + " ?")) {
            Inventory.deletePart(selectedPart);
            partsTable.setItems(parts);
            partSearch.setText("");
        }
    }
    /** method to delete a product */
    public void onDeleteProd() {
        selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
        if (Inventory.confirmDelete("Confirm?", "Are you sure you want to delete " + selectedProduct.getName() +" ?")){
            Inventory.deleteProduct(selectedProduct);
            productsTable.setItems(products);
            productSearch.setText("");
        }
    }
    /** method to search for a product.
     @param searchTerm a string sent to be searched for */
    public void productSearch(String searchTerm) {
        String loweredSearch = searchTerm.toLowerCase();
        productsTable.setItems(Inventory.lookupProduct(loweredSearch));
    }
//    This function takes the listener from the text field and sets the partsTable data
//    to the output of the lookupPart Function
    /** method to search for a part
     @param searchTerm a string to be searched for */
    public void partSearch(String searchTerm) {
        String loweredSearch = searchTerm.toLowerCase();
        partsTable.setItems(Inventory.lookupPart(loweredSearch));
    }

}