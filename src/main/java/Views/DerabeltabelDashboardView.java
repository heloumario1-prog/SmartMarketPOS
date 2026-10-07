package Views;

import Models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DerabeltabelDashboardView extends Stage {

    private Parent root;

    private TableView<User> table;
    private Button promoteBtn;
    private Button demoteBtn;
    private Button regenVoidBtn;
    private Button deleteBtn;
    private Button addAdminBtn;
    private Button logoutBtn;

    private TextField adminUserField;
    private TextField adminPassField;

    public DerabeltabelDashboardView() {
        buildUI();

        Scene scene = new Scene(root);
        setScene(scene);

        setTitle("Derabeltabel Control Center");
        setMaximized(true);
    }

    private void buildUI() {

        Label title = new Label("🛡 Derabeltabel User Manager");
        title.setFont(Font.font("Arial", 26));

        table = new TableView<>();

        TableColumn<User, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<User, String> colName = new TableColumn<>("Username");
        colName.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> colPass = new TableColumn<>("Password");
        colPass.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<User, String> colRole = new TableColumn<>("Role");
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<User, String> colVoid = new TableColumn<>("Void Code");
        colVoid.setCellValueFactory(new PropertyValueFactory<>("voidCode"));

        table.getColumns().addAll(colId, colName, colPass, colRole, colVoid);

        // -------------------------
        // Add Admin UI (NEW)
        // -------------------------
        adminUserField = new TextField();
        adminUserField.setPromptText("New Admin Username");

        adminPassField = new TextField();
        adminPassField.setPromptText("New Admin Password");

        addAdminBtn = blue("➕ Add Admin");

        VBox addAdminBox = new VBox(10,
                new Label("Create Admin:"),
                adminUserField,
                adminPassField,
                addAdminBtn
        );
        addAdminBox.setPadding(new Insets(10));
        addAdminBox.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 10;");

        promoteBtn = blue("⬆ Promote to Admin");
        demoteBtn = blue("⬇ Demote to Cashier");
        regenVoidBtn = blue("♻ Regenerate Void Code");
        deleteBtn = red("🗑 Delete User");
        logoutBtn = red("🚪 Logout");

        VBox leftPanel = new VBox(20,
                title,
                addAdminBox,
                promoteBtn,
                demoteBtn,
                regenVoidBtn,
                deleteBtn,
                logoutBtn
        );
        leftPanel.setPadding(new Insets(20));
        leftPanel.setPrefWidth(320);
        leftPanel.setAlignment(Pos.TOP_CENTER);
        leftPanel.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10;");

        HBox layout = new HBox(20, leftPanel, table);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f5f7fa;");

        this.root = layout;
    }

    private Button blue(String text) {
        Button b = new Button(text);
        b.setStyle("""
           -fx-background-color: #1565c0;
           -fx-text-fill: white;
           -fx-font-size: 16px;
           -fx-padding: 10 20;
           -fx-background-radius: 10;
        """);
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    private Button red(String text) {
        Button b = new Button(text);
        b.setStyle("""
           -fx-background-color: #c62828;
           -fx-text-fill: white;
           -fx-font-size: 16px;
           -fx-padding: 10 20;
           -fx-background-radius: 10;
        """);
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    public Parent getRoot() {
        return root;
    }

    public TableView<User> getTable() {
        return table;
    }

    public Button getPromoteBtn() {
        return promoteBtn;
    }

    public Button getDemoteBtn() {
        return demoteBtn;
    }

    public Button getRegenVoidBtn() {
        return regenVoidBtn;
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }

    public Button getAddAdminBtn() {
        return addAdminBtn;
    }

    public TextField getAdminUserField() {
        return adminUserField;
    }

    public TextField getAdminPassField() {
        return adminPassField;
    }

    public Button getLogoutBtn() {
        return logoutBtn;
    }
}
