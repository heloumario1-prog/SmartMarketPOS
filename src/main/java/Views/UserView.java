package Views;

import Models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class UserView {

    private Parent root;

    private TextField usernameField;
    private TextField passwordField;

    private Button addBtn;
    private Button editBtn;
    private Button deleteBtn;
    private Button clearBtn;

    private TableView<User> table;

    public UserView() {
        buildUI();
    }

    private void buildUI() {

        Label title = new Label("👤 Manage Cashiers");
        title.setFont(Font.font("Arial", 24));

        usernameField = new TextField();
        passwordField = new TextField();

        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

        addBtn = blueBtn("➕ Add");
        editBtn = blueBtn("✏ Edit");
        deleteBtn = redBtn("🗑 Delete");
        clearBtn = blueBtn("Clear Fields");

        VBox form = new VBox(15,
                title,
                usernameField,
                passwordField,
                addBtn,
                editBtn,
                deleteBtn,
                clearBtn
        );

        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        form.setPrefWidth(300);
        form.setAlignment(Pos.TOP_CENTER);

        table = buildTable();
        table.setPrefWidth(650);

        HBox layout = new HBox(20, form, table);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f5f7fa;");

        this.root = layout; // ✅ instead of Scene/Stage
    }

    private Button blueBtn(String text) {
        Button b = new Button(text);
        b.setStyle("""
            -fx-background-color: #1976d2;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10 20;
            -fx-background-radius: 20;
        """);
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    private Button redBtn(String text) {
        Button b = new Button(text);
        b.setStyle("""
            -fx-background-color: #d32f2f;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10 20;
            -fx-background-radius: 20;
        """);
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    private TableView<User> buildTable() {
        TableView<User> table = new TableView<>();

        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        table.getColumns().addAll(idCol, usernameCol, passwordCol, roleCol);

        return table;
    }

    // ✅ IMPORTANT
    public Parent getRoot() {
        return root;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public Button getAddBtn() {
        return addBtn;
    }

    public Button getEditBtn() {
        return editBtn;
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }

    public TableView<User> getTable() {
        return table;
    }

    public Button getClearBtn() {
        return clearBtn;
    }

}
