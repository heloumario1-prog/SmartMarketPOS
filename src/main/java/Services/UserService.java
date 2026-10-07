package Services;

import Util.DatabaseConnection;
import Models.User;
import static Util.VoidCodeGenerator.generateVoidCode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final DatabaseConnection conn = new DatabaseConnection();

    public User Login(String username, String password) throws SQLException {
        conn.Connect();
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        User u = null;

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                u = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("void_code")
                );
            }
        }

        conn.Disconnect();
        return u;
    }

    public User addUser(String username, String password, String role) throws SQLException {
        conn.Connect();
        User created = null;
        String voidCode = role.equalsIgnoreCase("admin") ? generateVoidCode() : null;
        String sql = "INSERT INTO users(username, password, role, void_code) VALUES(?, ?, ?, ?)";

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role.toLowerCase());
            ps.setString(4, voidCode);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    created = new User(
                            rs.getInt(1),
                            username,
                            password,
                            role.toLowerCase(),
                            voidCode
                    );
                }
            }
        }

        conn.Disconnect();
        return created;
    }

    public void deleteUser(int userId) throws SQLException {
        conn.Connect();
        String sql = "DELETE FROM users WHERE user_id=?";

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }

        conn.Disconnect();
    }

    public void editUser(int userId, String username, String password) throws SQLException {
        conn.Connect();
        String sql = "UPDATE users SET username=?, password=? WHERE user_id=?";

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setInt(3, userId);
            ps.executeUpdate();
        }

        conn.Disconnect();
    }

    public List<User> getAllUsers() throws SQLException {
        conn.Connect();
        List<User> list = new ArrayList<>();

        String sql = "SELECT * FROM users WHERE role <> 'DERABELTABEL'";

        try (Statement st = conn.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("void_code")
                ));
            }
        }

        conn.Disconnect();
        return list;
    }

    public List<User> getAdmins() throws SQLException {
        conn.Connect();
        List<User> admins = new ArrayList<>();

        String sql = "SELECT * FROM users WHERE role='admin'";

        try (Statement st = conn.getConnection().createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                admins.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("void_code")
                ));
            }
        }

        conn.Disconnect();
        return admins;
    }

    public void promoteToAdmin(int userId, String voidCode) throws SQLException {
        conn.Connect();
        String sql = "UPDATE users SET role='admin', void_code=? WHERE user_id=?";

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, voidCode);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }

        conn.Disconnect();
    }

    public void demoteToCashier(int userId) throws SQLException {
        conn.Connect();
        String sql = "UPDATE users SET role='cashier', void_code=NULL WHERE user_id=?";

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }

        conn.Disconnect();
    }

    public void updateVoidCode(int userId, String voidCode) throws SQLException {
        conn.Connect();
        String sql = "UPDATE users SET void_code=? WHERE user_id=?";

        try (PreparedStatement ps = conn.getConnection().prepareStatement(sql)) {
            ps.setString(1, voidCode);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }

        conn.Disconnect();
    }
}
