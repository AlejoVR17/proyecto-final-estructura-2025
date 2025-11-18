package com.syncup.data;

import com.syncup.graph.GrafoDeSimilitud;
import com.syncup.model.Cancion;

import java.util.*;
import java.util.stream.Collectors;

public class RadioService {

    private final GrafoDeSimilitud grafo;

    public RadioService(GrafoDeSimilitud grafo) {
        this.grafo = grafo;
    }

    public List<Cancion> generarRadio(int idBase) {

        // Validar canción base
        Cancion base = CancionRepository.getInstance().obtenerCancion(idBase);
        if (base == null) return Collections.emptyList();

        // Ejecutar Dijkstra
        Map<Integer, Integer> distancias = grafo.dijkstra(idBase);

        List<Integer> orden = distancias.entrySet().stream()
                .filter(e -> e.getKey() != idBase)
                .sorted(Map.Entry.comparingByValue()) // menor distancia → más afinidad
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return orden.stream()
                .map(id -> CancionRepository.getInstance().obtenerCancion(id))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
