package Views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AdminDashboardView extends Stage {

    private BorderPane root;
    private Scene scene;

    private Button manageProductsBtn;
    private Button manageCategoriesBtn;
    private Button manageUsersBtn;
    private Button manageCurrenciesBtn;
    private Button reportsBtn;
    private Button logoutBtn;

    public AdminDashboardView() {
        buildUI();
    }

    private void buildUI() {
        root = new BorderPane();

        // ✅ Top menu bar
        HBox menu = new HBox(
                manageProductsBtn = blue("🛒 Products"),
                manageCategoriesBtn = blue("📂 Categories"),
                manageUsersBtn = blue("👥 Cashiers"),
                manageCurrenciesBtn = blue("💱 Currency"),
                reportsBtn = blue("📊 Reports"),
                logoutBtn = red("🚪 Logout")
        );

        menu.setSpacing(10);
        menu.setPadding(new Insets(10));
        menu.setAlignment(Pos.CENTER_LEFT);
        menu.setStyle("-fx-background-color: #1e88e5;");

        root.setTop(menu);

        // ✅ Default center placeholder
        Label placeholder = new Label("Select an option from the menu");
        placeholder.setStyle("-fx-font-size: 28px; -fx-text-fill: #555;");
        BorderPane.setAlignment(placeholder, Pos.CENTER);
        root.setCenter(placeholder);

        scene = new Scene(root);
        setScene(scene);
        setTitle("SmartMarket POS - Admin Dashboard");

        // ✅ Full screen (maximized window)
        setMaximized(true);
    }

    // ✅ Swap center content
    public void setContent(Node node) {
        if (node == null) {
            Label placeholder = new Label("Select an option from the menu");
            placeholder.setStyle("-fx-font-size: 28px; -fx-text-fill: #555;");
            BorderPane.setAlignment(placeholder, Pos.CENTER);
            root.setCenter(placeholder);
        } else {
            root.setCenter(node);
        }
    }

    // ✅ Blue button with border
    private Button blue(String text) {
        Button b = new Button(text);
        b.setStyle("""
            -fx-background-color: #1976d2;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10 20;
            -fx-background-radius: 10;
            -fx-border-color: #115293;
            -fx-border-width: 1;
            -fx-border-radius: 10;
            -fx-cursor: hand;
        """);

        b.setOnMouseEntered(e -> b.setStyle("""
            -fx-background-color: #1565c0;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10 20;
            -fx-background-radius: 10;
            -fx-border-color: #0d3c86;
            -fx-border-width: 1;
            -fx-border-radius: 10;
            -fx-cursor: hand;
        """));

        b.setOnMouseExited(e -> b.setStyle("""
            -fx-background-color: #1976d2;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10 20;
            -fx-background-radius: 10;
            -fx-border-color: #115293;
            -fx-border-width: 1;
            -fx-border-radius: 10;
            -fx-cursor: hand;
        """));

        return b;
    }

    // ✅ Red button with border (Logout)
    private Button red(String text) {
        Button b = new Button(text);
        b.setStyle("""
            -fx-background-color: #d32f2f;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10 20;
            -fx-background-radius: 10;
            -fx-border-color: #9a0007;
            -fx-border-width: 1;
            -fx-border-radius: 10;
            -fx-cursor: hand;
        """);

        b.setOnMouseEntered(e -> b.setStyle("""
            -fx-background-color: #b71c1c;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10 20;
            -fx-background-radius: 10;
            -fx-border-color: #7f0000;
            -fx-border-width: 1;
            -fx-border-radius: 10;
            -fx-cursor: hand;
        """));

        b.setOnMouseExited(e -> b.setStyle("""
            -fx-background-color: #d32f2f;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10 20;
            -fx-background-radius: 10;
            -fx-border-color: #9a0007;
            -fx-border-width: 1;
            -fx-border-radius: 10;
            -fx-cursor: hand;
        """));

        return b;
    }

    // ✅ Getters
    public Button getManageProductsBtn() {
        return manageProductsBtn;
    }

    public Button getManageCategoriesBtn() {
        return manageCategoriesBtn;
    }

    public Button getManageUsersBtn() {
        return manageUsersBtn;
    }

    public Button getManageCurrenciesBtn() {
        return manageCurrenciesBtn;
    }

    public Button getReportsBtn() {
        return reportsBtn;
    }

    public Button getLogoutBtn() {
        return logoutBtn;
    }
}
