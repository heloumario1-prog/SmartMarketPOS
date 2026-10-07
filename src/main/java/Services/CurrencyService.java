package Services;

import Models.Currency;
import Util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles CRUD + rate lookup for currencies.
 *
 * currencies table (SQL): currency_code VARCHAR(3) PK currency_name VARCHAR(50)
 * rate_to_usd DECIMAL(18,6)
 */
public class CurrencyService {

    private final DatabaseConnection conn = new DatabaseConnection();

    // -------------------------------------------------------
    //   1. GET RATE
    // -------------------------------------------------------
    public double getRatePerUsd(String currencyCode) throws SQLException {
        conn.Connect();
        String sql = "SELECT rate_to_usd FROM currencies WHERE currency_code = ?";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, currencyCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("rate_to_usd");
                }
            }
        } finally {
            conn.Disconnect();
        }
        throw new SQLException("Currency not found: " + currencyCode);
    }

    // -------------------------------------------------------
    //   2. UPSERT
    // -------------------------------------------------------
    public void upsertCurrency(String code, String name, double ratePerUsd) throws SQLException {
        conn.Connect();
        String sql = """
                INSERT INTO currencies(currency_code, currency_name, rate_to_usd)
                VALUES (?,?,?)
                ON DUPLICATE KEY UPDATE
                    currency_name = VALUES(currency_name),
                    rate_to_usd = VALUES(rate_to_usd)
                """;
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, code);
            ps.setString(2, name);
            ps.setDouble(3, ratePerUsd);
            ps.executeUpdate();
        } finally {
            conn.Disconnect();
        }
    }

    // -------------------------------------------------------
    //   3. GET ALL CURRENCIES
    // -------------------------------------------------------
    public List<Currency> getAllCurrencies() throws SQLException {
        List<Currency> list = new ArrayList<>();
        conn.Connect();

        String sql = "SELECT currency_code, currency_name, rate_to_usd FROM currencies";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Currency(
                        rs.getString("currency_code"),
                        rs.getString("currency_name"),
                        rs.getDouble("rate_to_usd")
                ));
            }
        }

        conn.Disconnect();
        return list;
    }

    // -------------------------------------------------------
    //   4. ADD CURRENCY
    // -------------------------------------------------------
    public void addCurrency(String code, String name, double ratePerUsd) throws SQLException {
        conn.Connect();
        String sql = "INSERT INTO currencies(currency_code, currency_name, rate_to_usd) VALUES(?,?,?)";

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, code);
            ps.setString(2, name);
            ps.setDouble(3, ratePerUsd);
            ps.executeUpdate();
        }

        conn.Disconnect();
    }

    // -------------------------------------------------------
    //   5. EDIT CURRENCY
    // -------------------------------------------------------
    public void editCurrency(String code, String newName, double newRate) throws SQLException {
        conn.Connect();
        String sql = "UPDATE currencies SET currency_name=?, rate_to_usd=? WHERE currency_code=?";

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setDouble(2, newRate);
            ps.setString(3, code);
            ps.executeUpdate();
        }

        conn.Disconnect();
    }

    // -------------------------------------------------------
    //   6. DELETE CURRENCY
    // -------------------------------------------------------
    public void deleteCurrency(String code) throws SQLException {
        conn.Connect();

        String sql = "DELETE FROM currencies WHERE currency_code=?";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        }

        conn.Disconnect();
    }
}
