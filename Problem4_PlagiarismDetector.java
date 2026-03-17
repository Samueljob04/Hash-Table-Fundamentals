import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Problem4_PlagiarismDetector {
    // n-gram -> set of document ids
    private final Map<String, Set<String>> index = new HashMap<>();
    private final int n;

    public Problem4_PlagiarismDetector(int n) { this.n = n; }

    public void indexDocument(String docId, String text) {
        String[] words = text.split("\\s+");
        for (int i = 0; i + n <= words.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if (j>0) sb.append(' ');
                sb.append(words[i+j]);
            }
            String gram = sb.toString();
            index.computeIfAbsent(gram, k -> new HashSet<>()).add(docId);
        }
    }

    public Map<String, Integer> analyzeDocument(String docId, String text) {
        Map<String, Integer> matches = new HashMap<>();
        String[] words = text.split("\\s+");
        for (int i = 0; i + n <= words.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if (j>0) sb.append(' ');
                sb.append(words[i+j]);
            }
            String gram = sb.toString();
            Set<String> docs = index.getOrDefault(gram, Collections.emptySet());
            for (String other : docs) {
                if (other.equals(docId)) continue;
                matches.put(other, matches.getOrDefault(other, 0) + 1);
            }
        }
        return matches;
    }

    public static void main(String[] args) throws Exception {
        Problem4_PlagiarismDetector det = new Problem4_PlagiarismDetector(5);
        det.indexDocument("essay_089.txt", "This is a sample essay text with some overlapping phrases and repeated content.");
        det.indexDocument("essay_092.txt", "This is a sample essay text with many overlapping phrases and much repeated content."
        );
        Map<String,Integer> res = det.analyzeDocument("essay_123.txt", "This is a sample essay text with some overlapping phrases and repeated content.");
        for (Map.Entry<String,Integer> e: res.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }
    }
}
