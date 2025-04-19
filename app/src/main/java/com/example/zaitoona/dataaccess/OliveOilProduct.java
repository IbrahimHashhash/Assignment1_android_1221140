package com.example.zaitoona.dataaccess;

// Olive oil product class. this is the class we use as our main prodcut
public class OliveOilProduct {
    private String name;
    private String type; //Extra Virgin, Pure, Pomance.
    private double price;
    private int quantity; // available stock
    private int imageResId; // resource ID for product image
    private boolean inStock;
    private int productId;

    public double getLiters() {
        return liters;
    }

    public void setLiters(double liters) {
        this.liters = liters;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    private double liters;

    // Constructor
    public OliveOilProduct(int productId,String name, String type, double price, int quantity, int imageResId,double liters) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.imageResId = imageResId;
        this.productId=productId;
        this.liters=liters;
        updateInStock();
    }

    // Getters
    public String getName() { return name; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getImageResId() { return imageResId; }
    public boolean isInStock() { return inStock; }

    // Setters
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateInStock();
    }

    private void updateInStock() {
        this.inStock = this.quantity > 0;
    }
}
