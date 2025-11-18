package com.syncup.controller;

import com.syncup.model.Cancion;
import com.syncup.service.AdminSongService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;

public class AdminCancionController {

    // ========== TABLA ==========
    @FXML private TableView<Cancion> tableCanciones;

    @FXML private TableColumn<Cancion, Integer> colId;
    @FXML private TableColumn<Cancion, String> colTitulo;
    @FXML private TableColumn<Cancion, String> colArtista;
    @FXML private TableColumn<Cancion, String> colGenero;
    @FXML private TableColumn<Cancion, Integer> colAnio;
    @FXML private TableColumn<Cancion, Double> colDuracion;

    // ========== CAMPOS ==========
    @FXML private TextField txtId;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtArtista;
    @FXML private TextField txtGenero;
    @FXML private TextField txtAnio;
    @FXML private TextField txtDuracion;

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarCanciones();

        // Rellenar los campos al seleccionar una fila
        tableCanciones.setOnMouseClicked(event -> {
            Cancion c = tableCanciones.getSelectionModel().getSelectedItem();
            if (c != null) llenarFormulario(c);
        });
    }

    // =============================
    // CONFIGURAR COLUMNAS
    // =============================

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colArtista.setCellValueFactory(new PropertyValueFactory<>("artista"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));

        // ⬅️ DEBE SER "anio", porque tu método es getAnio()
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));

        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
    }



    // =============================
    // CARGAR TABLA
    // =============================
    private void cargarCanciones() {
        tableCanciones.setItems(
                FXCollections.observableArrayList(AdminSongService.obtenerCanciones())
        );
    }

    // =============================
    // LLENAR FORMULARIO
    // =============================
    private void llenarFormulario(Cancion c) {
        txtId.setText(String.valueOf(c.getId()));
        txtTitulo.setText(c.getTitulo());
        txtArtista.setText(c.getArtista());
        txtGenero.setText(c.getGenero());
        txtAnio.setText(String.valueOf(c.getAnio()));
        txtDuracion.setText(String.valueOf(c.getDuracion()));
    }

    // =============================
    //      BOTÓN: AGREGAR
    // =============================
    @FXML
    private void onAgregar() {
        try {
            Cancion c = new Cancion(
                    Integer.parseInt(txtId.getText()),
                    txtTitulo.getText(),
                    txtArtista.getText(),
                    txtGenero.getText(),
                    Integer.parseInt(txtAnio.getText()),
                    Double.parseDouble(txtDuracion.getText())
            );

            if (AdminSongService.agregarCancion(c)) {
                mostrar("Canción agregada");
                cargarCanciones();
                limpiar();
            } else {
                mostrar("❗ El ID ya existe");
            }

        } catch (Exception e) {
            mostrar("❗ Datos inválidos");
        }
    }

    // =============================
    //        BOTÓN: EDITAR
    // =============================
    @FXML
    private void onEditar() {
        Cancion seleccionada = tableCanciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrar("Seleccione una canción");
            return;
        }

        try {
            Cancion nueva = new Cancion(
                    seleccionada.getId(),  // ID NO SE CAMBIA
                    txtTitulo.getText(),
                    txtArtista.getText(),
                    txtGenero.getText(),
                    Integer.parseInt(txtAnio.getText()),
                    Double.parseDouble(txtDuracion.getText())
            );

            if (AdminSongService.editarCancion(nueva)) {
                mostrar("Canción modificada");
                cargarCanciones();
            }

        } catch (Exception e) {
            mostrar("❗ Datos inválidos");
        }
    }

    // =============================
    //       BOTÓN: ELIMINAR
    // =============================
    @FXML
    private void onEliminar() {
        Cancion seleccionada = tableCanciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrar("Seleccione una canción");
            return;
        }

        AdminSongService.eliminarCancion(seleccionada.getId());
        mostrar("Canción eliminada");
        cargarCanciones();
        limpiar();
    }

    // =============================
    // UTILIDADES
    // =============================
    private void limpiar() {
        txtId.clear();
        txtTitulo.clear();
        txtArtista.clear();
        txtGenero.clear();
        txtAnio.clear();
        txtDuracion.clear();
    }

    private void mostrar(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).show();
    }
}
