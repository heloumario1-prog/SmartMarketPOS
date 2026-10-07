package Controllers;

import Models.Currency;
import Services.CurrencyService;
import Views.CurrencySettingsView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.List;

public class CurrencySettingsController {

    private CurrencySettingsView view;
    private CurrencyService currencyService;
    private AdminDashboardController adminController;

    public CurrencySettingsController(AdminDashboardController adminController) {
        this.adminController = adminController;
        this.currencyService = new CurrencyService();
        this.view = new CurrencySettingsView();

        attachHandlers();
        loadCurrencies();

        // ✅ insert into dashboard center
        adminController.getView().setContent(view.getRoot());
    }

    // ✅ optional constructor for testing without dashboard
    public CurrencySettingsController(CurrencySettingsView view, CurrencyService currencyService) {
        this.view = view;
        this.currencyService = currencyService;
        attachHandlers();
        loadCurrencies();
    }

    private void attachHandlers() {
        view.getSaveBtn().setOnAction(e -> saveCurrency());
        view.getDeleteBtn().setOnAction(e -> deleteCurrency());
        view.getClearBtn().setOnAction(e -> clearForm());
        view.getTable().setOnMouseClicked(e -> loadSelected());
    }

    private void loadCurrencies() {
        try {
            List<Currency> list = currencyService.getAllCurrencies();
            ObservableList<Currency> items = FXCollections.observableArrayList(list);
            view.getTable().setItems(items);
        } catch (Exception ex) {
            show("Error loading currencies: " + ex.getMessage());
        }
    }

    private void saveCurrency() {
        String code = view.getCodeCombo().getValue();
        String name = view.getNameField().getText().trim();
        String rateText = view.getRateField().getText().trim();

        if (code == null || name.isEmpty() || rateText.isEmpty()) {
            show("All fields are required.");
            return;
        }

        try {
            double rate = Double.parseDouble(rateText);
            currencyService.upsertCurrency(code, name, rate);
            loadCurrencies();
        } catch (Exception ex) {
            show("Error saving currency: " + ex.getMessage());
        }
    }

    private void deleteCurrency() {
        Currency selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            show("Select a currency to delete.");
            return;
        }

        try {
            currencyService.deleteCurrency(selected.getCurrencyCode());
            loadCurrencies();
        } catch (Exception ex) {
            show("Error deleting currency: " + ex.getMessage());
        }
    }

    private void loadSelected() {
        Currency c = view.getTable().getSelectionModel().getSelectedItem();
        if (c == null) {
            return;
        }

        view.getCodeCombo().setValue(c.getCurrencyCode());
        view.getNameField().setText(c.getCurrencyName());
        view.getRateField().setText(String.valueOf(c.getRateToUsd()));
    }

    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void clearForm() {
        view.getCodeCombo().getSelectionModel().clearSelection();
        view.getNameField().clear();
        view.getRateField().clear();
    }

}
