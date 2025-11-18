package com.syncup.utils;

import java.util.*;

public class TrieAutocompletado {

    private static class Node {
        Map<Character, Node> children = new HashMap<>();
        boolean isWord = false;
        String originalWord;   // Guarda la palabra como fue escrita
    }

    private final Node root = new Node();

    /**
     * Inserta una palabra en el Trie
     */
    public void insert(String word) {
        if (word == null || word.isEmpty()) return;

        Node cur = root;
        String lower = word.toLowerCase();

        for (char ch : lower.toCharArray()) {
            cur = cur.children.computeIfAbsent(ch, k -> new Node());
        }

        cur.isWord = true;
        cur.originalWord = word;   // palabra original (con mayúsculas, acentos, etc.)
    }

    /**
     * Devuelve una lista de autocompletados
     */
    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        if (prefix == null) return results;

        Node cur = root;
        String lower = prefix.toLowerCase();

        for (char ch : lower.toCharArray()) {
            cur = cur.children.get(ch);
            if (cur == null) return results; // no hay coincidencias
        }

        collect(cur, results);
        return results;
    }

    /**
     * Recolección recursiva
     */
    private void collect(Node node, List<String> results) {
        if (node.isWord) {
            results.add(node.originalWord); // devolvemos palabra original
        }

        for (Map.Entry<Character, Node> entry : node.children.entrySet()) {
            collect(entry.getValue(), results);
        }
    }
}
