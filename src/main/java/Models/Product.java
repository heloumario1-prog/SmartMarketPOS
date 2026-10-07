/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author HP
 */
public class Product {

    private int productId;
    private String name;
    private int category;
    private Double price;
    private int stock;
    private String barcode;
    private String categoryName;


    @Override
    public String toString() {
        return "Product{" + "productId=" + productId + ", name=" + name + ", category=" + category + ", price=" + price + ", stock=" + stock + ", barcode=" + barcode + '}';
    }

    public Product(int productId, String name, int category, Double price, int stock, String barcode) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.barcode = barcode;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getCategory() {
        return category;
    }

    public Double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
