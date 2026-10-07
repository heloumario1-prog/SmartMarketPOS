/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author HP
 */
public class Sale {

    private int saleId;
    private int cashierId;
    private LocalDateTime date;
    private List<SaleItem> items;
    private double totalAmount;
    private double discount;
    private double finalAmount;

    public Sale(int saleId, int cashierId, LocalDateTime date, List<SaleItem> items, double totalAmount,double discount, double finalAmount) {
        this.saleId = saleId;
        this.cashierId = cashierId;
        this.date = date;
        this.items = items;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.finalAmount = finalAmount;
    }

    @Override
    public String toString() {
        return "Sale{" + "saleId=" + saleId + ", cashierId=" + cashierId + ", date=" + getFormattedDate() + ", items=" + items + ", totalAmount=" + totalAmount + ", discount=" + discount + ", finalAmount=" + finalAmount + '}';
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public void setCashierId(int cashierId) {
        this.cashierId = cashierId;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setFinalAmount(double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public int getSaleId() {
        return saleId;
    }

    public int getCashierId() {
        return cashierId;
    }

    public LocalDateTime getDate() {
        return date;
    }
  
    public String getFormattedDate() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return date.format(formatter);
}

    public List<SaleItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public double getFinalAmount() {
        return finalAmount;
    }

}
