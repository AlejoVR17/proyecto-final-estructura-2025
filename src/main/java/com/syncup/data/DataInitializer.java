package com.syncup.data;

import com.syncup.graph.GrafoSocial;
import com.syncup.model.User;

public class DataInitializer {

    private static final GrafoSocial grafoSocial = GrafoSocial.getInstance();

    //   Metodo principal
    public static void init() {
        cargarUsuarios();
        initSocialGraph();
    }

    //   CARGAR USUARIOS
    public static void cargarUsuarios() {
        UserRepository repo = UserRepository.getInstance();

        if (!repo.getAll().isEmpty()) return;  // Evitar duplicados

        repo.addUser(new User("juan", "1234", "Juan"));
        repo.addUser(new User("maria", "abcd", "Maria"));
        repo.addUser(new User("pedro", "0000", "Pedro"));

        System.out.println("✔ Usuarios cargados correctamente.");
    }

    //   CREAR GRAFO SOCIAL + RELACIONES
    private static void initSocialGraph() {
        UserRepository repo = UserRepository.getInstance();

        // Agregar todos los usuarios al grafo
        for (User u : repo.getAll()) {
            grafoSocial.addUser(u);
        }

        // Relaciones de ejemplo
        grafoSocial.follow("juan", "maria");
        grafoSocial.follow("maria", "pedro");

        System.out.println("✔ Grafo social inicializado.");
    }

    //   RETORNAR EL GRAFO
    public static GrafoSocial getGrafoSocial() {
        return grafoSocial;
    }
}
