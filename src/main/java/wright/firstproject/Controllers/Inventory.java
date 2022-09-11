package wright.firstproject.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import wright.firstproject.Models.InHouse;
import wright.firstproject.Models.OutSourced;
import wright.firstproject.Models.Part;
import wright.firstproject.Models.Product;

import java.io.IOException;
import java.util.Random;

public class Inventory {
    private static ObservableList<Part> parts = FXCollections.observableArrayList();
    private static ObservableList<Product> products = FXCollections.observableArrayList();
    //returns a unique random part Id
    public static int getRandomPartId(){
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
    //returns a unique random product Id
    public static int getRandomProductId(){
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
        int rand1 = getRandomPartId();
        parts.add(new InHouse(rand1, "gear", 22, 9, 1,10, 1234));
        int rand2 = getRandomPartId();
        parts.add(new InHouse(rand2, "wheel", 123, 8, 1,10, 2121));
        int rand3 = getRandomPartId();
        parts.add(new OutSourced(rand3, "muffler", 499, 3, 1,10, "Acme"));

        ObservableList<Part> seedParts = FXCollections.observableArrayList();
        seedParts.add((Part.getPartById(rand2)));
        int rand4 = getRandomPartId();
        products.add( new Product( rand4, "bike", 109.99, 100, 1, 100, seedParts ));
        int rand5 = getRandomPartId();
        products.add( new Product( rand5, "tetris", 500.95, 100, 1, 100, FXCollections.observableArrayList()));
        int rand6 = getRandomPartId();
        products.add( new Product( rand6, "golf club", 55.99, 100, 1, 100, FXCollections.observableArrayList() ));
    }

    public static ObservableList<Part> getAllParts(){
        return parts;
    }

    public static ObservableList<Product> getAllProducts(){
        return products;
    }


    public static void addPart(Part newPart){
        parts.add(newPart);
    }
    public static void addProduct(Product newProduct) {
        products.add(newProduct);
    }
//    for the update methods I could not use ".set" because my part and product Ids have a minimum of four digits.
//    When using the ".set" method I get an  out-of-bounds exception so the setting must be done manually.
    public static void updatePart(Part modifiedPart, int machineId, String company) throws IOException {
        int id = modifiedPart.getId();
        try{
            Part.getPartById(id).setName(modifiedPart.getName());
            Part.getPartById(id).setStock(modifiedPart.getStock());
            Part.getPartById(id).setPrice(modifiedPart.getPrice());
            Part.getPartById(id).setMin(modifiedPart.getMin());
            Part.getPartById(id).setMax(modifiedPart.getMax());
            if (Part.getPartById(id) instanceof InHouse){
                ((InHouse) Part.getPartById(id)).setMachineId(machineId);
            }
            if (Part.getPartById(id) instanceof OutSourced){
                ((OutSourced) Part.getPartById(id)).setCompany(company);
            }
        }
        catch (Exception error){
            System.out.println(error);
        }
    }
    public static void updateProduct(Product modifiedProd) throws IOException {
        int id = modifiedProd.getId();
        try{
            Product.getProductById(id).setName(modifiedProd.getName());
            Product.getProductById(id).setStock(modifiedProd.getStock());
            Product.getProductById(id).setPrice(modifiedProd.getPrice());
            Product.getProductById(id).setMin(modifiedProd.getMin());
            Product.getProductById(id).setMax(modifiedProd.getMax());
            Product.getProductById(id).setAssociatedParts(modifiedProd.getAssociatedParts());
        }
        catch (Exception error){
            System.out.println(error);
        }
    }

    public static boolean deletePart(Part selectedPart){
        return parts.remove(selectedPart);
    }

    public static boolean deleteProduct(Product selectedProd){
        return products.remove(selectedProd);
    }

    public static Part lookupPart(int id){
        for (Part part: parts){
            if (part.getId() == id){
                return Part.getPartById(id);
            }
        } return null;
    }

//    logic for the search bar on the Main screen, looks up a part and returns a list that match the search term.
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList resultParts = FXCollections.observableArrayList();
        for (Part part: parts){
            String loweredName = part.getName().toLowerCase();
            if (loweredName.contains(partName) || String.valueOf(part.getId()).contains(partName)){
                resultParts.add(part);
            }
        } return resultParts;
    }

    public static Product lookupProduct(int id){
        for (Product product: products){
            if (product.getId() == id){
                return Product.getProductById(id);
            }
        } return null;
    }
//  logic for the search bar on the Main screen, looks up a product and returns a list that match the search term.
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList resultProducts = FXCollections.observableArrayList();
        for (Product product: products){
            String loweredName = product.getName().toLowerCase();
            if (loweredName.contains(productName) || String.valueOf(product.getId()).contains(productName)){
                resultProducts.add(product);
            }
        } return resultProducts;
    }
//  Warning used in various places to alert a user when an error is caught
    public static void warnDialog(String title, String header){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

}
