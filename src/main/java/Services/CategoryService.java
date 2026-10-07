package Services;

import Models.Category;
import Util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    private final DatabaseConnection conn = new DatabaseConnection();

    // Get all category NAMES (for ComboBox, etc.)
    public List<String> getAllCategories() throws SQLException {
        List<String> list = new ArrayList<>();
        conn.Connect();

        String sql = "SELECT name FROM categories ORDER BY name";
        Statement st = conn.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            list.add(rs.getString("name"));
        }

        rs.close();
        st.close();
        conn.Disconnect();
        return list;
    }

    // Get full objects (for Category table)
    public List<Models.Category> getAllCategoryObjects() throws SQLException {
        List<Models.Category> list = new ArrayList<>();
        conn.Connect();

        String sql = "SELECT category_id, name FROM categories ORDER BY category_id";
        Statement st = conn.getConnection().createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            Models.Category c = new Models.Category(
                    rs.getInt("category_id"),
                    rs.getString("name")
            );
            list.add(c);
        }

        rs.close();
        st.close();
        conn.Disconnect();
        return list;
    }

    // Get category NAME by ID (int -> String)
    public String getCategoryNameById(int id) throws SQLException {
        conn.Connect();
        String sql = "SELECT name FROM categories WHERE category_id=?";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        }
        conn.Disconnect();
        return null;
    }

    // Get category ID by NAME (String -> int)
    public int getCategoryIdByName(String name) throws SQLException {
        conn.Connect();
        String sql = "SELECT category_id FROM categories WHERE name=?";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("category_id");
            }
        }
        conn.Disconnect();
        throw new SQLException("Category not found: " + name);
    }

    // Add new category
    public Category addCategory(String name) throws SQLException {
        conn.Connect();
        String sql = "INSERT INTO categories(name) VALUES(?)";
        Category c = null;

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    c = new Models.Category(id, name);
                }
                rs.close();
            }
        }
        conn.Disconnect();
        return c;
    }

    // Edit category
    public void editCategory(int id, String name) throws SQLException {
        conn.Connect();
        String sql = "UPDATE categories SET name=? WHERE category_id=?";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
        conn.Disconnect();
    }

    // Delete category
    public void deleteCategory(int id) throws SQLException {
        conn.Connect();
        String sql = "DELETE FROM categories WHERE category_id=?";
        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        conn.Disconnect();
    }
}
