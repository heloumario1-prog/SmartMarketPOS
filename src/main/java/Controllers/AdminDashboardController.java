package Controllers;

import Models.User;
import Services.UserService;
import Views.*;

public class AdminDashboardController {

    private AdminDashboardView view;
    private UserService userService;
    private User currentAdmin;

    public AdminDashboardController(User adminUser, UserService userService) {
        this.currentAdmin = adminUser;
        this.userService = userService;

        this.view = new AdminDashboardView(); // ✅ creates its own Stage

        attachHandlers();
        view.show(); // ✅ open admin window
    }

    private void attachHandlers() {

        // ✅ Switch center content, NO new Stage
        view.getManageProductsBtn().setOnAction(ev -> openProducts());
        view.getManageCategoriesBtn().setOnAction(ev -> openCategories());
        view.getManageUsersBtn().setOnAction(ev -> openUsers());
        view.getManageCurrenciesBtn().setOnAction(ev -> openCurrencies());
        view.getReportsBtn().setOnAction(ev -> openReports());

        // ✅ Logout returns to login screen
        view.getLogoutBtn().setOnAction(ev -> logout());
    }

    private void openProducts() {
        new ProductController(this);
    }

    private void openCategories() {
        new CategoryController(this);
    }

    private void openUsers() {
        new UserController(this);
    }

    private void openCurrencies() {
        new CurrencySettingsController(this);
    }

    private void openReports() {
        new ReportController(this);
    }

    private void logout() {
        // ✅ close admin window and open login
        view.close();
        new LoginController().handleLogin();
    }

    // ✅ allow sub-views to return to dashboard content if needed
    public void returnToDashboard() {
        view.setContent(null);
    }

    public AdminDashboardView getView() {
        return view;
    }

    public User getCurrentAdmin() {
        return currentAdmin;
    }
}
