package com.syncup.service;

import com.syncup.data.UserRepository;
import com.syncup.graph.GrafoSocial;
import com.syncup.model.User;

import java.util.*;

public class SocialSuggestionService {

    public static List<User> sugerirUsuarios(String username) {

        if (username == null || username.isBlank())
            return Collections.emptyList();

        // Normalizar username
        final String usernameNormalized = username.toLowerCase();

        // Obtener usuario base (CORREGIDO)
        User user = UserRepository.getInstance().get(usernameNormalized);
        if (user == null) return Collections.emptyList();

        // Obtener singleton del grafo social
        GrafoSocial grafo = GrafoSocial.getInstance();

        // Obtener sugerencias mediante BFS
        List<User> sugeridos = grafo.sugerirUsuarios(usernameNormalized, 2, 10);

        // Filtrar: no incluir el propio usuario
        sugeridos.removeIf(u -> {
            String uName = u == null ? null : u.getUsername();
            return uName != null && uName.equalsIgnoreCase(usernameNormalized);
        });

        return sugeridos;
    }
}
