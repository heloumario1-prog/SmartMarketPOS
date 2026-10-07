package Models;

public class CashierReport {

    private String cashier;
    private double total;
    private double percentage;

    public CashierReport(String cashier, double total, double percentage) {
        this.cashier = cashier;
        this.total = total;
        this.percentage = percentage;
    }

    public String getCashier() {
        return cashier;
    }

    public double getTotal() {
        return total;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getPercentageFormatted() {
        return String.format("%.2f", percentage);
    }

    public String getTotalFormatted() {
        return String.format("%.2f$", total);
    }
}
