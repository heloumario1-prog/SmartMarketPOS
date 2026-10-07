/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author HP
 */
public class User {

    private int userId;
    private String username;
    private String password;
    private String role;
    private String voidCode;

    public User(int userId, String username, String password, String role,String voidCode) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.voidCode=voidCode;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return username;
    }

    public String getVoidCode() {
        return voidCode;
    }

    public void setVoidCode(String code) {
        this.voidCode = code;
    }

}
