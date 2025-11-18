package com.syncup.graph;

import com.syncup.model.Cancion;
import java.util.*;

public class GrafoDeSimilitud {

    // Lista de adyacencia: idCancion → lista de conexiones
    private final Map<Integer, List<Edge>> adjList = new HashMap<>();

    // Clase interna Edge
    public static class Edge {
        public final int destino;
        public final int peso;

        public Edge(int destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    // =========================================================================
    //                           AGREGAR RELACIÓN
    // =========================================================================
    public void agregarConexion(int id1, int id2, int peso) {

        if (peso <= 0) return; // Mejora menor: evitar pesos inválidos

        adjList.computeIfAbsent(id1, k -> new ArrayList<>()).add(new Edge(id2, peso));
        adjList.computeIfAbsent(id2, k -> new ArrayList<>()).add(new Edge(id1, peso));
    }

    // =========================================================================
    //                     OBTENER SIMILARES DIRECTOS
    // =========================================================================
    public List<Integer> obtenerSimilares(int id) {
        List<Edge> edges = adjList.getOrDefault(id, Collections.emptyList());
        List<Integer> resultado = new ArrayList<>(edges.size());

        for (Edge e : edges) {
            resultado.add(e.destino);
        }

        return resultado;
    }

    // =========================================================================
    //                       DIJKSTRA: DISTANCIAS
    // =========================================================================
    public Map<Integer, Integer> dijkstra(int startId) {

        Map<Integer, Integer> dist = new HashMap<>();
        Set<Integer> visited = new HashSet<>();

        // Inicializar distancias
        for (Integer id : adjList.keySet()) {
            dist.put(id, Integer.MAX_VALUE);
        }
        dist.put(startId, 0);

        PriorityQueue<int[]> pq =
                new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        pq.offer(new int[]{startId, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int actual = cur[0];
            int costoActual = cur[1];

            if (visited.contains(actual)) continue;
            visited.add(actual);

            for (Edge e : adjList.getOrDefault(actual, Collections.emptyList())) {
                int vecino = e.destino;
                int nuevoCosto = costoActual + e.peso;

                if (nuevoCosto < dist.get(vecino)) {
                    dist.put(vecino, nuevoCosto);
                    pq.offer(new int[]{vecino, nuevoCosto});
                }
            }
        }

        return dist;
    }

    // =========================================================================
    //                    RUTA MÁS CORTA ENTRE DOS CANCIONES
    // =========================================================================
    public List<Integer> rutaMasCorta(int origen, int destino) {

        Map<Integer, Integer> dist = new HashMap<>();
        Map<Integer, Integer> padre = new HashMap<>();
        Set<Integer> visitados = new HashSet<>();

        for (Integer id : adjList.keySet()) {
            dist.put(id, Integer.MAX_VALUE);
        }
        dist.put(origen, 0);

        PriorityQueue<int[]> pq =
                new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        pq.offer(new int[]{origen, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int actual = cur[0];

            if (visitados.contains(actual)) continue;
            visitados.add(actual);

            if (actual == destino) break;

            for (Edge e : adjList.getOrDefault(actual, Collections.emptyList())) {
                int vecino = e.destino;
                int nuevo = dist.get(actual) + e.peso;

                if (nuevo < dist.get(vecino)) {
                    dist.put(vecino, nuevo);
                    padre.put(vecino, actual);
                    pq.offer(new int[]{vecino, nuevo});
                }
            }
        }

        // Si no hay ruta
        if (!padre.containsKey(destino) && origen != destino) {
            return Collections.emptyList();
        }

        // Reconstruir camino
        List<Integer> camino = new ArrayList<>();
        int actual = destino;

        while (actual != origen) {
            camino.add(actual);
            actual = padre.get(actual);
        }

        camino.add(origen);
        Collections.reverse(camino);
        return camino;
    }

    // =========================================================================
    //       GENERAR RELACIONES AUTOMÁTICAS (ARTISTA, GÉNERO, AÑO)
    // =========================================================================
    public void generarRelacionesAuto(List<Cancion> lista) {

        for (int i = 0; i < lista.size(); i++) {
            for (int j = i + 1; j < lista.size(); j++) {

                Cancion a = lista.get(i);
                Cancion b = lista.get(j);

                int peso = 0;

                if (a.getGenero().equalsIgnoreCase(b.getGenero()))
                    peso += 1;

                if (a.getArtista().equalsIgnoreCase(b.getArtista()))
                    peso += 1;

                if (Math.abs(a.getAnio() - b.getAnio()) <= 3)
                    peso += 1;

                if (peso > 0) {
                    agregarConexion(a.getId(), b.getId(), peso);
                }
            }
        }
    }

    // =========================================================================
    //                          LIMPIAR GRAFO
    // =========================================================================
    public void limpiar() {
        adjList.clear();
    }

    // =========================================================================
    //                     ACCESO DESDE SERVICIOS
    // =========================================================================
    public Map<Integer, List<Edge>> getAdjList() {
        return adjList;
    }
}
