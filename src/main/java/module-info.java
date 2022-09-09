module wright.firstproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens wright.firstproject to javafx.fxml;
    opens wright.firstproject.Models;
    exports wright.firstproject;
    exports wright.firstproject.Controllers;
    opens wright.firstproject.Controllers to javafx.fxml;
}