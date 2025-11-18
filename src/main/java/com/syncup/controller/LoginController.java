package com.syncup.controller;

import com.syncup.data.DataInitializer;
import com.syncup.data.UserRepository;
import com.syncup.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMessage;  // ✔ Debe coincidir con el FXML

    private static boolean initialized = false;

    @FXML
    private void onLogin(ActionEvent event) {

        // Inicializar solo una vez
        if (!initialized) {
            DataInitializer.init();
            initialized = true;
        }

        String username = txtUsername.getText();
        String password = txtPassword.getText();

        UserRepository repo = UserRepository.getInstance();
        User usuario = repo.getUser(username);

        // Validación
        if (usuario != null && usuario.getPassword().equals(password)) {

            lblMessage.setText("Inicio de sesión exitoso.");
            lblMessage.setStyle("-fx-text-fill: #4CAF50;"); // verde

            abrirDashboard(usuario);

        } else {
            lblMessage.setText("Credenciales incorrectas.");
            lblMessage.setStyle("-fx-text-fill: #FF5252;"); // rojo
        }
    }

    private void abrirDashboard(User usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/syncup/view/UserDashboard.fxml")
            );
            Parent root = loader.load();

            UserDashboardController controller = loader.getController();
            controller.setUsuarioActual(usuario);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onRegister(ActionEvent actionEvent) {
        lblMessage.setText("Función de registro en desarrollo.");
        lblMessage.setStyle("-fx-text-fill: #FFA000;");
    }
}
