import redis.clients.jedis.Jedis;

public class RateLimiter {
    public static boolean allowRequest(Jedis jedis, String userId, int limit) {
        // Hint: Use jedis.incr(userId)
        long count = jedis.incr(userId);
        System.out.println("Redis Count for " + userId + ": " + count);

        if (count == 1) {
            jedis.expire(userId, 60);
        } 
        
        if (count > limit) {
            return false;
        } 
        
        return true;
    }

    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("redis", 6379)) {
            jedis.del("hacker_1"); 
            System.out.println("Hacker Attack Simulation");

            for (int i = 1; i <= 10; i++) {
                boolean allowed = allowRequest(jedis, "hacker_1", 5);
                
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
