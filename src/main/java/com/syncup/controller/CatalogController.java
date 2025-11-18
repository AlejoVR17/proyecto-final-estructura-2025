package com.syncup.controller;

import com.syncup.data.CancionRepository;
import com.syncup.data.DataInitializerMusic;
import com.syncup.model.Cancion;
import com.syncup.model.User;
import com.syncup.search.AdvancedSearch;
import com.syncup.service.RecommendationService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CatalogController {

    @FXML private TableView<Cancion> tableCatalog;
    @FXML private TableColumn<Cancion, String> colTitulo;
    @FXML private TableColumn<Cancion, String> colArtista;
    @FXML private TableColumn<Cancion, String> colGenero;

    @FXML private VBox panelResultados;
    @FXML private HBox searchBar;

    private RecommendationService recomendador;
    private CancionRepository repo;
    private User usuarioActual;

    @FXML
    public void initialize() {

        repo = DataInitializerMusic.initRepository();

        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colArtista.setCellValueFactory(new PropertyValueFactory<>("artista"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));

        // CORRECCIÓN
        List<Cancion> catalogo = new ArrayList<>(repo.obtenerTodas());
        tableCatalog.getItems().setAll(catalogo);

        FXMLLoader loader = (FXMLLoader) searchBar.getProperties().get("fx:loader");
        SearchBarController searchController = loader.getController();

        AdvancedSearch motor = new AdvancedSearch(catalogo);

        searchController.init(motor, panelResultados);

        tableCatalog.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Cancion c = tableCatalog.getSelectionModel().getSelectedItem();
                if (c != null) abrirDetalles(c);
            }
        });
    }

    public void recibirRecomendador(RecommendationService r) {
        this.recomendador = r;
    }

    public void setUsuarioActual(User user) {
        this.usuarioActual = user;
    }

    private void abrirDetalles(Cancion cancion) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/syncup/view/song_detail.fxml")
            );
            Parent root = loader.load();

            SongDetailController controller = loader.getController();
            controller.setData(cancion, recomendador);
            controller.setUsuarioActual(usuarioActual);

            Stage window = new Stage();
            window.setTitle("Detalles de " + cancion.getTitulo());
            window.setScene(new Scene(root));
            window.show();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error al cargar detalles");
            alert.setContentText("No se pudo abrir song_detail.fxml");
            alert.show();
            e.printStackTrace();
        }
    }
}
