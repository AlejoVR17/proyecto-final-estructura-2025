package com.syncup.controller;

import com.syncup.model.Cancion;
import com.syncup.search.AdvancedSearch;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.util.List;

public class SearchBarController {

    @FXML
    private TextField txtArtista;
    @FXML
    private TextField txtGenero;
    @FXML
    private TextField txtAnio;
    @FXML
    private Button btnBuscar;

    private AdvancedSearch motor;
    private VBox panelResultados;

    public void init(AdvancedSearch motor, VBox panelResultados) {
        this.motor = motor;
        this.panelResultados = panelResultados;
    }

    @FXML
    private void onBuscar() {

        // Evitar errores si no se ha inicializado correctamente
        if (motor == null || panelResultados == null) {
            System.out.println("⚠ SearchBarController: motor o panelResultados NO inicializados.");
            return;
        }

        panelResultados.getChildren().clear();

        try {
            String artista = txtArtista.getText().trim();
            String genero = txtGenero.getText().trim();

            Integer anio = null;
            String txtA = txtAnio.getText().trim();

            if (!txtA.isEmpty()) {
                anio = Integer.parseInt(txtA);
            }

            // Ejecución
            List<Cancion> resultados = motor.buscar(artista, genero, anio);

            // Mostrar resultados
            if (resultados.isEmpty()) {
                panelResultados.getChildren().add(new Label("Sin resultados."));
            } else {
                for (Cancion c : resultados) {
                    panelResultados.getChildren().add(
                            new Label(c.getTitulo() + " - " + c.getArtista())
                    );
                }
            }

        } catch (NumberFormatException e) {
            panelResultados.getChildren().add(new Label("Año inválido. Debe ser un número."));
        } catch (Exception e) {
            e.printStackTrace();
            panelResultados.getChildren().add(new Label("Error al ejecutar la búsqueda."));
        }
    }
}
