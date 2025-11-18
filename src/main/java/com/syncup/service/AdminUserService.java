package com.syncup.service;

import com.syncup.data.UserRepository;
import com.syncup.model.User;

import java.util.Collection;

/**
 * Servicio encargado de administrar operaciones sobre usuarios.
 * Actúa como capa intermedia entre el controlador y el repositorio.
 */
public class AdminUserService {

    private final UserRepository repo = UserRepository.getInstance();

    /**
     * Retorna todos los usuarios registrados.
     *
     * @return colección de usuarios
     */
    public Collection<User> getAllUsers() {
        return repo.getAll();
    }

    /**
     * Agrega un usuario al sistema siempre que no exista uno con el mismo username.
     *
     * @param u objeto User a agregar
     * @return true si fue agregado, false si ya existía o si el usuario es inválido
     */
    public boolean addUser(User u) {
        if (u == null || u.getUsername() == null || u.getUsername().isBlank())
            return false;

        if (repo.exists(u.getUsername()))
            return false;

        return repo.addUser(u);
    }

    /**
     * Elimina un usuario por su username.
     *
     * @param username identificador único del usuario
     * @return true si fue eliminado, false si no existía
     */
    public boolean deleteUser(String username) {
        if (username == null || username.isBlank()) return false;
        return repo.eliminarUsuario(username);
    }

    /**
     * Obtiene un usuario por su username.
     *
     * @param username identificador único del usuario
     * @return instancia de User o null si no existe
     */
    public User getUser(String username) {
        if (username == null || username.isBlank()) return null;
        return repo.get(username);
    }
}
