package com.syncup.controller;

import com.syncup.data.UserRepository;
import com.syncup.graph.GrafoSocial;
import com.syncup.model.User;
import com.syncup.service.SocialSuggestionService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class FriendsController {

    @FXML private TableView<User> tblUsuarios;
    @FXML private TableColumn<User, String> colUsuario;
    @FXML private TableColumn<User, String> colNombre;
    @FXML private TableColumn<User, String> colEstado;
    @FXML private Label lblResultado;

    private final UserRepository userRepo = UserRepository.getInstance();
    private GrafoSocial grafo;
    private User usuarioActual;

    // =========================
    //   MÉTODOS DE INYECCIÓN
    // =========================
    public void setGrafoSocial(GrafoSocial grafo) {
        this.grafo = grafo;
    }

    public void setCurrentUser(User user) {
        this.usuarioActual = user;
    }

    // =========================
    //   INICIALIZACIÓN
    // =========================
    public void initialize() {

        colUsuario.setCellValueFactory(c -> c.getValue().usernameProperty());
        colNombre.setCellValueFactory(c -> c.getValue().nombreRealProperty());
        colEstado.setCellValueFactory(c -> c.getValue().estadoProperty());

        refrescarTabla();
    }

    private void refrescarTabla() {
        tblUsuarios.setItems(FXCollections.observableArrayList(userRepo.getAll()));
    }

    // =========================
    //   ACCIÓN: SEGUIR
    // =========================
    @FXML
    private void onSeguir() {

        User sel = tblUsuarios.getSelectionModel().getSelectedItem();

        if (sel == null) {
            lblResultado.setText("Seleccione un usuario.");
            return;
        }

        if (usuarioActual == null) {
            lblResultado.setText("Error: usuario no cargado.");
            return;
        }

        if (grafo == null) {
            lblResultado.setText("Error: grafo social no disponible.");
            return;
        }

        if (sel.getUsername().equalsIgnoreCase(usuarioActual.getUsername())) {
            lblResultado.setText("No puedes seguirte a ti mismo.");
            return;
        }

        boolean ok = grafo.follow(usuarioActual.getUsername(), sel.getUsername());

        if (ok) {
            lblResultado.setText("Ahora sigues a " + sel.getUsername());
        } else {
            lblResultado.setText("Ya sigues a " + sel.getUsername());
        }
    }

    // =========================
    //   ACCIÓN: DEJAR DE SEGUIR
    // =========================
    @FXML
    private void onDejarSeguir() {

        User sel = tblUsuarios.getSelectionModel().getSelectedItem();

        if (sel == null) {
            lblResultado.setText("Seleccione un usuario.");
            return;
        }

        boolean ok = grafo.unfollow(usuarioActual.getUsername(), sel.getUsername());

        if (ok) {
            lblResultado.setText("Has dejado de seguir a " + sel.getUsername());
        } else {
            lblResultado.setText("No seguías a ese usuario.");
        }
    }

    // =========================
    //   ACCIÓN: SUGERENCIAS
    // =========================
    @FXML
    private void onSugerencias() {

        if (usuarioActual == null) {
            lblResultado.setText("Error: usuario no cargado.");
            return;
        }

        List<User> sugerencias = SocialSuggestionService.sugerirUsuarios(usuarioActual.getUsername());

        if (sugerencias.isEmpty()) {
            lblResultado.setText("No hay sugerencias disponibles.");
        } else {
            lblResultado.setText("Sugerencias: " +
                    String.join(", ", sugerencias.stream().map(User::getUsername).toList()));
        }
    }

    // =========================
    //   ACCIÓN: AMIGOS EN COMÚN
    // =========================
    @FXML
    private void onAmigosEnComun() {

        User sel = tblUsuarios.getSelectionModel().getSelectedItem();

        if (sel == null) {
            lblResultado.setText("Seleccione un usuario.");
            return;
        }

        List<User> comun = grafo.amigosEnComun(
                usuarioActual.getUsername(),
                sel.getUsername()
        );

        if (comun.isEmpty()) {
            lblResultado.setText("No tienen amigos en común.");
        } else {
            lblResultado.setText(
                    "Amigos en común: " +
                            String.join(", ", comun.stream().map(User::getUsername).toList())
            );
        }
    }


    // =========================
    //   ACCIÓN: AMIGOS DE AMIGOS (BFS)
    // =========================
    @FXML
    private void onAmigosDeAmigos() {

        List<User> bfs = grafo.sugerirUsuarios(usuarioActual.getUsername(), 2, 10);

        if (bfs.isEmpty()) {
            lblResultado.setText("No hay usuarios a 2 niveles de distancia.");
        } else {
            lblResultado.setText("Usuarios a 2 niveles: " +
                    String.join(", ", bfs.stream().map(User::getUsername).toList()));
        }
    }
}
