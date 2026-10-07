/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import Models.Sale;
import Util.DatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author HP
 */
public class SaleService {

    DatabaseConnection conn = new DatabaseConnection();

    public Sale addSale(int cashierId, LocalDateTime date, double totalAmount, double discount, double finalAmount) throws SQLException {
        Sale s = null;
        conn.Connect();
        String sql = "INSERT INTO sales(cashier_id, date, total_amount, discount) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, cashierId);
            ps.setTimestamp(2, Timestamp.valueOf(date));
            ps.setDouble(3, totalAmount);
            ps.setDouble(4, discount);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        s = new Sale(id, cashierId, date, null, totalAmount, discount, finalAmount);
                    }
                }
            } else {
                System.out.println("Error inserting sale!");
            }
        }
        conn.Disconnect();
        return s;
    }

    public void deleteSale(int id, int cashierId) throws SQLException {
        conn.Connect();
        String sql = "DELETE FROM sales WHERE sale_id=? AND cashier_id=?";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, cashierId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Deleted Successfully!!!");
            } else {
                System.out.println("Error!!!");
            }
        }
        conn.Disconnect();
    }

    public void editSale(int id, int cashierId, LocalDateTime date, double totalAmount, int discount, double finalAmount) throws SQLException {
        conn.Connect();
        String sql = "UPDATE sales SET cashier_id=?, date=?, total_amount=?, discount=? WHERE sale_id=?";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setInt(1, cashierId);
            ps.setTimestamp(2, Timestamp.valueOf(date));
            ps.setDouble(3, totalAmount);
            ps.setInt(4, discount);
            ps.setInt(5, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Updated Successfully!!!");
            } else {
                System.out.println("Error!!!");
            }
        }
        conn.Disconnect();
    }
}
