package Controllers;

import Models.Category;
import Services.CategoryService;
import Views.CategoryView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.List;

public class CategoryController {

    private CategoryView view;
    private CategoryService categoryService;
    private AdminDashboardController adminController;

    public CategoryController(AdminDashboardController adminController) {
        this.adminController = adminController;
        this.categoryService = new CategoryService();
        this.view = new CategoryView();

        attachHandlers();
        loadCategories();

        // ✅ Insert UI into AdminDashboard center
        adminController.getView().setContent(view.getRoot());
    }

    // ✅ optional constructor for testing
    public CategoryController(CategoryView view, CategoryService service) {
        this.view = view;
        this.categoryService = service;

        attachHandlers();
        loadCategories();
    }

    private void attachHandlers() {
        view.getAddBtn().setOnAction(e -> addCategory());
        view.getEditBtn().setOnAction(e -> editCategory());
        view.getDeleteBtn().setOnAction(e -> deleteCategory());
        view.getTable().setOnMouseClicked(e -> loadSelectedCategory());
        view.getClearBtn().setOnAction(e -> clearForm());
    }

    private void clearForm() {
        view.getNameField().clear();
    }

    private void loadCategories() {
        try {
            List<Category> list = categoryService.getAllCategoryObjects();
            ObservableList<Category> items = FXCollections.observableArrayList(list);
            view.getTable().setItems(items);
        } catch (Exception ex) {
            show("Error loading categories: " + ex.getMessage());
        }
    }

    private void addCategory() {
        String name = view.getNameField().getText().trim();
        if (name.isEmpty()) {
            show("Category name is required.");
            return;
        }

        try {
            categoryService.addCategory(name);
            view.getNameField().clear();
            loadCategories();
        } catch (Exception ex) {
            show("Error adding category: " + ex.getMessage());
        }
    }

    private void editCategory() {
        Category selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            show("Select a category to edit.");
            return;
        }

        String name = view.getNameField().getText().trim();
        if (name.isEmpty()) {
            show("Category name is required.");
            return;
        }

        try {
            categoryService.editCategory(selected.getCategoryId(), name);
            view.getNameField().clear();
            loadCategories();
        } catch (Exception ex) {
            show("Error editing category: " + ex.getMessage());
        }
    }

    private void deleteCategory() {
        Category selected = view.getTable().getSelectionModel().getSelectedItem();
        if (selected == null) {
            show("Select a category to delete.");
            return;
        }

        try {
            categoryService.deleteCategory(selected.getCategoryId());
            loadCategories();
        } catch (Exception ex) {
            show("Error deleting category: " + ex.getMessage());
        }
    }

    private void loadSelectedCategory() {
        Category c = view.getTable().getSelectionModel().getSelectedItem();
        if (c == null) {
            return;
        }

        view.getNameField().setText(c.getName());
    }

    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
