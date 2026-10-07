package Controllers;

import Models.Product;
import Models.Sale;
import Models.SaleItem;
import Models.User;
import Services.CurrencyService;
import Services.ProductService;
import Services.SaleItemService;
import Services.SaleService;
import Services.UserService;
import Views.SalesView;
import java.io.InputStream;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputDialog;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SalesController {

    private SalesView view;
    private User cashier;
    private ProductService productService;
    private SaleService saleService;
    private SaleItemService saleItemService;
    private UserService userService;
    private CurrencyService currencyService;

    private ObservableList<SaleItem> cart = FXCollections.observableArrayList();
    private boolean authorizedToVoid = false;

    // PAYMENT ENGINE STATE
    private double finalAmountUsd = 0.0;
    private double remainingUsd = 0.0;
    private List<String> paymentHistory = new ArrayList<>();

    // Exchange rates
    private double lbpRate = 89700.0;
    private double eurRate = 0.92;

    private String currentCurrency = "USD"; // default payment currency

    // Discount state (percentage as 0.05, 0.10, etc.)
    private double discountPercent = 0.0;

    // ----------------- SUSPENDED SALES -----------------
    private static class SuspendedSale {

        int id;
        List<SaleItem> items;
        String currency;
        double finalUsd;
        double remainingUsd;
        List<String> history;
        double discountPercent;
        LocalDateTime createdAt;
    }

    private List<SuspendedSale> suspendedSales = new ArrayList<>();
    private int nextSuspendedId = 1;

    public SalesController(User cashier) throws SQLException {
        this.cashier = cashier;

        productService = new ProductService();
        saleService = new SaleService();
        saleItemService = new SaleItemService();
        userService = new UserService();
        currencyService = new CurrencyService();

        view = new SalesView();
        view.getTable().setItems(cart);
        view.getConfirmSaleBtn().setDisable(true);

        loadExchangeRates();
        attachHandlers();
        updateTotals();
        view.show();
    }

    private void loadExchangeRates() {
        try {
            lbpRate = currencyService.getRatePerUsd("LBP");
        } catch (Exception ignored) {
        }

        try {
            eurRate = currencyService.getRatePerUsd("EUR");
        } catch (Exception ignored) {
        }

        view.getRateLabel().setText(
                String.format("💱 1 USD = %,d LBP | 1 USD = %.2f EUR", (int) lbpRate, eurRate)
        );
    }

    private String formatLBP(double value) {
        NumberFormat f = NumberFormat.getInstance(Locale.US);
        return f.format(Math.round(value));
    }

    private double convertFromUsd(double usd, String currency) {
        switch (currency) {
            case "LBP":
                return usd * lbpRate;
            case "EUR":
                return usd * eurRate;
            default:
                return usd;
        }
    }

    private double convertToUsd(double amount, String currency) {
        switch (currency) {
            case "LBP":
                return amount / lbpRate;
            case "EUR":
                return amount / eurRate;
            default:
                return amount;
        }
    }

    private void updateRemainingLabels() {

        double remUSD = remainingUsd;
        double remLBP = remainingUsd * lbpRate;
        double remEUR = remainingUsd * eurRate;

        String status;
        if (remainingUsd > 0.0001) {
            status = "Customer still owes:";
        } else if (remainingUsd < -0.0001) {
            status = "Cashier should return:";
        } else {
            status = "Payment complete:";
        }

        String text = status
                + " USD: " + String.format("%.2f", remUSD) + "\t\t"
                + " LBP: " + formatLBP(remLBP) + "\t"
                + " EUR: " + String.format("%.2f", remEUR);

        view.getRemainingLabel().setText(text);
    }

    private void attachHandlers() {

        view.getLogoutBtn().setOnAction(e -> {

            if (!suspendedSales.isEmpty()) {
                showError("Cannot logout: There are suspended sales.\nResume or cancel them first.");
                return;
            }

            view.close();
            LoginController login = new LoginController();
            login.handleLogin();
        });

        view.getAddItemBtn().setOnAction(e -> {
            try {
                addItem();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        view.getBarcodeField().setOnAction(e -> {
            try {
                // Only force qty=1 when scanner mode ON
                if (view.getScanModeToggle().isSelected()) {
                    if (view.getQuantityField().getText().trim().isEmpty()) {
                        view.getQuantityField().setText("1");
                    }
                }
                addItem();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // LIVE SEARCH on product name (scanner mode OFF only)
        view.getSearchField().textProperty().addListener((obs, oldVal, newVal) -> {
            String text = newVal == null ? "" : newVal.trim();

            // If scanner mode ON, disable suggestions
            if (view.getScanModeToggle().isSelected()) {
                view.getAutoMenu().hide();
                return;
            }

            if (text.isEmpty()) {
                view.getAutoMenu().hide();
                return;
            }

            showSearchResults(text);
        });

        view.getCurrencyCombo().valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                currentCurrency = newVal;
            }
        });

        view.getVoidOffBtn().setOnAction(e -> {
            authorizedToVoid = false;
            view.getVoidOffBtn().setDisable(true);
            view.getDiscountBtn().setDisable(true);
            view.getRemoveItemBtn().setDisable(true);  // <--- ADD THIS
            view.getDeletePaymentBtn().setDisable(true);
            showInfo("VOID mode disabled. Admin must scan again.");
        });

        view.getRemoveItemBtn().setOnAction(e -> {
            if (!authorizedToVoid) {
                requestAdminOverride();
                return;
            }

            SaleItem selected = view.getTable().getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Select an item to remove.");
                return;
            }

            cart.remove(selected);
            updateTotals();
        });

        view.getMoneyInputField().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                return;
            }
            if (Math.abs(remainingUsd) < 0.0001) {
                view.getMoneyInputField().clear();
                return;
            }

            double amountToFill = convertFromUsd(Math.abs(remainingUsd), currentCurrency);

            if ("LBP".equals(currentCurrency)) {
                view.getMoneyInputField().setText(String.valueOf((int) Math.round(amountToFill)));
            } else {
                view.getMoneyInputField().setText(String.format(Locale.US, "%.2f", amountToFill));
            }

            view.getMoneyInputField().selectAll();
        });

        view.getMoneyInputField().setOnAction(e -> processMoneyFlow());

        view.getConfirmSaleBtn().setOnAction(e -> {
            if (remainingUsd == 0) {
                saveSale();
            } else {
                showError("Payment incomplete.");
            }
        });

        // New Sale
        view.getNewSaleBtn().setOnAction(e -> resetSale());

        // Suspend & Resume
        view.getSuspendBtn().setOnAction(e -> suspendCurrentSale());
        view.getResumeBtn().setOnAction(e -> resumeSuspendedSale());

        // Discount
        view.getDiscountBtn().setOnAction(e -> {
            if (!authorizedToVoid) {
                requestAdminOverride();
                return;
            }
            applyDiscount();
        });
        view.getUnlockVoidBtn().setOnAction(e -> requestAdminOverride());

        view.getDeletePaymentBtn().setOnAction(e -> {
            if (!authorizedToVoid) {
                requestAdminOverride();
                return;
            }

            String selected = view.getHistoryList().getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Select a payment to remove.");
                return;
            }

            // Remove from UI and memory
            paymentHistory.remove(selected);
            view.getHistoryList().getItems().remove(selected);

            // Recalculate remaining USD based on updated history
            recalcPayments();
        });

    }

    private void recalcPayments() {
        remainingUsd = finalAmountUsd;

        for (String line : paymentHistory) {
            // line example: "+ 20 USD"
            String[] parts = line.split(" ");
            double amount = Double.parseDouble(parts[1]);
            String currency = parts[2];

            double usd = convertToUsd(amount, currency);

            if (line.startsWith("+")) {
                remainingUsd -= usd;
            } else {
                remainingUsd += usd;
            }
        }

        updateRemainingLabels();

        view.getConfirmSaleBtn().setDisable(Math.abs(remainingUsd) > 0.0001);
    }

    // LIVE SEARCH - builds and shows ContextMenu under the search field
    private void showSearchResults(String text) {
        try {
            List<Product> matches = productService.getAllProducts()
                    .stream()
                    .filter(p -> p.getName().toLowerCase().contains(text.toLowerCase()))
                    .limit(15)
                    .toList();

            ContextMenu menu = view.getAutoMenu();
            menu.getItems().clear();

            if (matches.isEmpty()) {
                menu.hide();
                return;
            }

            for (Product p : matches) {
                String label = p.getName();
                MenuItem item = new MenuItem(label);
                item.setOnAction(e -> {
                    view.getSearchField().setText(p.getName());
                    view.getBarcodeField().setText(p.getBarcode());
                    menu.hide();
                    view.getQuantityField().requestFocus();
                });
                menu.getItems().add(item);
            }

            if (!menu.isShowing()) {
                menu.show(view.getSearchField(), Side.BOTTOM, 0, 0);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addItem() throws SQLException {

        String barcode = view.getBarcodeField().getText().trim();
        String search = view.getSearchField().getText().trim();
        String qtyText = view.getQuantityField().getText().trim();

        if (barcode.isEmpty() && search.isEmpty()) {
            showError("Scan barcode or search by name.");
            return;
        }
        if (qtyText.isEmpty()) {
            showError("Enter quantity.");

            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyText);
        } catch (NumberFormatException ex) {
            showError("Quantity must be a number.");
            return;
        }

        Product product;
        if (!barcode.isEmpty()) {
            playBeep();

            // SCAN: use barcode only
            product = productService.getAllProducts()
                    .stream()
                    .filter(p -> p.getBarcode().equals(barcode))
                    .findFirst().orElse(null);
        } else {
            // SEARCH: use name only
            product = productService.getAllProducts()
                    .stream()
                    .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                    .findFirst().orElse(null);
        }

        if (product == null) {
            showError("Product not found.");
            view.clearBarcodeField();
            view.clearQuantityField();
            return;
        }

        SaleItem existing = cart.stream()
                .filter(i -> i.getProduct_id() == product.getProductId())
                .findFirst()
                .orElse(null);

        if (existing != null) {
            int newQty = existing.getQuantity() + qty;

            if (product.getStock() < newQty) {
                showError("Only " + product.getStock() + " left.");
                return;
            }

            existing.setQuantity(newQty);
            double newSubtotal = Math.round(existing.getPrice() * newQty * 100.0) / 100.0;
            existing.setSubtotal(newSubtotal);
            view.getTable().refresh();
        } else {
            if (product.getStock() < qty) {
                showError("Only " + product.getStock() + " left.");
                return;
            }

            double subtotal = product.getPrice() * qty;

            cart.add(new SaleItem(
                    0, 0,
                    product.getProductId(),
                    qty,
                    product.getPrice(),
                    subtotal,
                    product.getName()
            ));
        }

        updateTotals();

        view.getAutoMenu().hide();
        view.getSearchField().clear();
        view.getBarcodeField().clear();
        view.getQuantityField().clear();
    }

    private void updateTotals() {

        finalAmountUsd = cart.stream().mapToDouble(SaleItem::getSubtotal).sum();
        remainingUsd = finalAmountUsd;

        double lbp = finalAmountUsd * lbpRate;
        double eur = finalAmountUsd * eurRate;

        view.getTotalLabel().setText(String.format("Total (USD): %.2f", finalAmountUsd));
        view.getTotalLBPLabel().setText("Total (LBP): " + formatLBP(lbp));
        view.getTotalEURLabel().setText(String.format("Total (EUR): %.2f", eur));
        view.getDiscountLabel().setText("Discount: 0%");
        discountPercent = 0.0;

        updateRemainingLabels();
        view.getConfirmSaleBtn().setDisable(true);
    }

    // Apply simple percentage discount: 5%, 10%, 15%, 20%
    private void applyDiscount() {

        if (cart.isEmpty()) {
            showError("No items to discount.");
            return;
        }

        List<String> options = List.of("5%", "10%", "15%", "20%");
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.setTitle("Discount");
        dialog.setHeaderText("Select a discount to apply");
        dialog.setContentText("Discount:");

        var result = dialog.showAndWait();
        if (result.isEmpty()) {
            return;
        }

        String choice = result.get();
        int percent;
        try {
            percent = Integer.parseInt(choice.replace("%", "").trim());
        } catch (Exception ex) {
            return;
        }

        double total = cart.stream().mapToDouble(SaleItem::getSubtotal).sum();
        discountPercent = percent / 100.0;

        finalAmountUsd = total * (1 - discountPercent);
        remainingUsd = finalAmountUsd;

        double lbp = finalAmountUsd * lbpRate;
        double eur = finalAmountUsd * eurRate;

        view.getTotalLabel().setText(String.format("Total (USD): %.2f", finalAmountUsd));
        view.getTotalLBPLabel().setText("Total (LBP): " + formatLBP(lbp));
        view.getTotalEURLabel().setText(String.format("Total (EUR): %.2f", eur));
        view.getDiscountLabel().setText("Discount: " + percent + "%");

        // Reset payments because total changed
        paymentHistory.clear();
        view.getHistoryList().getItems().clear();
        view.getMoneyInputField().clear();

        updateRemainingLabels();
        view.getConfirmSaleBtn().setDisable(true);
    }

    private void processMoneyFlow() {

        String txt = view.getMoneyInputField().getText().trim();
        if (txt.isEmpty()) {
            return;
        }

        try {
            double amount = Double.parseDouble(txt);

            if (amount <= 0) {
                showError("Enter a positive amount.");
                view.getMoneyInputField().clear();
                return;
            }

            double usd = convertToUsd(amount, currentCurrency);
            String entry;

            if (remainingUsd > 0.0001) {
                remainingUsd -= usd;
                entry = "+ " + amount + " " + currentCurrency;
            } else if (remainingUsd < -0.0001) {
                remainingUsd += usd;
                entry = "- " + amount + " " + currentCurrency;
            } else {
                remainingUsd -= usd;
                entry = "+ " + amount + " " + currentCurrency;
            }

            paymentHistory.add(entry);
            view.getHistoryList().getItems().add(entry);

            updateRemainingLabels();

            if (Math.abs(remainingUsd) < 0.0001) {
                remainingUsd = 0;
                updateRemainingLabels();
                view.getConfirmSaleBtn().setDisable(false);
            } else {
                view.getConfirmSaleBtn().setDisable(true);
            }

            view.getMoneyInputField().clear();

        } catch (NumberFormatException ex) {
            showError("Invalid amount entered.");
            view.getMoneyInputField().clear();
        }
    }

    private void saveSale() {

        if (cart.isEmpty()) {
            showError("Cart is empty.");
            return;
        }

        try {
            // NOTE: discount is UI-only for now; DB still stores finalAmount only
            Sale sale = saleService.addSale(
                    cashier.getUserId(),
                    LocalDateTime.now(),
                    finalAmountUsd,
                    0,
                    finalAmountUsd
            );

            for (SaleItem item : cart) {
                saleItemService.addSaleItem(
                        sale.getSaleId(),
                        item.getProduct_id(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getSubtotal(),
                        item.getProductName()
                );
                productService.reduceStock(item.getProduct_id(), item.getQuantity());
            }

            showReceipt(sale);

            resetSale();

        } catch (Exception ex) {
            showError("Sale failed: " + ex.getMessage());
        }
    }

    // ----------------- SUSPEND / RESUME LOGIC -----------------
    private void suspendCurrentSale() {

        if (cart.isEmpty()) {
            showError("No items to suspend.");
            return;
        }

        SuspendedSale s = new SuspendedSale();
        s.id = nextSuspendedId++;
        s.createdAt = LocalDateTime.now();
        s.currency = currentCurrency;
        s.finalUsd = finalAmountUsd;
        s.remainingUsd = remainingUsd;
        s.history = new ArrayList<>(paymentHistory);
        s.discountPercent = this.discountPercent;

        s.items = new ArrayList<>();
        for (SaleItem item : cart) {
            s.items.add(new SaleItem(
                    item.getSaleItemId(),
                    item.getSale_id(),
                    item.getProduct_id(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getSubtotal(),
                    item.getProductName()
            ));
        }

        suspendedSales.add(s);

        resetSale();

        showInfo("Sale suspended as Ticket #" + s.id);
    }

    private void resumeSuspendedSale() {

        if (suspendedSales.isEmpty()) {
            showError("No suspended sales.");
            return;
        }

        List<String> choices = new ArrayList<>();
        for (SuspendedSale s : suspendedSales) {
            String label = String.format("Ticket #%d - %d items - %.2f USD",
                    s.id,
                    s.items.size(),
                    s.finalUsd
            );
            choices.add(label);
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Resume Suspended Sale");
        dialog.setHeaderText("Select a suspended sale to resume");
        dialog.setContentText("Suspended sales:");

        var result = dialog.showAndWait();
        if (result.isEmpty()) {
            return;
        }

        String selected = result.get();
        int idx = choices.indexOf(selected);
        if (idx < 0) {
            return;
        }

        SuspendedSale s = suspendedSales.get(idx);

        cart.clear();
        cart.addAll(s.items);
        view.getTable().refresh();

        currentCurrency = s.currency;
        view.getCurrencyCombo().setValue(currentCurrency);

        finalAmountUsd = s.finalUsd;
        remainingUsd = s.remainingUsd;

        discountPercent = s.discountPercent;
        if (discountPercent > 0) {
            view.getDiscountLabel().setText("Discount: " + Math.round(discountPercent * 100) + "%");
        } else {
            view.getDiscountLabel().setText("Discount: 0%");
        }

        paymentHistory.clear();
        paymentHistory.addAll(s.history);

        view.getHistoryList().getItems().clear();
        view.getHistoryList().getItems().addAll(paymentHistory);

        double lbp = finalAmountUsd * lbpRate;
        double eur = finalAmountUsd * eurRate;
        view.getTotalLabel().setText(String.format("Total (USD): %.2f", finalAmountUsd));
        view.getTotalLBPLabel().setText("Total (LBP): " + formatLBP(lbp));
        view.getTotalEURLabel().setText(String.format("Total (EUR): %.2f", eur));

        updateRemainingLabels();
        view.getConfirmSaleBtn().setDisable(Math.abs(remainingUsd) > 0.0001);

        suspendedSales.remove(idx);

        showInfo("Resumed " + selected);
    }

    // ✅ RESET SALE METHOD
    private void resetSale() {

        cart.clear();
        paymentHistory.clear();
        authorizedToVoid = false;
        discountPercent = 0.0;

        view.getTable().getItems().clear();
        view.getHistoryList().getItems().clear();
        view.getAutoMenu().hide();

        finalAmountUsd = 0;
        remainingUsd = 0;

        view.getMoneyInputField().clear();
        view.getSearchField().clear();
        view.getBarcodeField().clear();
        view.getQuantityField().clear();

        currentCurrency = "USD";
        view.getCurrencyCombo().setValue("USD");

        view.getTotalLabel().setText("Total (USD): 0.00");
        view.getTotalLBPLabel().setText("Total (LBP): 0");
        view.getTotalEURLabel().setText("Total (EUR): 0.00");
        view.getDiscountLabel().setText("Discount: 0%");

        view.getVoidOffBtn().setDisable(true);
        view.getConfirmSaleBtn().setDisable(true);

        updateRemainingLabels();

        if (view.getScanModeToggle().isSelected()) {
            view.getBarcodeField().requestFocus();
        }
    }

    private void showReceipt(Sale sale) {

        StringBuilder sb = new StringBuilder();
        sb.append("Sale #").append(sale.getSaleId()).append("\n");
        sb.append("Cashier: ").append(cashier.getUsername()).append("\n");
        sb.append("Date: ").append(getFormattedDate(sale.getDate())).append("\n\n");

        sb.append("Items:\n");
        for (SaleItem item : cart) {
            sb.append(item.getProductName())
                    .append(" x").append(item.getQuantity())
                    .append(" — ").append(item.getSubtotal()).append(" USD\n");
        }

        sb.append("\nTotals:\n");
        sb.append(String.format("USD: %.2f\n", finalAmountUsd));
        sb.append(String.format("LBP: %s\n", formatLBP(finalAmountUsd * lbpRate)));
        sb.append(String.format("EUR: %.2f\n", finalAmountUsd * eurRate));

        sb.append("\nPayments:\n");
        paymentHistory.forEach(line -> sb.append(line).append("\n"));

        sb.append("\nRates used:\n");
        sb.append(String.format("1 USD = %,d LBP\n", (int) lbpRate));
        sb.append(String.format("1 USD = %.2f EUR\n", eurRate));

        Alert r = new Alert(Alert.AlertType.INFORMATION);
        r.setHeaderText("Sale Receipt");
        r.setContentText(sb.toString());
        r.showAndWait();
    }

    private void requestAdminOverride() {

        javafx.scene.control.Dialog<String> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Admin Override");
        dialog.setHeaderText("Scan admin VOID card");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter VOID code");

        dialog.getDialogPane().setContent(passwordField);
        dialog.getDialogPane().getButtonTypes().addAll(
                javafx.scene.control.ButtonType.OK,
                javafx.scene.control.ButtonType.CANCEL
        );

        dialog.setResultConverter(button -> {
            if (button == javafx.scene.control.ButtonType.OK) {
                return passwordField.getText();
            }
            return null;
        });

        String code = dialog.showAndWait().orElse("");
        if (code.isEmpty()) {
            return;
        }

        // Compare normally (plaintext)
        try {
            boolean ok = new UserService().getAdmins()
                    .stream()
                    .anyMatch(a -> code.equals(a.getVoidCode()));

            if (ok) {
                authorizedToVoid = true;
                view.getVoidOffBtn().setDisable(false);
                view.getDiscountBtn().setDisable(false);
                view.getRemoveItemBtn().setDisable(false);
                view.getDeletePaymentBtn().setDisable(false);
                showInfo("VOID unlocked successfully.");
            } else {
                showError("Invalid VOID code.");
            }

        } catch (Exception ex) {
            showError("Authorization error.");
        }
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).show();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }

    public String getFormattedDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(formatter);
    }

    private void playBeep() {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/sound/beep.wav");
            if (audioSrc == null) {
                System.out.println("Beep file not found!");
                return;
            }

            InputStream bufferedIn = new java.io.BufferedInputStream(audioSrc);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(bufferedIn));
            clip.start();

        } catch (Exception e) {
            System.out.println("Cannot play beep: " + e.getMessage());
        }
    }

}
