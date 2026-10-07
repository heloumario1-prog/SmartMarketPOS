/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author HP
 */
public class SaleItem {

    private int saleItemId;
    private int sale_id;
    private int product_id;
    private int quantity;
    private double price;
    private double subtotal;
    private String productName;

    public SaleItem(int saleItemId, int sale_id, int product_id, int quantity, double price, double subtotal,String productName) {
        this.saleItemId = saleItemId;
        this.sale_id = sale_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
        this.productName=productName;
    }

    @Override
    public String toString() {
        return "SaleItem{" + "saleItemId=" + saleItemId + ", sale_id=" + sale_id + ", product_id=" + product_id + ", quantity=" + quantity + ", price=" + price + ", subtotal=" + subtotal + '}';
    }

    public void setSaleItemId(int saleItemId) {
        this.saleItemId = saleItemId;
    }

    public void setSale_id(int sale_id) {
        this.sale_id = sale_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getSaleItemId() {
        return saleItemId;
    }

    public int getSale_id() {
        return sale_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String name) {
        this.productName = name;
    }
}
