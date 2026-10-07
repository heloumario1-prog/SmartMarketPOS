package Controllers;

import Models.User;
import Services.UserService;
import Views.UserView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.List;

public class UserController {

    private UserView view;
    private UserService userService;
    private AdminDashboardController adminController;

    public UserController(AdminDashboardController adminController) {
        this.adminController = adminController;
        this.userService = new UserService();
        this.view = new UserView();

        attachHandlers();
        loadUsers();

        // ✅ Insert UI into AdminDashboard center
        adminController.getView().setContent(view.getRoot());
    }

    // ✅ Optional: testing constructor
    public UserController(UserView view, UserService service) {
        this.view = view;
        this.userService = service;

        attachHandlers();
        loadUsers();
    }

    private void attachHandlers() {
        view.getAddBtn().setOnAction(e -> addCashier());
        view.getEditBtn().setOnAction(e -> editCashier());
        view.getDeleteBtn().setOnAction(e -> deleteCashier());
        view.getTable().setOnMouseClicked(e -> loadSelectedCashier());
        view.getClearBtn().setOnAction(e -> clearForm());
    }

    private void loadUsers() {
        try {
            List<User> list = userService.getAllUsers();
            ObservableList<User> items = FXCollections.observableArrayList(list);
            view.getTable().setItems(items);
        } catch (Exception ex) {
            show("Error loading cashiers: " + ex.getMessage());
        }
    }

    private void loadSelectedCashier() {
        User u = view.getTable().getSelectionModel().getSelectedItem();
        if (u == null) {
            return;
        }

        view.getUsernameField().setText(u.getUsername());
        view.getPasswordField().setText(u.getPassword());
    }

    private void addCashier() {
        String username = view.getUsernameField().getText().trim();
        String password = view.getPasswordField().getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            show("All fields are required.");
            return;
        }

        try {
            userService.addUser(username, password,"cashier");
            clearForm();
            loadUsers();
        } catch (Exception ex) {
            show("Error adding cashier: " + ex.getMessage());
        }
    }

    private void editCashier() {
        User u = view.getTable().getSelectionModel().getSelectedItem();
        if (u == null) {
            show("Select a cashier to edit.");
            return;
        }

        String username = view.getUsernameField().getText().trim();
        String password = view.getPasswordField().getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            show("All fields are required.");
            return;
        }

        try {
            userService.editUser(u.getUserId(), username, password);
            clearForm();
            loadUsers();
        } catch (Exception ex) {
            show("Error editing cashier: " + ex.getMessage());
        }
    }

    private void deleteCashier() {
        User u = view.getTable().getSelectionModel().getSelectedItem();
        if (u == null) {
            show("Select a cashier to delete.");
            return;
        }

        try {
            userService.deleteUser(u.getUserId());
            clearForm();
            loadUsers();
        } catch (Exception ex) {
            show("Error deleting cashier: " + ex.getMessage());
        }
    }

    private void clearForm() {
        view.getUsernameField().clear();
        view.getPasswordField().clear();
    }

    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

}
