package Views;

import Models.Category;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class CategoryView {

    private Parent root;

    private TextField nameField;
    private Button addBtn;
    private Button editBtn;
    private Button deleteBtn;
    private Button clearBtn;

    private TableView<Category> table;

    public CategoryView() {
        buildUI();
    }

    private void buildUI() {
        Label title = new Label("📂 Category Management");
        title.setFont(Font.font("Arial", 24));

        nameField = new TextField();
        nameField.setPromptText("Category Name");
        nameField.setMaxWidth(Double.MAX_VALUE);

        addBtn = styledBlue("➕ Add");
        editBtn = styledBlue("✏ Edit");
        deleteBtn = styledRed("🗑 Delete");
        clearBtn = styledBlue("🧹 Clear Fields");
        VBox form = new VBox(15,
                title,
                nameField,
                addBtn,
                editBtn,
                deleteBtn,
                clearBtn
        );

        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12;");
        form.setPrefWidth(300);
        form.setAlignment(Pos.TOP_CENTER);

        table = buildTable();
        table.setPrefWidth(400);

        HBox layout = new HBox(20, form, table);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f5f7fa;");

        this.root = layout; // ✅ store the UI
    }

    private Button styledBlue(String text) {
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

    private Button styledRed(String text) {
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

    private Button styledGrey(String text) {
        Button b = new Button(text);
        b.setStyle("""
            -fx-background-color: #9e9e9e;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 8 16;
            -fx-background-radius: 20;
        """);
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    private TableView<Category> buildTable() {
        TableView<Category> table = new TableView<>();

        TableColumn<Category, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("categoryId"));

        TableColumn<Category, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getColumns().addAll(idCol, nameCol);
        return table;
    }

    // ✅ IMPORTANT
    public Parent getRoot() {
        return root;
    }

    public TextField getNameField() {
        return nameField;
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

    public TableView<Category> getTable() {
        return table;
    }

    public Button getClearBtn() {
        return clearBtn;
    }
}
