/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import Models.SaleItem;
import Util.DatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HP
 */
public class SaleItemService {

    DatabaseConnection conn = new DatabaseConnection();

    public SaleItem addSaleItem(int saleId, int productId, int quantity, double price, double subtotal,String name) throws SQLException {
        SaleItem si = null;
        conn.Connect();
        String sql = "INSERT INTO sale_items(sale_id, product_id, quantity, price, subtotal,Product_Name) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, saleId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            ps.setDouble(5, subtotal);
            ps.setString(6, name);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        si = new SaleItem(id, saleId, productId, quantity, price, subtotal,name);
                    }
                }
            } else {
                System.out.println("Error inserting sale item!");
            }
        }
        conn.Disconnect();
        return si;
    }

    public void deleteSaleItem(int id, int saleId) throws SQLException {
        conn.Connect();
        String sql = "DELETE FROM sale_items WHERE sale_item_id=? AND sale_id=?";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, saleId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Deleted Successfully!!!");
            } else {
                System.out.println("Error!!!");
            }
        }
        conn.Disconnect();
    }

    public void editSaleItem(int id, int saleId, int productId, int quantity, double price, double subtotal,String name) throws SQLException {
        conn.Connect();
        String sql = "UPDATE sale_items SET sale_id=?, product_id=?, quantity=?, price=?, subtotal=?,product_Name=? WHERE sale_item_id=?";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setInt(1, saleId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            ps.setDouble(5, subtotal);
            ps.setString(6, name);
            ps.setInt(7, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Updated Successfully!!!");
            } else {
                System.out.println("Error!!!");
            }
        }
        conn.Disconnect();
    }

    public List<SaleItem> getSaleItems(int saleId) throws SQLException {
        List<SaleItem> items = new ArrayList<>();
        conn.Connect();
        String sql = "SELECT * FROM sale_items WHERE sale_id = ? GROUP BY ";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setInt(1, saleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SaleItem item = new SaleItem(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getInt(3),
                            rs.getInt(4),
                            rs.getDouble(5),
                            rs.getDouble(6),
                            rs.getString(7)
                    );
                    items.add(item);
                }
                rs.close();
            }
            ps.close();
        }
        conn.Disconnect();
        return items;
    }
}