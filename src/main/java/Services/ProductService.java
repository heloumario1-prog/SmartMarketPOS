package Services;

import Models.Product;
import Util.DatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    DatabaseConnection conn = new DatabaseConnection();

    public Product addProduct(String name, int category, Double price, int stock, String barcode) throws SQLException {
        Product p = null;
        conn.Connect();
        String sql = "INSERT INTO products(name, category_id, price, stock, barcode) VALUES(?,?,?,?,?)";

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setInt(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setString(5, barcode);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        p = new Product(id, name, category, price, stock, barcode);
                    }
                }
            }
        }
        conn.Disconnect();
        return p;
    }

    public void deleteProduct(int id) throws SQLException {
        conn.Connect();
        String sql = "DELETE FROM products WHERE product_id=?";

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        conn.Disconnect();
    }

    public void editProduct(int id, String name, int category, Double price, int stock, String barcode) throws SQLException {
        conn.Connect();
        String sql = "UPDATE products SET name=?, category_id=?, price=?, stock=?, barcode=? WHERE product_id=?";

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setString(5, barcode);
            ps.setInt(6, id);
            ps.executeUpdate();
        }
        conn.Disconnect();
    }

    public List<Product> getAllProducts() throws SQLException {
        conn.Connect();
        List<Product> list = new ArrayList<>();

        String sql = """
            SELECT p.*, c.name AS categoryName
            FROM products p
            JOIN categories c ON p.category_id = c.category_id
            """;

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getInt("category_id"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("barcode")
                );
                p.setCategoryName(rs.getString("categoryName"));
                list.add(p);
            }
        }
        conn.Disconnect();
        return list;
    }

    public void reduceStock(int productId, int quantity) throws SQLException {
        conn.Connect();
        String sql = "UPDATE products SET stock = stock - ? WHERE product_id = ? AND stock >= ?";

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
        conn.Disconnect();
    }
}
