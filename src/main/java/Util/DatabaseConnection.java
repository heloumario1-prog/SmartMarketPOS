

package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author HP
 */
public class DatabaseConnection {

String url = "jdbc:mysql://localhost:3306/smartmarketpos?useSSL=false";
String user = System.getenv("DB_USER");
String password = System.getenv("DB_PASSWORD");

    private Connection conn;

    public void Connect() throws SQLException {
        conn = DriverManager.getConnection(url, user, password);
        conn.setAutoCommit(true);
        System.out.println("Connected Succesfully");
    }

    public void Disconnect() throws SQLException {
        conn.close();
    }

    public Connection getConnection() {
        return conn;
    }
}
