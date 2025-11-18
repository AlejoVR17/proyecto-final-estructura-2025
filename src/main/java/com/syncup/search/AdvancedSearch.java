package com.syncup.search;

import com.syncup.model.Cancion;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class AdvancedSearch {

    private final List<Cancion> catalogo;

    public AdvancedSearch(List<Cancion> catalogo) {
        this.catalogo = catalogo;
    }

    public List<Cancion> buscar(String titulo, String artista, String genero, Integer anio) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(4);

        Callable<List<Cancion>> tareaTitulo = () -> {
            if (titulo == null || titulo.isBlank()) return catalogo;
            return catalogo.stream()
                    .filter(c -> c.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                    .collect(Collectors.toList());
        };

        Callable<List<Cancion>> tareaArtista = () -> {
            if (artista == null || artista.isBlank()) return catalogo;
            return catalogo.stream()
                    .filter(c -> c.getArtista().equalsIgnoreCase(artista))
                    .collect(Collectors.toList());
        };

        Callable<List<Cancion>> tareaGenero = () -> {
            if (genero == null || genero.isBlank()) return catalogo;
            return catalogo.stream()
                    .filter(c -> c.getGenero().equalsIgnoreCase(genero))
                    .collect(Collectors.toList());
        };

        Callable<List<Cancion>> tareaAnio = () -> {
            if (anio == null) return catalogo;
            return catalogo.stream()
                    .filter(c -> c.getAnio() == anio)
                    .collect(Collectors.toList());
        };

        Future<List<Cancion>> rTitulo = executor.submit(tareaTitulo);
        Future<List<Cancion>> rArtista = executor.submit(tareaArtista);
        Future<List<Cancion>> rGenero = executor.submit(tareaGenero);
        Future<List<Cancion>> rAnio = executor.submit(tareaAnio);

        // Intersección final
        List<Cancion> resultado = new ArrayList<>(rTitulo.get());
        resultado.retainAll(rArtista.get());
        resultado.retainAll(rGenero.get());
        resultado.retainAll(rAnio.get());

        executor.shutdown();
        return resultado;
    }

    public List<Cancion> buscar(String artista, String genero, Integer anio) throws Exception {
        return buscar(null, artista, genero, anio);
    }
}
