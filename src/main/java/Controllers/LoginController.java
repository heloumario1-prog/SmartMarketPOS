package Controllers;

import Models.User;
import Services.UserService;
import Views.LoginView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import java.sql.SQLException;

public class LoginController {

    private final LoginView view;
    private final UserService userService;

    public LoginController() {
        this.view = new LoginView();
        this.userService = new UserService();
        attachHandlers();
    }

    public LoginController(LoginView view, UserService userService) {
        this.view = view;
        this.userService = userService;
        attachHandlers();
    }

    private void attachHandlers() {
        view.getLoginButton().setOnAction(e -> doLogin());
        view.getQuitButton().setOnAction(e -> Platform.exit());
    }

    private void doLogin() {
        String username = view.getUsernameField().getText().trim();
        String password = view.getPasswordField().getText();

        if (username.isEmpty() || password.isEmpty()) {
            show("Username and password cannot be empty.");
            return;
        }

        try {
            User user = userService.Login(username, password);

            if (user == null) {
                show("Invalid username or password.");
                return;
            }

            openDashboard(user);
            view.close();

        } catch (SQLException ex) {
            show("Error: " + ex.getMessage());
        }
    }
    
    private void openDashboard(User user) throws SQLException {

        String role = user.getRole();
        if (role == null) {
            role = "cashier"; // Fallback
        }

        switch (role.toLowerCase()) {

            case "admin":
                new AdminDashboardController(user, userService);
                break;

            case "derabeltabel":
                new DerabeltabelDashboardController(user, userService);
                break;

            default: // cashier
                new SalesController(user);
                break;
        }
    }
    
    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public void handleLogin() {
        view.show();
    }
}
