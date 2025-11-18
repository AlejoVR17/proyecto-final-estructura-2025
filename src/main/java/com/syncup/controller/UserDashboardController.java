package com.syncup.controller;

import com.syncup.data.DataInitializer;
import com.syncup.graph.GrafoSocial;
import com.syncup.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserDashboardController {

    private User usuarioActual;
    private GrafoSocial grafoSocial;

    // ============================================================
    //  CARGAR USUARIO
    // ============================================================
    public void setUsuarioActual(User usuario) {
        this.usuarioActual = usuario;
        System.out.println("Usuario cargado en dashboard: " + usuario.getUsername());
    }

    // ============================================================
    //  INICIALIZAR — obtener grafo desde DataInitializer
    // ============================================================
    @FXML
    public void initialize() {
        grafoSocial = DataInitializer.getGrafoSocial();
    }

    // ============================================================
    //  ABRIR VISTA DE AMIGOS
    // ============================================================
    @FXML
    private void onOpenFriends(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/syncup/view/FriendsView.fxml"));
            Parent root = loader.load();

            FriendsController controller = loader.getController();
            controller.setCurrentUser(usuarioActual);
            controller.setGrafoSocial(grafoSocial);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Amigos");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================================================
    //  ABRIR CATÁLOGO
    // ============================================================
    @FXML
    private void onOpenCatalog(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/syncup/view/CatalogView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Catálogo de Canciones");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================================================
    //  ABRIR FAVORITOS
    // ============================================================
    @FXML
    private void onOpenFavorites(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/syncup/view/FavoritesView.fxml"));
            Parent root = loader.load();

            FavoritesController controller = loader.getController();
            controller.setCurrentUser(usuarioActual);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Mis Favoritos");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================================================
    //  EXPORTAR FAVORITOS CSV SIN ABRIR LA VISTA
    // ============================================================
    @FXML
    private void onExportFavoritesCSV(ActionEvent event) {
        try {
            FavoritesController.exportarFavoritosCSV(usuarioActual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
