package com.syncup.service;

import com.syncup.data.CancionRepository;
import com.syncup.graph.GrafoDeSimilitud;
import com.syncup.model.Cancion;
import com.syncup.model.User;

import java.util.*;
import java.util.stream.Collectors;

public class RecommendationService {

    private GrafoDeSimilitud grafo;
    private CancionRepository canciones;

    public RecommendationService(GrafoDeSimilitud grafo, CancionRepository canciones) {
        this.grafo = grafo;
        this.canciones = canciones;
    }

    /**
     * Descubrimiento Semanal:
     * - Toma las canciones favoritas del usuario
     * - Ejecuta Dijkstra desde cada favorito
     * - Filtra favoritos
     * - Devuelve las más similares (menor costo)
     */
    public List<Cancion> generarDescubrimientoSemanal(User user) {

        Set<Cancion> favoritos = new HashSet<>(user.getListaFavoritos());
        Map<Integer, Integer> acumulado = new HashMap<>();

        // Dijkstra desde cada favorito
        for (Cancion fav : favoritos) {

            Map<Integer, Integer> dist = grafo.dijkstra(fav.getId());

            for (var entry : dist.entrySet()) {

                int id = entry.getKey();
                int costo = entry.getValue();

                // Evitar recomendar canciones ya favoritas
                boolean esFavorita = favoritos.stream()
                        .anyMatch(c -> c.getId() == id);

                if (esFavorita) continue;

                // Acumular por menor costo
                acumulado.merge(id, costo, Integer::min);
            }
        }

        // Ordenar por similitud
        return acumulado.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(15)
                .map(e -> canciones.obtenerCancion(e.getKey()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Genera una "Radio" desde una canción base
     * utilizando Dijkstra y devolviendo una cola ordenada.
     */
    public Queue<Cancion> generarRadio(Cancion base) {

        Queue<Cancion> cola = new LinkedList<>();
        Map<Integer, Integer> dist = grafo.dijkstra(base.getId());

        dist.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(30)
                .forEach(e -> {
                    Cancion c = canciones.obtenerCancion(e.getKey());
                    if (c != null) cola.add(c);
                });

        return cola;
    }
}
