package Views;

import Models.SaleItem;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SalesView extends Stage {

    // Input fields
    private TextField searchField;
    private TextField barcodeField;
    private TextField quantityField;

    // Payment fields
    private TextField moneyInputField;
    private Label remainingLabel;
    private ListView<String> historyList;
    private Label rateLabel;

    // Currency selector
    private ComboBox<String> currencyCombo;

    // Totals
    private Label totalLabel;
    private Label totalLBPLabel;
    private Label totalEURLabel;
    private Label discountLabel;

    // Buttons
    private Button addItemBtn;
    private Button removeItemBtn;
    private Button confirmSaleBtn;
    private Button newSaleBtn;
    private Button logoutBtn;
    private Button voidOffBtn;
    private Button suspendBtn;
    private Button resumeBtn;
    private Button discountBtn;
    private Button unlockVoidBtn;
    private Button deletePaymentBtn;

    // Scan mode
    private CheckBox scanModeToggle;

    public Button getDeletePaymentBtn() {
        return deletePaymentBtn;
    }

    // Table
    private TableView<SaleItem> table;

    // Live search context menu
    private ContextMenu autoMenu = new ContextMenu();

    public SalesView() {

        setTitle("New Sale - Cashier");

        // --------------------- TOP BAR ----------------------
        Label title = new Label("SmartMarket POS - Sale");
        title.setStyle("-fx-font-size: 22px; -fx-text-fill: white; -fx-font-weight: bold;");

        logoutBtn = red("🚪 Logout");

        rateLabel = new Label("Rate: loading...");
        rateLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        VBox rightBox = new VBox(5, logoutBtn, rateLabel);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(title, spacer, rightBox);
        topBar.setPadding(new Insets(10, 20, 10, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #1e88e5;");

        // --------------------- INPUTS ----------------------
        searchField = new TextField();
        searchField.setPromptText("Search by name");

        barcodeField = new TextField();
        barcodeField.setPromptText("Barcode");

        quantityField = new TextField();
        quantityField.setPromptText("Qty");

        addItemBtn = blue("➕ Add Item");
        removeItemBtn = red("🗑 Remove Item");
        removeItemBtn.setDisable(true);

        HBox inputBox = new HBox(
                10,
                searchField,
                barcodeField,
                quantityField,
                addItemBtn,
                removeItemBtn,
                rateLabel
        );
        rateLabel.setStyle("-fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold;");

        inputBox.setAlignment(Pos.CENTER_LEFT);

        // --------------------- TABLE ----------------------
        table = new TableView<>();

        TableColumn<SaleItem, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("saleItemId"));

        TableColumn<SaleItem, Integer> colProd = new TableColumn<>("Product");
        colProd.setCellValueFactory(new PropertyValueFactory<>("product_id"));

        TableColumn<SaleItem, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<SaleItem, Integer> colQty = new TableColumn<>("Qty");
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<SaleItem, Double> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<SaleItem, Double> colSub = new TableColumn<>("Subtotal");
        colSub.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        table.getColumns().addAll(colId, colProd, colName, colQty, colPrice, colSub);
        table.setPrefHeight(300);

        // --------------------- TOTALS ----------------------
        totalLabel = new Label("Total (USD): 0.00");
        totalLBPLabel = new Label("Total (LBP): 0");
        totalEURLabel = new Label("Total (EUR): 0.00");
        discountLabel = new Label("Discount: 0%");

        VBox totalsBox = new VBox(6, totalLabel, totalLBPLabel, totalEURLabel, discountLabel);
        totalsBox.setAlignment(Pos.CENTER_LEFT);

        // --------------------- PAYMENT ----------------------
        currencyCombo = new ComboBox<>();
        currencyCombo.getItems().addAll("USD", "LBP", "EUR");
        currencyCombo.setValue("USD");

        HBox currencyRow = new HBox(10, new Label("Payment currency:"), currencyCombo);
        currencyRow.setAlignment(Pos.CENTER_LEFT);

        remainingLabel = new Label("Remaining: 0.00 USD");

        moneyInputField = new TextField();
        moneyInputField.setPromptText("Enter amount");

        historyList = new ListView<>();
        historyList.setPrefHeight(120);

        deletePaymentBtn = red("❌ Remove Payment");
        deletePaymentBtn.setDisable(true);  // admin only

        VBox paymentBox = new VBox(8,
                currencyRow,
                remainingLabel,
                new Label("Money input:"),
                moneyInputField,
                new Label("History"),
                historyList
        );

        // --------------------- BOTTOM ACTIONS ----------------------
        voidOffBtn = red("🔒 Lock VOID");
        voidOffBtn.setDisable(true);

        suspendBtn = blue("⏸ Suspend");
        resumeBtn = blue("♻ Resume");
        discountBtn = blue("💸 Discount");
        discountBtn.setDisable(true);

        confirmSaleBtn = green("✅ Confirm Sale");
        newSaleBtn = blue("🆕 New Sale");

        scanModeToggle = new CheckBox("Scanner Mode");
        scanModeToggle.setSelected(true);

        unlockVoidBtn = blue("🔓 Unlock VOID");

        HBox actions = new HBox(15,
                scanModeToggle,
                voidOffBtn,
                suspendBtn,
                resumeBtn,
                discountBtn,
                confirmSaleBtn,
                newSaleBtn,
                unlockVoidBtn,
                deletePaymentBtn // NEW: placed next to Unlock VOID
        );

        actions.setAlignment(Pos.CENTER_LEFT);

        // --------------------- MAIN LAYOUT ----------------------
        VBox center = new VBox(15, inputBox, table, totalsBox, paymentBox, actions);
        center.setPadding(new Insets(20));

        BorderPane root = new BorderPane(center);
        root.setTop(topBar);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case F2 -> {
                    scanModeToggle.setSelected(!scanModeToggle.isSelected());
                    barcodeField.requestFocus();
                }
            }
        });

        setScene(scene);

        setMaximized(true);

        // Autofocus logic for scanning
        Platform.runLater(() -> {
            if (scanModeToggle.isSelected()) {
                barcodeField.requestFocus();
            }
        });

        scene.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
            if (!scanModeToggle.isSelected()) {
                return;
            }
            if (e.getTarget() instanceof TextField) {
                return;
            }
            barcodeField.requestFocus();
        });
    }

    public Button getUnlockVoidBtn() {
        return unlockVoidBtn;
    }

    // --------------------- BUTTON STYLES ----------------------
    private Button blue(String text) {
        Button b = new Button(text);
        b.setStyle("""
            -fx-background-color: #1976d2;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 8 20;
            -fx-background-radius: 10;
            -fx-border-color: #115293;
            -fx-border-width: 1;
            -fx-border-radius: 10;
            -fx-cursor: hand;
        """);
        return b;
    }

    private Button red(String text) {
        Button b = new Button(text);
        b.setStyle("""
            -fx-background-color: #d32f2f;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 8 20;
            -fx-background-radius: 10;
            -fx-border-color: #9a0007;
            -fx-border-width: 1;
            -fx-border-radius: 10;
            -fx-cursor: hand;
        """);
        return b;
    }

    private Button green(String text) {
        Button b = new Button(text);
        b.setStyle("""
            -fx-background-color: #2e7d32;
            -fx-text-fill: white;
            -fx-font-size: 14px;
            -fx-padding: 8 20;
            -fx-background-radius: 10;
            -fx-border-color: #1b5e20;
            -fx-border-width: 1;
            -fx-border-radius: 10;
            -fx-cursor: hand;
        """);
        return b;
    }

    // --------------------- GETTERS ----------------------
    public TextField getSearchField() {
        return searchField;
    }

    public TextField getBarcodeField() {
        return barcodeField;
    }

    public void clearBarcodeField() {
        barcodeField.clear();
    }

    public TextField getQuantityField() {
        return quantityField;
    }
    
    public void clearQuantityField() {
        quantityField.clear();
    }

    public TextField getMoneyInputField() {
        return moneyInputField;
    }

    public Label getRemainingLabel() {
        return remainingLabel;
    }

    public ListView<String> getHistoryList() {
        return historyList;
    }

    public Label getRateLabel() {
        return rateLabel;
    }

    public ComboBox<String> getCurrencyCombo() {
        return currencyCombo;
    }

    public Button getAddItemBtn() {
        return addItemBtn;
    }

    public Button getRemoveItemBtn() {
        return removeItemBtn;
    }

    public Button getConfirmSaleBtn() {
        return confirmSaleBtn;
    }

    public Button getNewSaleBtn() {
        return newSaleBtn;
    }

    public Button getLogoutBtn() {
        return logoutBtn;
    }

    public Button getVoidOffBtn() {
        return voidOffBtn;
    }

    public Button getSuspendBtn() {
        return suspendBtn;
    }

    public Button getResumeBtn() {
        return resumeBtn;
    }

    public Button getDiscountBtn() {
        return discountBtn;
    }

    public TableView<SaleItem> getTable() {
        return table;
    }

    public Label getTotalLabel() {
        return totalLabel;
    }

    public Label getTotalLBPLabel() {
        return totalLBPLabel;
    }

    public Label getTotalEURLabel() {
        return totalEURLabel;
    }

    public Label getDiscountLabel() {
        return discountLabel;
    }

    public ContextMenu getAutoMenu() {
        return autoMenu;
    }

    public CheckBox getScanModeToggle() {
        return scanModeToggle;
    }
}
