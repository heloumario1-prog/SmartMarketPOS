package Controllers;

import Models.CashierReport;
import Models.ReportRow;
import Services.ReportService;
import Views.ReportView;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;

public class ReportController {

    private ReportView view;
    private ReportService service;
    private AdminDashboardController adminController;

    public ReportController(AdminDashboardController adminController) {
        this.adminController = adminController;
        this.service = new ReportService();
        this.view = new ReportView();

        attachHandlers();
        loadAll();

        // ✅ Insert into dashboard center
        adminController.getView().setContent(view.getRoot());
    }

    private void attachHandlers() {
        view.getTodayBtn().setOnAction(e -> loadToday());
        view.getWeekBtn().setOnAction(e -> loadWeek());
        view.getMonthBtn().setOnAction(e -> loadMonth());
        view.getAllBtn().setOnAction(e -> loadAll());
        view.getExportBtn().setOnAction(e -> exportCSV());
    }

    private void load(List<ReportRow> rows) {
        view.getTable().setItems(FXCollections.observableArrayList(rows));
    }

    private void loadCashierStats(LocalDate start, LocalDate end) {
        List<CashierReport> stats = service.getCashierTotalsBetween(start, end);
        view.getCashierTable().setItems(FXCollections.observableArrayList(stats));
    }

    private void loadToday() {
        LocalDate d = LocalDate.now();
        load(service.getSalesBetween(d, d));
        loadCashierStats(d, d);
    }

    private void loadWeek() {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(7);
        load(service.getSalesBetween(start, end));
        loadCashierStats(start, end);
    }

    private void loadMonth() {
        LocalDate end = LocalDate.now();
        LocalDate start = end.withDayOfMonth(1);
        load(service.getSalesBetween(start, end));
        loadCashierStats(start, end);
    }

    private void loadAll() {
        LocalDate start = LocalDate.of(2000, 1, 1);
        LocalDate end = LocalDate.now();
        load(service.getSalesBetween(start, end));
        loadCashierStats(start, end);
    }

    private void exportCSV() {
        try {
            String desktop = System.getProperty("user.home") + "/Desktop/sales_report.csv";
            FileWriter fw = new FileWriter(desktop);

            fw.write("SaleID,Date,Total,Cashier\n");

            for (ReportRow r : view.getTable().getItems()) {

                fw.write(r.getSaleId() + ","
                        + "\"" + r.getDate() + "\"," // ← DATE FIXED
                        + r.getTotal() + ","
                        + r.getCashier() + "\n");
            }

            fw.close();
            alert("Exported successfully to Desktop as sales_report.csv");

        } catch (Exception ex) {
            alert("Error exporting CSV: " + ex.getMessage());
        }
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
