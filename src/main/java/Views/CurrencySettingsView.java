package Views;

import Models.Currency;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class CurrencySettingsView {

    private Parent root;

    private ComboBox<String> codeCombo;
    private TextField nameField;
    private TextField rateField;

    private Button saveBtn;
    private Button deleteBtn;
    private Button clearBtn;

    private TableView<Currency> table;

    public CurrencySettingsView() {
        buildUI();
    }

    private void buildUI() {

        Label title = new Label("💱 Currency Settings");
        title.setFont(Font.font("Arial", 24));

        codeCombo = new ComboBox<>();
        codeCombo.getItems().addAll("USD", "EUR", "LBP");
        codeCombo.setPromptText("Currency Code (USD/EUR/LBP)");

        nameField = new TextField();
        nameField.setPromptText("Currency Name");

        rateField = new TextField();
        rateField.setPromptText("Rate to USD (1 USD = rate * currency)");

        codeCombo.setMaxWidth(Double.MAX_VALUE);
        nameField.setMaxWidth(Double.MAX_VALUE);
        rateField.setMaxWidth(Double.MAX_VALUE);

        saveBtn = blue("💾 Save / Update");
        deleteBtn = red("🗑 Delete");
        clearBtn = blue("🧹 Clear Fields");

        VBox form = new VBox(10,
                title,
                codeCombo,
                nameField,
                rateField,
                saveBtn,
                deleteBtn,
                clearBtn
        );

        form.setPadding(new Insets(20));
        form.setPrefWidth(300);
        form.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        form.setAlignment(Pos.TOP_CENTER);

        table = buildTable();
        table.setPrefWidth(450);

        HBox layout = new HBox(20, form, table);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f5f7fa;");

        this.root = layout;  // ✅ store UI instead of Scene/Stage
    }

    private Button blue(String text) {
        Button b = new Button(text);
        b.setStyle("""
            -fx-background-color: #1976d2;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 8 16;
            -fx-background-radius: 20;
        """);
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    private Button red(String text) {
        Button b = new Button(text);
        b.setStyle("""
            -fx-background-color: #d32f2f;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 8 16;
            -fx-background-radius: 20;
        """);
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    private Button grey(String text) {
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

    private TableView<Currency> buildTable() {
        TableView<Currency> t = new TableView<>();

        TableColumn<Currency, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("currencyCode"));

        TableColumn<Currency, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("currencyName"));

        TableColumn<Currency, Double> rateCol = new TableColumn<>("Rate to USD");
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rateToUsd"));

        t.getColumns().addAll(codeCol, nameCol, rateCol);
        return t;
    }

    // ✅ IMPORTANT
    public Parent getRoot() {
        return root;
    }

    public ComboBox<String> getCodeCombo() {
        return codeCombo;
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextField getRateField() {
        return rateField;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }

    public TableView<Currency> getTable() {
        return table;
    }

    public Button getClearBtn() {
        return clearBtn;
    }
}
