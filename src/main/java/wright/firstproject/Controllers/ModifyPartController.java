package wright.firstproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import wright.firstproject.MainApplication;
import wright.firstproject.Models.Part;
import wright.firstproject.Models.InHouse;
import wright.firstproject.Models.OutSourced;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
/** This class manages the modify Part screen.
 * LOGICAL ERROR I could not save a modified part that was changed from an inHouse to an outsourced  instance. I resolved this by deleting the original and saving the new part.
 FUTURE ENHANCEMENT for changing a part from inHouse to Outsourced, I would want to work out some better logic for changing and removing the previous instance of the model. */
public class ModifyPartController implements Initializable {
    public Part selectedPart = MainController.getSelectedPart();
    @FXML
    private Label partManufacturer;
    @FXML
    private RadioButton inHouse, outsourced;
    @FXML
    private TextField partIdField, partNameField, partInvField, partCostField,
            partMaxField, partManufacturerField, partMinField;
    @FXML
    private Button saveButton;

    // if one or any of the checks fail, this will be set to false so that the form is not submitted.
    private boolean formSubmittable = true;

    // function to change window back to main
    @FXML
    private void toMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/wright/firstproject/main.fxml")));
        Stage stage = (Stage)((javafx.scene.Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main");
        stage.setScene(scene);
        scene.getStylesheets().add(MainApplication.class.getResource("bootstrap3.css").toExternalForm());
    }

    /** method to set the machine id label and return a boolean toggle */
    // boolean toggle for parts made in house
    public boolean madeInHouse() {
        if (inHouse.isSelected()){
            partManufacturer.setText("Machine ID");
            return true;
        }
        return false;
    }
    /** method to set the company name label and return a boolean toggle */
    // boolean toggle for outsourced parts
    public boolean madeOutsourced() {
        if (outsourced.isSelected()) {
            partManufacturer.setText("Company Name");
            return true;
        }
        return false;
    }
    /** method to save the modified part */
    public void onSave(ActionEvent actionEvent){
        //quick validation on save button click
        if (partNameField.getText().isEmpty() || partNameField.getText().matches(".*\\d.*")){
            partNameField.setStyle("-fx-border-color: red;");
            formSubmittable = false;
        }
        if (partInvField.getText().length() == 0 || !partInvField.getText().matches(".*\\d.*")){
            partInvField.setStyle("-fx-border-color: red;");
            formSubmittable = false;
        }
        if (partCostField.getText().length() == 0 || !partCostField.getText().matches(".*\\d.*")){
            partCostField.setStyle("-fx-border-color: red;");
            formSubmittable = false;
        }
        if (partMinField.getText().length() == 0 || !partMinField.getText().matches(".*\\d.*")){
            partMinField.setStyle("-fx-border-color: red;");
            formSubmittable = false;
        }
        if (partMaxField.getText().length() == 0 || !partMaxField.getText().matches(".*\\d.*")){
            partMaxField.setStyle("-fx-border-color: red;");
            formSubmittable = false;
        }
        if (partManufacturerField.getText().length() == 0){
            partManufacturerField.setStyle("-fx-border-color: red;");
            formSubmittable = false;
        }
        if (Integer.parseInt(partMinField.getText()) > Integer.parseInt(partMaxField.getText())) {
            Inventory.warnDialog("Part Error", "min cannot be greater than max");
            formSubmittable = false;
        }
        if (Integer.parseInt(partInvField.getText()) > Integer.parseInt(partMaxField.getText()) || Integer.parseInt(partInvField.getText()) < Integer.parseInt(partMinField.getText())) {
            Inventory.warnDialog("Quantity Error", "Inventory must fall within the min and max");
            formSubmittable = false;
        }
        // adding new part fields to a new part and adding them to the parts list
        // Tried several variations of this part of the method, I ran into an issue where if a user changes what kind of part instance
        // this is (InHouse or Outsourced), then when updated the part would be an instance of both.
        // this caused bugs when loading the selected part. In order to fix the easiest and most efficient method,
        // is to delete the part and add a new one with all the original part's data.
        try{
            int addPartId = Integer.parseInt(partIdField.getText());
            String addPartName = partNameField.getText().trim();
            int addPartInv = Integer.parseInt(partInvField.getText().trim());
            double addPartCost = Double.parseDouble(partCostField.getText().trim());
            int addPartMin = Integer.parseInt(partMinField.getText().trim());
            int addPartMax = Integer.parseInt(partMaxField.getText().trim());
            if (madeInHouse()){
                int machineId = Integer.parseInt(partManufacturerField.getText().trim());
                InHouse addPart = new InHouse(addPartId, addPartName, addPartCost, addPartInv, addPartMin, addPartMax, machineId);
                if (formSubmittable) {
                    Inventory.deletePart(selectedPart);
                    Inventory.addPart(addPart);
                    toMain(actionEvent);
                }
            }
            if (madeOutsourced()) {
                String companyName = partManufacturerField.getText().trim();
                OutSourced addPart = new OutSourced(addPartId, addPartName, addPartCost, addPartInv, addPartMin, addPartMax, companyName);
                if (formSubmittable) {
                    Inventory.deletePart(selectedPart);
                    Inventory.addPart(addPart);
                    toMain(actionEvent);
                }
            }
            formSubmittable = true;
        } catch (Exception error){
            Inventory.warnDialog("Error adding part", "Double check your input criteria");
        }

    }
    /** method to initialize the values of the selected part and determine what label to use. */
    // populates data from selected part
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            partIdField.setText(String.valueOf(selectedPart.getId()));
            partNameField.setText(selectedPart.getName());
            partCostField.setText(String.valueOf(selectedPart.getPrice()));
            partInvField.setText(String.valueOf(selectedPart.getStock()));
            partMinField.setText(String.valueOf(selectedPart.getMin()));
            partMaxField.setText(String.valueOf(selectedPart.getMax()));
            if (selectedPart instanceof InHouse) {
                partManufacturerField.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
                inHouse.setSelected(true);
            }
            if (selectedPart instanceof OutSourced) {
                partManufacturerField.setText(((OutSourced) selectedPart).getCompany());
                outsourced.setSelected(true);
            }
    }
}
