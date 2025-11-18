package com.syncup.search;

import java.util.*;

public class Trie {

    private static class Node {
        Map<Character, Node> children = new HashMap<>();
        boolean isWord = false;
    }

    private final Node root = new Node();

    public void insert(String word) {
        Node curr = root;
        for (char c : word.toLowerCase().toCharArray()) {
            curr.children.putIfAbsent(c, new Node());
            curr = curr.children.get(c);
        }
        curr.isWord = true;
    }

    public List<String> autoComplete(String prefix) {
        List<String> res = new ArrayList<>();
        Node curr = root;
        for (char c : prefix.toLowerCase().toCharArray()) {
            curr = curr.children.get(c);
            if (curr == null) return res;
        }
        collect(curr, prefix.toLowerCase(), res);
        return res;
    }

    private void collect(Node node, String prefix, List<String> list) {
        if (node.isWord) list.add(prefix);
        for (char c : node.children.keySet()) {
            collect(node.children.get(c), prefix + c, list);
        }
    }
}
