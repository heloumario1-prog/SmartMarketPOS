package Models;

public class Currency {

    private String currencyCode;   // "USD", "EUR", "LBP"
    private String currencyName;
    private double rateToUsd;

    public Currency(String currencyCode, String currencyName, double rateToUsd) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.rateToUsd = rateToUsd;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public double getRateToUsd() {
        return rateToUsd;
    }

    public void setRateToUsd(double rateToUsd) {
        this.rateToUsd = rateToUsd;
    }

    @Override
    public String toString() {
        return currencyCode + " (" + rateToUsd + ")";
    }
}
