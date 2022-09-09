module wright.firstproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens wright.firstproject to javafx.fxml;
    exports wright.firstproject;
}