import redis.clients.jedis.Jedis;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class RateLimiter {
    private static String loadScript() {
        try {
            return Files.readString(Path.of("src/main/resources/request_rate_limiter.lua"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean allowRequest(Jedis jedis, String userId, int capacity, double tokensPerSecond) {
        String script = loadScript();
        long time = Instant.now().getEpochSecond(); //System.currentTimeMillis()

        Object result = jedis.eval(script, 1, userId, String.valueOf(capacity), String.valueOf(tokensPerSecond), String.valueOf(time));
        return "1".equals(result.toString());
    }

    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("redis", 6379)) {
            jedis.del("hacker_1"); 
            System.out.println("Hacker Attack Simulation");

            for (int i = 1; i <= 10; i++) {
                boolean allowed = allowRequest(jedis, "hacker_1", 5, 1);
                
                if (allowed) {
                    System.out.println("Request " + i + ": ALLOWED");
                } else {
                    System.out.println("Request " + i + ": BLOCKED (Too Many Requests)");
                }
                
                Thread.sleep(200); 
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
