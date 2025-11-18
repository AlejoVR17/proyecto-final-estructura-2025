package com.syncup.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.LinkedList;
import java.util.Objects;

public class User {

    private StringProperty username;
    private StringProperty password;
    private StringProperty nombre;
    private StringProperty estado; // ← NUEVO

    private LinkedList<Cancion> listaFavoritos;

    public User(String username, String password, String nombre) {
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.nombre = new SimpleStringProperty(nombre);

        // Por defecto, todos "Activo"
        this.estado = new SimpleStringProperty("Activo");

        this.listaFavoritos = new LinkedList<>();
    }

    // ---------- PROPERTIES ----------
    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty nombreRealProperty() {
        return nombre;
    }

    public StringProperty estadoProperty() {
        return estado;
    }

    // ---------- GETTERS / SETTERS ----------
    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getEstado() {
        return estado.get();
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    public LinkedList<Cancion> getListaFavoritos() {
        return listaFavoritos;
    }

    // FAVORITOS
    public boolean agregarFavorito(Cancion c) {
        if (c == null) return false;
        if (!listaFavoritos.contains(c)) {
            listaFavoritos.add(c);
            return true;
        }
        return false;
    }

    public boolean eliminarFavorito(Cancion c) {
        if (c == null) return false;
        return listaFavoritos.remove(c);
    }

    // EQUALS & HASHCODE
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(username.get(), user.username.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username.get());
    }

    @Override
    public String toString() {
        return username.get();
    }
}
