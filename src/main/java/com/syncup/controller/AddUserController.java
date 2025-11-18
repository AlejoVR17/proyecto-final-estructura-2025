package com.syncup.controller;

import com.syncup.data.UserRepository;
import com.syncup.model.User;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddUserController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtNombre;
    @FXML private Label lblError;

    private boolean creado = false;

    // Permite al Admin saber si se creó correctamente
    public boolean usuarioCreado() {
        return creado;
    }

    @FXML
    private void onAceptar() {

        String username = txtUsername.getText().trim().toLowerCase();
        String password = txtPassword.getText().trim();
        String nombre   = txtNombre.getText().trim();

        UserRepository repo = UserRepository.getInstance();

        // ===============================================
        // VALIDACIONES
        // ===============================================

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("⚠ El usuario y la contraseña son obligatorios.");
            return;
        }

        // Username permitido (solo letras, números y _.-)
        if (!username.matches("^[a-zA-Z0-9_.-]+$")) {
            lblError.setText("⚠ El usuario solo puede contener letras, números y _.-");
            return;
        }

        // Contraseña mínima requerida
        if (password.length() < 4) {
            lblError.setText("⚠ La contraseña debe tener al menos 4 caracteres.");
            return;
        }

        // Nombre solo con letras (incluye acentos y espacios)
        if (!nombre.isEmpty() && !nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            lblError.setText("⚠ El nombre solo puede contener letras.");
            return;
        }

        // Username debe ser único
        if (repo.exists(username)) {
            lblError.setText("❌ Ese usuario ya existe.");
            return;
        }

        // Nombre por defecto
        if (nombre.isEmpty()) {
            nombre = "Usuario";
        }

        // ===============================================
        // CREAR USUARIO
        // ===============================================

        User nuevo = new User(username, password, nombre);

        boolean ok = repo.addUser(nuevo);

        if (!ok) {
            lblError.setText("❌ No se pudo agregar el usuario.");
            return;
        }

        creado = true;

        // ===============================================
        // CERRAR VENTANA
        // ===============================================
        Stage stage = (Stage) txtUsername.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancelar() {
        Stage stage = (Stage) txtUsername.getScene().getWindow();
        stage.close();
    }
}
