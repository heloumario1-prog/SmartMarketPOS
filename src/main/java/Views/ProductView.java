package Views;

import Models.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class ProductView {

    private Parent root;

    private TextField nameField;
    private ComboBox<String> categoryCombo;
    private TextField barcodeField;
    private TextField priceField;
    private TextField stockField;

    private Button addBtn;
    private Button editBtn;
    private Button deleteBtn;
    private Button clearBtn;

    private TableView<Product> table;

    public ProductView() {
        buildUI();
    }

    private void buildUI() {

        Label title = new Label("🛒 Product Management");
        title.setFont(Font.font("Arial", 24));

        nameField = new TextField();
        categoryCombo = new ComboBox<>();
        barcodeField = new TextField();
        priceField = new TextField();
        stockField = new TextField();

        nameField.setPromptText("Product Name");
        categoryCombo.setPromptText("Category");
        barcodeField.setPromptText("Barcode");
        priceField.setPromptText("Price");
        stockField.setPromptText("Stock");

        addBtn = styledBlue("➕ Add");
        editBtn = styledBlue("✏ Edit");
        deleteBtn = styledRed("🗑 Delete");
        clearBtn = styledBlue("🧹 Clear Fields");

        VBox form = new VBox(15,
                title,
                nameField,
                categoryCombo,
                barcodeField,
                priceField,
                stockField,
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

        this.root = layout; // ✅ store root instead of Scene/Stage
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

    private TableView<Product> buildTable() {
        TableView<Product> table = new TableView<>();

        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Integer> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        TableColumn<Product, String> barcodeCol = new TableColumn<>("Barcode");
        barcodeCol.setCellValueFactory(new PropertyValueFactory<>("barcode"));

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        table.getColumns().addAll(idCol, nameCol, catCol, barcodeCol, priceCol, stockCol);

        return table;
    }

    // ✅ IMPORTANT: return the UI so controller can insert it
    public Parent getRoot() {
        return root;
    }

    public TextField getNameField() {
        return nameField;
    }

    public ComboBox<String> getCategoryCombo() {
        return categoryCombo;
    }

    public TextField getBarcodeField() {
        return barcodeField;
    }

    public TextField getPriceField() {
        return priceField;
    }

    public TextField getStockField() {
        return stockField;
    }

    public TableView<Product> getTable() {
        return table;
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

    public Button getClearBtn() {
        return clearBtn;
    }
}
