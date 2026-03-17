import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Problem6_DistributedRateLimiter {
    static class TokenBucket {
        final int maxTokens;
        final double refillPerSecond;
        double tokens;
        long lastRefill;
        TokenBucket(int maxTokens, double refillPerSecond) {
            this.maxTokens = maxTokens; this.refillPerSecond = refillPerSecond; this.tokens = maxTokens; this.lastRefill = System.nanoTime();
        }
        synchronized boolean allowRequest() {
            refill();
            if (tokens >= 1) { tokens -= 1; return true; }
            return false;
        }
        private void refill() {
            long now = System.nanoTime();
            double secs = (now - lastRefill) / 1e9;
            double add = secs * refillPerSecond;
            if (add > 0) {
                tokens = Math.min(maxTokens, tokens + add);
                lastRefill = now;
            }
        }
    }

    private final ConcurrentHashMap<String, TokenBucket> clients = new ConcurrentHashMap<>();

    public void registerClient(String clientId, int limitPerHour) {
        double refillPerSec = (double)limitPerHour / 3600.0;
        clients.put(clientId, new TokenBucket(limitPerHour, refillPerSec));
    }

    public boolean checkRateLimit(String clientId) {
        TokenBucket tb = clients.get(clientId);
        if (tb == null) return true; // unknown clients allowed by default
        return tb.allowRequest();
    }

    public static void main(String[] args) throws Exception {
        Problem6_DistributedRateLimiter rl = new Problem6_DistributedRateLimiter();
        rl.registerClient("abc123", 1000);
        for (int i=0;i<1005;i++) {
            boolean ok = rl.checkRateLimit("abc123");
            System.out.println(i+1 + ": " + ok);
        }
    }
}
