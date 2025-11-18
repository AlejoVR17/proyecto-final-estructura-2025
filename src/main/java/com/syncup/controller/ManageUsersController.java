package com.syncup.controller;

import com.syncup.data.UserRepository;
import com.syncup.model.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ManageUsersController {

    @FXML private TableView<User> tblUsers;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colNombre;
    @FXML private Label lblMessage;

    private final UserRepository userRepo = UserRepository.getInstance();

    @FXML
    public void initialize() {

        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        actualizarTabla();
    }

    // --------------------------------------------------------------
    //                   AGREGAR USUARIO
    // --------------------------------------------------------------
    @FXML
    private void onAddUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/syncup/view/add_user_dialog.fxml"));
            Parent root = loader.load();

            AddUserController controller = loader.getController();

            Stage dialog = new Stage();
            dialog.setTitle("Agregar Usuario");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

            if (controller.usuarioCreado()) {
                actualizarTabla();
                lblMessage.setText("✔ Usuario agregado con éxito");
            }

        } catch (Exception e) {
            lblMessage.setText("❌ Error al abrir el formulario");
            e.printStackTrace();
        }
    }

    // --------------------------------------------------------------
    //                    ACTUALIZAR
    // --------------------------------------------------------------
    @FXML
    public void onRefresh() {
        actualizarTabla();
        lblMessage.setText("✔ Lista actualizada");
    }

    private void actualizarTabla() {
        tblUsers.getItems().setAll(userRepo.getAll());
    }

    // --------------------------------------------------------------
    //                    ELIMINAR
    // --------------------------------------------------------------
    @FXML
    private void onDeleteUser() {

        User seleccionado = tblUsers.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            lblMessage.setText("⚠ Debes seleccionar un usuario");
            return;
        }

        boolean ok = userRepo.eliminarUsuario(seleccionado.getUsername());

        if (ok) {
            actualizarTabla();
            lblMessage.setText("✔ Usuario eliminado");
        } else {
            lblMessage.setText("❌ No se pudo eliminar");
        }
    }
}
