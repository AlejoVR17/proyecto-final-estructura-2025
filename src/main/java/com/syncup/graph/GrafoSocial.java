    package com.syncup.graph;

    import com.syncup.data.UserRepository;
    import com.syncup.model.User;

    import java.util.*;

    /**
     * Grafo social sencillo (singleton) que guarda relaciones no dirigidas entre usuarios.
     */
    public class GrafoSocial {

        // SINGLETON
        private static final GrafoSocial instance = new GrafoSocial();
        public static GrafoSocial getInstance() { return instance; }

        // Adyacencias: username -> amigos
        private final Map<String, Set<String>> adj = new HashMap<>();

        private final UserRepository userRepo = UserRepository.getInstance();

        // Constructor privado para singleton
        private GrafoSocial() {}

        private String normalize(String s) {
            return (s == null) ? null : s.trim().toLowerCase();
        }

        // -------------------------------------------------
        //  AGREGAR USUARIO
        // -------------------------------------------------
        public void addUser(User u) {
            if (u == null || u.getUsername() == null) return;

            String username = normalize(u.getUsername());
            // CORRECCIÓN: HashSet con 'S' mayúscula
            adj.putIfAbsent(username, new HashSet<>());
        }

        // -------------------------------------------------
        //  ELIMINAR USUARIO DEL GRAFO
        // -------------------------------------------------
        public void removeUser(String username) {
            username = normalize(username);
            if (username == null) return;

            // Eliminar del grafo
            Set<String> amigos = adj.remove(username);
            if (amigos != null) {
                for (String a : amigos) {
                    Set<String> vec = adj.get(a);
                    if (vec != null) vec.remove(username);
                }
            }
        }

        // -------------------------------------------------
        //  AGREGAR AMISTAD BIDIRECCIONAL
        // -------------------------------------------------
        public boolean follow(String a, String b) {
            a = normalize(a);
            b = normalize(b);

            if (a == null || b == null || a.equals(b)) return false;
            if (!userRepo.exists(a) || !userRepo.exists(b)) return false;

            adj.putIfAbsent(a, new HashSet<>());
            adj.putIfAbsent(b, new HashSet<>());

            boolean changed = adj.get(a).add(b);
            adj.get(b).add(a);

            return changed;
        }

        // -------------------------------------------------
        //  ELIMINAR AMISTAD
        // -------------------------------------------------
        public boolean unfollow(String a, String b) {
            a = normalize(a);
            b = normalize(b);

            if (a == null || b == null) return false;

            boolean removed = false;

            if (adj.containsKey(a)) removed |= adj.get(a).remove(b);
            if (adj.containsKey(b)) adj.get(b).remove(a);

            return removed;
        }

        // -------------------------------------------------
        //  DEVOLVER AMIGOS
        // -------------------------------------------------
        public Set<String> neighbors(String username) {
            username = normalize(username);
            if (username == null) return Collections.emptySet();

            return Collections.unmodifiableSet(
                    adj.getOrDefault(username, Collections.emptySet())
            );
        }

        // -------------------------------------------------
        //  ¿SON AMIGOS?
        // -------------------------------------------------
        public boolean areConnected(String u1, String u2) {
            u1 = normalize(u1);
            u2 = normalize(u2);
            if (u1 == null || u2 == null) return false;

            return adj.getOrDefault(u1, Collections.emptySet()).contains(u2);
        }

        // -------------------------------------------------
        //  AMIGOS EN COMÚN
        // -------------------------------------------------
        public List<User> amigosEnComun(String u1, String u2) {
            u1 = normalize(u1);
            u2 = normalize(u2);

            if (u1 == null || u2 == null) return Collections.emptyList();
            if (!adj.containsKey(u1) || !adj.containsKey(u2)) return Collections.emptyList();

            Set<String> a1 = adj.get(u1);
            Set<String> a2 = adj.get(u2);

            List<User> comunes = new ArrayList<>();

            for (String amigo : a1) {
                if (a2.contains(amigo)) {
                    User u = userRepo.get(amigo);
                    if (u != null) comunes.add(u);
                }
            }

            return comunes;
        }

        // -------------------------------------------------
        //  SUGERENCIAS DE AMISTADES (BFS)
        // -------------------------------------------------
        public List<User> sugerirUsuarios(String start, int maxDepth, int limit) {
            start = normalize(start);

            if (start == null || !adj.containsKey(start) || maxDepth < 1)
                return Collections.emptyList();

            List<User> sugerencias = new ArrayList<>();
            Queue<String> queue = new LinkedList<>();
            Set<String> visited = new HashSet<>();
            Map<String, Integer> depth = new HashMap<>();

            visited.add(start);
            queue.add(start);
            depth.put(start, 0);

            // No sugerir amigos actuales ni uno mismo
            Set<String> excluidos = new HashSet<>(neighbors(start));
            excluidos.add(start);

            while (!queue.isEmpty() && sugerencias.size() < limit) {

                String actual = queue.poll();
                int d = depth.get(actual);

                if (d >= maxDepth) continue;

                for (String vecino : adj.getOrDefault(actual, Collections.emptySet())) {

                    if (visited.contains(vecino)) continue;

                    visited.add(vecino);
                    depth.put(vecino, d + 1);
                    queue.add(vecino);

                    if (!excluidos.contains(vecino)) {
                        User u = userRepo.get(vecino);
                        if (u != null) {
                            sugerencias.add(u);
                            excluidos.add(vecino);
                            if (sugerencias.size() >= limit) break;
                        }
                    }
                }
            }

            return sugerencias;
        }
    }
