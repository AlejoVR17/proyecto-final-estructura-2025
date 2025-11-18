package com.syncup.utils;

import com.syncup.model.Cancion;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportadorCSV {

    /**
     * Exporta una lista de canciones a un archivo CSV.
     * RF-009: Descargar reporte de canciones favoritas.
     *
     * @param canciones Lista de canciones a exportar.
     * @param rutaArchivo Ruta del archivo destino .csv
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public static void exportarFavoritosCSV(List<Cancion> canciones, String rutaArchivo) throws IOException {

        FileWriter writer = new FileWriter(rutaArchivo);

        // Encabezados CSV
        writer.write("ID,Titulo,Artista,Genero,Anio,Duracion\n");

        // Cada canción en una línea
        for (Cancion c : canciones) {
            writer.write(
                    c.getId() + "," +
                            limpiar(c.getTitulo()) + "," +
                            limpiar(c.getArtista()) + "," +
                            limpiar(c.getGenero()) + "," +
                            c.getAnio() + "," +
                            c.getDuracion() + "\n"
            );
        }

        writer.close();
    }

    /**
     * Evita problemas si algún campo contiene comas.
     */
    private static String limpiar(String texto) {
        if (texto.contains(",")) {
            return "\"" + texto + "\"";
        }
        return texto;
    }
}
