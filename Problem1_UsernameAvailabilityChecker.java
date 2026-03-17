import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Problem1_UsernameAvailabilityChecker {
    // username -> userId
    private final ConcurrentHashMap<String, Long> users = new ConcurrentHashMap<>();
    // username attempt frequency
    private final ConcurrentHashMap<String, AtomicInteger> attempts = new ConcurrentHashMap<>();

    public Problem1_UsernameAvailabilityChecker() {
        // prefill with some users
        users.put("john_doe", 1L);
        users.put("admin", 2L);
    }

    public boolean checkAvailability(String username) {
        attempts.putIfAbsent(username, new AtomicInteger(0));
        attempts.get(username).incrementAndGet();
        return !users.containsKey(username);
    }

    public List<String> suggestAlternatives(String username, int maxSuggestions) {
        List<String> res = new ArrayList<>();
        // try append numbers
        for (int i = 1; res.size() < maxSuggestions && i < 1000; i++) {
            String candidate = username + i;
            if (!users.containsKey(candidate)) res.add(candidate);
        }
        // try replace underscore with dot
        if (res.size() < maxSuggestions) {
            String cand = username.replace('_', '.');
            if (!users.containsKey(cand)) res.add(cand);
        }
        return res;
    }

    public String getMostAttempted() {
        String best = null;
        int max = 0;
        for (Map.Entry<String, AtomicInteger> e : attempts.entrySet()) {
            int v = e.getValue().get();
            if (v > max) { max = v; best = e.getKey(); }
        }
        return best + " (" + max + " attempts)";
    }

    // sample
    public static void main(String[] args) throws InterruptedException {
        Problem1_UsernameAvailabilityChecker checker = new Problem1_UsernameAvailabilityChecker();
        System.out.println(checker.checkAvailability("john_doe"));
        System.out.println(checker.checkAvailability("jane_smith"));
        System.out.println(checker.suggestAlternatives("john_doe", 3));
        for (int i = 0; i < 10543; i++) checker.checkAvailability("admin");
        System.out.println(checker.getMostAttempted());
    }
}
