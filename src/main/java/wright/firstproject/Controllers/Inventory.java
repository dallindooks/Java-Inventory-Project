package wright.firstproject.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import wright.firstproject.Models.InHouse;
import wright.firstproject.Models.OutSourced;
import wright.firstproject.Models.Part;
import wright.firstproject.Models.Product;

import java.util.Random;

public class Inventory {
    private static ObservableList<Part> parts = FXCollections.observableArrayList();
    private static ObservableList<Product> products = FXCollections.observableArrayList();

    public static int getRandomId(){
        Random rand = new Random();
        int bound = 9999;
        int origin = 1000;
        if (parts.size()-1 >= bound-origin) {
            bound = bound + 1000;
        }

        int randNum = rand.nextInt(origin, bound);
        for (int i = 0; i < parts.size(); i++){
            if (parts.get(i).getId() == randNum) {
                randNum = rand.nextInt(origin, bound);
                i = 0;
            }
        }
        return randNum;
    }

    public static void addSeedData(){
        int rand1 = getRandomId();
        parts.add(new InHouse(rand1, "gear", 22, 9, 1,10, 1234));
        int rand2 = getRandomId();
        parts.add(new InHouse(rand2, "wheel", 123, 8, 1,10, 2121));
        int rand3 = getRandomId();
        parts.add(new OutSourced(rand3, "muffler", 499, 3, 1,10, "Acme"));

        int rand4 = getRandomId();
        products.add( new Product( rand4, "bike", 109.99, 100, 1, 100 ));
        int rand5 = getRandomId();
        products.add( new Product( rand5, "tetris", 500.95, 100, 1, 100 ));
        int rand6 = getRandomId();
        products.add( new Product( rand6, "golf club", 55.99, 100, 1, 100 ));
    }

    public static ObservableList<Part> getParts(){
        return parts;
    }

    public static ObservableList<Product> getProducts(){
        return products;
    }


    public static void addPart(Part newPart){
        parts.add(newPart);
    }
    public static void updatePart(Part modifiedPart, int partId) {parts.set(partId, modifiedPart);}

    public static void warnDialog(String title, String header){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

}
