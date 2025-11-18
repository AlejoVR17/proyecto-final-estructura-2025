package com.syncup.service;

import com.syncup.data.CancionRepository;
import com.syncup.model.Cancion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminSongService {

    private static final CancionRepository repo = CancionRepository.getInstance();

    // CREATE
    public static boolean agregarCancion(Cancion c) {
        return repo.agregarCancion(c);
    }

    // READ
    public static List<Cancion> obtenerCanciones() {
        return new ArrayList<>(repo.obtenerTodas());
    }

    // UPDATE
    public static boolean editarCancion(Cancion modificada) {
        Cancion original = repo.obtenerCancion(modificada.getId());
        if (original == null) return false;

        original.setTitulo(modificada.getTitulo());
        original.setArtista(modificada.getArtista());
        original.setGenero(modificada.getGenero());
        original.setAnio(modificada.getAnio());
        original.setDuracion(modificada.getDuracion());

        return true;
    }

    // DELETE
    public static boolean eliminarCancion(int id) {
        return repo.eliminarCancion(id);
    }

    /**
     * ===============================
     *  RF-012: CARGA MASIVA DE CANCIONES
     * ===============================
     */
    public static int cargarCancionesDesdeArchivo(File archivo) throws IOException {

        int cargadas = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                if (linea.trim().isEmpty() || !linea.contains(";")) continue;

                String[] p = linea.split(";");
                if (p.length != 6) continue;

                try {
                    int id = Integer.parseInt(p[0]);
                    String titulo = p[1];
                    String artista = p[2];
                    String genero = p[3];
                    int anio = Integer.parseInt(p[4]);
                    double duracion = Double.parseDouble(p[5]);

                    Cancion nueva = new Cancion(id, titulo, artista, genero, anio, duracion);

                    if (repo.agregarCancion(nueva)) {
                        cargadas++;
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Línea ignorada por formato incorrecto: " + linea);
                }
            }
        }

        return cargadas;
    }
}
