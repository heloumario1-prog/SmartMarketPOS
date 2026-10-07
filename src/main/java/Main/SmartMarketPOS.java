package Main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author HP
 */
public class SmartMarketPOS extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Use refactored LoginController which will create LoginView and show it.
        Controllers.LoginController lc = new Controllers.LoginController();
        lc.handleLogin(); // shows the login window
    }

}
