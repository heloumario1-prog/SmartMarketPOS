package Controllers;

import Models.Product;
import Services.CategoryService;
import Services.ProductService;
import Views.ProductView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.List;

public class ProductController {
    
    private ProductView view;
    private ProductService productService;
    private AdminDashboardController adminController;
    private CategoryService categoryService = new CategoryService();

    // ✅ NEW constructor for dashboard navigation
    public ProductController(AdminDashboardController adminController) {
        this.adminController = adminController;
        this.productService = new ProductService();
        this.view = new ProductView();
        
        loadCategories();
        attachHandlers();
        loadProducts();

        // ✅ Inject UI into AdminDashboard center
        adminController.getView().setContent(view.getRoot());
    }

    // ✅ optional constructor for testing / reuse (no Stage)
    public ProductController(ProductView view, ProductService service) {
        this.view = view;
        this.productService = service;
        
        loadCategories();
        attachHandlers();
        loadProducts();
    }
    
    private void attachHandlers() {
        view.getAddBtn().setOnAction(e -> addProduct());
        view.getEditBtn().setOnAction(e -> editProduct());
        view.getDeleteBtn().setOnAction(e -> deleteProduct());
        view.getClearBtn().setOnAction(e -> clearForm());
        view.getTable().setOnMouseClicked(e -> {
            try {
                loadSelectedProduct();
            } catch (SQLException ex) {
                System.getLogger(ProductController.class.getName())
                        .log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
    }
    
    private void loadProducts() {
        try {
            List<Product> list = productService.getAllProducts();
            ObservableList<Product> items = FXCollections.observableArrayList(list);
            view.getTable().setItems(items);
        } catch (Exception ex) {
            show("Error loading products: " + ex.getMessage());
        }
    }
    
    private void addProduct() {
        String name = view.getNameField().getText().trim();
        String categoryName = view.getCategoryCombo().getValue();
        String barcode = view.getBarcodeField().getText().trim();
        String priceText = view.getPriceField().getText().trim();
        String stockText = view.getStockField().getText().trim();
        
        if (name.isEmpty() || categoryName == null || barcode.isEmpty()
                || priceText.isEmpty() || stockText.isEmpty()) {
            show("All fields are required.");
            return;
        }
        
        try {
            int categoryId = categoryService.getCategoryIdByName(categoryName);
            double price = Double.parseDouble(priceText);
            int stock = Integer.parseInt(stockText);
            
            productService.addProduct(name, categoryId, price, stock, barcode);
            
            clearForm();
            loadProducts();
        } catch (Exception ex) {
            show("Error adding product: " + ex.getMessage());
        }
    }
    
    private void editProduct() {
        Product selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            show("Select a product to edit.");
            return;
        }
        
        String name = view.getNameField().getText().trim();
        String categoryName = view.getCategoryCombo().getValue();
        String barcode = view.getBarcodeField().getText().trim();
        String priceText = view.getPriceField().getText().trim();
        String stockText = view.getStockField().getText().trim();
        
        if (name.isEmpty() || categoryName == null || barcode.isEmpty()
                || priceText.isEmpty() || stockText.isEmpty()) {
            show("All fields are required.");
            return;
        }
        
        try {
            int categoryId = categoryService.getCategoryIdByName(categoryName);
            double price = Double.parseDouble(priceText);
            int stock = Integer.parseInt(stockText);
            
            productService.editProduct(
                    selected.getProductId(),
                    name,
                    categoryId,
                    price,
                    stock,
                    barcode
            );
            
            clearForm();
            loadProducts();
        } catch (Exception ex) {
            show("Error editing product: " + ex.getMessage());
        }
    }
    
    private void deleteProduct() {
        Product selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            show("Select a product to delete.");
            return;
        }
        
        try {
            productService.deleteProduct(selected.getProductId());
            loadProducts();
        } catch (Exception ex) {
            show("Error deleting product: " + ex.getMessage());
        }
    }
    
    private void clearForm() {
        view.getNameField().clear();
        view.getCategoryCombo().setValue(null);
        view.getBarcodeField().clear();
        view.getPriceField().clear();
        view.getStockField().clear();
    }
    
    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
    
    private void loadSelectedProduct() throws SQLException {
        Product p = view.getTable().getSelectionModel().getSelectedItem();
        if (p == null) {
            return;
        }
        
        String categoryName = categoryService.getCategoryNameById(p.getCategory());
        
        view.getNameField().setText(p.getName());
        view.getCategoryCombo().setValue(categoryName);
        view.getBarcodeField().setText(p.getBarcode());
        view.getPriceField().setText(String.valueOf(p.getPrice()));
        view.getStockField().setText(String.valueOf(p.getStock()));
    }
    
    private void loadCategories() {
        try {
            var list = categoryService.getAllCategories();
            view.getCategoryCombo().getItems().setAll(list);
        } catch (Exception ex) {
            System.out.println("Error loading categories: " + ex.getMessage());
        }
    }
    
}
