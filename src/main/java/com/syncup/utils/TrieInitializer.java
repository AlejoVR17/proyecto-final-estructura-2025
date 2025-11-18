package com.syncup.utils;

import com.syncup.data.CancionRepository;
import com.syncup.model.Cancion;

public class TrieInitializer {

    private static TrieAutocompletado trie;

    /** Devuelve el Trie cargado */
    public static TrieAutocompletado getTrie() {
        // Lazy initialization
        if (trie == null) {
            cargarCanciones();
        }
        return trie;
    }

    /** Carga todas las canciones en el Trie */
    public static void cargarCanciones() {
        trie = new TrieAutocompletado();

        CancionRepository repo = CancionRepository.getInstance();

        for (Cancion c : repo.obtenerTodas()) {
            if (c.getTitulo() != null && !c.getTitulo().isBlank()) {
                trie.insert(c.getTitulo());
                trie.insert(c.getArtista());
                trie.insert(c.getGenero());

            }
        }
    }
}
