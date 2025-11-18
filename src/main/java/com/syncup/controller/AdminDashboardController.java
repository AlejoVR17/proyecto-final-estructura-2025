package com.syncup.controller;

import com.syncup.utils.SceneSwitcher;
import com.syncup.utils.TrieInitializer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class AdminDashboardController {

    @FXML
    private StackPane contentArea;

    //      GESTIONAR CANCIONES
    @FXML
    public void onGestionarCanciones(ActionEvent event) {
        loadIntoContent("/com/syncup/view/admin_songs.fxml",
                "❌ Error al cargar la gestión de canciones.");
    }

    //      GESTIONAR USUARIOS
    @FXML
    public void onGestionarUsuarios(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/com/syncup/view/manage_users.fxml"));

            Parent view = loader.load();

            ManageUsersController controller = loader.getController();
            controller.onRefresh();

            contentArea.getChildren().setAll(view);

        } catch (Exception e) {
            e.printStackTrace();
            showError("❌ Error al cargar la gestión de usuarios.");
        }
    }

    // CARGA MASIVA DE CANCIONES (TXT)
    @FXML
    public void onCargarArchivo(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"));
        fileChooser.setTitle("Seleccionar archivo de canciones");

        File file = fileChooser.showOpenDialog(null);
        if (file == null) return;

        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty() || line.startsWith("#"))
                    continue;

                String[] parts = line.split(";");
                if (parts.length != 6) continue;

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String titulo = parts[1].trim();
                    String artista = parts[2].trim();
                    String genero = parts[3].trim();
                    int anio = Integer.parseInt(parts[4].trim());
                    double duracion = Double.parseDouble(parts[5].trim());

                    com.syncup.model.Cancion c =
                            new com.syncup.model.Cancion(id, titulo, artista, genero, anio, duracion);

                    com.syncup.data.CancionRepository.getInstance().agregarCancion(c);


                    // Insertar título en el Trie
                    TrieInitializer.getTrie().insert(titulo);

                    count++;

                } catch (Exception ignored) {
                }
            }

            Label ok = new Label("✅ Se cargaron " + count + " canciones correctamente.");
            ok.setStyle("-fx-text-fill: #1db954; -fx-font-size: 16px;");
            contentArea.getChildren().setAll(ok);

        } catch (Exception e) {
            e.printStackTrace();
            showError("❌ Error al leer el archivo.");
        }
    }

    // CERRAR SESIÓN
    @FXML
    public void onCerrarSesion(ActionEvent event) {
        SceneSwitcher.switchScene(event,
                "/com/syncup/view/login.fxml",
                "SyncUp - Inicio de Sesión");
    }

    //     UTILIDADES
    private void loadIntoContent(String fxml, String msgError) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
            showError(msgError);
        }
    }

    private void showError(String msg) {
        Label lbl = new Label(msg);
        lbl.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        contentArea.getChildren().setAll(lbl);
    }
}
