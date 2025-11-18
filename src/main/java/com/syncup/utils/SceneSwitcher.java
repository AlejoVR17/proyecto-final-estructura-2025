package com.syncup.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {

    /**
     * Cambia de escena usando un archivo FXML.
     *
     * @param event    Evento del botón que dispara el cambio
     * @param fxmlPath Ruta absoluta del archivo dentro de /resources
     * @param title    Título de la ventana
     */
    public static void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            // Cargar FXML
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Obtener ventana actual
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Crear escena con tu propio tema
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    SceneSwitcher.class.getResource("/css/spotify-theme.css").toExternalForm()
            );

            // Aplicar y mostrar
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("❌ Error cargando la escena: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
