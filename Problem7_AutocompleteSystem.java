import java.util.*;
import java.util.concurrent.*;

public class Problem7_AutocompleteSystem {
    private final ConcurrentHashMap<String, Integer> freq = new ConcurrentHashMap<>();
    private final TrieNode root = new TrieNode();

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean end;
    }

    public void addQuery(String q) {
        freq.merge(q, 1, Integer::sum);
        TrieNode cur = root;
        for (char c: q.toCharArray()) {
            cur = cur.children.computeIfAbsent(c, k -> new TrieNode());
        }
        cur.end = true;
    }

    public List<String> suggest(String prefix, int k) {
        TrieNode cur = root;
        for (char c: prefix.toCharArray()) {
            cur = cur.children.get(c);
            if (cur == null) return Collections.emptyList();
        }
        // gather candidates via DFS
        List<String> candidates = new ArrayList<>();
        StringBuilder sb = new StringBuilder(prefix);
        dfs(cur, sb, candidates);
        candidates.sort((a,b)->Integer.compare(freq.getOrDefault(b,0), freq.getOrDefault(a,0)));
        return candidates.subList(0, Math.min(k, candidates.size()));
    }

    private void dfs(TrieNode node, StringBuilder sb, List<String> out) {
        if (node.end) out.add(sb.toString());
        for (Map.Entry<Character, TrieNode> e: node.children.entrySet()) {
            sb.append(e.getKey());
            dfs(e.getValue(), sb, out);
            sb.setLength(sb.length()-1);
        }
    }

    public static void main(String[] args) {
        Problem7_AutocompleteSystem s = new Problem7_AutocompleteSystem();
        s.addQuery("java tutorial"); s.addQuery("javascript"); s.addQuery("java download");
        System.out.println(s.suggest("jav", 10));
    }
}
