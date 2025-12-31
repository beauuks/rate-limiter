import redis.clients.jedis.Jedis;

public class RateLimiter {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("redis", 6379)) {

            jedis.set("visitor_count", "0");
            System.out.println("Set visitor_count to 0");

            long count = jedis.incr("visitor_count");
            System.out.println("Increment visitor_count. Value: " + count);

            String value = jedis.get("visitor_count");
            System.out.println("Get value from visitor_count: " + value);

        } catch (Exception e) {
            System.err.println("Could not connect to Redis: " + e);
        }
    }
}
