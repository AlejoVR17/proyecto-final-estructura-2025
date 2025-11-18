package com.syncup.data;

import com.syncup.graph.GrafoDeSimilitud;
import com.syncup.model.Cancion;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataInitializerMusic {

    private static CancionRepository repo = null;
    private static GrafoDeSimilitud grafo = null;

    /**
     * Inicializa el repositorio cargando canciones desde resources/data/canciones.txt
     */
    public static CancionRepository initRepository() {

        if (repo != null) return repo;

        repo = CancionRepository.getInstance();

        try {
            // Cargar archivo dentro del JAR: /data/canciones.txt
            InputStream input = DataInitializerMusic.class.getResourceAsStream("/data/canciones.txt");

            if (input == null) {
                throw new RuntimeException("No se encontró el archivo data/canciones.txt");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String linea;

            int added = 0;

            while ((linea = br.readLine()) != null) {

                // saltar líneas vacías
                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(";");

                int id = Integer.parseInt(partes[0].trim());
                String titulo = partes[1].trim();
                String artista = partes[2].trim();
                String genero = partes[3].trim();
                int anio = Integer.parseInt(partes[4].trim());
                double duracion = Double.parseDouble(partes[5].trim());

                Cancion c = new Cancion(id, titulo, artista, genero, anio, duracion);

                if (repo.agregarCancion(c)) {
                    added++;
                } else {
                    System.out.println("Aviso: la canción con id " + id + " ya existe y no se agregó.");
                }
            }

            br.close();
            System.out.println("Inicialización de repo: se agregaron " + added + " canciones desde canciones.txt.");

        } catch (Exception e) {
            System.out.println("Error cargando canciones: " + e.getMessage());
            e.printStackTrace();
        }

        return repo;
    }

    /**
     * Inicializar grafo de similitud (esto no cambia)
     */
    public static GrafoDeSimilitud initGrafo() {

        if (grafo != null) return grafo;

        grafo = new GrafoDeSimilitud();

        grafo.agregarConexion(1, 2, 1);
        grafo.agregarConexion(1, 5, 3);
        grafo.agregarConexion(2, 5, 2);
        grafo.agregarConexion(3, 4, 4);
        grafo.agregarConexion(3, 6, 5);
        grafo.agregarConexion(4, 6, 1);
        grafo.agregarConexion(7, 3, 9);

        return grafo;
    }
}
