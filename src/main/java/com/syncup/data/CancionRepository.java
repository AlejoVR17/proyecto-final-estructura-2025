package com.syncup.data;

import com.syncup.model.Cancion;
import java.util.*;

public class CancionRepository {

    private static final Map<Integer, Cancion> canciones = new HashMap<>();

    // ---------- SINGLETON ----------
    private static final CancionRepository instance = new CancionRepository();

    private CancionRepository() {}

    public static CancionRepository getInstance() {
        return instance;
    }

    // ---------- CRUD ----------
    public boolean agregarCancion(Cancion c) {
        if (canciones.containsKey(c.getId())) return false;
        canciones.put(c.getId(), c);
        return true;
    }

    public boolean eliminarCancion(int id) {
        return canciones.remove(id) != null;
    }

    public boolean editarCancion(Cancion nueva) {
        if (!canciones.containsKey(nueva.getId())) return false;
        canciones.put(nueva.getId(), nueva);
        return true;
    }

    public Cancion obtenerCancion(int id) {
        return canciones.get(id);
    }

    public List<Cancion> obtenerTodas() {
        return new ArrayList<>(canciones.values());
    }

    // Cargar datos iniciales una sola vez
    public void cargarInicial(List<Cancion> lista) {
        if (canciones.isEmpty()) {
            for (Cancion c : lista) {
                canciones.put(c.getId(), c);
            }
        }
    }
}
