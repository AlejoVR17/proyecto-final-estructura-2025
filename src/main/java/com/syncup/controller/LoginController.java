package com.syncup.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.syncup.data.UserRepository;
import com.syncup.model.User;
import com.syncup.utils.SceneSwitcher;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMessage;

    public void onLogin(ActionEvent event) {

        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("⚠️ Ingresa usuario y contraseña");
            return;
        }

        try {
            UserRepository repo = UserRepository.getInstance();
            User user = repo.getUser(username);

            // VALIDACIÓN GENERAL
            if (user == null || !user.getPassword().equals(password)) {
                lblMessage.setText("❌ Usuario o contraseña incorrectos");
                return;
            }

            // ADMIN LOGIN
            if (username.equals("admin") && password.equals("admin")) {

                FXMLLoader loader =
                        new FXMLLoader(getClass().getResource("/com/syncup/view/admin_dashboard.fxml"));

                Parent root = loader.load();

                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

                stage.setTitle("SyncUp - Administrador");
                stage.setScene(scene);
                stage.show();

                return;
            }

            // LOGIN USUARIO NORMAL
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/com/syncup/view/user_dashboard.fxml"));

            Parent root = loader.load();

            UserDashboardController controller = loader.getController();
            controller.setCurrentUser(user.getUsername());

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
            stage.setTitle("SyncUp - Usuario");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("⚠️ Error al cargar la interfaz.");
        }
    }


    public void onRegister(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/com/syncup/view/register.fxml", "SyncUp - Registro de Usuario");
    }
}
