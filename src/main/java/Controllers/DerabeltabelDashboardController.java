package Controllers;

import Models.User;
import Services.UserService;
import Views.DerabeltabelDashboardView;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import java.util.UUID;

public class DerabeltabelDashboardController {

    private DerabeltabelDashboardView view;
    private UserService service;

    // This constructor is used from Admin Dashboard
    public DerabeltabelDashboardController(AdminDashboardController admin) {
        this.view = new DerabeltabelDashboardView();
        this.service = new UserService();

        admin.getView().setContent(view.getRoot());
        attachHandlers();
        loadUsers();
    }

    // This constructor is used when Derabeltabel logs in directly
    public DerabeltabelDashboardController(User user, UserService service) {
        this.view = new DerabeltabelDashboardView();
        this.service = service;

        attachHandlers();
        loadUsers();
        view.show();
    }

    private void attachHandlers() {
        view.getAddAdminBtn().setOnAction(e -> addAdmin());
        view.getPromoteBtn().setOnAction(e -> promote());
        view.getDemoteBtn().setOnAction(e -> demote());
        view.getRegenVoidBtn().setOnAction(e -> regenVoid());
        view.getDeleteBtn().setOnAction(e -> deleteUser());
        view.getLogoutBtn().setOnAction(e -> logout());
    }

    private void loadUsers() {
        try {
            view.getTable().setItems(FXCollections.observableArrayList(service.getAllUsers()));
        } catch (Exception ex) {
            show(ex.getMessage());
        }
    }

    // -------------------------------
    // ADD ADMIN (manual)
    // -------------------------------
    private void addAdmin() {
        String user = view.getAdminUserField().getText().trim();
        String pass = view.getAdminPassField().getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            show("Username & Password required.");
            return;
        }

        try {
            String voidCode = generateVoidCode();
            service.addUser(user, pass, "admin");

            view.getAdminUserField().clear();
            view.getAdminPassField().clear();

            loadUsers();
            show("Admin created.\nVOID Code: " + voidCode);

        } catch (Exception ex) {
            show(ex.getMessage());
        }
    }

    // -------------------------------
    // PROMOTE
    // -------------------------------
    private void promote() {
        User u = selected();
        if (u == null) {
            return;
        }

        if (u.getRole().equalsIgnoreCase("admin")) {
            show("⚠ This user is already an ADMIN.");
            return;
        }

        try {
            String voidCode = generateVoidCode();
            service.promoteToAdmin(u.getUserId(), voidCode);

            loadUsers();
            show("User promoted to ADMIN.\nVOID Code: " + voidCode);

        } catch (Exception ex) {
            show(ex.getMessage());
        }
    }

    // -------------------------------
    // DEMOTE
    // -------------------------------
    private void demote() {
        User u = selected();
        if (u == null) {
            return;
        }

        if (u.getRole().equalsIgnoreCase("cashier")) {
            show("⚠ This user is already a CASHIER.");
            return;
        }

        try {
            service.demoteToCashier(u.getUserId());
            loadUsers();
            show("User demoted to CASHIER.");

        } catch (Exception ex) {
            show(ex.getMessage());
        }
    }

    // -------------------------------
    // REGENERATE VOID CODE
    // -------------------------------
    private void regenVoid() {
        User u = selected();
        if (u == null) {
            return;
        }

        if (!u.getRole().equalsIgnoreCase("admin")) {
            show("⚠ VOID codes belong only to ADMINS.");
            return;
        }

        try {
            String code = generateVoidCode();
            service.updateVoidCode(u.getUserId(), code);

            loadUsers();
            show("New VOID Code:\n" + code);

        } catch (Exception ex) {
            show(ex.getMessage());
        }
    }

    // -------------------------------
    // DELETE
    // -------------------------------
    private void deleteUser() {
        User u = selected();
        if (u == null) {
            return;
        }

        try {
            service.deleteUser(u.getUserId());
            loadUsers();
            show("User deleted.");

        } catch (Exception ex) {
            show(ex.getMessage());
        }
    }

    // -------------------------------
    // LOGOUT
    // -------------------------------
    private void logout() {
        view.close();
        new LoginController().handleLogin();
    }

    private User selected() {
        User u = view.getTable().getSelectionModel().getSelectedItem();
        if (u == null) {
            show("Select a user first.");
        }
        return u;
    }

    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private String generateVoidCode() {
        return "ADM-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }
}
