package Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportRow {

    private int saleId;
    private LocalDateTime date;
    private double total;
    private String cashier;

    public ReportRow(int saleId, LocalDateTime date, double total, String cashier) {
        this.saleId = saleId;
        this.date = date;
        this.total = total;
        this.cashier = cashier;
    }

    public int getSaleId() {
        return saleId;
    }

    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public double getTotal() {
        return total;
    }

    public String getCashier() {
        return cashier;
    }
}
