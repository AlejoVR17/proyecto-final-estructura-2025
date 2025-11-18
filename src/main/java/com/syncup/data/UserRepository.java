package com.syncup.data;

import com.syncup.graph.GrafoSocial;
import com.syncup.model.User;

import java.util.*;

public class UserRepository {

    // Singleton
    private static final UserRepository instance = new UserRepository();
    public static UserRepository getInstance() { return instance; }

    // Mapa de usuarios: clave = username en minúsculas
    private final Map<String, User> users = new HashMap<>();

    // --------------------------------------------------------------
    //                 CONSTRUCTOR — CREA ADMIN
    // --------------------------------------------------------------
    private UserRepository() {
        // Crear admin por defecto si no existe
        User admin = new User("admin", "admin", "Administrador");

        users.put("admin", admin);
        GrafoSocial.getInstance().addUser(admin);

        System.out.println("✔ Usuario admin creado por defecto");
    }

    // --------------------------------------------------------------
    //                     OPERACIONES BÁSICAS
    // --------------------------------------------------------------

    public boolean addUser(User u) {
        if (u == null || u.getUsername() == null) return false;

        String username = u.getUsername().toLowerCase();

        if (users.containsKey(username)) return false;

        // Guardar en repositorio
        users.put(username, u);

        // Sincronizar con grafo social
        GrafoSocial.getInstance().addUser(u);

        return true;
    }

    public boolean exists(String username) {
        if (username == null) return false;
        return users.containsKey(username.toLowerCase());
    }

    public User get(String username) {
        if (username == null) return null;
        return users.get(username.toLowerCase());
    }

    public Collection<User> getAll() {
        return users.values();
    }

    // --------------------------------------------------------------
    //                     ELIMINAR USUARIO
    // --------------------------------------------------------------
    public boolean eliminarUsuario(String username) {
        if (username == null) return false;

        username = username.toLowerCase();

        if (!users.containsKey(username)) return false;

        // 1. Eliminar del repositorio
        users.remove(username);

        // 2. Eliminar del grafo social
        GrafoSocial.getInstance().removeUser(username);

        return true;
    }

    public User getUser(String username) {
        if (username == null) return null;
        return users.get(username.toLowerCase());
    }
}
