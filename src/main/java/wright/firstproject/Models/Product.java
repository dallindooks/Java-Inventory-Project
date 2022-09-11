package wright.firstproject.Models;

import javafx.collections.ObservableList;
import wright.firstproject.Controllers.Inventory;

public class Product {

    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private ObservableList<Part> associatedParts;

    public Product(int id, String name, double price, int stock, int min, int max, ObservableList<Part> associatedParts){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.associatedParts = associatedParts;
    }

    public static Product getProductById(int id) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == id) return allProducts.get(i);
        } return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
    public void setAssociatedParts(ObservableList<Part> parts) { this.associatedParts = parts; }
    public ObservableList<Part> getAssociatedParts(){
        return associatedParts;
    }
    public void addAssociatedPart(Part newPart){
        associatedParts.add(newPart);
    }
}
