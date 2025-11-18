package com.syncup.controller;

import com.syncup.data.UserRepository;
import com.syncup.graph.GrafoSocial;
import com.syncup.model.User;
import com.syncup.utils.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField txtUser;
    @FXML private TextField txtNombre;
    @FXML private PasswordField txtPass;
    @FXML private PasswordField txtConfirm;
    @FXML private Label lblMessage;

    private final UserRepository repo = UserRepository.getInstance();
    private final GrafoSocial grafo = GrafoSocial.getInstance();

    @FXML
    private void onRegister(ActionEvent event) {

        String username = txtUser.getText().trim().toLowerCase();
        String nombre   = txtNombre.getText().trim();
        String pass     = txtPass.getText().trim();
        String confirm  = txtConfirm.getText().trim();

        // ------------------------
        // VALIDACIONES
        // ------------------------
        if (username.isEmpty() || nombre.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            lblMessage.setText("⚠ Todos los campos son obligatorios");
            return;
        }

        if (!pass.equals(confirm)) {
            lblMessage.setText("❌ Las contraseñas no coinciden");
            return;
        }

        if (repo.exists(username)) {
            lblMessage.setText("❌ Ese usuario ya existe");
            return;
        }

        // ------------------------
        // CREAR USUARIO
        // ------------------------
        User newUser = new User(username, pass, nombre);

        boolean ok = repo.addUser(newUser);
        if (!ok) {
            lblMessage.setText("❌ Error: no se pudo registrar el usuario");
            return;
        }

        // IMPORTANTE:
        // NO agregamos al grafo aquí porque UserRepository.addUser()
        // ya lo agrega automáticamente. Si lo hacemos aquí → duplicados.
        //
        // grafo.addUser(newUser);  ← NO HACER

        lblMessage.setText("✅ Usuario registrado con éxito");

        // Pequeño delay para que el usuario vea el mensaje
        try { Thread.sleep(500); } catch (Exception ignored) {}

        // Cambiar a pantalla de login
        SceneSwitcher.switchScene(event, "/com/syncup/view/login.fxml", "Iniciar Sesión");
    }

    @FXML
    private void onCancel(ActionEvent event) {
        SceneSwitcher.switchScene(event, "/com/syncup/view/login.fxml", "Iniciar Sesión");
    }
}
