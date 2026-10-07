package Views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginView extends Stage {

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button quitButton;

    public LoginView() {
        buildUI();
    }

    private void buildUI() {

        // ✅ Title
        Label title = new Label("SmartMarket POS");
        title.setFont(Font.font("Arial", 32));
        title.setStyle("-fx-font-weight: bold;");

        // ✅ Inputs
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefWidth(250);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(250);

        // ✅ Buttons
        loginButton = blueButton("Login");
        loginButton.setDefaultButton(true);

        quitButton = greyButton("Quit");

        VBox formBox = new VBox(12,
                title,
                usernameField,
                passwordField,
                loginButton,
                quitButton
        );
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));

        // ✅ White card with shadow
        VBox card = new VBox(formBox);
        card.setAlignment(Pos.CENTER);
        card.setStyle("""
            -fx-background-color: white;
        """);
        card.setPadding(new Insets(20));

        card.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.2)));

        // ✅ Fullscreen background
        BorderPane root = new BorderPane(card);
        BorderPane.setAlignment(card, Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e3f2fd, #bbdefb);");

        Scene scene = new Scene(root, 600, 400);
        setScene(scene);
        setTitle("Login - SmartMarket POS");
        setResizable(false);
    }

    private Button blueButton(String text) {
        Button b = new Button(text);
        b.setStyle("""
            -fx-background-color: #1976d2;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10 26;
            -fx-background-radius: 20;
            -fx-border-color: #0d47a1;
            -fx-border-width: 1px;
            -fx-border-radius: 20;
            -fx-cursor: hand;
        """);
        b.setPrefWidth(200);
        return b;
    }

    private Button greyButton(String text) {
        Button b = new Button(text);
        b.setStyle("""
            -fx-background-color: #9e9e9e;
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-padding: 10 26;
            -fx-background-radius: 20;
            -fx-border-color: #616161;
            -fx-border-width: 1px;
            -fx-border-radius: 20;
            -fx-cursor: hand;
        """);
        b.setPrefWidth(200);
        return b;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getQuitButton() {
        return quitButton;
    }
}
