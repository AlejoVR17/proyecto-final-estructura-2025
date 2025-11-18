package com.syncup.data;

import com.syncup.graph.GrafoDeSimilitud;
import com.syncup.graph.GrafoDeSimilitud.Edge;
import com.syncup.model.Cancion;
import com.syncup.model.User;
import java.util.*;
import java.util.stream.Collectors;

public class PlaylistService {

    private final GrafoDeSimilitud grafo;
    private final CancionRepository repo = CancionRepository.getInstance();

    public PlaylistService(GrafoDeSimilitud grafo) {
        this.grafo = grafo;
    }

    public List<Cancion> generarDescubrimientoSemanal(User user) {

        Set<Cancion> recomendadas = new LinkedHashSet<>();

        // 1. Obtener favoritos con null-safety
        List<Cancion> favoritos = (user == null || user.getListaFavoritos() == null)
                ? new ArrayList<>()
                : new ArrayList<>(user.getListaFavoritos());

        // 2. Si no tiene favoritos
        if (favoritos.isEmpty()) {
            return repo.obtenerTodas()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
        }

        // 3. Buscar similares en el grafo
        for (Cancion fav : favoritos) {
            List<Edge> vecinos = grafo.getAdjList()
                    .getOrDefault(fav.getId(), Collections.emptyList());

            vecinos.stream()
                    .sorted(Comparator.comparingInt(e -> e.peso))  // menor peso = más similar
                    .limit(3)
                    .forEach(edge -> {
                        Cancion candidata = repo.obtenerCancion(edge.destino);

                        if (candidata != null && !favoritos.contains(candidata)) {
                            recomendadas.add(candidata);
                        }
                    });
        }

        // 4. Rellenar
        if (recomendadas.size() < 5) {
            List<Cancion> restantes = repo.obtenerTodas().stream()
                    .filter(c -> !favoritos.contains(c))
                    .filter(c -> !recomendadas.contains(c))
                    .limit(5 - recomendadas.size())
                    .collect(Collectors.toList());

            recomendadas.addAll(restantes);
        }

        return new ArrayList<>(recomendadas);
    }
}
