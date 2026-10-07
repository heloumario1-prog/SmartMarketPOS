package Views;

import Models.ReportRow;
import Models.CashierReport;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class ReportView {

    private Parent root;

    private Button todayBtn;
    private Button weekBtn;
    private Button monthBtn;
    private Button allBtn;
    private Button exportBtn;

    private TableView<ReportRow> table;
    private TableView<CashierReport> cashierTable;

    public ReportView() {
        buildUI();
    }

    private void buildUI() {

        Label title = new Label("📊 Sales Reports");
        title.setFont(Font.font("Arial", 26));

        todayBtn = blue("🔅 Today");
        weekBtn = blue("📅 This Week");
        monthBtn = blue("🗓 This Month");
        allBtn = blue("📊 All Time");
        exportBtn = green("⬇ Export CSV");

        VBox leftPanel = new VBox(15,
                title,
                todayBtn,
                weekBtn,
                monthBtn,
                allBtn,
                exportBtn
        );

        leftPanel.setPadding(new Insets(20));
        leftPanel.setPrefWidth(260);
        leftPanel.setAlignment(Pos.TOP_CENTER);
        leftPanel.setStyle("-fx-background-color: white; -fx-background-radius: 12;");

        table = buildSalesTable();
        cashierTable = buildCashierTable();

        HBox layout = new HBox(20, leftPanel, table, cashierTable);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f5f7fa;");

        this.root = layout;
    }

    private Button blue(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:#1976d2; -fx-text-fill:white; -fx-font-size:16px;"
                + "-fx-padding:10 20; -fx-background-radius:20;");
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    private Button green(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:#2e7d32; -fx-text-fill:white; -fx-font-size:16px;"
                + "-fx-padding:10 20; -fx-background-radius:20;");
        b.setMaxWidth(Double.MAX_VALUE);
        return b;
    }

    private TableView<ReportRow> buildSalesTable() {
        TableView<ReportRow> t = new TableView<>();

        TableColumn<ReportRow, Integer> idCol = new TableColumn<>("Sale ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("saleId"));

        TableColumn<ReportRow, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<ReportRow, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<ReportRow, String> cashierCol = new TableColumn<>("Cashier");
        cashierCol.setCellValueFactory(new PropertyValueFactory<>("cashier"));

        t.getColumns().addAll(idCol, dateCol, totalCol, cashierCol);

        t.setPrefWidth(500);
        t.setPrefHeight(550);

        return t;
    }

    private TableView<CashierReport> buildCashierTable() {
        TableView<CashierReport> t = new TableView<>();

        TableColumn<CashierReport, String> cashierCol = new TableColumn<>("Cashier");
        cashierCol.setCellValueFactory(new PropertyValueFactory<>("cashier"));

        TableColumn<CashierReport, String> totalCol = new TableColumn<>("Total Money");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalFormatted"));

        TableColumn<CashierReport, String> percentCol = new TableColumn<>("%");
        percentCol.setCellValueFactory(new PropertyValueFactory<>("percentageFormatted"));

        t.getColumns().addAll(cashierCol, totalCol, percentCol);

        t.setPrefWidth(350);
        t.setPrefHeight(550);

        return t;
    }

    // ✅ IMPORTANT
    public Parent getRoot() {
        return root;
    }

    public Button getTodayBtn() {
        return todayBtn;
    }

    public Button getWeekBtn() {
        return weekBtn;
    }

    public Button getMonthBtn() {
        return monthBtn;
    }

    public Button getAllBtn() {
        return allBtn;
    }

    public Button getExportBtn() {
        return exportBtn;
    }


    public TableView<ReportRow> getTable() {
        return table;
    }

    public TableView<CashierReport> getCashierTable() {
        return cashierTable;
    }
}
