package com.syncup.controller;

import com.syncup.model.Cancion;
import com.syncup.model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.FileWriter;
import java.io.PrintWriter;

public class FavoritesController {

    @FXML private TableView<Cancion> tblFavoritos;
    @FXML private TableColumn<Cancion, String> colTitulo;
    @FXML private TableColumn<Cancion, String> colArtista;
    @FXML private TableColumn<Cancion, String> colGenero;
    @FXML private Label lblMensaje;

    private User currentUser;

    // ============================================================
    //  SETTER CORRECTO (el que usa tu Dashboard)
    // ============================================================
    public void setCurrentUser(User user) {
        this.currentUser = user;
        cargarFavoritos();
    }

    // ============================================================
    //  CARGAR TABLA DE FAVORITOS
    // ============================================================
    private void cargarFavoritos() {

        colTitulo.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getTitulo()));

        colArtista.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getArtista()));

        colGenero.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getGenero()));

        if (currentUser != null) {
            tblFavoritos.setItems(
                    FXCollections.observableArrayList(currentUser.getListaFavoritos())
            );
        }
    }

    // ============================================================
    //  ELIMINAR FAVORITO
    // ============================================================
    @FXML
    private void onEliminarFavorito() {
        Cancion seleccionada = tblFavoritos.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            lblMensaje.setText("⚠ Selecciona un favorito para eliminar");
            return;
        }

        currentUser.eliminarFavorito(seleccionada);

        lblMensaje.setText("❌ Eliminado: " + seleccionada.getTitulo());
        cargarFavoritos();
    }

    // ============================================================
    //  EXPORTAR CSV (ESTÁTICO PARA USARLO DESDE EL DASHBOARD)
    // ============================================================
    public static void exportarFavoritosCSV(User user) {
        try {
            String fileName = "favoritos_" + user.getUsername() + ".csv";
            PrintWriter pw = new PrintWriter(new FileWriter(fileName));

            pw.println("Titulo,Artista,Genero");

            for (Cancion c : user.getListaFavoritos()) {
                pw.println(c.getTitulo() + "," + c.getArtista() + "," + c.getGenero());
            }

            pw.close();
            System.out.println("CSV exportado -> " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
