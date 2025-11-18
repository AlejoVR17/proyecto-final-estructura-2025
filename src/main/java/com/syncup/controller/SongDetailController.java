package com.syncup.controller;

import com.syncup.model.Cancion;
import com.syncup.model.User;
import com.syncup.service.RecommendationService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Queue;

public class SongDetailController {

    @FXML
    private ImageView imgCover;

    @FXML
    private Label lblTitle;
    @FXML
    private Label lblArtist;
    @FXML
    private Label lblGenre;
    @FXML
    private Label lblYear;
    @FXML
    private Label lblDuration;

    @FXML
    private Button btnRadio;
    @FXML
    private Button btnFav;

    private Cancion cancion;
    private RecommendationService recomendador;
    private User usuarioActual;

    // Recibir usuario
    public void setUsuarioActual(User u) {
        this.usuarioActual = u;

        // Si la canción ya estaba en favoritos → actualiza el texto del botón
        if (cancion != null) {
            actualizarBotonFavorito();
        }
    }

    // Recibir canción + recomendador
    public void setData(Cancion c, RecommendationService r) {
        this.cancion = c;
        this.recomendador = r;

        lblTitle.setText(c.getTitulo());
        lblArtist.setText("Artista: " + c.getArtista());
        lblGenre.setText("Género: " + c.getGenero());
        lblYear.setText("Año: " + c.getAnio());
        lblDuration.setText("Duración: " + formatearDuracion(c.getDuracion()));

        try {
            Image imagen = new Image(getClass().getResourceAsStream("/images/default_cover.png"));
            imgCover.setImage(imagen);
        } catch (Exception e) {
            imgCover.setImage(null);
        }

        // Configurar botones
        btnRadio.setOnAction(e -> generarRadio());
        btnFav.setOnAction(e -> onFavorito());

        actualizarBotonFavorito();
    }

    private void actualizarBotonFavorito() {
        if (usuarioActual == null) return;

        if (usuarioActual.getListaFavoritos().contains(cancion)) {
            btnFav.setText("💔 Quitar");
        } else {
            btnFav.setText("❤️ Favorito");
        }
    }

    @FXML
    private void onFavorito() {

        if (usuarioActual == null) {
            System.out.println("⚠ Usuario no asignado");
            return;
        }

        if (!usuarioActual.getListaFavoritos().contains(cancion)) {
            usuarioActual.getListaFavoritos().add(cancion);
            System.out.println("❤ Agregado a favoritos: " + cancion.getTitulo());
        } else {
            usuarioActual.getListaFavoritos().remove(cancion);
            System.out.println("Removido de favoritos: " + cancion.getTitulo());
        }

        actualizarBotonFavorito();
    }

    private void generarRadio() {
        if (recomendador == null) {
            System.out.println("⚠ Recomendador no asignado");
            return;
        }

        Queue<Cancion> cola = recomendador.generarRadio(cancion);

        StringBuilder sb = new StringBuilder("📻 Radio basada en: " + cancion.getTitulo() + "\n\n");

        for (Cancion c : cola) {
            sb.append("• ").append(c.getTitulo())
                    .append(" - ").append(c.getArtista())
                    .append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Radio Generada");
        alert.setTitle("Radio");
        alert.setContentText(sb.toString());
        alert.show();
    }

    private String formatearDuracion(double duracionMinutos) {
        int minutos = (int) duracionMinutos;
        int segundos = (int) ((duracionMinutos - minutos) * 100);
        if (segundos >= 60) segundos = 59;
        return String.format("%02d:%02d", minutos, segundos);
    }
}
